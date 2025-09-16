package behavioural.command.banking;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Concrete Command - Withdraw Money
 * Encapsulates a withdrawal transaction request
 */
public class WithdrawCommand implements BankingCommand {
    private BankAccount account;
    private double amount;
    private String transactionId;
    private LocalDateTime timestamp;
    private boolean executed;

    public WithdrawCommand(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
        this.transactionId = "WTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.timestamp = LocalDateTime.now();
        this.executed = false;
    }

    @Override
    public boolean execute() {
        if (executed) {
            System.out.println("⚠️ Transaction " + transactionId + " already executed");
            return false;
        }

        boolean success = account.withdraw(amount, transactionId);
        if (success) {
            executed = true;
            System.out.println("✅ Withdrawal transaction " + transactionId + " completed successfully");
        } else {
            System.out.println("❌ Withdrawal transaction " + transactionId + " failed");
        }
        return success;
    }

    @Override
    public boolean undo() {
        if (!executed) {
            System.out.println("⚠️ Cannot undo transaction " + transactionId + " - not executed");
            return false;
        }

        // To undo a withdrawal, we need to deposit the same amount back
        boolean success = account.deposit(amount, "UNDO-" + transactionId);
        if (success) {
            executed = false;
            System.out.println("↩️ Withdrawal transaction " + transactionId + " reversed successfully");
        } else {
            System.out.println("❌ Failed to reverse withdrawal transaction " + transactionId);
        }
        return success;
    }

    @Override
    public String getDescription() {
        return "Withdraw $" + String.format("%.2f", amount) + " from account " + account.getAccountNumber();
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
