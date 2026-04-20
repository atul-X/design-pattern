package lld.movieticketbooking.service;

public class CardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay() {
        System.out.println("Payment via Card ending: " + cardNumber.substring(cardNumber.length() - 4));
    }

    @Override
    public void cancel() {
        System.out.println("Card payment refunded to card ending: " + cardNumber.substring(cardNumber.length() - 4));
    }
}
