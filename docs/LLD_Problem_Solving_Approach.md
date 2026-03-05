# 🎯 LLD Problem Solving Approach - Complete Guide

## 📋 Table of Contents
- [Understanding the Problem](#understanding-the-problem)
- [Requirements Gathering](#requirements-gathering)
- [System Design](#system-design)
- [Class Design](#class-design)
- [Design Patterns](#design-patterns)
- [Implementation Strategy](#implementation-strategy)
- [Testing Approach](#testing-approach)
- [Common Mistakes](#common-mistakes)
- [Interview Tips](#interview-tips)

---

## 🎯 Step 1: Understanding the Problem

### **🔍 Clarify the Problem Statement**

#### **✅ Key Questions to Ask:**
1. **What is the core functionality?**
   - What does the system do?
   - Who are the users?
   - What are the main operations?

2. **What are the constraints?**
   - Performance requirements
   - Scalability needs
   - Memory limitations
   - Concurrent users

3. **What are the edge cases?**
   - Error conditions
   - Boundary conditions
   - Invalid inputs

#### **📝 Example: LMS System**
```
🎯 Problem: Design a Learning Management System

❓ Questions to ask:
- What types of users? (Students, Teachers, Admins)
- What operations? (Create courses, enroll students, take quizzes)
- What constraints? (1000 concurrent users, 1M courses)
- What edge cases? (Invalid enrollment, course deletion)
```

---

## 🎯 Step 2: Requirements Gathering

### **📋 Functional Requirements**

#### **✅ Identify Core Entities**
```
🎯 LMS Example:
- User (Student, Teacher, Admin)
- Course
- Module
- Lesson
- Quiz
- Question
- Enrollment
- Grade
```

#### **✅ Identify Core Operations**
```
🎯 LMS Example:
- User Management: Register, Login, Profile
- Course Management: Create, Update, Delete, Publish
- Enrollment: Enroll, Drop, View enrolled courses
- Content: Add modules, lessons, quizzes
- Assessment: Take quiz, submit answers, get grades
```

#### **✅ Identify Business Rules**
```
🎯 LMS Example:
- Only published courses can be enrolled in
- Teachers can only edit their own courses
- Students can only take published quizzes
- Courses must have at least one module
```

### **📋 Non-Functional Requirements**

#### **✅ Performance Requirements**
```
🎯 LMS Example:
- Response time < 200ms for course listing
- Support 1000 concurrent users
- Quiz submission < 500ms
```

#### **✅ Scalability Requirements**
```
🎯 LMS Example:
- Handle 1M users
- Handle 100K courses
- Handle 1M quiz attempts per day
```

---

## 🎯 Step 3: System Design

### **🏗️ High-Level Architecture**

#### **✅ Choose Architecture Pattern**
```
🎯 Common Patterns:
- Layered Architecture (most common)
- Microservices (for large systems)
- Event-Driven (for real-time systems)
- MVC (for web applications)
```

#### **✅ Define Layers**
```
🎯 LMS Example:
┌─────────────────┐
│   Presentation  │  (UI/Controllers)
├─────────────────┤
│    Business     │  (Services/Logic)
├─────────────────┤
│     Data        │  (Repositories/Database)
└─────────────────┘
```

#### **✅ Identify Key Components**
```
🎯 LMS Example:
- UserService (user management)
- CourseService (course operations)
- EnrollmentService (enrollment logic)
- QuizService (quiz management)
- GradeService (grading logic)
```

### **🔗 Define Relationships**

#### **✅ Entity Relationships**
```
🎯 LMS Example:
User 1---* Enrollment *---1 Course
Course 1---* Module *---1 Lesson
Lesson 1---* Quiz *---* Question
Quiz 1---* QuizAttempt *---1 User
```

#### **✅ Service Dependencies**
```
🎯 LMS Example:
CourseService → UserService (validation)
EnrollmentService → CourseService (availability)
QuizService → CourseService (access control)
GradeService → QuizService (grading)
```

---

## 🎯 Step 4: Class Design

### **🏗️ Class Identification**

#### **✅ Entity Classes**
```java
// Core business objects
public class User {
    private int id;
    private String name;
    private UserType userType;
    private List<Enrollment> enrollments;
}

public class Course {
    private int id;
    private String name;
    private User instructor;
    private List<Module> modules;
    private CourseState state;
}

public class Enrollment {
    private User student;
    private Course course;
    private Date enrollmentDate;
    private EnrollmentStatus status;
}
```

#### **✅ Service Classes**
```java
// Business logic
public class CourseService {
    public Course createCourse(Course course);
    public void publishCourse(int courseId);
    public void archiveCourse(int courseId);
    public List<Course> getCoursesByInstructor(int instructorId);
}

public class EnrollmentService {
    public void enrollStudent(int studentId, int courseId);
    public void dropCourse(int studentId, int courseId);
    public List<Course> getEnrolledCourses(int studentId);
}
```

#### **✅ Repository/DAO Classes**
```java
// Data access
public interface CourseRepository {
    Course save(Course course);
    Course findById(int id);
    List<Course> findByInstructor(int instructorId);
    void delete(Course course);
}
```

### **🎯 SOLID Principles Application**

#### **✅ Single Responsibility Principle**
```java
// ❌ WRONG: One class doing everything
public class CourseManager {
    public void createCourse() { ... }
    public void saveToDatabase() { ... }
    public void sendEmail() { ... }
    public void validateInput() { ... }
}

// ✅ RIGHT: Separate responsibilities
public class CourseService {
    public void createCourse() { ... }
}

public class CourseRepository {
    public void save(Course course) { ... }
}

public class EmailService {
    public void sendEmail() { ... }
}

public class CourseValidator {
    public void validate(Course course) { ... }
}
```

#### **✅ Open/Closed Principle**
```java
// ✅ Open for extension, closed for modification
public interface GradingStrategy {
    double calculateGrade(List<QuizAttempt> attempts);
}

public class PercentageGradingStrategy implements GradingStrategy {
    public double calculateGrade(List<QuizAttempt> attempts) { ... }
}

public class CurveGradingStrategy implements GradingStrategy {
    public double calculateGrade(List<QuizAttempt> attempts) { ... }
}
```

#### **✅ Liskov Substitution Principle**
```java
// ✅ Subtypes must be substitutable
public abstract class Lesson {
    public abstract void deliver();
    public abstract int getDuration();
}

public class VideoLesson extends Lesson {
    public void deliver() { /* play video */ }
    public int getDuration() { return video.getLength(); }
}

public class TextLesson extends Lesson {
    public void deliver() { /* display text */ }
    public int getDuration() { return text.getWordCount() / 200; }
}
```

---

## 🎯 Step 5: Design Patterns

### **🎯 Choose Appropriate Patterns**

#### **✅ Creational Patterns**
```
🎯 When to use:
- Factory Method: When you need to create objects without specifying exact class
- Builder: When object has many optional parameters
- Singleton: When you need exactly one instance
```

#### **✅ Structural Patterns**
```
🎯 When to use:
- Adapter: When you need to integrate incompatible interfaces
- Decorator: When you need to add functionality dynamically
- Facade: When you need to simplify complex subsystem
- Composite: When you need to treat individual objects and compositions uniformly
```

#### **✅ Behavioral Patterns**
```
🎯 When to use:
- Strategy: When you need multiple algorithms
- Observer: When you need to notify multiple objects
- Command: When you need to undo/redo operations
- State: When behavior changes based on state
- Template Method: When you have algorithm skeleton
```

### **🎯 Pattern Selection Guide**

#### **✅ LMS Pattern Application**
```java
// 🎯 State Pattern: Course lifecycle
public interface CourseState {
    void publish(Course course);
    void archive(Course course);
    void addQuiz(Course course, Quiz quiz);
}

// 🎯 Command Pattern: Undoable operations
public interface Command {
    void execute();
    void undo();
}

// 🎯 Strategy Pattern: Different grading methods
public interface GradingStrategy {
    double calculateGrade(List<QuizAttempt> attempts);
}

// 🎯 Template Method: Lesson delivery
public abstract class Lesson {
    public final void deliver() {
        prepareContent();
        deliverContent();
        trackProgress();
    }
    
    protected abstract void deliverContent();
}

// 🎯 Factory Method: Create different lesson types
public class LessonFactory {
    public static Lesson createLesson(LessonType type, ...) {
        switch(type) {
            case VIDEO: return new VideoLesson(...);
            case TEXT: return new TextLesson(...);
            case QUIZ: return new QuizLesson(...);
        }
    }
}
```

---

## 🎯 Step 6: Implementation Strategy

### **🚀 Development Approach**

#### **✅ Incremental Development**
```
🎯 Phase 1: Core Entities
- Create User, Course, Enrollment classes
- Basic CRUD operations
- Simple validation

🎯 Phase 2: Business Logic
- Add service classes
- Implement business rules
- Add state management

🎯 Phase 3: Advanced Features
- Add design patterns
- Implement undo/redo
- Add complex validations

🎯 Phase 4: Optimization
- Add caching
- Improve performance
- Add error handling
```

#### **✅ Test-Driven Development**
```java
// ✅ Write tests first
@Test
void shouldCreateCourseSuccessfully() {
    // Given
    Course course = new Course("Java Course", "Learn Java");
    
    // When
    Course created = courseService.createCourse(course);
    
    // Then
    assertNotNull(created);
    assertEquals("Java Course", created.getName());
}

// ✅ Then implement
public Course createCourse(Course course) {
    validateCourse(course);
    return courseRepository.save(course);
}
```

### **🔧 Code Organization**

#### **✅ Package Structure**
```
com.lms/
├── model/           # Entity classes
│   ├── User.java
│   ├── Course.java
│   └── Enrollment.java
├── service/         # Business logic
│   ├── UserService.java
│   ├── CourseService.java
│   └── EnrollmentService.java
├── repository/      # Data access
│   ├── UserRepository.java
│   └── CourseRepository.java
├── controller/      # API layer
│   ├── UserController.java
│   └── CourseController.java
├── util/           # Utility classes
│   ├── Validator.java
│   └── EmailService.java
└── exception/      # Custom exceptions
    ├── CourseNotFoundException.java
    └── EnrollmentException.java
```

---

## 🎯 Step 7: Testing Approach

### **🧪 Testing Strategy**

#### **✅ Unit Testing**
```java
// Test individual classes in isolation
@Test
void shouldValidateCourseCorrectly() {
    CourseValidator validator = new CourseValidator();
    Course course = new Course("", ""); // Invalid course
    
    assertThrows(ValidationException.class, 
        () -> validator.validate(course));
}

@Test
void shouldCalculateGradeCorrectly() {
    GradingStrategy strategy = new PercentageGradingStrategy();
    List<QuizAttempt> attempts = createMockAttempts();
    
    double grade = strategy.calculateGrade(attempts);
    
    assertEquals(85.0, grade);
}
```

#### **✅ Integration Testing**
```java
// Test service interactions
@SpringBootTest
class CourseServiceIntegrationTest {
    @Autowired private CourseService courseService;
    @Autowired private UserRepository userRepository;
    
    @Test
    void shouldCreateCourseWithValidInstructor() {
        User instructor = createTestInstructor();
        Course course = new Course("Test Course", instructor);
        
        Course created = courseService.createCourse(course);
        
        assertNotNull(created);
        assertEquals(instructor, created.getInstructor());
    }
}
```

#### **✅ Edge Case Testing**
```java
@Test
void shouldHandleInvalidEnrollment() {
    // Test enrolling in non-existent course
    assertThrows(CourseNotFoundException.class,
        () -> enrollmentService.enrollStudent(1, 999));
    
    // Test enrolling twice in same course
    enrollmentService.enrollStudent(1, 1);
    assertThrows(EnrollmentException.class,
        () -> enrollmentService.enrollStudent(1, 1));
}
```

---

## 🎯 Step 8: Common Mistakes to Avoid

### **❌ Design Mistakes**

#### **1. God Class**
```java
// ❌ WRONG: One class doing everything
public class LMSManager {
    public void createUser() { ... }
    public void createCourse() { ... }
    public void enrollStudent() { ... }
    public void saveToDatabase() { ... }
    public void sendEmail() { ... }
    public void validateInput() { ... }
}

// ✅ RIGHT: Separate responsibilities
public class UserService { public void createUser() { ... } }
public class CourseService { public void createCourse() { ... } }
public class EnrollmentService { public void enrollStudent() { ... } }
```

#### **2. Tight Coupling**
```java
// ❌ WRONG: Direct dependency on concrete classes
public class CourseService {
    private MySQLCourseRepository repository = new MySQLCourseRepository();
}

// ✅ RIGHT: Dependency injection
public class CourseService {
    private CourseRepository repository;
    
    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }
}
```

#### **3. Violating SRP**
```java
// ❌ WRONG: Multiple responsibilities
public class Course {
    public void save() { ... }           // Data access
    public void validate() { ... }        // Validation
    public void sendEmail() { ... }       // Notification
}

// ✅ RIGHT: Single responsibility
public class Course { /* Only course data */ }
public class CourseRepository { public void save() { ... } }
public class CourseValidator { public void validate() { ... } }
public class EmailService { public void sendEmail() { ... } }
```

### **❌ Implementation Mistakes**

#### **1. Not Handling Edge Cases**
```java
// ❌ WRONG: No null checks
public void enrollStudent(int studentId, int courseId) {
    Course course = courseRepository.findById(courseId);
    course.enroll(studentId); // NPE if course is null
}

// ✅ RIGHT: Proper error handling
public void enrollStudent(int studentId, int courseId) {
    Course course = courseRepository.findById(courseId);
    if (course == null) {
        throw new CourseNotFoundException(courseId);
    }
    if (!course.isPublished()) {
        throw new CourseNotPublishedException(courseId);
    }
    course.enroll(studentId);
}
```

#### **2. Not Using Design Patterns**
```java
// ❌ WRONG: Hard-coded logic
public String getGrade(double percentage) {
    if (percentage >= 90) return "A";
    else if (percentage >= 80) return "B";
    else if (percentage >= 70) return "C";
    else return "F";
}

// ✅ RIGHT: Strategy pattern
public class GradingService {
    private GradingStrategy strategy;
    
    public String getGrade(double percentage) {
        return strategy.calculateGrade(percentage);
    }
}
```

---

## 🎯 Step 9: Interview Tips

### **💡 Communication Strategy**

#### **✅ Problem Clarification**
```
Interviewer: "Design a parking lot system"

You: "Let me clarify a few things:
1. What types of vehicles? (Car, Motorcycle, Truck)
2. What operations? (Park, Unpark, Payment)
3. What constraints? (1000 spots, multiple levels)
4. Any special features? (VIP spots, disabled parking)"
```

#### **✅ Think Out Loud**
```
You: "I'm thinking about the core entities:
- ParkingLot with multiple levels
- Level with multiple spots
- Spot that can be occupied or empty
- Vehicle of different types
- Ticket for payment tracking

Let me start with the basic structure and then add complexity..."
```

#### **✅ Explain Design Decisions**
```
You: "I'm using the State pattern for parking spots
because a spot can be empty, occupied, or reserved.
This makes it easy to change behavior based on state.

I'm using the Factory pattern for creating different
vehicle types because we might add new vehicle types later."
```

### **🎯 Common Interview Questions**

#### **✅ "Why did you choose this design pattern?"**
```
Answer: "I chose the State pattern because:
1. Parking spots have different behaviors based on their state
2. We need to add new states easily (reserved, under maintenance)
3. It follows Open/Closed principle
4. It makes the code more readable and maintainable"
```

#### **✅ "How would you scale this system?"**
```
Answer: "For scaling, I would:
1. Use distributed database for multiple locations
2. Add caching for frequently accessed data
3. Use message queues for async operations
4. Implement load balancing for high traffic
5. Add monitoring and alerting"
```

#### **✅ "What are the trade-offs?"**
```
Answer: "The trade-offs are:
1. More classes increase complexity
2. Initial development time is higher
3. Memory usage is slightly higher
4. But benefits: maintainability, extensibility, testability"
```

### **🎯 Time Management**

#### **✅ 45-Minute Breakdown**
```
🕐 0-5 min: Clarify requirements
🕐 5-15 min: High-level design and entities
🕐 15-30 min: Class design and relationships
🕐 30-40 min: Design patterns and implementation
🕐 40-45 min: Testing and edge cases
```

---

## 🎯 Step 10: Complete Example

### **🎯 Parking Lot System - Complete Approach**

#### **✅ 1. Requirements**
```
Entities: ParkingLot, Level, Spot, Vehicle, Ticket
Operations: Park, Unpark, Payment
Constraints: Multiple levels, different vehicle types
```

#### **✅ 2. Class Design**
```java
public class ParkingLot {
    private List<Level> levels;
    private ParkingStrategy strategy;
}

public class Level {
    private int levelId;
    private List<Spot> spots;
}

public class Spot {
    private int spotId;
    private VehicleType vehicleType;
    private SpotState state;
}

public abstract class Vehicle {
    private String licensePlate;
    private VehicleType type;
}
```

#### **✅ 3. Design Patterns**
```java
// State pattern for spots
public interface SpotState {
    void park(Vehicle vehicle);
    void unpark();
}

// Strategy pattern for parking
public interface ParkingStrategy {
    Spot findSpot(List<Spot> spots, VehicleType type);
}

// Factory pattern for vehicles
public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type, String plate) {
        // Create based on type
    }
}
```

#### **✅ 4. Implementation**
```java
public class ParkingService {
    public Ticket parkVehicle(Vehicle vehicle) {
        Spot spot = strategy.findSpot(availableSpots, vehicle.getType());
        spot.park(vehicle);
        return new Ticket(spot, vehicle);
    }
    
    public void unparkVehicle(Ticket ticket) {
        Spot spot = ticket.getSpot();
        spot.unpark();
        processPayment(ticket);
    }
}
```

---

## 🎯 Summary

### **🎯 Complete LLD Approach:**

1. **🔍 Understand Problem**: Clarify requirements and constraints
2. **📋 Gather Requirements**: Identify entities, operations, rules
3. **🏗️ System Design**: Choose architecture, define components
4. **🎯 Class Design**: Apply SOLID principles, define relationships
5. **🎨 Design Patterns**: Choose appropriate patterns
6. **🚀 Implementation**: Incremental development, TDD
7. **🧪 Testing**: Unit, integration, edge cases
8. **❌ Avoid Mistakes**: Common pitfalls and solutions
9. **💡 Interview Tips**: Communication, time management
10. **🎯 Practice**: Complete examples and scenarios

### **🎯 Key Takeaways:**

- **Start simple**, then add complexity
- **Think out loud** in interviews
- **Apply SOLID principles** consistently
- **Choose design patterns** wisely
- **Test thoroughly** including edge cases
- **Explain trade-offs** and decisions
- **Practice common scenarios**

**This approach will help you solve any LLD problem systematically and confidently!** 🎯
