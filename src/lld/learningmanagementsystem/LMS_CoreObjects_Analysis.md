# LMS Core Objects & Relationships Analysis

## 🎯 Executive Summary

The Learning Management System (LMS) is built around **5 core domain objects** that interact through **service layers** implementing multiple design patterns. The system manages the complete lifecycle of courses, quizzes, and student learning experiences.

---

## 🏗️ Core Domain Objects

### 1. **Students** - The Learner Entity
**Purpose**: Represents learners who take courses and quizzes

**Key Attributes**:
- `id`: Unique identifier
- `name`: Student's full name
- `mobile`: Contact information

**Core Responsibilities**:
- Register in the system
- Enroll in courses
- Attempt quizzes
- Receive grades and certificates

**Relationships**:
- `1 → *` QuizAttempts (one student can attempt many quizzes)
- `* → *` Courses (many-to-many via enrollment)

---

### 2. **Teacher** - The Instructor Entity
**Purpose**: Represents educators who create and manage courses

**Key Attributes**:
- `id`: Unique identifier
- `name`: Teacher's full name
- `mobile`: Contact information

**Core Responsibilities**:
- Create and manage courses
- Add quizzes to courses
- Track student progress

**Relationships**:
- `1 → *` Courses (one teacher can create many courses)

---

### 3. **Course** - The Learning Container
**Purpose**: Primary container for educational content and student enrollment

**Key Attributes**:
- `id`: Unique identifier
- `name`: Course title
- `description`: Course overview
- `instructor`: Assigned teacher
- `quizList`: Collection of quizzes
- `currentState`: Lifecycle state (Draft/Published/Archived)

**Core Responsibilities**:
- Manage enrollment through state transitions
- Host quizzes and track student progress
- Control access based on lifecycle state

**Relationships**:
- `* → 1` Teacher (many courses to one teacher)
- `1 → *` Quizzes (one course contains many quizzes)
- `* → *` Students (many-to-many via enrollment)
- `1 → 1` CourseState (composition)

---

### 4. **Quiz** - The Assessment Tool
**Purpose**: Evaluates student knowledge and skills

**Key Attributes**:
- `id`: Unique identifier
- `totalMarks`: Maximum possible score
- `passingMarks`: Minimum score to pass
- `questions`: Collection of assessment items

**Core Responsibilities**:
- Define assessment criteria
- Track student attempts
- Calculate grades through strategy pattern

**Relationships**:
- `* → 1` Course (many quizzes belong to one course)
- `1 → *` Questions (one quiz has many questions)
- `1 → *` QuizAttempts (one quiz attempted by many students)

---

### 5. **Questions** - The Assessment Items
**Purpose**: Individual items that make up quizzes

**Key Attributes**:
- `id`: Unique identifier
- `name`: Question title
- `description`: Question details
- `type`: Question format (MCQ, etc.)
- `options`: Available choices
- `marks`: Point value
- `ans`: Correct answer

**Core Responsibilities**:
- Define specific assessment content
- Provide correct answers for grading
- Support different question types

**Relationships**:
- `* → 1` Quiz (many questions belong to one quiz)

---

## 🔄 Supporting Objects

### 6. **QuizAttempt** - The Assessment Context
**Purpose**: Tracks individual student quiz sessions

**Key Attributes**:
- `currentState`: Quiz lifecycle state
- `student`: Reference to the student
- `quiz`: Reference to the quiz
- `answers`: Student's responses

**Core Responsibilities**:
- Manage quiz lifecycle through state pattern
- Store student answers
- Calculate final grades

**Relationships**:
- `1 → 1` Student (each attempt belongs to one student)
- `1 → 1` Quiz (each attempt is for one quiz)
- `1 → 1` QuizState (composition)

---

## 🎲 Design Pattern Objects

### State Pattern Objects
- **CourseState**: Interface for course lifecycle
- **DraftCourse/PublishCourse/ArchiveCourse**: Concrete course states
- **QuizState**: Interface for quiz lifecycle  
- **NotStartedState/InProgressState/SubmittedState/GradedState**: Concrete quiz states

### Command Pattern Objects
- **Command**: Interface for undoable operations
- **CommandImpl**: Lambda-based command implementation
- **CommandInvoker**: Manages command history and undo

### Strategy Pattern Objects
- **GradingStrategy**: Interface for grading algorithms
- **AutoGradeingService**: Automatic grading implementation
- **GradingService**: Context for grading strategies

---

## 🔗 Object Relationships Matrix

| Object | Students | Teacher | Course | Quiz | Questions | QuizAttempt |
|--------|----------|---------|--------|------|-----------|-------------|
| **Students** | - | - | * enrollment | * attempts | - | 1 attempts |
| **Teacher** | - | - | * creates | - | - | - |
| **Course** | * enrollment | * created by | - | * contains | - | - |
| **Quiz** | * attempts | - | * belongs to | - | * has | * attempted |
| **Questions** | - | - | - | * part of | - | - |
| **QuizAttempt** | 1 belongs to | - | - | 1 for | - | - |

---

## 📊 Relationship Types Analysis

### **Composition Relationships (Strong Ownership)**
- `Course` → `CourseState` (course owns its current state)
- `QuizAttempt` → `QuizState` (attempt owns its current state)
- `GradingService` → `GradingStrategy` (service owns its strategy)

### **Aggregation Relationships (Loose Coupling)**
- `Course` → `Quiz` (course contains quizzes)
- `Quiz` → `Questions` (quiz contains questions)
- `Course` → `Students` (course enrolls students)

### **Association Relationships (Usage)**
- `Teacher` → `Course` (teacher manages courses)
- `Students` → `QuizAttempt` (student creates attempts)
- `QuizService` → `CommandInvoker` (service uses commands)

---

## 🔄 Interaction Flows

### **Course Enrollment Flow**
```
Student → LMSManager → EnrollmentService → CourseState → Course
```

### **Quiz Taking Flow**
```
Student → LMSManager → QuizService → QuizAttempt → QuizState → GradingService
```

### **Course Management Flow**
```
Teacher → LMSManager → CourseService → Course → CourseState
```

---

## 🎯 Key Design Principles Applied

### **Single Responsibility Principle**
- Each object has one clear purpose
- Services handle business logic separately from domain objects

### **Open/Closed Principle**
- Strategy pattern allows adding new grading algorithms
- State pattern allows adding new course/quiz states

### **Dependency Inversion**
- Services depend on abstractions (interfaces)
- LMSManager depends on service interfaces

### **Encapsulation**
- State transitions are encapsulated within state objects
- Command execution is encapsulated within command objects

---

## 📈 Scalability Considerations

### **Horizontal Scalability**
- Services can be distributed across multiple instances
- State objects are lightweight and stateless (except current state)

### **Vertical Scalability**
- Quiz attempts can be partitioned by student/course
- Course enrollments can be sharded by course ID

### **Extensibility Points**
- New question types via Questions subclassing
- New grading strategies via GradingStrategy interface
- New course states via CourseState interface
- New quiz states via QuizState interface

---

## 🎪 Real-World Mapping

| System Object | Real-World Equivalent | Business Impact |
|---------------|----------------------|-----------------|
| **Students** | Learners/Trainees | Customer acquisition & retention |
| **Teacher** | Instructors/Professors | Content quality & expertise |
| **Course** | Academic Programs | Revenue generation & curriculum |
| **Quiz** | Exams/Assessments | Learning validation & certification |
| **Questions** | Test Items | Knowledge measurement granularity |

---

## 🔍 Critical Success Factors

### **Data Integrity**
- Foreign key relationships maintained through services
- State transitions validated before execution

### **Performance**
- Lazy loading of quiz questions
- Command history bounded to prevent memory leaks

### **Maintainability**
- Clear separation between domain and infrastructure
- Design patterns enable isolated changes

### **Testability**
- Each object can be unit tested independently
- Mock implementations available for all interfaces

---

## 🎯 Conclusion

The LMS core objects form a **cohesive ecosystem** where each entity has clear responsibilities and well-defined relationships. The use of design patterns ensures **flexibility**, **maintainability**, and **extensibility** while keeping the business logic clean and separated from infrastructure concerns.

The **state patterns** provide robust lifecycle management, the **command pattern** ensures operation traceability, the **strategy pattern** enables algorithm flexibility, and the **facade pattern** presents a clean, unified API to the outside world.
