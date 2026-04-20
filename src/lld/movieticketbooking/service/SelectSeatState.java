package lld.movieticketbooking.service;

import lld.movieticketbooking.service.observer.BookingEventPublisher;

public class SelectSeatState implements MovieTicketStates {

    private final BookingEventPublisher publisher;

    public SelectSeatState(BookingEventPublisher publisher) {
        this.publisher = publisher;
    }
    @Override
    public void selectShow(MovieTicketContext context) {
        System.out.println("Show already selected.");
    }

    @Override
    public void selectSeats(MovieTicketContext context) {
        System.out.println("Seats selected. Total amount: " + context.getAmount());
        context.setState(new PayState(publisher));
    }

    @Override
    public void pay(MovieTicketContext context) {
        System.out.println("Please select seats first.");
    }

    @Override
    public void book(MovieTicketContext context) {
        System.out.println("Please select seats first.");
    }

    @Override
    public void cancel(MovieTicketContext context) {
        context.setState(new CancelState());
        context.cancel();
    }
}
