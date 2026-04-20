package lld.movieticketbooking.service;

public class UpiPayment implements PaymentStrategy {
    private final String upiId;

    public UpiPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public void pay() {
        System.out.println("Payment via UPI: " + upiId);
    }

    @Override
    public void cancel() {
        System.out.println("UPI payment refunded to: " + upiId);
    }
}
