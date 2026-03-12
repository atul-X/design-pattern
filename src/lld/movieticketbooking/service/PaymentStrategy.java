package lld.movieticketbooking.service;

public interface PaymentStrategy {
    public void pay();
    public void cancel();
}
