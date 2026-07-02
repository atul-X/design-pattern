package lld.ticketmaster.model;

import lld.ticketmaster.enums.BookingStatus;
import lld.ticketmaster.pattern.observer.BookingObserver;
import lld.ticketmaster.pattern.state.BookingState;
import lld.ticketmaster.pattern.state.CancelledState;
import lld.ticketmaster.pattern.state.ConfirmedState;
import lld.ticketmaster.pattern.state.ExpiredState;
import lld.ticketmaster.pattern.state.PendingState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Booking combines two patterns:
 *
 * State    — currentState validates each transition; illegal moves throw before
 *            any field is mutated, so the object is never left in a partial state.
 *
 * Observer — registered listeners are notified after every state change.
 *            BookingService registers Email / Push / Analytics observers at creation
 *            time; no other code needs to know they exist.
 */
public class Booking {

    private final String bookingId;
    private final String userId;
    private final String eventId;
    private final List<String> seatIds;
    private final double totalAmount;
    private final Instant createdAt;

    // State pattern
    private BookingState currentState;

    // Mutable after state transitions
    private String paymentId;
    private Instant updatedAt;

    // Observer pattern
    private final List<BookingObserver> observers = new ArrayList<>();

    public Booking(String bookingId, String userId, String eventId,
                   List<String> seatIds, double totalAmount) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.eventId = eventId;
        this.seatIds = List.copyOf(seatIds);
        this.totalAmount = totalAmount;
        this.currentState = new PendingState();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // ── Observer registration ────────────────────────────────────────────────────

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    private void notify(BookingStatus from, BookingStatus to) {
        observers.forEach(o -> o.onStateChange(this, from, to));
    }

    // ── State transitions ────────────────────────────────────────────────────────

    public synchronized void confirm(String paymentId) {
        BookingStatus prev = currentState.status();
        currentState.validateConfirm();           // throws if not PENDING
        this.paymentId = paymentId;
        this.currentState = new ConfirmedState();
        this.updatedAt = Instant.now();
        notify(prev, BookingStatus.CONFIRMED);
    }

    public synchronized void cancel() {
        BookingStatus prev = currentState.status();
        currentState.validateCancel();            // throws if already terminal
        this.currentState = new CancelledState();
        this.updatedAt = Instant.now();
        notify(prev, BookingStatus.CANCELLED);
    }

    public synchronized void expire() {
        BookingStatus prev = currentState.status();
        currentState.validateExpire();            // throws if already terminal
        this.currentState = new ExpiredState();
        this.updatedAt = Instant.now();
        notify(prev, BookingStatus.EXPIRED);
    }

    // ── Accessors ────────────────────────────────────────────────────────────────

    public String getBookingId()      { return bookingId; }
    public String getUserId()         { return userId; }
    public String getEventId()        { return eventId; }
    public List<String> getSeatIds()  { return seatIds; }
    public double getTotalAmount()    { return totalAmount; }
    public BookingStatus getStatus()  { return currentState.status(); }
    public String getPaymentId()      { return paymentId; }
    public Instant getCreatedAt()     { return createdAt; }
    public Instant getUpdatedAt()     { return updatedAt; }

    @Override
    public String toString() {
        return String.format("Booking[%s | user=%s | seats=%s | ¥%.0f | %s | payment=%s]",
                bookingId, userId, seatIds, totalAmount, getStatus(), paymentId);
    }
}