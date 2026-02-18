package behavioural.observer.stockmarket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Observer - Trading Bot
 * Automated trading system that responds to stock price changes
 */
public class TradingBot implements StockObserver {
    private String botName;
    private String strategy;
    private double buyThreshold;
    private double sellThreshold;
    private double stopLossPercent;
    private double maxPositionSize;
    private double currentCash;
    private List<Position> positions;
    private List<String> tradeHistory;

    public TradingBot(String botName, String strategy, double initialCash) {
        this.botName = botName;
        this.strategy = strategy;
        this.currentCash = initialCash;
        this.positions = new ArrayList<>();
        this.tradeHistory = new ArrayList<>();

        // Set default thresholds based on strategy
        switch (strategy.toUpperCase()) {
            case "MOMENTUM":
                this.buyThreshold = 2.0;  // Buy on 2% increase
                this.sellThreshold = -1.5; // Sell on 1.5% decrease
                this.stopLossPercent = 5.0;
                this.maxPositionSize = initialCash * 0.2; // 20% max position
                break;
            case "CONTRARIAN":
                this.buyThreshold = -3.0; // Buy on 3% decrease (buy the dip)
                this.sellThreshold = 4.0;  // Sell on 4% increase
                this.stopLossPercent = 8.0;
                this.maxPositionSize = initialCash * 0.15; // 15% max position
                break;
            case "SCALPING":
                this.buyThreshold = 0.5;  // Buy on small 0.5% increase
                this.sellThreshold = -0.3; // Sell on small 0.3% decrease
                this.stopLossPercent = 2.0;
                this.maxPositionSize = initialCash * 0.1; // 10% max position
                break;
            default:
                this.buyThreshold = 1.0;
                this.sellThreshold = -1.0;
                this.stopLossPercent = 3.0;
                this.maxPositionSize = initialCash * 0.25;
        }
    }

    @Override
    public void onPriceUpdate(Stock stock, double oldPrice, double newPrice, double changePercent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);

        System.out.println("🤖 [" + timestamp + "] " + botName + " (" + strategy + ") analyzing " +
                         stock.getSymbol() + ": " + String.format("%.2f%%", changePercent) + " change");

        Position existingPosition = findPosition(stock.getSymbol());

        if (existingPosition == null) {
            // No existing position - consider buying
            if (shouldBuy(changePercent, newPrice)) {
                executeBuy(stock, newPrice);
            }
        } else {
            // Have existing position - consider selling
            if (shouldSell(changePercent, existingPosition, newPrice)) {
                executeSell(stock, existingPosition, newPrice);
            }
        }
    }

    @Override
    public void onMilestone(Stock stock, String milestoneType, String details) {
        System.out.println("🎯 " + botName + " received milestone for " + stock.getSymbol() +
                         ": " + milestoneType + " - " + details);

        Position position = findPosition(stock.getSymbol());

        switch (milestoneType) {
            case "52_WEEK_HIGH":
                if (position != null && strategy.equals("MOMENTUM")) {
                    System.out.println("   📈 Momentum strategy: Holding position on 52-week high");
                } else if (position != null && strategy.equals("CONTRARIAN")) {
                    System.out.println("   📉 Contrarian strategy: Consider taking profits on 52-week high");
                    executeSell(stock, position, stock.getCurrentPrice());
                }
                break;
            case "VOLUME_SPIKE":
                System.out.println("   📊 High volume detected - increased market interest");
                break;
            case "SIGNIFICANT_MOVE":
                if (position != null) {
                    System.out.println("   ⚡ Significant price movement - monitoring position closely");
                }
                break;
        }
    }

    @Override
    public void onTradingHalt(Stock stock, String reason) {
        System.out.println("⛔ " + botName + " notified: Trading halted for " + stock.getSymbol() +
                         " - Reason: " + reason);

        Position position = findPosition(stock.getSymbol());
        if (position != null) {
            System.out.println("   ⚠️ WARNING: Holding position in halted stock - " + position.getShares() + " shares");
        }
    }

    private boolean shouldBuy(double changePercent, double price) {
        if (currentCash < maxPositionSize) {
            return false; // Not enough cash
        }

        switch (strategy.toUpperCase()) {
            case "MOMENTUM":
                return changePercent >= buyThreshold;
            case "CONTRARIAN":
                return changePercent <= buyThreshold; // Negative threshold for contrarian
            case "SCALPING":
                return changePercent >= buyThreshold;
            default:
                return changePercent >= buyThreshold;
        }
    }

    private boolean shouldSell(double changePercent, Position position, double currentPrice) {
        // Check stop loss first
        double positionChangePercent = ((currentPrice - position.getBuyPrice()) / position.getBuyPrice()) * 100;
        if (positionChangePercent <= -stopLossPercent) {
            System.out.println("   🛑 Stop loss triggered: " + String.format("%.2f%%", positionChangePercent) + " loss");
            return true;
        }

        switch (strategy.toUpperCase()) {
            case "MOMENTUM":
                return changePercent <= sellThreshold; // Sell on negative momentum
            case "CONTRARIAN":
                return changePercent >= sellThreshold; // Sell on positive momentum
            case "SCALPING":
                return changePercent <= sellThreshold || positionChangePercent >= 1.0; // Quick profits
            default:
                return changePercent <= sellThreshold;
        }
    }

    private void executeBuy(Stock stock, double price) {
        int shares = (int) (maxPositionSize / price);
        if (shares > 0) {
            double cost = shares * price;
            currentCash -= cost;

            Position newPosition = new Position(stock.getSymbol(), shares, price);
            positions.add(newPosition);

            String trade = String.format("BUY %d shares of %s at $%.2f (Cost: $%.2f)",
                                       shares, stock.getSymbol(), price, cost);
            tradeHistory.add(trade);

            System.out.println("   💰 " + botName + " executed: " + trade);
            System.out.println("   💵 Remaining cash: $" + String.format("%.2f", currentCash));
        }
    }

    private void executeSell(Stock stock, Position position, double price) {
        double proceeds = position.getShares() * price;
        currentCash += proceeds;

        double profit = proceeds - (position.getShares() * position.getBuyPrice());
        double profitPercent = (profit / (position.getShares() * position.getBuyPrice())) * 100;

        String trade = String.format("SELL %d shares of %s at $%.2f (Proceeds: $%.2f, P/L: $%.2f [%.2f%%])",
                                   position.getShares(), stock.getSymbol(), price, proceeds, profit, profitPercent);
        tradeHistory.add(trade);

        positions.remove(position);

        System.out.println("   💸 " + botName + " executed: " + trade);
        System.out.println("   💵 New cash balance: $" + String.format("%.2f", currentCash));
    }

    private Position findPosition(String symbol) {
        return positions.stream()
                .filter(p -> p.getSymbol().equals(symbol))
                .findFirst()
                .orElse(null);
    }

    public void printPortfolio() {
        System.out.println("📋 " + botName + " Portfolio (" + strategy + " Strategy):");
        System.out.println("   Cash: $" + String.format("%.2f", currentCash));
        System.out.println("   Positions: " + positions.size());

        double totalValue = currentCash;
        for (Position position : positions) {
            System.out.println("   - " + position);
            // Note: In real implementation, we'd need current price to calculate market value
        }

        System.out.println("   Recent Trades: " + tradeHistory.size());
        if (!tradeHistory.isEmpty()) {
            int start = Math.max(0, tradeHistory.size() - 3);
            for (int i = start; i < tradeHistory.size(); i++) {
                System.out.println("     " + (i + 1) + ". " + tradeHistory.get(i));
            }
        }
    }

    @Override
    public String getObserverName() {
        return botName + " (Trading Bot)";
    }

    // Getters
    public String getBotName() { return botName; }
    public String getStrategy() { return strategy; }
    public double getCurrentCash() { return currentCash; }
    public List<Position> getPositions() { return new ArrayList<>(positions); }
    public List<String> getTradeHistory() { return new ArrayList<>(tradeHistory); }

    /**
     * Inner class to represent a trading position
     */
    private static class Position {
        private String symbol;
        private int shares;
        private double buyPrice;
        private LocalDateTime buyTime;

        public Position(String symbol, int shares, double buyPrice) {
            this.symbol = symbol;
            this.shares = shares;
            this.buyPrice = buyPrice;
            this.buyTime = LocalDateTime.now();
        }

        public String getSymbol() { return symbol; }
        public int getShares() { return shares; }
        public double getBuyPrice() { return buyPrice; }
        public LocalDateTime getBuyTime() { return buyTime; }

        @Override
        public String toString() {
            return String.format("%s: %d shares @ $%.2f", symbol, shares, buyPrice);
        }
    }
}
