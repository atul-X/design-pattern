package lld.ticketmaster.pattern.state;

import lld.ticketmaster.enums.BookingStatus;

/**
 * State pattern for the booking lifecycle.
 *
 * Each concrete state validates whether a transition is legal.
 * Booking delegates to the current state before performing the change —
 * so invalid transitions are caught by the state, not by scattered if-else
 * checks spread across BookingService.
 *
 * Transitions:
 *   PENDING  → CONFIRMED | CANCELLED | EXPIRED
 *   CONFIRMED → (terminal)
 *   CANCELLED → (terminal)
 *   EXPIRED   → (terminal)
 */
public interface BookingState {
    void validateConfirm();
    void validateCancel();
    void validateExpire();
    BookingStatus status();
}