package lld.ticketmaster.pattern.state;

import lld.ticketmaster.enums.BookingStatus;

public class ExpiredState implements BookingState {
    @Override public void validateConfirm() { throw new IllegalStateException("Booking has expired"); }
    @Override public void validateCancel()  { throw new IllegalStateException("Booking has expired"); }
    @Override public void validateExpire()  { throw new IllegalStateException("Booking is already expired"); }
    @Override public BookingStatus status() { return BookingStatus.EXPIRED; }
}