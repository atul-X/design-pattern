package behavioural.command.banking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Invoker class - Banking Controller
 * Manages banking command execution, audit trails, and transaction reversal
 */
public class BankingController {
    private Stack<BankingCommand> transactionHistory;
    private List<BankingCommand> auditTrail;
    private double dailyTransactionLimit;
    private double dailyTransactionTotal;
    private LocalDateTime lastTransactionDate;

    public BankingController() {
        this.transactionHistory = new Stack<>();
        this.auditTrail = new ArrayList<>();
        this.dailyTransactionLimit = 50000.0; // Default daily limit
        this.dailyTransactionTotal = 0.0;
        this.lastTransactionDate = LocalDateTime.now();
    }

    /**
     * Execute a banking command with validation and logging
     */
    public boolean executeTransaction(BankingCommand command) {
        try {
            // Reset daily limit if new day
            resetDailyLimitIfNewDay();

            // Check daily transaction limit
            if (dailyTransactionTotal + command.getAmount() > dailyTransactionLimit) {
                System.out.println("❌ Daily transaction limit exceeded. Limit: $" +
                                 String.format("%.2f", dailyTransactionLimit) +
                                 ", Current total: $" + String.format("%.2f", dailyTransactionTotal));
                return false;
            }

            // Execute the command
            boolean success = command.execute();

            if (success) {
                // Add to history and audit trail
                transactionHistory.push(command);
                auditTrail.add(command);
                dailyTransactionTotal += command.getAmount();
                lastTransactionDate = LocalDateTime.now();

                System.out.println("📝 Transaction logged: " + command.getTransactionId() +
                                 " | " + command.getDescription());
                System.out.println("   Daily transaction total: $" + String.format("%.2f", dailyTransactionTotal));
            }

            return success;

        } catch (Exception e) {
            System.out.println("❌ Error executing transaction: " + e.getMessage());
            return false;
        }
    }

    /**
     * Undo the last executed transaction
     */
    public boolean undoLastTransaction() {
        if (transactionHistory.isEmpty()) {
            System.out.println("ℹ️ No transactions to undo");
            return false;
        }

        BankingCommand lastCommand = transactionHistory.pop();

        try {
            boolean success = lastCommand.undo();
            if (success) {
                // Subtract from daily total when undoing
                dailyTransactionTotal = Math.max(0, dailyTransactionTotal - lastCommand.getAmount());
                System.out.println("↩️ Transaction " + lastCommand.getTransactionId() + " undone successfully");
                System.out.println("   Updated daily transaction total: $" + String.format("%.2f", dailyTransactionTotal));

                // Add undo record to audit trail
                auditTrail.add(new UndoRecord(lastCommand));
            } else {
                // Put the command back if undo failed
                transactionHistory.push(lastCommand);
                System.out.println("❌ Failed to undo transaction " + lastCommand.getTransactionId());
            }
            return success;

        } catch (Exception e) {
            // Put the command back if undo failed
            transactionHistory.push(lastCommand);
            System.out.println("❌ Error undoing transaction: " + e.getMessage());
            return false;
        }
    }

    /**
     * Execute multiple transactions in sequence
     */
    public boolean executeTransactions(List<BankingCommand> commands) {
        boolean allSuccessful = true;
        List<BankingCommand> executedCommands = new ArrayList<>();

        for (BankingCommand command : commands) {
            boolean success = executeTransaction(command);
            if (success) {
                executedCommands.add(command);
            } else {
                allSuccessful = false;
                System.out.println("⚠️ Batch transaction failed at: " + command.getDescription());
                break;
            }
        }

        if (!allSuccessful) {
            System.out.println("🔄 Rolling back executed transactions in batch...");
            // Rollback executed commands in reverse order
            for (int i = executedCommands.size() - 1; i >= 0; i--) {
                undoLastTransaction();
            }
        }

        return allSuccessful;
    }

    /**
     * Show transaction history
     */
    public void showTransactionHistory() {
        System.out.println("\n📜 Transaction History:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (transactionHistory.isEmpty()) {
            System.out.println("   No transactions executed yet");
        } else {
            List<BankingCommand> historyList = new ArrayList<>(transactionHistory);
            for (int i = 0; i < historyList.size(); i++) {
                BankingCommand cmd = historyList.get(i);
                System.out.println("   " + (i + 1) + ". [" + cmd.getTransactionId() + "] " +
                                 cmd.getDescription() + " | $" + String.format("%.2f", cmd.getAmount()));
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Show complete audit trail
     */
    public void showAuditTrail() {
        System.out.println("\n📊 Complete Audit Trail:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (auditTrail.isEmpty()) {
            System.out.println("   No transactions in audit trail");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < auditTrail.size(); i++) {
                BankingCommand cmd = auditTrail.get(i);
                System.out.println("   " + (i + 1) + ". [" + cmd.getTimestamp().format(formatter) + "] " +
                                 "[" + cmd.getTransactionId() + "] " + cmd.getDescription() +
                                 " | $" + String.format("%.2f", cmd.getAmount()) +
                                 " | " + (cmd.isExecuted() ? "EXECUTED" : "UNDONE"));
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Show daily transaction summary
     */
    public void showDailyTransactionSummary() {
        System.out.println("\n📈 Daily Transaction Summary:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("   Date: " + lastTransactionDate.toLocalDate());
        System.out.println("   Total Transactions: " + transactionHistory.size());
        System.out.println("   Total Amount: $" + String.format("%.2f", dailyTransactionTotal));
        System.out.println("   Daily Limit: $" + String.format("%.2f", dailyTransactionLimit));
        System.out.println("   Remaining Limit: $" + String.format("%.2f", dailyTransactionLimit - dailyTransactionTotal));
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    private void resetDailyLimitIfNewDay() {
        LocalDateTime now = LocalDateTime.now();
        if (lastTransactionDate.toLocalDate().isBefore(now.toLocalDate())) {
            dailyTransactionTotal = 0.0;
            lastTransactionDate = now;
            System.out.println("🔄 Daily transaction limit reset for new day");
        }
    }

    /**
     * Clear transaction history (keep audit trail)
     */
    public void clearTransactionHistory() {
        transactionHistory.clear();
        System.out.println("🗑️ Transaction history cleared (audit trail preserved)");
    }

    // Getters and setters
    public int getTransactionCount() { return transactionHistory.size(); }
    public double getDailyTransactionLimit() { return dailyTransactionLimit; }
    public void setDailyTransactionLimit(double limit) { this.dailyTransactionLimit = limit; }
    public double getDailyTransactionTotal() { return dailyTransactionTotal; }

    /**
     * Inner class to represent undo records in audit trail
     */
    private static class UndoRecord implements BankingCommand {
        private BankingCommand originalCommand;
        private LocalDateTime undoTimestamp;

        public UndoRecord(BankingCommand originalCommand) {
            this.originalCommand = originalCommand;
            this.undoTimestamp = LocalDateTime.now();
        }

        @Override
        public boolean execute() { return false; }

        @Override
        public boolean undo() { return false; }

        @Override
        public String getDescription() {
            return "UNDO: " + originalCommand.getDescription();
        }

        @Override
        public String getTransactionId() {
            return "UNDO-" + originalCommand.getTransactionId();
        }

        @Override
        public LocalDateTime getTimestamp() {
            return undoTimestamp;
        }

        @Override
        public double getAmount() {
            return originalCommand.getAmount();
        }

        @Override
        public boolean isExecuted() {
            return false; // Undo records are not "executed"
        }
    }
}
