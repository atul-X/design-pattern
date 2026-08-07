package lld.ticketmaster.pattern.strategy;

import lld.ticketmaster.enums.PaymentMethod;
import lld.ticketmaster.exception.PaymentException;
import lld.ticketmaster.model.Payment;

public class CreditCardStrategy implements PaymentStrategy {

    private final String maskedCardNumber;

    public CreditCardStrategy(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    @Override
    public void execute(Payment payment) {
        // Real impl: call Stripe/Adyen with tokenised card
        System.out.printf("[Credit Card] charging ¥%.0f to card %s%n",
                payment.getAmount(), maskedCardNumber);
        payment.markSuccess();
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.CREDIT_CARD;
    }
}