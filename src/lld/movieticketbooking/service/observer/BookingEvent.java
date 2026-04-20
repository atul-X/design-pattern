package lld.movieticketbooking.service.observer;

import lld.movieticketbooking.model.Seat;
import lld.movieticketbooking.model.Show;

import java.util.List;

public class BookingEvent {
    private final Show show;
    private final List<Seat> bookedSeats;
    private final double totalAmount;

    public BookingEvent(Show show, List<Seat> bookedSeats, double totalAmount) {
        this.show = show;
        this.bookedSeats = bookedSeats;
        this.totalAmount = totalAmount;
    }

    public Show getShow()               { return show; }
    public List<Seat> getBookedSeats()  { return bookedSeats; }
    public double getTotalAmount()      { return totalAmount; }
}
