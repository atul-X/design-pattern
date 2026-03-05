package lld.learningmanagementsystem;

import lld.learningmanagementsystem.model.*;
import lld.learningmanagementsystem.service.*;

/**
 * 🎯 INTERVIEW-READY COMMAND PATTERN EXPLANATION
 * 
 * This guide explains exactly how to explain Command pattern in interviews
 * with clear mechanisms, examples, and implementation steps.
 */
public class CommandPatternInterviewGuide {
    public static void main(String[] args) {
        System.out.println("=== 🎯 COMMAND PATTERN - INTERVIEW READY GUIDE ===\n");

        System.out.println("📋 QUESTION: \"How does Command pattern work? Can you implement it?\"\n");

        // === CORE CONCEPT EXPLANATION ===
        System.out.println("=== 🎯 CORE CONCEPT (What to say first) ===");
        System.out.println("💡 Command pattern turns requests into objects.");
        System.out.println("   • Each operation becomes a Command object");
        System.out.println("   • Command stores: WHAT to do + HOW to undo");
        System.out.println("   • Invoker manages command history");
        System.out.println("   • Enables undo/redo, queuing, logging\n");

        // === MECHANISM EXPLANATION ===
        System.out.println("=== 🔧 MECHANISM (How it works step-by-step) ===");
        demonstrateMechanism();

        System.out.println("\n=== 🏗️ IMPLEMENTATION STEPS (For coding round) ===");
        demonstrateImplementation();

        System.out.println("\n=== 📝 INTERVIEW EXAMPLES (Common scenarios) ===");
        demonstrateInterviewExamples();

        System.out.println("\n=== 🎨 DESIGN BENEFITS (Why use this pattern) ===");
        demonstrateBenefits();

        System.out.println("\n=== ⚠️ COMMON MISTAKES (What to avoid) ===");
        demonstrateCommonMistakes();

        System.out.println("\n=== 🚀 ADVANCED FEATURES (Bonus points) ===");
        demonstrateAdvancedFeatures();
    }

    private static void demonstrateMechanism() {
        System.out.println("🔄 STEP 1: Create Command Interface");
        System.out.println("   public interface Command {");
        System.out.println("       void execute();");
        System.out.println("       void undo();");
        System.out.println("   }");
        System.out.println();

        System.out.println("🔄 STEP 2: Create Concrete Command");
        System.out.println("   class PublishCourseCommand implements Command {");
        System.out.println("       private Course course;");
        System.out.println("       private CourseState previousState;");
        System.out.println();
        System.out.println("       public PublishCourseCommand(Course course) {");
        System.out.println("           this.course = course;");
        System.out.println("           this.previousState = course.getCurrentState(); // Capture BEFORE");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void execute() {");
        System.out.println("           course.publish(); // Change state");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undo() {");
        System.out.println("           course.setCurrentState(previousState); // Restore");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("🔄 STEP 3: Create Invoker");
        System.out.println("   class CommandInvoker {");
        System.out.println("       private Stack<Command> history = new Stack<>();");
        System.out.println();
        System.out.println("       public void executeCommand(Command command) {");
        System.out.println("           command.execute(); // Do the action");
        System.out.println("           history.push(command); // Store for undo");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undo() {");
        System.out.println("           if (!history.isEmpty()) {");
        System.out.println("               Command command = history.pop(); // Get LAST");
        System.out.println("               command.undo(); // Undo it");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("🔄 STEP 4: Use in Service");
        System.out.println("   class CourseService {");
        System.out.println("       private CommandInvoker invoker = new CommandInvoker();");
        System.out.println();
        System.out.println("       public void publishCourse(int courseId) {");
        System.out.println("           Course course = getCourse(courseId);");
        System.out.println("           Command command = new PublishCourseCommand(course);");
        System.out.println("           invoker.executeCommand(command); // Store + Execute");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undoLastAction() {");
        System.out.println("           invoker.undo(); // Undo last operation");
        System.out.println("       }");
        System.out.println("   }");
    }

    private static void demonstrateImplementation() {
        System.out.println("📝 STEP 1: Define Command Interface");
        System.out.println("   // Always start with the interface - shows you know the pattern");
        System.out.println("   interface Command { void execute(); void undo(); }");
        System.out.println();

        System.out.println("📝 STEP 2: Create Concrete Command");
        System.out.println("   // Show state capture - this is KEY for undo");
        System.out.println("   class AddItemCommand implements Command {");
        System.out.println("       private List<String> list;");
        System.out.println("       private String item;");
        System.out.println("       private boolean wasAdded;");
        System.out.println();
        System.out.println("       public AddItemCommand(List<String> list, String item) {");
        System.out.println("           this.list = list;");
        System.out.println("           this.item = item;");
        System.out.println("           this.wasAdded = false; // Track for undo");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void execute() {");
        System.out.println("           wasAdded = list.add(item); // Store result for undo");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undo() {");
        System.out.println("           if (wasAdded) {");
        System.out.println("               list.remove(item); // Only remove if was added");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("📝 STEP 3: Create Invoker");
        System.out.println("   // Show Stack usage - demonstrates LIFO behavior");
        System.out.println("   class Invoker {");
        System.out.println("       private Stack<Command> history = new Stack<>();");
        System.out.println("       private static final int MAX_SIZE = 100;");
        System.out.println();
        System.out.println("       public void execute(Command cmd) {");
        System.out.println("           cmd.execute();");
        System.out.println("           if (history.size() >= MAX_SIZE) {");
        System.out.println("               history.remove(0); // Remove oldest");
        System.out.println("           }");
        System.out.println("           history.push(cmd); // Add to top");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undo() {");
        System.out.println("           if (!history.isEmpty()) {");
        System.out.println("               history.pop().undo(); // Remove + undo");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("📝 STEP 4: Integrate with Service");
        System.out.println("   // Show how to use in real service");
        System.out.println("   class TodoService {");
        System.out.println("       private Invoker invoker = new Invoker();");
        System.out.println("       private List<String> todos = new ArrayList<>();");
        System.out.println();
        System.out.println("       public void addTodo(String task) {");
        System.out.println("           Command cmd = new AddItemCommand(todos, task);");
        System.out.println("           invoker.execute(cmd);");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undo() {");
        System.out.println("           invoker.undo();");
        System.out.println("       }");
        System.out.println("   }");
    }

    private static void demonstrateInterviewExamples() {
        System.out.println("🎯 EXAMPLE 1: Text Editor (Classic)");
        System.out.println("   • Commands: InsertText, DeleteText, FormatText");
        System.out.println("   • Invoker: Editor with Ctrl+Z functionality");
        System.out.println("   • State: Text cursor position, selection");
        System.out.println();

        System.out.println("🎯 EXAMPLE 2: Restaurant Order System");
        System.out.println("   • Commands: PlaceOrder, CancelOrder, UpdateOrder");
        System.out.println("   • Invoker: OrderQueue with order history");
        System.out.println("   • State: Order status, items, prices");
        System.out.println();

        System.out.println("🎯 EXAMPLE 3: Smart Home (IoT)");
        System.out.println("   • Commands: TurnOnLight, SetTemperature, LockDoor");
        System.out.println("   • Invoker: Mobile app with voice commands");
        System.out.println("   • State: Device previous states");
        System.out.println();

        System.out.println("🎯 EXAMPLE 4: Database Transactions");
        System.out.println("   • Commands: InsertRecord, UpdateRecord, DeleteRecord");
        System.out.println("   • Invoker: TransactionManager");
        System.out.println("   • State: Database snapshots");
        System.out.println();

        System.out.println("🎯 EXAMPLE 5: GUI Buttons (Most Common)");
        System.out.println("   • Commands: CopyAction, PasteAction, SaveAction");
        System.out.println("   • Invoker: Button, MenuItem");
        System.out.println("   • State: Clipboard content, document state");
    }

    private static void demonstrateBenefits() {
        System.out.println("✅ BENEFIT 1: Decoupling");
        System.out.println("   • Invoker doesn't know about receivers");
        System.out.println("   • Commands encapsulate all operation details");
        System.out.println("   • Easy to add new operations without changing invoker");
        System.out.println();

        System.out.println("✅ BENEFIT 2: Undo/Redo");
        System.out.println("   • Natural fit with command history");
        System.out.println("   • Each command knows how to undo itself");
        System.out.println("   • Can implement multi-level undo");
        System.out.println();

        System.out.println("✅ BENEFIT 3: Queue & Batch");
        System.out.println("   • Commands can be queued for later execution");
        System.out.println("   • Batch operations can be executed together");
        System.out.println("   • Supports delayed/scheduled execution");
        System.out.println();

        System.out.println("✅ BENEFIT 4: Logging & Auditing");
        System.out.println("   • Every operation can be logged");
        System.out.println("   • Command history provides audit trail");
        System.out.println("   • Easy to track who did what when");
        System.out.println();

        System.out.println("✅ BENEFIT 5: Composite Commands");
        System.out.println("   • Multiple commands can be grouped");
        System.out.println("   • Macro commands (record sequence of actions)");
        System.out.println("   • Transaction-like behavior");
    }

    private static void demonstrateCommonMistakes() {
        System.out.println("❌ MISTAKE 1: Not Capturing State Before Execution");
        System.out.println("   // WRONG: Execute first, then try to figure out undo");
        System.out.println("   public void execute() {");
        System.out.println("       course.publish(); // State changed!");
        System.out.println("       // Now how to undo? Need previous state!");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // RIGHT: Capture state BEFORE execution");
        System.out.println("   public void execute() {");
        System.out.println("       previousState = course.getCurrentState(); // Save first");
        System.out.println("       course.publish(); // Then execute");
        System.out.println("   }");
        System.out.println();

        System.out.println("❌ MISTAKE 2: Forgetting Null Checks");
        System.out.println("   // WRONG: Assume invoker always has commands");
        System.out.println("   public void undo() {");
        System.out.println("       history.pop().undo(); // NPE if empty!");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // RIGHT: Check before undo");
        System.out.println("   public void undo() {");
        System.out.println("       if (!history.isEmpty()) {");
        System.out.println("           history.pop().undo();");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("❌ MISTAKE 3: Not Handling Memory Leaks");
        System.out.println("   // WRONG: Unlimited command history");
        System.out.println("   private Stack<Command> history = new Stack<>(); // Grows forever!");
        System.out.println();
        System.out.println("   // RIGHT: Limit history size");
        System.out.println("   if (history.size() >= MAX_SIZE) {");
        System.out.println("       history.remove(0); // Remove oldest");
        System.out.println("   }");
        System.out.println();

        System.out.println("❌ MISTAKE 4: Commands Not Self-Contained");
        System.out.println("   // WRONG: Command depends on external state");
        System.out.println("   class BadCommand {");
        System.out.println("       private static GlobalState state; // Bad!");
        System.out.println("   }");
        System.out.println();
        System.out.println("   // RIGHT: Command contains all needed data");
        System.out.println("   class GoodCommand {");
        System.out.println("       private final LocalState state; // Encapsulated");
        System.out.println("   }");
    }

    private static void demonstrateAdvancedFeatures() {
        System.out.println("🚀 FEATURE 1: Macro Commands");
        System.out.println("   class MacroCommand implements Command {");
        System.out.println("       private List<Command> commands;");
        System.out.println();
        System.out.println("       public void execute() {");
        System.out.println("           commands.forEach(Command::execute);");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undo() {");
        System.out.println("           for (int i = commands.size()-1; i >= 0; i--) {");
        System.out.println("               commands.get(i).undo(); // Reverse order");
        System.out.println("           }");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("🚀 FEATURE 2: Smart Invoker with Multiple Services");
        System.out.println("   class SmartInvoker {");
        System.out.println("       private Map<String, Stack<Command>> serviceHistories;");
        System.out.println();
        System.out.println("       public void execute(String service, Command cmd) {");
        System.out.println("           serviceHistories.computeIfAbsent(service, k -> new Stack<>())");
        System.out.println("                           .push(cmd);");
        System.out.println("           cmd.execute();");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void undoLast() {");
        System.out.println("           // Find service with most recent command");
        System.out.println("           // Undo from that service");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("🚀 FEATURE 3: Persistent Command History");
        System.out.println("   class PersistentInvoker {");
        System.out.println("       public void saveToDisk() {");
        System.out.println("           // Serialize command history");
        System.out.println("       }");
        System.out.println();
        System.out.println("       public void loadFromDisk() {");
        System.out.println("           // Restore command history");
        System.out.println("       }");
        System.out.println("   }");
        System.out.println();

        System.out.println("🚀 FEATURE 4: Async Command Execution");
        System.out.println("   class AsyncInvoker {");
        System.out.println("       private ExecutorService executor;");
        System.out.println();
        System.out.println("       public Future<Void> executeAsync(Command cmd) {");
        System.out.println("           return executor.submit(() -> {");
        System.out.println("               cmd.execute();");
        System.out.println("               return null;");
        System.out.println("           });");
        System.out.println("       }");
        System.out.println("   }");
    }

    // === QUICK REFERENCE FOR INTERVIEWS ===
    public static void quickReference() {
        System.out.println("=== 📚 QUICK INTERVIEW REFERENCE ===");
        System.out.println();
        System.out.println("🎯 WHEN TO USE COMMAND PATTERN:");
        System.out.println("   • Need undo/redo functionality");
        System.out.println("   • Want to queue operations");
        System.out.println("   • Need to log operations");
        System.out.println("   • GUI with buttons/menus");
        System.out.println("   • Transactional operations");
        System.out.println();
        System.out.println("🏗️ KEY COMPONENTS:");
        System.out.println("   1. Command Interface (execute, undo)");
        System.out.println("   2. Concrete Commands (specific operations)");
        System.out.println("   3. Invoker (manages command history)");
        System.out.println("   4. Receiver (object that performs work)");
        System.out.println();
        System.out.println("⚡ IMPLEMENTATION TIPS:");
        System.out.println("   • Capture state BEFORE execution");
        System.out.println("   • Use Stack for LIFO undo behavior");
        System.out.println("   • Limit history size to prevent memory leaks");
        System.out.println("   • Make commands self-contained");
        System.out.println("   • Handle null checks and edge cases");
        System.out.println();
        System.out.println("🎨 COMMON VARIATIONS:");
        System.out.println("   • Macro Commands (group multiple commands)");
        System.out.println("   • Smart Invokers (multiple services)");
        System.out.println("   • Persistent Commands (save/load)");
        System.out.println("   • Async Commands (background execution)");
    }
}
