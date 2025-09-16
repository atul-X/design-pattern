package behavioural.command.smarthome;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Invoker class - Smart Home Controller
 * Manages command execution, history, and undo functionality
 */
public class SmartHomeController {
    private Stack<SmartHomeCommand> commandHistory;
    private List<SmartHomeCommand> macroCommands;

    public SmartHomeController() {
        this.commandHistory = new Stack<>();
        this.macroCommands = new ArrayList<>();
    }

    /**
     * Execute a command and add it to history
     */
    public void executeCommand(SmartHomeCommand command) {
        try {
            command.execute();
            commandHistory.push(command);
            System.out.println("📝 Command logged: " + command.getDescription());
        } catch (Exception e) {
            System.out.println("❌ Error executing command: " + e.getMessage());
        }
    }

    /**
     * Undo the last executed command
     */
    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            SmartHomeCommand lastCommand = commandHistory.pop();
            try {
                lastCommand.undo();
                System.out.println("↩️ Undone: " + lastCommand.getDescription());
            } catch (Exception e) {
                System.out.println("❌ Error undoing command: " + e.getMessage());
                // Put the command back if undo failed
                commandHistory.push(lastCommand);
            }
        } else {
            System.out.println("ℹ️ No commands to undo");
        }
    }

    /**
     * Execute multiple commands in sequence
     */
    public void executeCommands(List<SmartHomeCommand> commands) {
        for (SmartHomeCommand command : commands) {
            executeCommand(command);
        }
    }

    /**
     * Add a macro command for later use
     */
    public void addMacroCommand(SmartHomeCommand macroCommand) {
        macroCommands.add(macroCommand);
        System.out.println("📋 Macro command added: " + macroCommand.getDescription());
    }

    /**
     * Execute a macro command by index
     */
    public void executeMacro(int index) {
        if (index >= 0 && index < macroCommands.size()) {
            executeCommand(macroCommands.get(index));
        } else {
            System.out.println("❌ Invalid macro index: " + index);
        }
    }

    /**
     * Show command history
     */
    public void showHistory() {
        System.out.println("\n📜 Command History:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (commandHistory.isEmpty()) {
            System.out.println("   No commands executed yet");
        } else {
            List<SmartHomeCommand> historyList = new ArrayList<>(commandHistory);
            for (int i = 0; i < historyList.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + historyList.get(i).getDescription());
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Show available macro commands
     */
    public void showMacros() {
        System.out.println("\n🎭 Available Macro Commands:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (macroCommands.isEmpty()) {
            System.out.println("   No macro commands available");
        } else {
            for (int i = 0; i < macroCommands.size(); i++) {
                System.out.println("   " + i + ". " + macroCommands.get(i).getDescription());
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Clear command history
     */
    public void clearHistory() {
        commandHistory.clear();
        System.out.println("🗑️ Command history cleared");
    }

    /**
     * Get the number of commands in history
     */
    public int getHistorySize() {
        return commandHistory.size();
    }
}
