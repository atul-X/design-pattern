package lld.ticketmaster.pattern.strategy;

import lld.ticketmaster.enums.PaymentMethod;
import lld.ticketmaster.model.Payment;

public class NetBankingStrategy implements PaymentStrategy {

    private final String bankCode;

    public NetBankingStrategy(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public void execute(Payment payment) {
        System.out.printf("[Net Banking] initiating ¥%.0f transfer via bank=%s%n",
                payment.getAmount(), bankCode);
        payment.markSuccess();
    }

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.NET_BANKING;
    }
}