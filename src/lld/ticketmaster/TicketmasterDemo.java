package lld.ticketmaster;

import lld.ticketmaster.enums.SeatType;
import lld.ticketmaster.exception.SeatNotAvailableException;
import lld.ticketmaster.model.Booking;
import lld.ticketmaster.model.Event;
import lld.ticketmaster.model.Seat;
import lld.ticketmaster.model.Venue;
import lld.ticketmaster.pattern.observer.AnalyticsObserver;
import lld.ticketmaster.pattern.observer.EmailNotificationObserver;
import lld.ticketmaster.pattern.observer.PushNotificationObserver;
import lld.ticketmaster.pattern.strategy.CreditCardStrategy;
import lld.ticketmaster.pattern.strategy.NetBankingStrategy;
import lld.ticketmaster.pattern.strategy.PayPayWalletStrategy;
import lld.ticketmaster.service.BookingService;
import lld.ticketmaster.service.EventService;
import lld.ticketmaster.service.PaymentService;
import lld.ticketmaster.service.SeatLockService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketmasterDemo {

    public static void main(String[] args) throws InterruptedException {

        // ── Bootstrap ────────────────────────────────────────────────────────────
        EventService eventService     = new EventService();
        PaymentService paymentService = new PaymentService();
        SeatLockService lockService   = new SeatLockService();
        BookingService bookingService = new BookingService(eventService, paymentService, lockService);

        // Observer pattern — register once globally; every new Booking gets all three
        bookingService.registerObserver(new EmailNotificationObserver());
        bookingService.registerObserver(new PushNotificationObserver());
        bookingService.registerObserver(new AnalyticsObserver());

        // ── Seed data ────────────────────────────────────────────────────────────
        Venue tokyoDome = new Venue("V1", "Tokyo Dome", "Tokyo", 55000);

        List<Seat> seats = List.of(
            new Seat("S1", "SEC-A", "A", 1, SeatType.VIP,      50_000.0),
            new Seat("S2", "SEC-A", "A", 2, SeatType.VIP,      50_000.0),
            new Seat("S3", "SEC-B", "B", 1, SeatType.PREMIUM,  30_000.0),
            new Seat("S4", "SEC-C", "C", 1, SeatType.STANDARD, 15_000.0),
            new Seat("S5", "SEC-C", "C", 2, SeatType.STANDARD, 15_000.0)
        );

        Event event = new Event("E1", "Taylor Swift | Eras Tour", "Taylor Swift",
                tokyoDome, Instant.now().plus(Duration.ofDays(30)), seats);
        eventService.addEvent(event);

        // ── Test 1: Strategy pattern — PayPay Wallet ─────────────────────────────
        separator("Test 1 — Alice pays with PayPay Wallet (Strategy)");

        Booking aliceBooking = bookingService.initiateBooking("U1", "E1", List.of("S1", "S2"));
        System.out.println("Initiated : " + aliceBooking);

        aliceBooking = bookingService.confirmBooking(aliceBooking.getBookingId(),
                new PayPayWalletStrategy()); // inject strategy
        System.out.println("Confirmed : " + aliceBooking);

        // ── Test 2: State pattern — invalid transition ────────────────────────────
        separator("Test 2 — State: replay confirm on an already-CONFIRMED booking");

        final String aliceBookingId = aliceBooking.getBookingId();
        try {
            bookingService.confirmBooking(aliceBookingId, new PayPayWalletStrategy());
        } catch (IllegalStateException e) {
            System.out.println("Caught (expected — ConfirmedState rejects): " + e.getMessage());
        }

        // ── Test 3: Strategy pattern — Credit Card ────────────────────────────────
        separator("Test 3 — Bob pays with Credit Card (Strategy)");

        Booking bobBooking = bookingService.initiateBooking("U2", "E1", List.of("S3", "S4"));
        bobBooking = bookingService.confirmBooking(bobBooking.getBookingId(),
                new CreditCardStrategy("****-****-****-4242"));
        System.out.println("Confirmed : " + bobBooking);

        // ── Test 4: Cancel → observers fire ──────────────────────────────────────
        separator("Test 4 — Charlie books S5 then cancels (Observer fires on cancel)");

        Booking charlieBooking = bookingService.initiateBooking("U3", "E1", List.of("S5"));
        System.out.println("Pending   : " + charlieBooking);
        bookingService.cancelBooking(charlieBooking.getBookingId());
        System.out.println("After cancel status: " + charlieBooking.getStatus());
        System.out.println("S5 available again  : " + event.getAvailableSeats().stream()
                .anyMatch(s -> s.getSeatId().equals("S5")));

        // ── Test 5: Race condition — two threads fight for S5 ────────────────────
        separator("Test 5 — Race: two threads try to grab the just-released S5");

        CountDownLatch gun = new CountDownLatch(1);
        AtomicInteger wins = new AtomicInteger(0);

        Runnable raceTask = () -> {
            String name = Thread.currentThread().getName();
            try {
                gun.await();
                Booking b = bookingService.initiateBooking(name, "E1", List.of("S5"));
                bookingService.confirmBooking(b.getBookingId(), new NetBankingStrategy("SMBC"));
                wins.incrementAndGet();
                System.out.println(name + " → WON. Booking confirmed.");
            } catch (SeatNotAvailableException e) {
                System.out.println(name + " → LOST: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread t1 = new Thread(raceTask, "Thread-Alice2");
        Thread t2 = new Thread(raceTask, "Thread-Bob2");
        t1.start(); t2.start();
        gun.countDown();
        t1.join(); t2.join();
        System.out.println("Only 1 should win. Wins = " + wins.get());

        // ── Final state ──────────────────────────────────────────────────────────
        separator("Final Seat State");
        event.getSeats().values().forEach(s ->
                System.out.printf("  %-4s %-10s %s%n", s.getSeatId(), s.getType(), s.getStatus()));

        lockService.shutdown();
    }

    private static void separator(String title) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("  " + title);
        System.out.println("─".repeat(60));
    }
}