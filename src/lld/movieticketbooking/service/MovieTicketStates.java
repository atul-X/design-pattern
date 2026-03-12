package lld.movieticketbooking.service;

public interface MovieTicketStates {
    public void  selectShow(MovieTicketContext context);
    public void selectSeats(MovieTicketContext context);
    public void pay(MovieTicketContext context);
    public void book(MovieTicketContext context);
    public void cancel(MovieTicketContext context);
}
