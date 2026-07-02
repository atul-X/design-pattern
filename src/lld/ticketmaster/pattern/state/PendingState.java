package lld.ticketmaster.pattern.state;

import lld.ticketmaster.enums.BookingStatus;

public class PendingState implements BookingState {
    @Override public void validateConfirm() { /* allowed */ }
    @Override public void validateCancel()  { /* allowed */ }
    @Override public void validateExpire()  { /* allowed */ }
    @Override public BookingStatus status() { return BookingStatus.PENDING; }
}