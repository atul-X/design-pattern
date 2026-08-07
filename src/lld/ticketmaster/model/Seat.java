package lld.ticketmaster.model;

import lld.ticketmaster.enums.SeatStatus;
import lld.ticketmaster.enums.SeatType;

import java.time.Duration;
import java.time.Instant;

/**
 * Thread-safe seat with per-object locking.
 *
 * Interview note: synchronized on `this` gives per-seat granularity — different seats
 * can be locked concurrently. In a distributed system this becomes Redis SETNX + Lua
 * script so the check-and-set is atomic across nodes.
 */
public class Seat {
    private final String seatId;
    private final String sectionId;
    private final String row;
    private final int number;
    private final SeatType type;
    private final double price;

    // Mutable state — all mutations go through synchronized methods
    private SeatStatus status = SeatStatus.AVAILABLE;
    private String lockedByBookingId;
    private Instant lockExpiry;

    public Seat(String seatId, String sectionId, String row, int number, SeatType type, double price) {
        this.seatId = seatId;
        this.sectionId = sectionId;
        this.row = row;
        this.number = number;
        this.type = type;
        this.price = price;
    }

    /**
     * Atomic check-and-lock. Returns true only if the seat transitions AVAILABLE → LOCKED.
     * Any other state (LOCKED, BOOKED) returns false.
     */
    public synchronized boolean tryLock(String bookingId, Duration lockDuration) {
        releaseIfExpired();
        if (status != SeatStatus.AVAILABLE) return false;
        status = SeatStatus.LOCKED;
        lockedByBookingId = bookingId;
        lockExpiry = Instant.now().plus(lockDuration);
        return true;
    }

    /**
     * Transitions LOCKED → BOOKED only for the booking that owns the lock.
     * Called after successful payment.
     */
    public synchronized boolean confirm(String bookingId) {
        if (status != SeatStatus.LOCKED || !bookingId.equals(lockedByBookingId)) return false;
        status = SeatStatus.BOOKED;
        lockedByBookingId = null;
        lockExpiry = null;
        return true;
    }

    /**
     * Releases the lock back to AVAILABLE. Used on payment failure or booking cancellation.
     * Only the booking that owns the lock can release it.
     */
    public synchronized boolean release(String bookingId) {
        if (status == SeatStatus.LOCKED && bookingId.equals(lockedByBookingId)) {
            status = SeatStatus.AVAILABLE;
            lockedByBookingId = null;
            lockExpiry = null;
            return true;
        }
        return false;
    }

    public synchronized SeatStatus getStatus() {
        releaseIfExpired(); // lazy TTL expiry on every read
        return status;
    }

    // Called inside synchronized blocks only — no extra lock needed
    private void releaseIfExpired() {
        if (status == SeatStatus.LOCKED && lockExpiry != null && Instant.now().isAfter(lockExpiry)) {
            status = SeatStatus.AVAILABLE;
            lockedByBookingId = null;
            lockExpiry = null;
        }
    }

    public String getSeatId()   { return seatId; }
    public String getSectionId(){ return sectionId; }
    public String getRow()      { return row; }
    public int getNumber()      { return number; }
    public SeatType getType()   { return type; }
    public double getPrice()    { return price; }

    @Override
    public String toString() {
        return String.format("Seat[%s | Row %s-%d | %s | ¥%.0f | %s]",
                seatId, row, number, type, price, status);
    }
}