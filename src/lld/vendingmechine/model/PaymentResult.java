package lld.vendingmechine.model;

public class PaymentResult {
    private boolean success;
    private double amount;
    private double change;
    private double amountNeeded;

    public PaymentResult(boolean success, double amount, double change, double amountNeeded) {
        this.success = success;
        this.amount = amount;
        this.change = change;
        this.amountNeeded = amountNeeded;
    }
    public PaymentResult(boolean success, double amount, double change) {
        this.success = success;
        this.amount = amount;
        this.change = change;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getAmount() {
        return amount;
    }

    public double getChange() {
        return change;
    }

    public double getAmountNeeded() {
        return amountNeeded;
    }
}
