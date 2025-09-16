package behavioural.command.banking;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Concrete Command - Deposit Money
 * Encapsulates a deposit transaction request
 */
public class DepositCommand implements BankingCommand {
    private BankAccount account;
    private double amount;
    private String transactionId;
    private LocalDateTime timestamp;
    private boolean executed;

    public DepositCommand(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
        this.transactionId = "DEP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.timestamp = LocalDateTime.now();
        this.executed = false;
    }

    @Override
    public boolean execute() {
        if (executed) {
            System.out.println("⚠️ Transaction " + transactionId + " already executed");
            return false;
        }

        boolean success = account.deposit(amount, transactionId);
        if (success) {
            executed = true;
            System.out.println("✅ Deposit transaction " + transactionId + " completed successfully");
        } else {
            System.out.println("❌ Deposit transaction " + transactionId + " failed");
        }
        return success;
    }

    @Override
    public boolean undo() {
        if (!executed) {
            System.out.println("⚠️ Cannot undo transaction " + transactionId + " - not executed");
            return false;
        }

        // To undo a deposit, we need to withdraw the same amount
        if (account.getBalance() < amount) {
            System.out.println("❌ Cannot undo deposit " + transactionId + " - insufficient funds for reversal");
            return false;
        }

        boolean success = account.withdraw(amount, "UNDO-" + transactionId);
        if (success) {
            executed = false;
            System.out.println("↩️ Deposit transaction " + transactionId + " reversed successfully");
        } else {
            System.out.println("❌ Failed to reverse deposit transaction " + transactionId);
        }
        return success;
    }

    @Override
    public String getDescription() {
        return "Deposit $" + String.format("%.2f", amount) + " to account " + account.getAccountNumber();
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }
}
