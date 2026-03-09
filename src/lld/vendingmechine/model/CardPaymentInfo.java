package lld.vendingmechine.model;

public class CardPaymentInfo extends  PaymentInfo {
    public String cardNumber;
    public String cardHolderName;
    public String expiryDate;
    public String cvv;

    public CardPaymentInfo(String paymentType, double amount, String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        super("CARD", amount);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
}