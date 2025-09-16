package behavioural.observer.stockmarket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete Observer - Portfolio Manager
 * Tracks portfolio performance and manages risk across multiple stocks
 */
public class PortfolioManager implements StockObserver {
    private String managerName;
    private Map<String, StockPosition> holdings;
    private List<String> alerts;
    private double totalPortfolioValue;
    private double maxRiskPerStock;
    private double portfolioRiskLimit;

    public PortfolioManager(String managerName, double initialValue) {
        this.managerName = managerName;
        this.holdings = new HashMap<>();
        this.alerts = new ArrayList<>();
        this.totalPortfolioValue = initialValue;
        this.maxRiskPerStock = 0.05; // 5% max risk per stock
        this.portfolioRiskLimit = 0.20; // 20% max portfolio risk
    }

    @Override
    public void onPriceUpdate(Stock stock, double oldPrice, double newPrice, double changePercent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);

        StockPosition position = holdings.get(stock.getSymbol());
        if (position != null) {
            // Update position value
            double oldValue = position.getCurrentValue();
            position.updatePrice(newPrice);
            double newValue = position.getCurrentValue();
            double valueChange = newValue - oldValue;

            System.out.println("📊 [" + timestamp + "] " + managerName + " tracking " + stock.getSymbol() +
                             ": Position value change $" + String.format("%.2f", valueChange));

            // Check for risk alerts
            checkRiskAlerts(stock, position, changePercent);

            // Update total portfolio value
            updatePortfolioValue();
        }
    }

    @Override
    public void onMilestone(Stock stock, String milestoneType, String details) {
        System.out.println("🏆 " + managerName + " milestone alert for " + stock.getSymbol() +
                         ": " + milestoneType + " - " + details);

        StockPosition position = holdings.get(stock.getSymbol());
        if (position != null) {
            String alert = String.format("[%s] %s milestone: %s - %s",
                                       LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                       stock.getSymbol(), milestoneType, details);
            alerts.add(alert);

            // Take action based on milestone type
            switch (milestoneType) {
                case "52_WEEK_HIGH":
                    if (position.getUnrealizedGainPercent() > 20) {
                        System.out.println("   💡 Recommendation: Consider taking partial profits on " + stock.getSymbol());
                    }
                    break;
                case "52_WEEK_LOW":
                    System.out.println("   💡 Recommendation: Review " + stock.getSymbol() + " fundamentals for potential opportunity");
                    break;
                case "VOLUME_SPIKE":
                    System.out.println("   💡 Recommendation: Monitor " + stock.getSymbol() + " for potential news or events");
                    break;
            }
        }
    }

    @Override
    public void onTradingHalt(Stock stock, String reason) {
        System.out.println("⛔ " + managerName + " URGENT: Trading halted for " + stock.getSymbol() +
                         " - Reason: " + reason);

        StockPosition position = holdings.get(stock.getSymbol());
        if (position != null) {
            String alert = String.format("TRADING HALT: %s - %s (Position: %d shares, Value: $%.2f)",
                                       stock.getSymbol(), reason, position.getShares(), position.getCurrentValue());
            alerts.add(alert);

            System.out.println("   🚨 RISK ALERT: Cannot liquidate position in " + stock.getSymbol());
            System.out.println("   📋 Position details: " + position.getShares() + " shares @ $" +
                             String.format("%.2f", position.getAveragePrice()));
        }
    }

    /**
     * Add a stock position to the portfolio
     */
    public void addPosition(String symbol, int shares, double averagePrice) {
        StockPosition position = holdings.get(symbol);
        if (position != null) {
            // Update existing position
            position.addShares(shares, averagePrice);
        } else {
            // Create new position
            position = new StockPosition(symbol, shares, averagePrice);
            holdings.put(symbol, position);
        }

        System.out.println("📈 " + managerName + " added position: " + shares + " shares of " + symbol +
                         " @ $" + String.format("%.2f", averagePrice));
        updatePortfolioValue();
    }

    /**
     * Remove shares from a position
     */
    public void reducePosition(String symbol, int sharesToSell, double salePrice) {
        StockPosition position = holdings.get(symbol);
        if (position != null) {
            double realizedGain = position.sellShares(sharesToSell, salePrice);

            System.out.println("📉 " + managerName + " reduced position: Sold " + sharesToSell +
                             " shares of " + symbol + " @ $" + String.format("%.2f", salePrice) +
                             " (Realized P/L: $" + String.format("%.2f", realizedGain) + ")");

            if (position.getShares() == 0) {
                holdings.remove(symbol);
                System.out.println("   Position in " + symbol + " fully closed");
            }

            updatePortfolioValue();
        }
    }

    private void checkRiskAlerts(Stock stock, StockPosition position, double changePercent) {
        // Check individual stock risk
        double positionRisk = Math.abs(position.getUnrealizedGainPercent());
        if (positionRisk > maxRiskPerStock * 100) {
            String alert = String.format("RISK ALERT: %s position risk %.2f%% exceeds limit %.2f%%",
                                       stock.getSymbol(), positionRisk, maxRiskPerStock * 100);
            alerts.add(alert);
            System.out.println("   🚨 " + alert);
        }

        // Check for significant daily moves
        if (Math.abs(changePercent) > 3.0) {
            String direction = changePercent > 0 ? "surge" : "drop";
            System.out.println("   ⚡ VOLATILITY ALERT: " + stock.getSymbol() + " " + direction + " of " +
                             String.format("%.2f%%", Math.abs(changePercent)));
        }
    }

    private void updatePortfolioValue() {
        double newTotalValue = 0;
        for (StockPosition position : holdings.values()) {
            newTotalValue += position.getCurrentValue();
        }
        totalPortfolioValue = newTotalValue;
    }

    /**
     * Generate portfolio performance report
     */
    public void generateReport() {
        System.out.println("\n📊 " + managerName + " Portfolio Report");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Total Portfolio Value: $" + String.format("%.2f", totalPortfolioValue));
        System.out.println("Number of Holdings: " + holdings.size());
        System.out.println("Active Alerts: " + alerts.size());

        if (!holdings.isEmpty()) {
            System.out.println("\nPositions:");
            double totalUnrealizedGain = 0;
            for (StockPosition position : holdings.values()) {
                System.out.println("  " + position.getDetailedString());
                totalUnrealizedGain += position.getUnrealizedGain();
            }

            System.out.println("\nTotal Unrealized P/L: $" + String.format("%.2f", totalUnrealizedGain) +
                             " (" + String.format("%.2f%%", (totalUnrealizedGain / totalPortfolioValue) * 100) + ")");
        }

        if (!alerts.isEmpty()) {
            System.out.println("\nRecent Alerts (last 5):");
            int start = Math.max(0, alerts.size() - 5);
            for (int i = start; i < alerts.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + alerts.get(i));
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    @Override
    public String getObserverName() {
        return managerName + " (Portfolio Manager)";
    }

    // Getters
    public Map<String, StockPosition> getHoldings() { return new HashMap<>(holdings); }
    public List<String> getAlerts() { return new ArrayList<>(alerts); }
    public double getTotalPortfolioValue() { return totalPortfolioValue; }

    /**
     * Inner class to represent a stock position in the portfolio
     */
    public static class StockPosition {
        private String symbol;
        private int shares;
        private double averagePrice;
        private double currentPrice;
        private double realizedGains;

        public StockPosition(String symbol, int shares, double averagePrice) {
            this.symbol = symbol;
            this.shares = shares;
            this.averagePrice = averagePrice;
            this.currentPrice = averagePrice;
            this.realizedGains = 0;
        }

        public void addShares(int newShares, double price) {
            double totalCost = (shares * averagePrice) + (newShares * price);
            shares += newShares;
            averagePrice = totalCost / shares;
        }

        public double sellShares(int sharesToSell, double salePrice) {
            if (sharesToSell > shares) {
                sharesToSell = shares;
            }

            double realizedGain = (salePrice - averagePrice) * sharesToSell;
            realizedGains += realizedGain;
            shares -= sharesToSell;

            return realizedGain;
        }

        public void updatePrice(double newPrice) {
            this.currentPrice = newPrice;
        }

        public double getCurrentValue() {
            return shares * currentPrice;
        }

        public double getUnrealizedGain() {
            return (currentPrice - averagePrice) * shares;
        }

        public double getUnrealizedGainPercent() {
            return ((currentPrice - averagePrice) / averagePrice) * 100;
        }

        public String getDetailedString() {
            return String.format("%s: %d shares @ $%.2f (Current: $%.2f) | P/L: $%.2f (%.2f%%)",
                               symbol, shares, averagePrice, currentPrice,
                               getUnrealizedGain(), getUnrealizedGainPercent());
        }

        // Getters
        public String getSymbol() { return symbol; }
        public int getShares() { return shares; }
        public double getAveragePrice() { return averagePrice; }
        public double getCurrentPrice() { return currentPrice; }
        public double getRealizedGains() { return realizedGains; }
    }
}
