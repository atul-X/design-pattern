package lld.learningmanagementsystem;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Module;
import lld.learningmanagementsystem.model.Lesson;
import lld.learningmanagementsystem.model.VideoLesson;
import lld.learningmanagementsystem.model.TextLesson;
import lld.learningmanagementsystem.model.Students;
import lld.learningmanagementsystem.model.LearningResult;

/**
 * Demo of the improved LMS with proper SOLID principles and design patterns.
 */
public class ImprovedLMSDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Improved LMS Demo ===\n");
        
        // Create course with new architecture
        Course course = new Course();
        course.setId(1001);
        course.setName("Java Programming Fundamentals");
        course.setDescription("Learn Java from basics to advanced concepts");
        course.setInstructor("Dr. Smith");
        
        System.out.println("✅ Created course: " + course.getName());
        System.out.println("📊 Course state: " + course.getCurrentState().getClass().getSimpleName());
        
        // Create modules
        Module module1 = new Module(1, "Java Basics", "Introduction to Java", course.getId());
        Module module2 = new Module(2, "OOP Concepts", "Object-Oriented Programming", course.getId());
        
        // Create different lesson types (Open/Closed Principle)
        Lesson videoLesson = new VideoLesson(101, "Java Introduction Video", 
            "Introductory video about Java", "https://video.java.com/intro", module1.getId());
        Lesson textLesson = new TextLesson(102, "Java Syntax", 
            "Text-based lesson on Java syntax", "https://content.java.com/syntax", module1.getId());
        Lesson videoLesson2 = new VideoLesson(103, "OOP Principles", 
            "Video explaining OOP concepts", "https://video.java.com/oop", module2.getId());
        
        // Add lessons to modules
        module1.addLesson(videoLesson);
        module1.addLesson(textLesson);
        module2.addLesson(videoLesson2);
        
        // Add modules to course
        course.addModule(module1);
        course.addModule(module2);
        
        System.out.println("📚 Added modules and lessons:");
        System.out.println("  - Module 1: " + module1.getName() + " (" + module1.getLessonList().size() + " lessons)");
        System.out.println("  - Module 2: " + module2.getName() + " (" + module2.getLessonList().size() + " lessons)");
        
        // Create student
        Students student = new Students();
        student.setId(1);
        student.setName("Alice");
        
        System.out.println("\n👤 Created student: " + student.getName());
        
        // Test Template Method pattern with different lesson types
        System.out.println("\n🎯 Testing Template Method Pattern:");
        
        System.out.println("\n--- Delivering Video Lesson ---");
        LearningResult result1 = videoLesson.deliver(student);
        System.out.println("✅ Video lesson completed: " + result1.isCompleted() + 
                          " (Time: " + result1.getCompletionTime() + "ms)");
        
        System.out.println("\n--- Delivering Text Lesson ---");
        LearningResult result2 = textLesson.deliver(student);
        System.out.println("✅ Text lesson completed: " + result2.isCompleted() + 
                          " (Time: " + result2.getCompletionTime() + "ms)");
        
        // Test Single Responsibility Principle
        System.out.println("\n🔧 Testing Single Responsibility Principle:");
        System.out.println("  - Module only manages lesson organization");
        System.out.println("  - Lesson only handles content delivery");
        System.out.println("  - Course only manages overall structure");
        
        // Test Open/Closed Principle
        System.out.println("\n🔓 Testing Open/Closed Principle:");
        System.out.println("  - VideoLesson extends Lesson without modifying base class");
        System.out.println("  - TextLesson extends Lesson without modifying base class");
        System.out.println("  - Can add new lesson types (InteractiveLesson, ARlesson, etc.)");
        
        System.out.println("\n🎉 Improved LMS Demo Complete!");
        System.out.println("\n📈 Design Principles Demonstrated:");
        System.out.println("✅ Single Responsibility: Each class has one clear purpose");
        System.out.println("✅ Open/Closed: Extensible without modification");
        System.out.println("✅ Template Method: Algorithm structure in base class");
        System.out.println("✅ State Pattern: Course lifecycle management");
        System.out.println("✅ Strategy Pattern: Different lesson delivery strategies");
    }
}
