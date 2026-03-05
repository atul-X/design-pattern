package lld.learningmanagementsystem;

import lld.learningmanagementsystem.model.*;
import lld.learningmanagementsystem.service.*;

/**
 * Demo to showcase Command Pattern usage in CourseService
 * Demonstrates undoable operations for course management
 */
public class CommandPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== LMS Command Pattern Demo ===\n");

        // Initialize LMS with Command pattern support
        LMSManager lms = new LMSManager();

        // Create a course
        Course course = new Course();
        course.setId(1001);
        course.setName("Java Programming with Commands");
        course.setDescription("Learn Java with undoable operations");
        course.setInstructor("Dr. Smith");

        Course createdCourse = lms.createCourse(course);
        System.out.println("✅ Created course: " + createdCourse.getName());
        System.out.println("📊 Initial state: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Demo 1: Publish course with Command pattern
        System.out.println("\n--- Demo 1: Publishing Course (Undoable) ---");
        lms.publishCourse(1001);
        System.out.println("📊 After publish: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Undo the publish operation
        System.out.println("\n--- Undoing Publish Operation ---");
        lms.undoLastCourseAction();
        System.out.println("📊 After undo: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Demo 2: Multiple operations with undo
        System.out.println("\n--- Demo 2: Multiple Operations with Undo ---");
        
        // Publish again
        lms.publishCourse(1001);
        System.out.println("📊 Published: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Add a quiz (using legacy method for demo)
        Quiz quiz = new Quiz();
        quiz.setId(2001);
        quiz.setTotalMarks(100);
        quiz.setPassingMarks(60);
        lms.addQuizToCourse(1001, quiz);
        System.out.println("✅ Added quiz to course");

        // Archive the course
        lms.archiveCourse(1001);
        System.out.println("📊 Archived: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Show command history
        System.out.println("\n--- Command History ---");
        System.out.println("📈 Total commands in history: " + lms.getTotalCommandHistorySize());

        // Undo operations in reverse order
        System.out.println("\n--- Undoing Operations in Reverse Order ---");
        
        lms.undoLastCourseAction(); // Undo archive
        System.out.println("📊 After undo 1 (archive): " + createdCourse.getCurrentState().getClass().getSimpleName());
        
        lms.undoLastCourseAction(); // Undo quiz addition
        System.out.println("✅ After undo 2 (quiz): Quiz removed");
        
        lms.undoLastCourseAction(); // Undo publish
        System.out.println("📊 After undo 3 (publish): " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Demo 3: Enrollment with Command pattern
        System.out.println("\n--- Demo 3: Enrollment with Commands ---");
        
        // Create student
        Students student = new Students();
        student.setId(1);
        student.setName("Alice");
        lms.registerStudent(student);

        // Publish course first
        lms.publishCourse(1001);
        System.out.println("📊 Course published: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Enroll student
        lms.enrollStudent(1, 1001);
        System.out.println("✅ Student enrolled");

        // Undo enrollment
        lms.undoLastEnrollmentAction();
        System.out.println("↩️ Student enrollment undone");

        // Re-enroll
        lms.enrollStudent(1, 1001);
        System.out.println("✅ Student re-enrolled");

        // Demo 4: Quiz operations with Command pattern
        System.out.println("\n--- Demo 4: Quiz Operations with Commands ---");
        
        // Add quiz to lesson (new architecture)
        lld.learningmanagementsystem.model.Module module = new lld.learningmanagementsystem.model.Module(1, "Java Basics", "Introduction to Java", course.getId());
        Lesson lesson = new VideoLesson(101, "Java Fundamentals", "Basic Java concepts", "https://video.java.com/basics", module.getId());
        module.addLesson(lesson);
        createdCourse.addModule(module);

        lms.addQuizToLesson(1001, 1, 101, quiz);
        System.out.println("✅ Quiz added to lesson");

        // Undo quiz addition
        lms.undoLastQuizAction();
        System.out.println("↩️ Quiz addition undone");

        // Demo 5: System-wide undo
        System.out.println("\n--- Demo 5: System-wide Undo ---");
        
        // Perform multiple operations
        lms.addQuizToLesson(1001, 1, 101, quiz);
        lms.enrollStudent(1, 1001);
        lms.archiveCourse(1001);
        
        System.out.println("📈 Commands executed: " + lms.getTotalCommandHistorySize());

        // System-wide undo (undoes last operation across all services)
        lms.undoLastSystemAction();
        System.out.println("↩️ System-wide undo executed");
        System.out.println("📊 Course state: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Demo 6: Clear command history
        System.out.println("\n--- Demo 6: Clear Command History ---");
        System.out.println("📈 Commands before clear: " + lms.getTotalCommandHistorySize());
        
        lms.clearAllCommandHistory();
        System.out.println("📈 Commands after clear: " + lms.getTotalCommandHistorySize());

        // Demo 7: Legacy vs Command pattern methods
        System.out.println("\n--- Demo 7: Legacy vs Command Pattern Methods ---");
        
        // Legacy method (non-undoable)
        lms.publishCourseDirect(1001);
        System.out.println("📊 Published with legacy method: " + createdCourse.getCurrentState().getClass().getSimpleName());
        
        // Try to undo - won't work because legacy method was used
        lms.undoLastCourseAction();
        System.out.println("📊 After undo attempt: " + createdCourse.getCurrentState().getClass().getSimpleName() + " (no change expected)");

        // Command pattern method (undoable)
        lms.archiveCourse(1001);
        System.out.println("📊 Archived with command method: " + createdCourse.getCurrentState().getClass().getSimpleName());
        
        // Undo - this will work
        lms.undoLastCourseAction();
        System.out.println("📊 After undo: " + createdCourse.getCurrentState().getClass().getSimpleName() + " (should be back to published)");

        System.out.println("\n=== Command Pattern Demo Complete ===");
        System.out.println("\n🎯 Key Demonstrations:");
        System.out.println("✅ Course state changes are undoable");
        System.out.println("✅ Quiz operations are undoable");
        System.out.println("✅ Enrollment operations are undoable");
        System.out.println("✅ System-wide undo functionality");
        System.out.println("✅ Command history management");
        System.out.println("✅ Legacy vs Command pattern methods");
        System.out.println("✅ Proper state restoration on undo");
    }
}
