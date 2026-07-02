package lld.ticketmaster.pattern.observer;

import lld.ticketmaster.enums.BookingStatus;
import lld.ticketmaster.model.Booking;

public class EmailNotificationObserver implements BookingObserver {

    @Override
    public void onStateChange(Booking booking, BookingStatus from, BookingStatus to) {
        String subject = buildSubject(to);
        System.out.printf("[EMAIL] → user=%s | booking=%s | %s → %s | subject: \"%s\"%n",
                booking.getUserId(), booking.getBookingId(), from, to, subject);
    }

    private String buildSubject(BookingStatus to) {
        return switch (to) {
            case CONFIRMED  -> "Your tickets are confirmed! 🎉";
            case CANCELLED  -> "Your booking has been cancelled";
            case EXPIRED    -> "Your seat reservation expired";
            default         -> "Booking update";
        };
    }
}