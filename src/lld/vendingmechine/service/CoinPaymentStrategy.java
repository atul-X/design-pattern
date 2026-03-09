package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.CoinPaymentInfo;
import lld.vendingmechine.model.PaymentInfo;
import lld.vendingmechine.model.PaymentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinPaymentStrategy implements PaymentStrategy{
    private List<Coin> insertedCoins=new ArrayList<>();
    public double totalAmount=0.0;

    public List<Coin> getInsertedCoins() {
        return insertedCoins;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public PaymentResult processPayment(double amount) {
        if (totalAmount>=amount){
            PaymentResult result=
                    new PaymentResult(true,amount,amount-totalAmount,0);
            totalAmount=0;
            clearInsertedCoins();
            return result;
        }else {
            return new PaymentResult(false, totalAmount, amount - totalAmount);

        }
    }
    public void clearInsertedCoins() {
        insertedCoins.clear();
        totalAmount = 0.0;
    }
    public boolean insertCoin(Coin coin){
        if (validateCoin(coin)){
            insertedCoins.add(coin);
            totalAmount+=coin.getValue();
            return true;
        }
        return false;
    }
    @Override
    public boolean validatePayment(PaymentInfo paymentInfo) {
//        return false;
        if (paymentInfo instanceof CoinPaymentInfo) {
            CoinPaymentInfo coinPaymentInfo = (CoinPaymentInfo) paymentInfo;
            return validateCoin(coinPaymentInfo.getCoin());
        }
        return false;
    }
    private boolean validateCoin(Coin coin){
        return coin!=null &&isValidDenomination(coin);
    }
    private boolean isValidDenomination(Coin coin) {
        // Check if coin is a valid denomination
        return Arrays.stream(Coin.values())
                .anyMatch(validCoin -> validCoin.getValue() == coin.getValue());
    }

    @Override
    public void refund(PaymentInfo paymentInfo) {
        if (paymentInfo instanceof  CoinPaymentInfo){
            CoinPaymentInfo coinPaymentInfo=(CoinPaymentInfo)paymentInfo;
            System.out.println("refunding coint amount"+coinPaymentInfo.getCoin().getValue());
        }
    }

    @Override
    public String getPaymentType() {
        return "COIN";
    }
}
