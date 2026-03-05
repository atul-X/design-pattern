package lld.learningmanagementsystem;

import lld.learningmanagementsystem.model.*;
import lld.learningmanagementsystem.service.*;

/**
 * Demo to show what happens when undo operations are mismatched
 * Last action was addQuiz, but we call undoCourse
 */
public class UndoMismatchDemo {
    public static void main(String[] args) {
        System.out.println("=== Undo Mismatch Demo: Last Action = addQuiz, Call = undoCourse ===\n");

        // Initialize LMS
        LMSManager lms = new LMSManager();

        // Create course
        Course course = new Course();
        course.setId(1001);
        course.setName("Undo Mismatch Demo Course");
        course.setInstructor("Dr. Smith");
        Course createdCourse = lms.createCourse(course);

        // Create student
        Students student = new Students();
        student.setId(1);
        student.setName("Alice");
        lms.registerStudent(student);

        // Create quiz
        Quiz quiz = new Quiz();
        quiz.setId(2001);
        quiz.setTotalMarks(100);
        quiz.setPassingMarks(60);

        // Create module and lesson for quiz
        lld.learningmanagementsystem.model.Module module = new lld.learningmanagementsystem.model.Module(1, "Java Basics", "Intro", course.getId());
        Lesson lesson = new VideoLesson(101, "Java Fundamentals", "Basic concepts", "https://video.java.com/basics", module.getId());
        module.addLesson(lesson);
        createdCourse.addModule(module);

        System.out.println("📊 Initial state:");
        System.out.println("   Course: " + createdCourse.getCurrentState().getClass().getSimpleName());
        System.out.println("   CourseService commands: " + lms.getCourseService().getCommandInvoker().getHistorySize());
        System.out.println("   QuizService commands: " + lms.getQuizService().getCommandInvoker().getHistorySize());
        System.out.println("   EnrollmentService commands: " + lms.getEnrollmentService().getCommandInvoker().getHistorySize());

        // === SCENARIO: Last action is addQuiz ===
        System.out.println("\n=== Step 1: Execute addQuiz (Last Action) ===");
        lms.addQuizToLesson(1001, 1, 101, quiz);
        System.out.println("✅ Quiz added to lesson");

        System.out.println("\n📊 After addQuiz:");
        System.out.println("   Course: " + createdCourse.getCurrentState().getClass().getSimpleName());
        System.out.println("   CourseService commands: " + lms.getCourseService().getCommandInvoker().getHistorySize());
        System.out.println("   QuizService commands: " + lms.getQuizService().getCommandInvoker().getHistorySize());
        System.out.println("   EnrollmentService commands: " + lms.getEnrollmentService().getCommandInvoker().getHistorySize());

        // === SCENARIO: Call undoCourse ===
        System.out.println("\n=== Step 2: Call undoCourse (Mismatched) ===");
        System.out.println("🤔 Question: Last action was addQuiz (in QuizService)");
        System.out.println("🤔 But we're calling undoCourse (from CourseService)");
        System.out.println("");

        System.out.println("🔍 What happens in each service:");
        
        // Check CourseService
        System.out.println("\n--- CourseService.undoLastAction() ---");
        boolean courseHasCommands = lms.getCourseService().getCommandInvoker().hasUndoableActions();
        System.out.println("   Has commands to undo: " + courseHasCommands);
        
        if (!courseHasCommands) {
            System.out.println("   ✅ Nothing happens - CourseService has no commands in history");
            System.out.println("   📊 Course state remains: " + createdCourse.getCurrentState().getClass().getSimpleName());
        }

        // Check QuizService  
        System.out.println("\n--- QuizService (where the actual command is) ---");
        boolean quizHasCommands = lms.getQuizService().getCommandInvoker().hasUndoableActions();
        System.out.println("   Has commands to undo: " + quizHasCommands);
        System.out.println("   📊 QuizService has the addQuiz command ready to be undone");

        // Check EnrollmentService
        System.out.println("\n--- EnrollmentService ---");
        boolean enrollmentHasCommands = lms.getEnrollmentService().getCommandInvoker().hasUndoableActions();
        System.out.println("   Has commands to undo: " + enrollmentHasCommands);

        // === DEMONSTRATE THE ACTUAL UNDO ===
        System.out.println("\n=== Step 3: What Actually Happens When We Call undoCourse() ===");
        
        // Call the mismatched undo
        System.out.println("🔄 Calling lms.undoLastCourseAction()...");
        lms.undoLastCourseAction();
        
        System.out.println("\n📊 After undoCourse():");
        System.out.println("   Course: " + createdCourse.getCurrentState().getClass().getSimpleName());
        System.out.println("   CourseService commands: " + lms.getCourseService().getCommandInvoker().getHistorySize());
        System.out.println("   QuizService commands: " + lms.getQuizService().getCommandInvoker().getHistorySize());
        System.out.println("   EnrollmentService commands: " + lms.getEnrollmentService().getCommandInvoker().getHistorySize());

        // === NOW UNDO THE ACTUAL COMMAND ===
        System.out.println("\n=== Step 4: Undo the Actual Command (addQuiz) ===");
        System.out.println("🔄 Calling lms.undoLastQuizAction()...");
        lms.undoLastQuizAction();
        
        System.out.println("\n📊 After undoQuiz():");
        System.out.println("   Course: " + createdCourse.getCurrentState().getClass().getSimpleName());
        System.out.println("   CourseService commands: " + lms.getCourseService().getCommandInvoker().getHistorySize());
        System.out.println("   QuizService commands: " + lms.getQuizService().getCommandInvoker().getHistorySize());
        System.out.println("   EnrollmentService commands: " + lms.getEnrollmentService().getCommandInvoker().getHistorySize());

        // === SYSTEM-WIDE UNDO DEMO ===
        System.out.println("\n=== Step 5: System-Wide Undo (Smart Undo) ===");
        
        // Add some commands to different services
        lms.publishCourse(1001);        // CourseService
        lms.enrollStudent(1, 1001);      // EnrollmentService
        lms.addQuizToLesson(1001, 1, 101, quiz); // QuizService
        
        System.out.println("📊 After mixed operations:");
        System.out.println("   Course: " + createdCourse.getCurrentState().getClass().getSimpleName());
        System.out.println("   CourseService commands: " + lms.getCourseService().getCommandInvoker().getHistorySize());
        System.out.println("   QuizService commands: " + lms.getQuizService().getCommandInvoker().getHistorySize());
        System.out.println("   EnrollmentService commands: " + lms.getEnrollmentService().getCommandInvoker().getHistorySize());

        System.out.println("\n🔄 Calling system-wide undo (undoLastSystemAction)...");
        System.out.println("🎯 This will undo the LAST executed command across ALL services");
        
        lms.undoLastSystemAction();
        
        System.out.println("\n📊 After system-wide undo:");
        System.out.println("   Course: " + createdCourse.getCurrentState().getClass().getSimpleName());
        System.out.println("   CourseService commands: " + lms.getCourseService().getCommandInvoker().getHistorySize());
        System.out.println("   QuizService commands: " + lms.getQuizService().getCommandInvoker().getHistorySize());
        System.out.println("   EnrollmentService commands: " + lms.getEnrollmentService().getCommandInvoker().getHistorySize());

        // === EXPLANATION ===
        System.out.println("\n=== 🎯 KEY EXPLANATION ===");
        System.out.println("");
        System.out.println("📚 Each Service has its OWN CommandInvoker:");
        System.out.println("   • CourseService → CourseInvoker (manages course operations)");
        System.out.println("   • QuizService → QuizInvoker (manages quiz operations)");
        System.out.println("   • EnrollmentService → EnrollmentInvoker (manages enrollment operations)");
        System.out.println("");
        System.out.println("🔄 When you call undoCourse():");
        System.out.println("   ✅ Only CourseService's CommandInvoker is checked");
        System.out.println("   ❌ QuizService's CommandInvoker is NOT touched");
        System.out.println("   ❌ EnrollmentService's CommandInvoker is NOT touched");
        System.out.println("");
        System.out.println("🎯 The addQuiz command is in QuizService, not CourseService!");
        System.out.println("   • So undoCourse() finds no commands to undo");
        System.out.println("   • The addQuiz command remains in QuizService history");
        System.out.println("   • You need to call undoQuiz() to undo the addQuiz");
        System.out.println("");
        System.out.println("🔧 Solution Options:");
        System.out.println("   1. Call the correct service-specific undo (undoQuiz, undoEnrollment, undoCourse)");
        System.out.println("   2. Use system-wide undo (undoLastSystemAction) - checks all services");
        System.out.println("   3. Use unified undo (undoLastSystemAction) - automatically finds last command");
        System.out.println("");
        System.out.println("💡 Best Practice: Use undoLastSystemAction() for simplicity!");
        System.out.println("   It automatically finds and undoes the last executed command");
        System.out.println("   regardless of which service it belongs to.");
    }
}
