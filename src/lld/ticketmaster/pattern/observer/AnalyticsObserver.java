package lld.ticketmaster.pattern.observer;

import lld.ticketmaster.enums.BookingStatus;
import lld.ticketmaster.model.Booking;

public class AnalyticsObserver implements BookingObserver {

    @Override
    public void onStateChange(Booking booking, BookingStatus from, BookingStatus to) {
        // In prod: emit to Kafka / BigQuery / DataDog
        System.out.printf("[ANALYTICS] event=booking_state_change | from=%s | to=%s | amount=%.0f | seats=%s%n",
                from, to, booking.getTotalAmount(), booking.getSeatIds());
    }
}