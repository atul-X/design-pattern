package behavioural.observer.stockmarket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class - Stock
 * Represents a stock that can be observed for price changes and events
 */
public class Stock {
    private String symbol;
    private String companyName;
    private double currentPrice;
    private double previousPrice;
    private double openPrice;
    private double dayHigh;
    private double dayLow;
    private double fiftyTwoWeekHigh;
    private double fiftyTwoWeekLow;
    private long volume;
    private long averageVolume;
    private boolean tradingHalted;
    private LocalDateTime lastUpdate;

    private List<StockObserver> observers;

    public Stock(String symbol, String companyName, double initialPrice) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = initialPrice;
        this.previousPrice = initialPrice;
        this.openPrice = initialPrice;
        this.dayHigh = initialPrice;
        this.dayLow = initialPrice;
        this.fiftyTwoWeekHigh = initialPrice;
        this.fiftyTwoWeekLow = initialPrice;
        this.volume = 0;
        this.averageVolume = 1000000; // Default average volume
        this.tradingHalted = false;
        this.lastUpdate = LocalDateTime.now();
        this.observers = new ArrayList<>();
    }

    /**
     * Subscribe an observer to this stock
     */
    public void addObserver(StockObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("📈 " + observer.getObserverName() + " subscribed to " + symbol + " updates");
        }
    }

    /**
     * Unsubscribe an observer from this stock
     */
    public void removeObserver(StockObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("📉 " + observer.getObserverName() + " unsubscribed from " + symbol + " updates");
        }
    }

    /**
     * Update the stock price and notify observers
     */
    public void updatePrice(double newPrice) {
        if (tradingHalted) {
            System.out.println("⛔ Trading halted for " + symbol + ". Price update ignored.");
            return;
        }

        double oldPrice = currentPrice;
        previousPrice = currentPrice;
        currentPrice = newPrice;
        lastUpdate = LocalDateTime.now();

        // Update daily high/low
        if (newPrice > dayHigh) {
            dayHigh = newPrice;
        }
        if (newPrice < dayLow) {
            dayLow = newPrice;
        }

        // Update 52-week high/low and check for milestones
        boolean hitFiftyTwoWeekHigh = false;
        boolean hitFiftyTwoWeekLow = false;

        if (newPrice > fiftyTwoWeekHigh) {
            fiftyTwoWeekHigh = newPrice;
            hitFiftyTwoWeekHigh = true;
        }
        if (newPrice < fiftyTwoWeekLow) {
            fiftyTwoWeekLow = newPrice;
            hitFiftyTwoWeekLow = true;
        }

        // Calculate percentage change
        double changePercent = ((newPrice - oldPrice) / oldPrice) * 100;

        // Notify observers of price update
        notifyPriceUpdate(oldPrice, newPrice, changePercent);

        // Check for milestones
        if (hitFiftyTwoWeekHigh) {
            notifyMilestone("52_WEEK_HIGH", "New 52-week high: $" + String.format("%.2f", newPrice));
        }
        if (hitFiftyTwoWeekLow) {
            notifyMilestone("52_WEEK_LOW", "New 52-week low: $" + String.format("%.2f", newPrice));
        }

        // Check for significant price movements
        if (Math.abs(changePercent) >= 5.0) {
            String direction = changePercent > 0 ? "surge" : "drop";
            notifyMilestone("SIGNIFICANT_MOVE", String.format("%.1f%% %s in price", Math.abs(changePercent), direction));
        }
    }

    /**
     * Update trading volume and check for volume spikes
     */
    public void updateVolume(long newVolume) {
        this.volume = newVolume;

        // Check for volume spike (3x average volume)
        if (newVolume > averageVolume * 3) {
            notifyMilestone("VOLUME_SPIKE", String.format("Volume spike: %,d (%.1fx average)", newVolume, (double)newVolume / averageVolume));
        }
    }

    /**
     * Halt trading for this stock
     */
    public void haltTrading(String reason) {
        if (!tradingHalted) {
            tradingHalted = true;
            notifyTradingHalt(reason);
        }
    }

    /**
     * Resume trading for this stock
     */
    public void resumeTrading() {
        if (tradingHalted) {
            tradingHalted = false;
            System.out.println("✅ Trading resumed for " + symbol);
        }
    }

    /**
     * Simulate market opening
     */
    public void marketOpen(double openingPrice) {
        this.openPrice = openingPrice;
        this.currentPrice = openingPrice;
        this.dayHigh = openingPrice;
        this.dayLow = openingPrice;
        System.out.println("🔔 Market opened for " + symbol + " at $" + String.format("%.2f", openingPrice));
    }

    private void notifyPriceUpdate(double oldPrice, double newPrice, double changePercent) {
        for (StockObserver observer : observers) {
            try {
                observer.onPriceUpdate(this, oldPrice, newPrice, changePercent);
            } catch (Exception e) {
                System.out.println("❌ Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }

    private void notifyMilestone(String milestoneType, String details) {
        for (StockObserver observer : observers) {
            try {
                observer.onMilestone(this, milestoneType, details);
            } catch (Exception e) {
                System.out.println("❌ Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }

    private void notifyTradingHalt(String reason) {
        for (StockObserver observer : observers) {
            try {
                observer.onTradingHalt(this, reason);
            } catch (Exception e) {
                System.out.println("❌ Error notifying observer " + observer.getObserverName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Get current stock information
     */
    public void printStockInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println("📊 " + symbol + " (" + companyName + ")");
        System.out.println("   Current Price: $" + String.format("%.2f", currentPrice));
        System.out.println("   Change: $" + String.format("%.2f", currentPrice - previousPrice) +
                         " (" + String.format("%.2f", ((currentPrice - previousPrice) / previousPrice) * 100) + "%)");
        System.out.println("   Day Range: $" + String.format("%.2f", dayLow) + " - $" + String.format("%.2f", dayHigh));
        System.out.println("   52W Range: $" + String.format("%.2f", fiftyTwoWeekLow) + " - $" + String.format("%.2f", fiftyTwoWeekHigh));
        System.out.println("   Volume: " + String.format("%,d", volume));
        System.out.println("   Status: " + (tradingHalted ? "HALTED" : "ACTIVE"));
        System.out.println("   Last Update: " + lastUpdate.format(formatter));
    }

    // Getters
    public String getSymbol() { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public double getPreviousPrice() { return previousPrice; }
    public double getOpenPrice() { return openPrice; }
    public double getDayHigh() { return dayHigh; }
    public double getDayLow() { return dayLow; }
    public double getFiftyTwoWeekHigh() { return fiftyTwoWeekHigh; }
    public double getFiftyTwoWeekLow() { return fiftyTwoWeekLow; }
    public long getVolume() { return volume; }
    public boolean isTradingHalted() { return tradingHalted; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public int getObserverCount() { return observers.size(); }

    // Setters
    public void setAverageVolume(long averageVolume) { this.averageVolume = averageVolume; }

    @Override
    public String toString() {
        return symbol + " - $" + String.format("%.2f", currentPrice) +
               " (" + String.format("%.2f", ((currentPrice - previousPrice) / previousPrice) * 100) + "%)";
    }
}
