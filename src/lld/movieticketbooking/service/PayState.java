package lld.movieticketbooking.service;

import lld.movieticketbooking.service.observer.BookingEventPublisher;

public class PayState implements MovieTicketStates {

    private final BookingEventPublisher publisher;

    public PayState(BookingEventPublisher publisher) {
        this.publisher = publisher;
    }
    @Override
    public void selectShow(MovieTicketContext context) {
        System.out.println("Show already selected.");
    }

    @Override
    public void selectSeats(MovieTicketContext context) {
        System.out.println("Seats already selected.");
    }

    @Override
    public void pay(MovieTicketContext context) {
        System.out.println("Payment successful. Amount paid: " + context.getAmount());
        context.setState(new BookState(publisher));
    }

    @Override
    public void book(MovieTicketContext context) {
        System.out.println("Please complete payment first.");
    }

    @Override
    public void cancel(MovieTicketContext context) {
        context.setState(new CancelState());
        context.cancel();
    }
}
