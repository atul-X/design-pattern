package lld.vendingmechine.service;

import lld.vendingmechine.model.PaymentInfo;
import lld.vendingmechine.model.PaymentResult;

public interface PaymentStrategy {
    PaymentResult processPayment(double amount);
    boolean validatePayment(PaymentInfo paymentInfo);
    void refund(PaymentInfo paymentInfo);
    String getPaymentType();
}
