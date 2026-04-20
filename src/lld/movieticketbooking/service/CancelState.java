package lld.movieticketbooking.service;

public class CancelState implements MovieTicketStates {
    @Override
    public void selectShow(MovieTicketContext context) {
        System.out.println("Booking cancelled. Start a new booking.");
    }

    @Override
    public void selectSeats(MovieTicketContext context) {
        System.out.println("Booking cancelled. Start a new booking.");
    }

    @Override
    public void pay(MovieTicketContext context) {
        System.out.println("Booking cancelled. Start a new booking.");
    }

    @Override
    public void book(MovieTicketContext context) {
        System.out.println("Booking cancelled. Start a new booking.");
    }

    @Override
    public void cancel(MovieTicketContext context) {
        context.getSelectedSeats().forEach(seat -> seat.setBooked(false));
        System.out.println("Booking cancelled successfully.");
    }
}
