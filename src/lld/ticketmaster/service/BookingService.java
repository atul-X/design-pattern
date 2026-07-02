package lld.ticketmaster.service;

import lld.ticketmaster.enums.BookingStatus;
import lld.ticketmaster.enums.EventStatus;
import lld.ticketmaster.exception.BookingNotFoundException;
import lld.ticketmaster.exception.PaymentException;
import lld.ticketmaster.exception.SeatNotAvailableException;
import lld.ticketmaster.model.Booking;
import lld.ticketmaster.model.Event;
import lld.ticketmaster.model.Payment;
import lld.ticketmaster.model.Seat;
import lld.ticketmaster.pattern.observer.BookingObserver;
import lld.ticketmaster.pattern.strategy.PaymentStrategy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Orchestrates the booking lifecycle.
 *
 * State    — Booking owns its state machine; BookingService drives transitions
 *            but never inspects raw status strings.
 * Observer — Observers are registered on the Booking at creation time.
 *            Notifications fire automatically inside Booking.confirm/cancel/expire.
 * Strategy — PaymentStrategy is passed in by the caller; BookingService
 *            forwards it to PaymentService unchanged.
 *
 * Saga-style compensation:
 *   seat lock fails   → release already-locked seats in this batch
 *   payment fails     → release all locked seats, cancel booking
 *   TTL fires         → same compensation via scheduler callback
 */
public class BookingService {

    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);

    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final EventService eventService;
    private final PaymentService paymentService;
    private final SeatLockService seatLockService;
    private final List<BookingObserver> globalObservers = new ArrayList<>();

    public BookingService(EventService eventService,
                          PaymentService paymentService,
                          SeatLockService seatLockService) {
        this.eventService = eventService;
        this.paymentService = paymentService;
        this.seatLockService = seatLockService;
    }

    public void registerObserver(BookingObserver observer) {
        globalObservers.add(observer);
    }

    // ── Step 1: Reserve ─────────────────────────────────────────────────────────

    public Booking initiateBooking(String userId, String eventId, List<String> seatIds) {
        if (seatIds == null || seatIds.isEmpty())
            throw new IllegalArgumentException("Must select at least one seat");

        Event event = eventService.getEvent(eventId);
        if (event.getStatus() != EventStatus.ACTIVE)
            throw new IllegalStateException("Event is not available: " + event.getStatus());

        List<Seat> seats = event.getSeatsByIds(seatIds);
        String bookingId = UUID.randomUUID().toString();
        List<Seat> lockedSoFar = new ArrayList<>();

        try {
            for (Seat seat : seats) {
                if (!seat.tryLock(bookingId, LOCK_DURATION))
                    throw new SeatNotAvailableException("Seat " + seat.getSeatId() + " is no longer available");
                lockedSoFar.add(seat);
            }

            double total = seats.stream().mapToDouble(Seat::getPrice).sum();
            Booking booking = new Booking(bookingId, userId, eventId, seatIds, total);

            // Register all global observers on this booking (Observer pattern wired here)
            globalObservers.forEach(booking::addObserver);

            bookings.put(bookingId, booking);
            seatLockService.scheduleExpiry(bookingId, () -> expireBooking(bookingId, seats), LOCK_DURATION);
            return booking;

        } catch (SeatNotAvailableException e) {
            lockedSoFar.forEach(s -> s.release(bookingId));
            throw e;
        }
    }

    // ── Step 2: Pay and confirm ──────────────────────────────────────────────────

    public Booking confirmBooking(String bookingId, PaymentStrategy strategy) {
        Booking booking = getBookingOrThrow(bookingId);

        // State pattern: Booking.confirm() will throw if not in PENDING state
        // We validate here first to fail fast before charging the card
        if (booking.getStatus() != BookingStatus.PENDING)
            throw new IllegalStateException("Cannot confirm booking in state: " + booking.getStatus());

        try {
            Payment payment = paymentService.processPayment(
                    bookingId, booking.getUserId(), booking.getTotalAmount(), strategy);

            Event event = eventService.getEvent(booking.getEventId());
            booking.getSeatIds().forEach(seatId -> event.getSeat(seatId).confirm(bookingId));

            booking.confirm(payment.getPaymentId()); // fires observers
            seatLockService.cancelExpiry(bookingId);
            return booking;

        } catch (PaymentException e) {
            cancelBookingInternal(booking);
            throw e;
        }
    }

    // ── Cancel ──────────────────────────────────────────────────────────────────

    public Booking cancelBooking(String bookingId) {
        Booking booking = getBookingOrThrow(bookingId);
        cancelBookingInternal(booking);
        return booking;
    }

    // ── Internal ─────────────────────────────────────────────────────────────────

    private void cancelBookingInternal(Booking booking) {
        Event event = eventService.getEvent(booking.getEventId());
        booking.getSeatIds().forEach(seatId -> event.getSeat(seatId).release(booking.getBookingId()));
        booking.cancel(); // fires observers
        seatLockService.cancelExpiry(booking.getBookingId());
    }

    void expireBooking(String bookingId, List<Seat> seats) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) return;

        synchronized (booking) {
            if (booking.getStatus() != BookingStatus.PENDING) return;
            seats.forEach(s -> s.release(bookingId));
            booking.expire(); // fires observers
        }
    }

    public Booking getBooking(String bookingId) {
        return getBookingOrThrow(bookingId);
    }

    private Booking getBookingOrThrow(String bookingId) {
        Booking b = bookings.get(bookingId);
        if (b == null) throw new BookingNotFoundException(bookingId);
        return b;
    }
}