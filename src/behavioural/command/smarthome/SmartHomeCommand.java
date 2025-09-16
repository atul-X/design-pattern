package behavioural.command.smarthome;

/**
 * Command Interface for Smart Home Automation System
 * Defines the contract for all smart home commands
 */
public interface SmartHomeCommand {
    /**
     * Execute the command
     */
    void execute();

    /**
     * Undo the command
     */
    void undo();

    /**
     * Get description of the command
     * @return String description of what the command does
     */
    String getDescription();
}
