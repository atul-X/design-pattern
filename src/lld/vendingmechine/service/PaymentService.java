package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.PaymentResult;

import java.util.List;

public class PaymentService {
    private PaymentStrategy paymentStrategy;
//    private List<PaymentObserver>

    public PaymentService(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = new CoinPaymentStrategy();
    }
    public PaymentResult processPayment(double amount){
        return paymentStrategy.processPayment(amount);
    }
    public boolean insertCoin(Coin coin){
        if (paymentStrategy instanceof CoinPaymentStrategy) {
            CoinPaymentStrategy coinPaymentStrategy=(CoinPaymentStrategy) paymentStrategy;
            return coinPaymentStrategy.insertCoin(coin);
        }
        return false;
    }
    public double getTotalInserted(){
        if (paymentStrategy instanceof CoinPaymentStrategy) {
            CoinPaymentStrategy coinPaymentStrategy=(CoinPaymentStrategy) paymentStrategy;
            return coinPaymentStrategy.getTotalAmount();
        }
        return 0;
    }
    public List<Coin> getInsertedCoins(){
        if (paymentStrategy instanceof CoinPaymentStrategy) {
            CoinPaymentStrategy coinPaymentStrategy=(CoinPaymentStrategy) paymentStrategy;
            return coinPaymentStrategy.getInsertedCoins();
        }
        return null;
    }
    public void clearInsertedCoins() {
        if (paymentStrategy instanceof CoinPaymentStrategy) {
            CoinPaymentStrategy coinStrategy = (CoinPaymentStrategy) paymentStrategy;
            coinStrategy.clearInsertedCoins();
        }
    }
}
