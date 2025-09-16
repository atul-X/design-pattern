package behavioural.command.banking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Receiver class - Bank Account
 * Performs the actual banking operations
 */
public class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private String accountType;
    private boolean isActive;
    private List<String> transactionHistory;
    private double dailyWithdrawalLimit;
    private double dailyWithdrawnAmount;
    private LocalDateTime lastTransactionDate;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance, String accountType) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.isActive = true;
        this.transactionHistory = new ArrayList<>();
        this.dailyWithdrawalLimit = 5000.0; // Default daily limit
        this.dailyWithdrawnAmount = 0.0;
        this.lastTransactionDate = LocalDateTime.now();

        addToHistory("Account created with initial balance: $" + String.format("%.2f", initialBalance));
    }

    public boolean deposit(double amount, String transactionId) {
        if (!isActive) {
            System.out.println("❌ Account " + accountNumber + " is inactive. Cannot deposit.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("❌ Invalid deposit amount: $" + String.format("%.2f", amount));
            return false;
        }

        balance += amount;
        addToHistory("DEPOSIT: $" + String.format("%.2f", amount) + " | Transaction ID: " + transactionId + " | New Balance: $" + String.format("%.2f", balance));
        System.out.println("💰 Deposited $" + String.format("%.2f", amount) + " to account " + accountNumber);
        System.out.println("   New balance: $" + String.format("%.2f", balance));
        return true;
    }

    public boolean withdraw(double amount, String transactionId) {
        if (!isActive) {
            System.out.println("❌ Account " + accountNumber + " is inactive. Cannot withdraw.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("❌ Invalid withdrawal amount: $" + String.format("%.2f", amount));
            return false;
        }

        if (balance < amount) {
            System.out.println("❌ Insufficient funds. Balance: $" + String.format("%.2f", balance) + ", Requested: $" + String.format("%.2f", amount));
            return false;
        }

        // Check daily withdrawal limit
        resetDailyLimitIfNewDay();
        if (dailyWithdrawnAmount + amount > dailyWithdrawalLimit) {
            System.out.println("❌ Daily withdrawal limit exceeded. Limit: $" + String.format("%.2f", dailyWithdrawalLimit) +
                             ", Already withdrawn today: $" + String.format("%.2f", dailyWithdrawnAmount));
            return false;
        }

        balance -= amount;
        dailyWithdrawnAmount += amount;
        addToHistory("WITHDRAWAL: $" + String.format("%.2f", amount) + " | Transaction ID: " + transactionId + " | New Balance: $" + String.format("%.2f", balance));
        System.out.println("💸 Withdrew $" + String.format("%.2f", amount) + " from account " + accountNumber);
        System.out.println("   New balance: $" + String.format("%.2f", balance));
        return true;
    }

    public boolean transfer(BankAccount targetAccount, double amount, String transactionId) {
        if (!isActive) {
            System.out.println("❌ Source account " + accountNumber + " is inactive. Cannot transfer.");
            return false;
        }

        if (!targetAccount.isActive()) {
            System.out.println("❌ Target account " + targetAccount.getAccountNumber() + " is inactive. Cannot transfer.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("❌ Invalid transfer amount: $" + String.format("%.2f", amount));
            return false;
        }

        if (balance < amount) {
            System.out.println("❌ Insufficient funds for transfer. Balance: $" + String.format("%.2f", balance) + ", Requested: $" + String.format("%.2f", amount));
            return false;
        }

        // Perform the transfer
        balance -= amount;
        targetAccount.balance += amount;

        // Add to both account histories
        addToHistory("TRANSFER OUT: $" + String.format("%.2f", amount) + " to " + targetAccount.getAccountNumber() +
                    " | Transaction ID: " + transactionId + " | New Balance: $" + String.format("%.2f", balance));
        targetAccount.addToHistory("TRANSFER IN: $" + String.format("%.2f", amount) + " from " + accountNumber +
                                  " | Transaction ID: " + transactionId + " | New Balance: $" + String.format("%.2f", targetAccount.balance));

        System.out.println("🔄 Transferred $" + String.format("%.2f", amount) + " from " + accountNumber + " to " + targetAccount.getAccountNumber());
        System.out.println("   Source balance: $" + String.format("%.2f", balance));
        System.out.println("   Target balance: $" + String.format("%.2f", targetAccount.balance));
        return true;
    }

    private void resetDailyLimitIfNewDay() {
        LocalDateTime now = LocalDateTime.now();
        if (lastTransactionDate.toLocalDate().isBefore(now.toLocalDate())) {
            dailyWithdrawnAmount = 0.0;
            lastTransactionDate = now;
        }
    }

    private void addToHistory(String transaction) {
        String timestamp = LocalDateTime.now().toString();
        transactionHistory.add("[" + timestamp + "] " + transaction);
        lastTransactionDate = LocalDateTime.now();
    }

    public void printAccountInfo() {
        System.out.println("📋 Account Information:");
        System.out.println("   Account Number: " + accountNumber);
        System.out.println("   Account Holder: " + accountHolderName);
        System.out.println("   Account Type: " + accountType);
        System.out.println("   Current Balance: $" + String.format("%.2f", balance));
        System.out.println("   Status: " + (isActive ? "Active" : "Inactive"));
        System.out.println("   Daily Withdrawal Limit: $" + String.format("%.2f", dailyWithdrawalLimit));
        System.out.println("   Daily Withdrawn Amount: $" + String.format("%.2f", dailyWithdrawnAmount));
    }

    public void printTransactionHistory(int lastNTransactions) {
        System.out.println("📜 Transaction History for Account " + accountNumber + ":");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        int startIndex = Math.max(0, transactionHistory.size() - lastNTransactions);
        for (int i = startIndex; i < transactionHistory.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + transactionHistory.get(i));
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public double getDailyWithdrawalLimit() { return dailyWithdrawalLimit; }
    public void setDailyWithdrawalLimit(double limit) { this.dailyWithdrawalLimit = limit; }
    public double getDailyWithdrawnAmount() { return dailyWithdrawnAmount; }

    @Override
    public String toString() {
        return accountType + " Account [" + accountNumber + "] - " + accountHolderName +
               " | Balance: $" + String.format("%.2f", balance) + " | " + (isActive ? "Active" : "Inactive");
    }
}
