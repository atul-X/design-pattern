package lld.ticketmaster.pattern.strategy;

import lld.ticketmaster.enums.PaymentMethod;
import lld.ticketmaster.exception.PaymentException;
import lld.ticketmaster.model.Payment;

public class PayPayWalletStrategy implements PaymentStrategy {

    @Override
    public void execute(Payment payment) {
        // Real impl: call PayPay Open Payment API, check wallet balance, deduct
        System.out.printf("[PayPay Wallet] charging ¥%.0f from user=%s%n",
                payment.getAmount(), payment.getUserId());
        payment.markSuccess();
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.PAYPAY_WALLET;
    }
}