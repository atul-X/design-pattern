package lld.movieticketbooking.service.observer;

public class InventoryObserver implements BookingObserver {
    @Override
    public void onBookingConfirmed(BookingEvent event) {
        long availableSeats = event.getShow().getSeats().stream()
            .filter(seat -> !seat.isBooked())
            .count();
        System.out.println("[INVENTORY] Show " + event.getShow().getShowId()
            + " | Booked: " + event.getBookedSeats().size()
            + " seats | Remaining: " + availableSeats + " seats");
    }
}
