package lld.movieticketbooking.service.observer;

import java.util.ArrayList;
import java.util.List;

public class BookingEventPublisher {
    private final List<BookingObserver> observers = new ArrayList<>();

    public void register(BookingObserver observer) {
        observers.add(observer);
    }

    public void unregister(BookingObserver observer) {
        observers.remove(observer);
    }

    public void notify(BookingEvent event) {
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(event);
        }
    }
}
