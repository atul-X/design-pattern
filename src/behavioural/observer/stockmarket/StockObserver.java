package behavioural.observer.stockmarket;

/**
 * Observer Interface for Stock Market Trading System
 * Defines the contract for objects that want to be notified of stock price changes
 */
public interface StockObserver {
    /**
     * Called when a stock price is updated
     * @param stock The stock that was updated
     * @param oldPrice The previous price
     * @param newPrice The current price
     * @param changePercent The percentage change
     */
    void onPriceUpdate(Stock stock, double oldPrice, double newPrice, double changePercent);

    /**
     * Called when a stock reaches a significant milestone
     * @param stock The stock that reached the milestone
     * @param milestoneType Type of milestone (e.g., "52_WEEK_HIGH", "VOLUME_SPIKE")
     * @param details Additional details about the milestone
     */
    void onMilestone(Stock stock, String milestoneType, String details);

    /**
     * Called when trading is halted for a stock
     * @param stock The stock for which trading is halted
     * @param reason The reason for the halt
     */
    void onTradingHalt(Stock stock, String reason);

    /**
     * Get the name/identifier of this observer
     * @return observer name
     */
    String getObserverName();
}
