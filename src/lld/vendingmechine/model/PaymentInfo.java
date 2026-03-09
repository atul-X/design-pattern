package lld.vendingmechine.model;

public class PaymentInfo {
    private String paymentType;
    private double amount;

    public PaymentInfo(String paymentType, double amount) {
        this.paymentType = paymentType;
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public double getAmount() {
        return amount;
    }
}
