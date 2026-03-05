package lld.learningmanagementsystem;

import lld.learningmanagementsystem.model.*;
import lld.learningmanagementsystem.service.*;

/**
 * Detailed explanation of how CommandInvoker tracks and reverts specific commands
 */
public class CommandPatternExplained {
    public static void main(String[] args) {
        System.out.println("=== Command Pattern - How Invoker Tracks and Reverts Commands ===\n");

        // Initialize services
        CourseService courseService = new CourseService();
        CommandInvoker invoker = courseService.getCommandInvoker();

        // Create a course
        Course course = new Course();
        course.setId(1001);
        course.setName("Command Pattern Demo Course");
        courseService.createCourse(course);

        System.out.println("📊 Initial course state: " + course.getCurrentState().getClass().getSimpleName());

        // === EXPLANATION 1: How Commands are Stored ===
        System.out.println("\n=== 1. How Commands are Stored in Invoker ===");
        System.out.println("🔍 Each command object contains TWO actions:");
        System.out.println("   - execute(): What to do when command is executed");
        System.out.println("   - undo(): What to do when command is reverted");
        System.out.println("📦 Invoker stores these command objects in a Stack (LIFO order)");

        // Execute first command
        System.out.println("\n--- Executing Publish Command ---");
        System.out.println("📝 Creating command with:");
        System.out.println("   Execute: course.publish()");
        System.out.println("   Undo: course.setCurrentState(previousState)");
        
        courseService.publishCourse(1001);
        System.out.println("✅ Command executed and stored in invoker");
        System.out.println("📊 Course state: " + course.getCurrentState().getClass().getSimpleName());
        System.out.println("📈 Command history size: " + invoker.getHistorySize());

        // Execute second command
        System.out.println("\n--- Executing Archive Command ---");
        System.out.println("📝 Creating command with:");
        System.out.println("   Execute: course.archive()");
        System.out.println("   Undo: course.setCurrentState(previousState)");
        
        courseService.archiveCourse(1001);
        System.out.println("✅ Command executed and stored in invoker");
        System.out.println("📊 Course state: " + course.getCurrentState().getClass().getSimpleName());
        System.out.println("📈 Command history size: " + invoker.getHistorySize());

        // === EXPLANATION 2: How Undo Works ===
        System.out.println("\n=== 2. How Undo Reverts Specific Commands ===");
        System.out.println("🔄 Undo process:");
        System.out.println("   1. Invoker pops LAST command from stack (LIFO)");
        System.out.println("   2. Calls undo() method on that specific command");
        System.out.println("   3. Command executes its stored undo action");
        System.out.println("   4. System state is reverted to previous state");

        // Undo archive command
        System.out.println("\n--- Undoing Archive Command (Last Executed) ---");
        System.out.println("🔄 Invoker pops ArchiveCommand from stack");
        System.out.println("🔄 Calls archiveCommand.undo()");
        System.out.println("🔄 ArchiveCommand executes: course.setCurrentState(PublishCourse)");
        
        courseService.undoLastAction();
        System.out.println("✅ Archive command undone");
        System.out.println("📊 Course state: " + course.getCurrentState().getClass().getSimpleName());
        System.out.println("📈 Command history size: " + invoker.getHistorySize());

        // Undo publish command
        System.out.println("\n--- Undoing Publish Command (Now Last Executed) ---");
        System.out.println("🔄 Invoker pops PublishCommand from stack");
        System.out.println("🔄 Calls publishCommand.undo()");
        System.out.println("🔄 PublishCommand executes: course.setCurrentState(DraftCourse)");
        
        courseService.undoLastAction();
        System.out.println("✅ Publish command undone");
        System.out.println("📊 Course state: " + course.getCurrentState().getClass().getSimpleName());
        System.out.println("📈 Command history size: " + invoker.getHistorySize());

        // === EXPLANATION 3: Command Object Structure ===
        System.out.println("\n=== 3. Command Object Structure ===");
        System.out.println("🏗️ Each CommandImpl object contains:");
        System.out.println("   private Runnable executeAction;  // What to do");
        System.out.println("   private Runnable undoAction;    // How to revert");
        System.out.println("");
        System.out.println("📋 Example Publish Command Structure:");
        System.out.println("   executeAction: () -> course.publish()");
        System.out.println("   undoAction: () -> course.setCurrentState(DraftCourse)");
        System.out.println("");
        System.out.println("📋 Example Archive Command Structure:");
        System.out.println("   executeAction: () -> course.archive()");
        System.out.println("   undoAction: () -> course.setCurrentState(PublishCourse)");

        // === EXPLANATION 4: State Capture ===
        System.out.println("\n=== 4. How State is Captured for Undo ===");
        System.out.println("💾 State capture happens BEFORE execution:");
        System.out.println("   1. Get current state: CourseState previousState = course.getCurrentState()");
        System.out.println("   2. Create command with state: new CommandImpl(execute, revert)");
        System.out.println("   3. Execute command: command.execute()");
        System.out.println("   4. Store command: invoker.executeCommand(command)");
        System.out.println("");
        System.out.println("🔄 During undo:");
        System.out.println("   1. Pop command from stack");
        System.out.println("   2. Call undo(): command.undo()");
        System.out.println("   3. Restore state: course.setCurrentState(previousState)");

        // Demonstrate with another example
        System.out.println("\n=== 5. Complete Example with State Capture ===");
        
        // Reset to draft
        System.out.println("🔄 Resetting course to Draft state");
        course.setCurrentState(new DraftCourse());
        
        // Publish with detailed state tracking
        System.out.println("\n--- Publishing with State Capture ---");
        System.out.println("📸 Capturing state BEFORE execution: DraftCourse");
        
        CourseState capturedState = course.getCurrentState();
        System.out.println("💾 State captured: " + capturedState.getClass().getSimpleName());
        
        // Create command manually to show the process
        Command publishCommand = new CommandImpl(
            () -> {
                System.out.println("🚀 Executing: course.publish()");
                course.publish();
                System.out.println("📊 New state: " + course.getCurrentState().getClass().getSimpleName());
            },
            () -> {
                System.out.println("🔄 Executing undo: course.setCurrentState(" + capturedState.getClass().getSimpleName() + ")");
                course.setCurrentState(capturedState);
                System.out.println("📊 Restored state: " + course.getCurrentState().getClass().getSimpleName());
            }
        );
        
        // Execute and store
        invoker.executeCommand(publishCommand);
        
        // Show the undo
        System.out.println("\n--- Manual Undo Demonstration ---");
        invoker.undo();

        // === EXPLANATION 5: Multiple Commands ===
        System.out.println("\n=== 6. Multiple Commands in Stack ===");
        System.out.println("📚 Stack structure (top to bottom):");
        System.out.println("   [Last Executed Command]");
        System.out.println("   [Second Last Command]");
        System.out.println("   [Third Last Command]");
        System.out.println("   [...]");
        System.out.println("   [First Executed Command]");
        System.out.println("");
        System.out.println("🔄 Undo always affects the LAST executed command (LIFO)");

        // Execute multiple commands
        courseService.publishCourse(1001);  // Command 1
        courseService.archiveCourse(1001);  // Command 2
        
        System.out.println("\n--- Multiple Commands Executed ---");
        System.out.println("📚 Stack (top to bottom):");
        System.out.println("   [ArchiveCommand] ← Will be undone first");
        System.out.println("   [PublishCommand] ← Will be undone second");
        
        // Undo both
        courseService.undoLastAction(); // Undo archive
        courseService.undoLastAction(); // Undo publish

        System.out.println("\n=== Summary ===");
        System.out.println("✅ Key Points:");
        System.out.println("   1. Each command stores BOTH execute and undo actions");
        System.out.println("   2. Invoker uses Stack to store commands (LIFO order)");
        System.out.println("   3. Undo always affects the LAST executed command");
        System.out.println("   4. State is captured BEFORE execution for proper restoration");
        System.out.println("   5. Each command knows exactly how to revert itself");
        System.out.println("");
        System.out.println("🎯 The CommandInvoker doesn't need to know WHAT to revert");
        System.out.println("   It just calls undo() on the command, and the command");
        System.out.println("   knows exactly how to revert its own changes!");
    }
}
