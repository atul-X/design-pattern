package lld.movieticketbooking.service.observer;

public interface BookingObserver {
    void onBookingConfirmed(BookingEvent event);
}
