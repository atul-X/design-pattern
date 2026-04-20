package lld.movieticketbooking.service.observer;

public class EmailObserver implements BookingObserver {
    @Override
    public void onBookingConfirmed(BookingEvent event) {
        System.out.println("[EMAIL] Booking confirmed for movie: "
            + event.getShow().getMovie().getName()
            + " | Seats: " + event.getBookedSeats().size()
            + " | Amount: ₹" + event.getTotalAmount());
    }
}
