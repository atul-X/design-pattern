package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChangeService {

    private Map<Coin, Integer> availableCoins;

    public ChangeService() {
        // Initialize with some coins
        availableCoins = new HashMap<>();
        availableCoins.put(Coin.DOLLAR, 20);
        availableCoins.put(Coin.QUARTER, 40);
        availableCoins.put(Coin.DIME, 50);
        availableCoins.put(Coin.NICKEL, 40);
        availableCoins.put(Coin.PENNY, 100);
    }

    public Map<Coin, Integer> calculateChange(double amount) {
        Map<Coin, Integer> change = new LinkedHashMap<>();
        double remainingAmount = amount;

        // Sort coins by value (descending)
        Coin[] sortedCoins = Coin.values();
        Arrays.sort(sortedCoins, (a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Coin coin : sortedCoins) {
            if (remainingAmount >= coin.getValue()) {
                int count = (int) (remainingAmount / coin.getValue());
                int availableCount = availableCoins.getOrDefault(coin, 0);
                int useCount = Math.min(count, availableCount);

                if (useCount > 0) {
                    change.put(coin, useCount);
                    remainingAmount -= useCount * coin.getValue();
                    availableCoins.put(coin, availableCount - useCount);
                }
            }
        }

        if (Math.abs(remainingAmount) > 0.01) {
            System.out.println("Warning: Cannot make exact change. Missing: $" +
                    String.format("%.2f", remainingAmount));
        }

        return change;
    }

    public void addCoins(Coin coin, int count) {
        availableCoins.merge(coin, count, Integer::sum);
    }

    public Map<Coin, Integer> getAvailableCoins() {
        return new HashMap<>(availableCoins);
    }
}
