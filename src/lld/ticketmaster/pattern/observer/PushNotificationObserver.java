package lld.ticketmaster.pattern.observer;

import lld.ticketmaster.enums.BookingStatus;
import lld.ticketmaster.model.Booking;

public class PushNotificationObserver implements BookingObserver {

    @Override
    public void onStateChange(Booking booking, BookingStatus from, BookingStatus to) {
        if (to == BookingStatus.CONFIRMED || to == BookingStatus.CANCELLED) {
            System.out.printf("[PUSH]  → user=%s | ¥%.0f booking %s%n",
                    booking.getUserId(), booking.getTotalAmount(), to.name().toLowerCase());
        }
    }
}