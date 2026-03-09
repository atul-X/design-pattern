package lld.vendingmechine.model;

public class CoinPaymentInfo extends PaymentInfo {
    private Coin coin;

    public CoinPaymentInfo(Coin coin) {
        super("COIN", coin.getValue());
        this.coin = coin;
    }

    public Coin getCoin() {
        return coin;
    }
}
