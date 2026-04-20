package lld.movieticketbooking.service.observer;

public class SMSObserver implements BookingObserver {
    @Override
    public void onBookingConfirmed(BookingEvent event) {
        System.out.println("[SMS] Your booking for "
            + event.getShow().getMovie().getName()
            + " on " + event.getShow().getDate()
            + " at " + event.getShow().getTime()
            + " is confirmed. Total: ₹" + event.getTotalAmount());
    }
}
