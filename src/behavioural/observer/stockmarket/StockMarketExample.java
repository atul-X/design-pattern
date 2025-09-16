package behavioural.observer.stockmarket;

import java.util.Scanner;

/**
 * Stock Market Trading System - Observer Pattern Example
 *
 * This example demonstrates the Observer Pattern in a real-world stock market scenario.
 *
 * Key Components:
 * - StockObserver: Observer interface for market participants
 * - Stock: Subject that notifies observers of price changes and events
 * - TradingBot: Concrete observer implementing automated trading strategies
 * - PortfolioManager: Concrete observer for portfolio tracking and risk management
 *
 * Benefits Demonstrated:
 * - Real-time notifications: Observers get immediate updates on price changes
 * - Loose coupling: Stocks don't need to know about specific observer implementations
 * - Dynamic subscription: Observers can subscribe/unsubscribe at runtime
 * - Multiple observer types: Different observers can react differently to the same events
 * - Event-driven architecture: System responds to market events automatically
 */
public class StockMarketExample {

    public static void main(String[] args) {
        System.out.println("📈 Stock Market Trading System");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("Demonstrating Observer Pattern with real-world stock market operations");
        System.out.println();

        // Create stocks (Subjects)
        Stock appleStock = new Stock("AAPL", "Apple Inc.", 150.00);
        Stock teslaStock = new Stock("TSLA", "Tesla Inc.", 250.00);
        Stock amazonStock = new Stock("AMZN", "Amazon.com Inc.", 3200.00);

        // Create observers (market participants)
        TradingBot momentumBot = new TradingBot("MomentumBot-Alpha", "MOMENTUM", 100000.0);
        TradingBot contrarianBot = new TradingBot("ContrarianBot-Beta", "CONTRARIAN", 75000.0);
        TradingBot scalpingBot = new TradingBot("ScalpingBot-Gamma", "SCALPING", 50000.0);

        PortfolioManager institutionalManager = new PortfolioManager("Institutional Portfolio", 1000000.0);
        PortfolioManager retailManager = new PortfolioManager("Retail Portfolio", 50000.0);

        // Set up initial portfolio positions
        setupInitialPositions(institutionalManager, retailManager);

        // Subscribe observers to stocks
        subscribeObserversToStocks(appleStock, teslaStock, amazonStock,
                                 momentumBot, contrarianBot, scalpingBot,
                                 institutionalManager, retailManager);

        // Demonstrate market opening
        demonstrateMarketOpening(appleStock, teslaStock, amazonStock);

        // Simulate real-time trading
        simulateRealTimeTrading(appleStock, teslaStock, amazonStock);

        // Demonstrate milestone events
        demonstrateMilestoneEvents(appleStock, teslaStock);

        // Demonstrate trading halts
        demonstrateTradingHalts(amazonStock);

        // Show final reports
        generateFinalReports(momentumBot, contrarianBot, scalpingBot,
                           institutionalManager, retailManager);

        // Interactive demo (optional)
        System.out.println("\n🎮 Would you like to try the interactive trading demo? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().toLowerCase();

        if (response.equals("y") || response.equals("yes")) {
            interactiveDemo(appleStock, teslaStock, amazonStock, momentumBot, institutionalManager);
        }

        scanner.close();

        System.out.println("\n✅ Stock Market Trading System demo completed!");
        System.out.println("📚 This example demonstrates the Observer Pattern's benefits in trading:");
        System.out.println("   • Real-time event notifications to multiple market participants");
        System.out.println("   • Loose coupling between stocks and trading systems");
        System.out.println("   • Dynamic subscription management for market data");
        System.out.println("   • Automated responses to market events and milestones");
        System.out.println("   • Risk management through real-time portfolio monitoring");
        System.out.println("   • Scalable architecture supporting multiple trading strategies");
    }

    private static void setupInitialPositions(PortfolioManager institutional, PortfolioManager retail) {
        System.out.println("🏦 SETTING UP INITIAL PORTFOLIO POSITIONS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Institutional portfolio - larger positions
        institutional.addPosition("AAPL", 1000, 148.50);
        institutional.addPosition("TSLA", 500, 245.00);
        institutional.addPosition("AMZN", 100, 3180.00);

        // Retail portfolio - smaller positions
        retail.addPosition("AAPL", 50, 149.00);
        retail.addPosition("TSLA", 20, 248.00);

        System.out.println();
    }

    private static void subscribeObserversToStocks(Stock apple, Stock tesla, Stock amazon,
                                                 TradingBot momentum, TradingBot contrarian, TradingBot scalping,
                                                 PortfolioManager institutional, PortfolioManager retail) {
        System.out.println("📡 SUBSCRIBING OBSERVERS TO STOCK UPDATES");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Subscribe all bots to all stocks
        apple.addObserver(momentum);
        apple.addObserver(contrarian);
        apple.addObserver(scalping);
        apple.addObserver(institutional);
        apple.addObserver(retail);

        tesla.addObserver(momentum);
        tesla.addObserver(contrarian);
        tesla.addObserver(scalping);
        tesla.addObserver(institutional);
        tesla.addObserver(retail);

        amazon.addObserver(momentum);
        amazon.addObserver(contrarian);
        amazon.addObserver(institutional); // Retail doesn't hold Amazon

        System.out.println();
    }

    private static void demonstrateMarketOpening(Stock apple, Stock tesla, Stock amazon) {
        System.out.println("🔔 MARKET OPENING DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        apple.marketOpen(151.25);
        tesla.marketOpen(252.80);
        amazon.marketOpen(3195.50);

        System.out.println();
    }

    private static void simulateRealTimeTrading(Stock apple, Stock tesla, Stock amazon) {
        System.out.println("⚡ REAL-TIME TRADING SIMULATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Simulate Apple price movements
        System.out.println("\n📱 Apple Stock Price Movements:");
        apple.updatePrice(152.10); // +0.56% increase
        apple.updateVolume(2500000);

        apple.updatePrice(153.50); // +0.92% increase
        apple.updateVolume(3200000);

        apple.updatePrice(151.80); // -1.11% decrease

        // Simulate Tesla volatility
        System.out.println("\n🚗 Tesla Stock Price Movements:");
        tesla.updatePrice(258.90); // +2.41% increase
        tesla.updateVolume(5000000);

        tesla.updatePrice(245.20); // -5.29% significant drop
        tesla.updateVolume(8500000); // Volume spike

        tesla.updatePrice(250.15); // +2.02% recovery

        // Simulate Amazon movements
        System.out.println("\n📦 Amazon Stock Price Movements:");
        amazon.updatePrice(3210.75); // +0.48% increase
        amazon.updatePrice(3185.20); // -0.80% decrease

        System.out.println();
    }

    private static void demonstrateMilestoneEvents(Stock apple, Stock tesla) {
        System.out.println("🎯 MILESTONE EVENTS DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Trigger 52-week high for Apple
        System.out.println("\n📈 Apple reaches new 52-week high:");
        apple.updatePrice(155.00); // New high

        // Trigger significant move for Tesla
        System.out.println("\n⚡ Tesla experiences significant price movement:");
        tesla.updatePrice(262.50); // +4.95% significant move

        System.out.println();
    }

    private static void demonstrateTradingHalts(Stock amazon) {
        System.out.println("⛔ TRADING HALT DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Halt trading due to news
        amazon.haltTrading("Pending major acquisition announcement");

        // Try to update price during halt
        amazon.updatePrice(3300.00); // This should be ignored

        // Resume trading
        amazon.resumeTrading();
        amazon.updatePrice(3250.00); // This should work

        System.out.println();
    }

    private static void generateFinalReports(TradingBot momentum, TradingBot contrarian, TradingBot scalping,
                                           PortfolioManager institutional, PortfolioManager retail) {
        System.out.println("📊 FINAL REPORTS AND ANALYSIS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Trading bot portfolios
        momentum.printPortfolio();
        System.out.println();

        contrarian.printPortfolio();
        System.out.println();

        scalping.printPortfolio();
        System.out.println();

        // Portfolio manager reports
        institutional.generateReport();
        retail.generateReport();

        System.out.println();
    }

    private static void interactiveDemo(Stock apple, Stock tesla, Stock amazon,
                                      TradingBot bot, PortfolioManager manager) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n🎮 INTERACTIVE STOCK MARKET SIMULATOR");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        while (running) {
            System.out.println("\nAvailable actions:");
            System.out.println("1. Update Apple stock price");
            System.out.println("2. Update Tesla stock price");
            System.out.println("3. Update Amazon stock price");
            System.out.println("4. Show stock information");
            System.out.println("5. Show trading bot portfolio");
            System.out.println("6. Show portfolio manager report");
            System.out.println("7. Halt/Resume trading for a stock");
            System.out.println("8. Simulate volume spike");
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter new Apple price: $");
                        double applePrice = Double.parseDouble(scanner.nextLine());
                        apple.updatePrice(applePrice);
                        break;
                    case 2:
                        System.out.print("Enter new Tesla price: $");
                        double teslaPrice = Double.parseDouble(scanner.nextLine());
                        tesla.updatePrice(teslaPrice);
                        break;
                    case 3:
                        System.out.print("Enter new Amazon price: $");
                        double amazonPrice = Double.parseDouble(scanner.nextLine());
                        amazon.updatePrice(amazonPrice);
                        break;
                    case 4:
                        apple.printStockInfo();
                        System.out.println();
                        tesla.printStockInfo();
                        System.out.println();
                        amazon.printStockInfo();
                        break;
                    case 5:
                        bot.printPortfolio();
                        break;
                    case 6:
                        manager.generateReport();
                        break;
                    case 7:
                        System.out.println("Select stock: 1=Apple, 2=Tesla, 3=Amazon");
                        int stockChoice = Integer.parseInt(scanner.nextLine());
                        Stock selectedStock = stockChoice == 1 ? apple : stockChoice == 2 ? tesla : amazon;

                        if (selectedStock.isTradingHalted()) {
                            selectedStock.resumeTrading();
                        } else {
                            System.out.print("Enter halt reason: ");
                            String reason = scanner.nextLine();
                            selectedStock.haltTrading(reason);
                        }
                        break;
                    case 8:
                        System.out.println("Select stock for volume spike: 1=Apple, 2=Tesla, 3=Amazon");
                        int volumeStockChoice = Integer.parseInt(scanner.nextLine());
                        Stock volumeStock = volumeStockChoice == 1 ? apple : volumeStockChoice == 2 ? tesla : amazon;
                        volumeStock.updateVolume(10000000); // Large volume
                        break;
                    case 0:
                        running = false;
                        System.out.println("👋 Exiting market simulator...");
                        break;
                    default:
                        System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
            }
        }
    }
}
