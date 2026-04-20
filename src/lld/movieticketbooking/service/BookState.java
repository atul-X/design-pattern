package lld.movieticketbooking.service;

import lld.movieticketbooking.model.Seat;
import lld.movieticketbooking.model.Show;
import lld.movieticketbooking.service.observer.BookingEvent;
import lld.movieticketbooking.service.observer.BookingEventPublisher;

import java.util.ArrayList;
import java.util.List;

public class BookState implements MovieTicketStates {

    private final BookingEventPublisher publisher;

    public BookState(BookingEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void selectShow(MovieTicketContext context) {
        System.out.println("Ticket already booked.");
    }

    @Override
    public void selectSeats(MovieTicketContext context) {
        System.out.println("Ticket already booked.");
    }

    @Override
    public void pay(MovieTicketContext context) {
        System.out.println("Payment already done.");
    }

    @Override
    public void book(MovieTicketContext context) {
        Show show = context.getSelectedShow();
        synchronized (show) {
            // check each seat is still available before booking
            List<String> alreadyBooked = new ArrayList<>();
            for (Seat seat : context.getSelectedSeats()) {
                if (seat.isBooked()) {
                    alreadyBooked.add(seat.getName());
                }
            }
            if (!alreadyBooked.isEmpty()) {
                throw new IllegalStateException("Seats already booked: " + alreadyBooked);
            }
            // mark seats booked
            context.getSelectedSeats().forEach(seat -> seat.setBooked(true));
            System.out.println("Booking confirmed for show: " + show.getMovie().getName());

            // fire event to all observers
            BookingEvent event = new BookingEvent(show, context.getSelectedSeats(), context.getAmount());
            publisher.notify(event);
        }
    }

    @Override
    public void cancel(MovieTicketContext context) {
        context.setState(new CancelState());
        context.cancel();
    }
}
