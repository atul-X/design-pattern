package lld.ticketmaster.pattern.observer;

import lld.ticketmaster.enums.BookingStatus;
import lld.ticketmaster.model.Booking;

public interface BookingObserver {
    void onStateChange(Booking booking, BookingStatus from, BookingStatus to);
}