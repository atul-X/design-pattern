package lld.movieticketbooking.service;

import lld.movieticketbooking.model.Seat;
import lld.movieticketbooking.model.Show;
import lld.movieticketbooking.service.observer.BookingEventPublisher;

import java.util.List;

public class MovieTicketContext {
    private Show selectedShow;
    private List<Seat> selectedSeats;
    private double amount;
    private MovieTicketStates state;

    public MovieTicketContext(BookingEventPublisher publisher) {
        this.state = new SelectShowState(publisher);
    }

    public void setState(MovieTicketStates state) { this.state = state; }
    public MovieTicketStates getState() { return state; }

    public void setSelectedShow(Show show) { this.selectedShow = show; }
    public Show getSelectedShow() { return selectedShow; }

    public void setSelectedSeats(List<Seat> seats) { this.selectedSeats = seats; }
    public List<Seat> getSelectedSeats() { return selectedSeats; }

    public void setAmount(double amount) { this.amount = amount; }
    public double getAmount() { return amount; }

    public void selectShow(Show show) {
        this.selectedShow = show;
        state.selectShow(this);
    }

    public void selectSeats(List<Seat> seats) {
        this.selectedSeats = seats;
        this.amount = seats.stream().mapToDouble(Seat::getPrice).sum();
        state.selectSeats(this);
    }

    public void pay(PaymentStrategy paymentStrategy) {
        paymentStrategy.pay();
        state.pay(this);
    }

    public void book() {
        state.book(this);
    }

    public void cancel() {
        state.cancel(this);
    }
}
