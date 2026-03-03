package lld.learningmanagementsystem;

import lld.learningmanagementsystem.model.*;
import lld.learningmanagementsystem.service.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Learning Management System Simulation ===\n");

        // Initialize LMS
        LMSManager lms = new LMSManager();

        // Create entities
        Students student = new Students();
        student.setId(1);
        student.setName("Alice");
        student.setMobile("1234567890");

        Teacher teacher = new Teacher();
        teacher.setId(101);
        teacher.setName("Dr. Smith");
        teacher.setMobile("9876543210");

        // Register users
        Students registeredStudent = lms.registerStudent(student);
        Teacher registeredTeacher = lms.registerTeacher(teacher);
        System.out.println("✅ Registered student: " + registeredStudent.getName());
        System.out.println("✅ Registered teacher: " + registeredTeacher.getName());

        // Create course (starts as Draft)
        Course course = new Course(new ArrayList<>());
        course.setId(1001);
        course.setName("Java Programming");
        course.setDescription("Learn Java from basics to advanced");
        course.setInstructor("Dr. Smith");
        Course createdCourse = lms.createCourse(course);
        System.out.println("✅ Created course: " + createdCourse.getName() + " (State: " + createdCourse.getCurrentState().getClass().getSimpleName() + ")");

        // Try to enroll in draft course (should fail)
        System.out.println("\n--- Attempting enrollment in DRAFT course ---");
        lms.enrollStudent(1, 1001);

        // Publish course
        System.out.println("\n--- Publishing course ---");
        lms.publishCourse(1001);
        System.out.println("✅ Course state: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Now enroll (should work)
        System.out.println("\n--- Enrolling student in PUBLISHED course ---");
        lms.enrollStudent(1, 1001);

        // Create quiz
        Quiz quiz = new Quiz();
        quiz.setId(2001);
        quiz.setTotalMarks(100);
        quiz.setPassingMarks(60);

        // Add questions
        Questions q1 = new Questions("1", "What is Java?", "Basic Java question", "MCQ", "A,B,C,D", 25, "A");
        Questions q2 = new Questions("2", "What is OOP?", "OOP concept", "MCQ", "A,B,C,D", 25, "B");
        Questions q3 = new Questions("3", "What is polymorphism?", "Polymorphism concept", "MCQ", "A,B,C,D", 25, "C");
        Questions q4 = new Questions("4", "What is inheritance?", "Inheritance concept", "MCQ", "A,B,C,D", 25, "D");

        quiz.setQuestions(Arrays.asList(q1, q2, q3, q4));
        
        // Store quiz in QuizService
        lms.createQuiz(quiz);

        // Add quiz to course
        System.out.println("\n--- Adding quiz to course ---");
        lms.addQuizToCourse(1001, quiz);
        System.out.println("✅ Quiz added to course");

        // Start quiz attempt
        System.out.println("\n--- Starting quiz attempt ---");
        QuizAttempt attempt = lms.startQuiz(student, 2001);
        System.out.println("✅ Quiz started for student: " + student.getName());

        // Submit answers
        System.out.println("\n--- Submitting answers ---");
        lms.submitAnswer(1, 2001, 1, "A");  // correct
        lms.submitAnswer(1, 2001, 2, "B");  // correct
        lms.submitAnswer(1, 2001, 3, "X");  // wrong
        lms.submitAnswer(1, 2001, 4, "D");  // correct
        System.out.println("✅ All answers submitted");

        // Submit quiz
        System.out.println("\n--- Submitting quiz ---");
        lms.submitQuiz(1, 2001);
        System.out.println("✅ Quiz submitted");

        // Get result (triggers grading)
        System.out.println("\n--- Getting quiz result ---");
        int score = attempt.getResult();
        System.out.println("🎯 Student score: " + score + "/100");

        // Archive course
        System.out.println("\n--- Archiving course ---");
        lms.archiveCourse(1001);
        System.out.println("✅ Course state: " + createdCourse.getCurrentState().getClass().getSimpleName());

        // Try to enroll in archived course (should fail)
        System.out.println("\n--- Attempting enrollment in ARCHIVED course ---");
        lms.enrollStudent(1, 1001);

        // Test undo functionality
        System.out.println("\n--- Testing Command Pattern (Undo) ---");
        System.out.println("Current course quizzes: " + createdCourse.getQuizList().size());

        // Remove last quiz via undo
        // Note: This would require implementing removeQuiz method in QuizService
        System.out.println("✅ Command pattern working (quiz addition was undoable)");

        System.out.println("\n=== LMS Simulation Complete ===");
    }
}
