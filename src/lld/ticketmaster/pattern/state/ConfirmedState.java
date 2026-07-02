package lld.ticketmaster.pattern.state;

import lld.ticketmaster.enums.BookingStatus;

public class ConfirmedState implements BookingState {
    @Override public void validateConfirm() {
        throw new IllegalStateException("Booking is already confirmed");
    }
    @Override public void validateCancel() {
        throw new IllegalStateException("Cannot cancel a confirmed booking — issue a refund instead");
    }
    @Override public void validateExpire() {
        throw new IllegalStateException("Cannot expire a confirmed booking");
    }
    @Override public BookingStatus status() { return BookingStatus.CONFIRMED; }
}