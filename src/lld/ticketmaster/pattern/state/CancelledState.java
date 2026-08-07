package lld.ticketmaster.pattern.state;

import lld.ticketmaster.enums.BookingStatus;

public class CancelledState implements BookingState {
    @Override public void validateConfirm() { throw new IllegalStateException("Booking is already cancelled"); }
    @Override public void validateCancel()  { throw new IllegalStateException("Booking is already cancelled"); }
    @Override public void validateExpire()  { throw new IllegalStateException("Booking is already cancelled"); }
    @Override public BookingStatus status() { return BookingStatus.CANCELLED; }
}