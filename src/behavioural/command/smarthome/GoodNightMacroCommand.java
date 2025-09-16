package behavioural.command.smarthome;

import java.util.ArrayList;
import java.util.List;

/**
 * Macro Command - Good Night Scenario
 * Executes multiple commands to prepare the home for bedtime
 */
public class GoodNightMacroCommand implements SmartHomeCommand {
    private List<SmartHomeCommand> commands;

    public GoodNightMacroCommand(List<SmartHomeCommand> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public void execute() {
        System.out.println("🌙 Executing Good Night scenario...");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        for (SmartHomeCommand command : commands) {
            command.execute();
        }

        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("✅ Good Night scenario completed!");
    }

    @Override
    public void undo() {
        System.out.println("🔄 Undoing Good Night scenario...");

        // Undo commands in reverse order
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }

        System.out.println("✅ Good Night scenario undone!");
    }

    @Override
    public String getDescription() {
        return "Good Night Macro (Turn off lights, set night mode security, lower temperature)";
    }

    public void addCommand(SmartHomeCommand command) {
        commands.add(command);
    }

    public void removeCommand(SmartHomeCommand command) {
        commands.remove(command);
    }
}
