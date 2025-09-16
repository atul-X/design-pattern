package behavioural.command.smarthome;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Smart Home Automation System - Command Pattern Example
 *
 * This example demonstrates the Command Pattern in a real-world smart home scenario.
 *
 * Key Components:
 * - SmartHomeCommand: Command interface
 * - SmartLight, SmartThermostat, SecuritySystem: Receiver objects
 * - Concrete Commands: LightOnCommand, LightOffCommand, SetTemperatureCommand, etc.
 * - SmartHomeController: Invoker that manages command execution and history
 * - Macro Commands: GoodNightMacroCommand for complex scenarios
 *
 * Benefits Demonstrated:
 * - Decoupling: Controller doesn't know about specific devices
 * - Undo functionality: All commands can be reversed
 * - Macro commands: Complex scenarios with multiple actions
 * - Command history: Track and replay actions
 * - Flexibility: Easy to add new devices and commands
 */
public class SmartHomeExample {

    public static void main(String[] args) {
        System.out.println("🏠 Smart Home Automation System");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("Demonstrating Command Pattern with real-world smart home devices");
        System.out.println();

        // Create smart home devices (Receivers)
        SmartLight livingRoomLight = new SmartLight("Living Room");
        SmartLight bedroomLight = new SmartLight("Bedroom");
        SmartLight kitchenLight = new SmartLight("Kitchen");
        SmartThermostat thermostat = new SmartThermostat();
        SecuritySystem security = new SecuritySystem();

        // Create the smart home controller (Invoker)
        SmartHomeController controller = new SmartHomeController();

        // Demonstrate basic commands
        demonstrateBasicCommands(controller, livingRoomLight, thermostat, security);

        // Demonstrate macro commands
        demonstrateMacroCommands(controller, livingRoomLight, bedroomLight, kitchenLight, thermostat, security);

        // Demonstrate undo functionality
        demonstrateUndoFunctionality(controller);

        // Interactive demo (optional)
        System.out.println("\n🎮 Would you like to try the interactive demo? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().toLowerCase();

        if (response.equals("y") || response.equals("yes")) {
            interactiveDemo(controller, livingRoomLight, bedroomLight, thermostat, security);
        }

        scanner.close();

        System.out.println("\n✅ Smart Home Automation System demo completed!");
        System.out.println("📚 This example demonstrates the Command Pattern's ability to:");
        System.out.println("   • Encapsulate requests as objects");
        System.out.println("   • Support undo operations");
        System.out.println("   • Create macro commands for complex scenarios");
        System.out.println("   • Maintain command history for auditing");
        System.out.println("   • Decouple invokers from receivers");
    }

    private static void demonstrateBasicCommands(SmartHomeController controller,
                                               SmartLight light,
                                               SmartThermostat thermostat,
                                               SecuritySystem security) {
        System.out.println("🔧 BASIC COMMANDS DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Light commands
        controller.executeCommand(new LightOnCommand(light));
        controller.executeCommand(new LightOffCommand(light));

        // Thermostat commands
        controller.executeCommand(new SetTemperatureCommand(thermostat, 24));
        controller.executeCommand(new SetTemperatureCommand(thermostat, 20));

        // Security commands
        controller.executeCommand(new SecurityArmCommand(security));

        System.out.println();
    }

    private static void demonstrateMacroCommands(SmartHomeController controller,
                                               SmartLight livingRoom,
                                               SmartLight bedroom,
                                               SmartLight kitchen,
                                               SmartThermostat thermostat,
                                               SecuritySystem security) {
        System.out.println("🎭 MACRO COMMANDS DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Create "Good Night" macro command
        GoodNightMacroCommand goodNightMacro = new GoodNightMacroCommand(Arrays.asList(
            new LightOffCommand(livingRoom),
            new LightOffCommand(kitchen),
            new LightOffCommand(bedroom),
            new SetTemperatureCommand(thermostat, 18),
            new SecurityArmCommand(security)
        ));

        // Add macro to controller and execute
        controller.addMacroCommand(goodNightMacro);
        controller.executeMacro(0);

        System.out.println();
    }

    private static void demonstrateUndoFunctionality(SmartHomeController controller) {
        System.out.println("↩️ UNDO FUNCTIONALITY DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        controller.showHistory();

        System.out.println("\n🔄 Undoing last 3 commands...");
        controller.undoLastCommand();
        controller.undoLastCommand();
        controller.undoLastCommand();

        System.out.println();
    }

    private static void interactiveDemo(SmartHomeController controller,
                                      SmartLight livingRoom,
                                      SmartLight bedroom,
                                      SmartThermostat thermostat,
                                      SecuritySystem security) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n🎮 INTERACTIVE SMART HOME CONTROLLER");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        while (running) {
            System.out.println("\nAvailable commands:");
            System.out.println("1. Turn on living room light");
            System.out.println("2. Turn off living room light");
            System.out.println("3. Turn on bedroom light");
            System.out.println("4. Turn off bedroom light");
            System.out.println("5. Set temperature");
            System.out.println("6. Arm security system");
            System.out.println("7. Show command history");
            System.out.println("8. Undo last command");
            System.out.println("9. Execute Good Night macro");
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        controller.executeCommand(new LightOnCommand(livingRoom));
                        break;
                    case 2:
                        controller.executeCommand(new LightOffCommand(livingRoom));
                        break;
                    case 3:
                        controller.executeCommand(new LightOnCommand(bedroom));
                        break;
                    case 4:
                        controller.executeCommand(new LightOffCommand(bedroom));
                        break;
                    case 5:
                        System.out.print("Enter temperature (16-30°C): ");
                        int temp = Integer.parseInt(scanner.nextLine());
                        controller.executeCommand(new SetTemperatureCommand(thermostat, temp));
                        break;
                    case 6:
                        controller.executeCommand(new SecurityArmCommand(security));
                        break;
                    case 7:
                        controller.showHistory();
                        break;
                    case 8:
                        controller.undoLastCommand();
                        break;
                    case 9:
                        controller.executeMacro(0);
                        break;
                    case 0:
                        running = false;
                        System.out.println("👋 Exiting interactive demo...");
                        break;
                    default:
                        System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
    }
}
