package behavioural.command.banking;

import java.time.LocalDateTime;

/**
 * Command Interface for Banking Transaction System
 * Defines the contract for all banking operations
 */
public interface BankingCommand {
    /**
     * Execute the banking command
     * @return true if execution was successful, false otherwise
     */
    boolean execute();

    /**
     * Undo the banking command (reverse the transaction)
     * @return true if undo was successful, false otherwise
     */
    boolean undo();

    /**
     * Get description of the command for audit purposes
     * @return String description of the transaction
     */
    String getDescription();

    /**
     * Get the transaction ID
     * @return unique transaction identifier
     */
    String getTransactionId();

    /**
     * Get the timestamp when the command was created
     * @return LocalDateTime of command creation
     */
    LocalDateTime getTimestamp();

    /**
     * Get the amount involved in the transaction
     * @return transaction amount
     */
    double getAmount();

    /**
     * Check if the command has been executed
     * @return true if executed, false otherwise
     */
    boolean isExecuted();
}
