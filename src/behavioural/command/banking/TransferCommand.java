package behavioural.command.banking;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Concrete Command - Transfer Money
 * Encapsulates a transfer transaction request between two accounts
 */
public class TransferCommand implements BankingCommand {
    private BankAccount sourceAccount;
    private BankAccount targetAccount;
    private double amount;
    private String transactionId;
    private LocalDateTime timestamp;
    private boolean executed;

    public TransferCommand(BankAccount sourceAccount, BankAccount targetAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.transactionId = "TRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.timestamp = LocalDateTime.now();
        this.executed = false;
    }

    @Override
    public boolean execute() {
        if (executed) {
            System.out.println("⚠️ Transaction " + transactionId + " already executed");
            return false;
        }

        boolean success = sourceAccount.transfer(targetAccount, amount, transactionId);
        if (success) {
            executed = true;
            System.out.println("✅ Transfer transaction " + transactionId + " completed successfully");
        } else {
            System.out.println("❌ Transfer transaction " + transactionId + " failed");
        }
        return success;
    }

    @Override
    public boolean undo() {
        if (!executed) {
            System.out.println("⚠️ Cannot undo transaction " + transactionId + " - not executed");
            return false;
        }

        // To undo a transfer, we need to transfer the money back
        if (targetAccount.getBalance() < amount) {
            System.out.println("❌ Cannot undo transfer " + transactionId + " - insufficient funds in target account for reversal");
            return false;
        }

        boolean success = targetAccount.transfer(sourceAccount, amount, "UNDO-" + transactionId);
        if (success) {
            executed = false;
            System.out.println("↩️ Transfer transaction " + transactionId + " reversed successfully");
        } else {
            System.out.println("❌ Failed to reverse transfer transaction " + transactionId);
        }
        return success;
    }

    @Override
    public String getDescription() {
        return "Transfer $" + String.format("%.2f", amount) + " from account " +
               sourceAccount.getAccountNumber() + " to account " + targetAccount.getAccountNumber();
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

    // Additional getters for transfer-specific information
    public BankAccount getSourceAccount() {
        return sourceAccount;
    }

    public BankAccount getTargetAccount() {
        return targetAccount;
    }
}
