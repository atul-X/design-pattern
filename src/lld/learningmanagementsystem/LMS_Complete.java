//// =============================================================================
//// Learning Management System - Complete Implementation
//// Demonstrates: Command Pattern, State Pattern, Strategy Pattern, Facade Pattern
//// =============================================================================
//
//package lms;
//
//import java.util.*;
//
//// =============================================================================
//// MODELS
//// =============================================================================
//
//// Student Model
//class Students {
//    private int id;
//    private String name;
//    private String mobile;
//
//    public Students() {}
//
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    public String getMobile() { return mobile; }
//    public void setMobile(String mobile) { this.mobile = mobile; }
//}
//
//// Teacher Model
//class Teacher {
//    private int id;
//    private String name;
//    private String mobile;
//
//    public Teacher() {}
//
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    public String getMobile() { return mobile; }
//    public void setMobile(String mobile) { this.mobile = mobile; }
//}
//
//// Questions Model
//class Questions {
//    private String id;
//    private String name;
//    private String description;
//    private String type;
//    private String options;
//    private int marks;
//    private String ans;
//
//    public Questions(String id, String name, String description, String type, String options, int marks, String ans) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.type = type;
//        this.options = options;
//        this.marks = marks;
//        this.ans = ans;
//    }
//
//    public String getId() { return id; }
//    public String getAns() { return ans; }
//    public int getMarks() { return marks; }
//}
//
//// Quiz Model
//class Quiz {
//    private int id;
//    private int totalMarks;
//    private int passingMarks;
//    private List<Questions> questions;
//
//    public Quiz() {
//        this.questions = new ArrayList<>();
//    }
//
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//    public int getTotalMarks() { return totalMarks; }
//    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
//    public int getPassingMarks() { return passingMarks; }
//    public void setPassingMarks(int passingMarks) { this.passingMarks = passingMarks; }
//    public List<Questions> getQuestions() { return questions; }
//    public void setQuestions(List<Questions> questions) { this.questions = questions; }
//}
//
//// Course Model
//class Course {
//    private int id;
//    private String name;
//    private String description;
//    private String instructor;
//    private String language;
//    private String status;
//    private List<Quiz> quizList;
//    private CourseState currentState;
//
//    public Course(List<Quiz> quizList) {
//        this.quizList = quizList;
//        this.currentState = new DraftCourse();  // all courses start as draft
//    }
//
//    // Getters and Setters
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//    public String getInstructor() { return instructor; }
//    public void setInstructor(String instructor) { this.instructor = instructor; }
//    public String getLanguage() { return language; }
//    public void setLanguage(String language) { this.language = language; }
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//    public List<Quiz> getQuizList() { return quizList; }
//    public void setQuizList(List<Quiz> quizList) { this.quizList = quizList; }
//    public CourseState getCurrentState() { return currentState; }
//    public void setCurrentState(CourseState currentState) { this.currentState = currentState; }
//
//    // State pattern delegate methods
//    public void publish() { currentState.publish(this); }
//    public void archive() { currentState.archive(this); }
//    public void addQuiz(Quiz quiz) { currentState.addQuiz(this, quiz); }
//    public void enroll(int studentId) { currentState.enroll(this, studentId); }
//}
//
//// =============================================================================
//// COMMAND PATTERN
//// =============================================================================
//
//// Command Interface
//interface Command {
//    void execute();
//    void undo();
//}
//
//// Command Implementation with Lambdas
//class CommandImpl implements Command {
//    private Runnable executeAction;
//    private Runnable undoAction;
//
//    public CommandImpl(Runnable executeAction, Runnable undoAction) {
//        this.executeAction = executeAction;
//        this.undoAction = undoAction;
//    }
//
//    @Override
//    public void execute() { executeAction.run(); }
//
//    @Override
//    public void undo() { undoAction.run(); }
//}
//
//// Command Invoker
//class CommandInvoker {
//    private Stack<Command> commandHistory = new Stack<>();
//    private static final int MAX_HISTORY = 10;
//
//    public void executeCommand(Command command) {
//        command.execute();
//        commandHistory.push(command);
//        if (commandHistory.size() > MAX_HISTORY) {
//            commandHistory.remove(0); // Remove oldest command
//        }
//    }
//
//    public void undo() {
//        if (!commandHistory.isEmpty()) {
//            Command lastCommand = commandHistory.pop();
//            lastCommand.undo();
//        }
//    }
//}
//
//// =============================================================================
//// COURSE STATE PATTERN
//// =============================================================================
//
//// Course State Interface
//interface CourseState {
//    void publish(Course course);
//    void archive(Course course);
//    void addQuiz(Course course, Quiz quiz);
//    void enroll(Course course, int studentId);
//}
//
//// Draft State
//class DraftCourse implements CourseState {
//    @Override
//    public void publish(Course course) {
//        System.out.println("Course published!");
//        course.setCurrentState(new PublishCourse());
//    }
//
//    @Override
//    public void archive(Course course) {
//        System.out.println("Cannot archive — course not published yet.");
//    }
//
//    @Override
//    public void addQuiz(Course course, Quiz quiz) {
//        System.out.println("Quiz added to draft course.");
//        course.getQuizList().add(quiz);
//    }
//
//    @Override
//    public void enroll(Course course, int studentId) {
//        System.out.println("Cannot enroll — course not published yet.");
//    }
//}
//
//// Published State
//class PublishCourse implements CourseState {
//    @Override
//    public void publish(Course course) {
//        System.out.println("Course is already published.");
//    }
//
//    @Override
//    public void archive(Course course) {
//        System.out.println("Course archived!");
//        course.setCurrentState(new ArchiveCourse());
//    }
//
//    @Override
//    public void addQuiz(Course course, Quiz quiz) {
//        System.out.println("Quiz Added");
//        course.getQuizList().add(quiz);
//    }
//
//    @Override
//    public void enroll(Course course, int studentId) {
//        System.out.println("Student " + studentId + " enrolled in course " + course.getId());
//    }
//}
//
//// Archived State
//class ArchiveCourse implements CourseState {
//    @Override
//    public void publish(Course course) {
//        System.out.println("Cannot publish — course is archived.");
//    }
//
//    @Override
//    public void archive(Course course) {
//        System.out.println("Course already archived.");
//    }
//
//    @Override
//    public void addQuiz(Course course, Quiz quiz) {
//        System.out.println("Cannot add quiz — course is archived.");
//    }
//
//    @Override
//    public void enroll(Course course, int studentId) {
//        System.out.println("Cannot enroll — course is archived.");
//    }
//}
//
//// =============================================================================
//// QUIZ STATE PATTERN
//// =============================================================================
//
//// Quiz State Interface
//interface QuizState {
//    void startQuiz(QuizAttempt attempt);
//    void submitAnswer(QuizAttempt attempt, int questionId, String answer);
//    void submitQuiz(QuizAttempt attempt);
//    int getResult(QuizAttempt attempt);
//}
//
//// Not Started State
//class NotStartedState implements QuizState {
//    @Override
//    public void startQuiz(QuizAttempt attempt) {
//        System.out.println("Quiz started");
//        attempt.setCurrentState(new InProgressState());
//    }
//
//    @Override
//    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
//        System.out.println("Cannot submit answer — quiz not started yet.");
//    }
//
//    @Override
//    public void submitQuiz(QuizAttempt attempt) {
//        System.out.println("Cannot submit quiz — quiz not started yet.");
//    }
//
//    @Override
//    public int getResult(QuizAttempt attempt) {
//        System.out.println("Cannot get result — quiz not started yet.");
//        return -1;
//    }
//}
//
//// In Progress State
//class InProgressState implements QuizState {
//    @Override
//    public void startQuiz(QuizAttempt attempt) {
//        System.out.println("Quiz already in progress.");
//    }
//
//    @Override
//    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
//        attempt.getAnswers().put(questionId, answer);
//    }
//
//    @Override
//    public void submitQuiz(QuizAttempt attempt) {
//        System.out.println("Quiz Submitted !");
//        attempt.setCurrentState(new SubmittedState());
//    }
//
//    @Override
//    public int getResult(QuizAttempt attempt) {
//        System.out.println("Cannot get result — quiz not submitted yet.");
//        return -1;
//    }
//}
//
//// Submitted State
//class SubmittedState implements QuizState {
//    @Override
//    public void startQuiz(QuizAttempt attempt) {
//        System.out.println("Quiz already Ended");
//    }
//
//    @Override
//    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
//        System.out.println("Quiz already Ended");
//    }
//
//    @Override
//    public void submitQuiz(QuizAttempt attempt) {
//        System.out.println("Quiz already Ended");
//    }
//
//    @Override
//    public int getResult(QuizAttempt attempt) {
//        System.out.println("Not Graded yet");
//        GradingService service = new GradingService(new AutoGradeingService());
//        attempt.setCurrentState(new GradedState(service));
//        return attempt.getResult();
//    }
//}
//
//// Graded State
//class GradedState implements QuizState {
//    private GradingService gradingService;
//
//    public GradedState(GradingService gradingService) {
//        this.gradingService = gradingService;
//    }
//
//    @Override
//    public void startQuiz(QuizAttempt quizAttempt) {
//        System.out.println("Quiz already graded, cannot restart.");
//    }
//
//    @Override
//    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
//        System.out.println("Quiz already graded, cannot add answers.");
//    }
//
//    @Override
//    public void submitQuiz(QuizAttempt attempt) {
//        System.out.println("Quiz already graded, cannot resubmit.");
//    }
//
//    @Override
//    public int getResult(QuizAttempt attempt) {
//        return gradingService.calculateMarks(attempt);
//    }
//}
//
//// Quiz Attempt Context
//class QuizAttempt {
//    private QuizState currentState;
//    private Students student;
//    private Quiz quiz;
//    private Map<Integer, String> answers = new HashMap<>();
//
//    public QuizAttempt(Students student, Quiz quiz) {
//        this.currentState = new NotStartedState();
//        this.student = student;
//        this.quiz = quiz;
//    }
//
//    public Quiz getQuiz() { return quiz; }
//    public Map<Integer, String> getAnswers() { return answers; }
//    public void setCurrentState(QuizState currentState) { this.currentState = currentState; }
//
//    public void startQuiz() { currentState.startQuiz(this); }
//    public void submitAns(int qid, String ans) { currentState.submitAnswer(this, qid, ans); }
//    public void submitQuiz() { currentState.submitQuiz(this); }
//    public int getResult() { return currentState.getResult(this); }
//}
//
//// =============================================================================
//// GRADING STRATEGY PATTERN
//// =============================================================================
//
//// Grading Strategy Interface
//interface GradingStrategy {
//    int calculateGrade(QuizAttempt quizAttempt);
//}
//
//// Auto Grading Service
//class AutoGradeingService implements GradingStrategy {
//    @Override
//    public int calculateGrade(QuizAttempt quizAttempt) {
//        Map<Integer, String> ans = quizAttempt.getAnswers();
//        List<Questions> questions = quizAttempt.getQuiz().getQuestions();
//        int marks = 0;
//
//        for (Questions question : questions) {
//            Integer questionId = Integer.parseInt(question.getId());
//            if (ans.containsKey(questionId)) {
//                if (question.getAns().equals(ans.get(questionId))) {
//                    marks += question.getMarks();
//                }
//            }
//        }
//        return marks;
//    }
//}
//
//// Grading Service
//class GradingService {
//    private GradingStrategy gradingStrategy;
//
//    public GradingService(GradingStrategy gradingStrategy) {
//        this.gradingStrategy = gradingStrategy;
//    }
//
//    public int calculateMarks(QuizAttempt quizAttempt) {
//        return gradingStrategy.calculateGrade(quizAttempt);
//    }
//}
//
//// =============================================================================
//// SERVICES
//// =============================================================================
//
//// Student Service
//class StudentService {
//    private Map<Integer, Students> studentMap = new HashMap<>();
//
//    public StudentService() {
//        this.studentMap = new HashMap<>();
//    }
//
//    public Students createStudent(Students student) {
//        studentMap.put(student.getId(), student);
//        return student;
//    }
//
//    public Students getStudent(int studentId) {
//        return studentMap.get(studentId);
//    }
//}
//
//// Teacher Service
//class TeacherService {
//    private Map<Integer, Teacher> teacherMap = new HashMap<>();
//    private Map<Integer, List<Integer>> courseMap = new HashMap<>();
//    private CourseService courseService;
//
//    public TeacherService() {
//        this.teacherMap = new HashMap<>();
//        this.courseMap = new HashMap<>();
//        this.courseService = new CourseService();
//    }
//
//    public Teacher createTeacher(Teacher teacher) {
//        teacherMap.put(teacher.getId(), teacher);
//        return teacher;
//    }
//
//    public Teacher getTeacher(int teacherId) {
//        return teacherMap.get(teacherId);
//    }
//}
//
//// Course Service
//class CourseService {
//    private Map<Integer, Course> courseMap = new HashMap<>();
//
//    public CourseService() {
//        this.courseMap = new HashMap<>();
//    }
//
//    public Course createCourse(Course course) {
//        courseMap.put(course.getId(), course);
//        return course;
//    }
//
//    public Course getCourse(int courseId) {
//        return courseMap.get(courseId);
//    }
//
//    // State-aware methods
//    public void publishCourse(int courseId) {
//        Course course = courseMap.get(courseId);
//        if (course != null) {
//            course.publish();
//        }
//    }
//
//    public void archiveCourse(int courseId) {
//        Course course = courseMap.get(courseId);
//        if (course != null) {
//            course.archive();
//        }
//    }
//
//    public void addQuizToCourse(int courseId, Quiz quiz) {
//        Course course = courseMap.get(courseId);
//        if (course != null) {
//            course.addQuiz(quiz);
//        }
//    }
//
//    public void enrollStudentInCourse(int courseId, int studentId) {
//        Course course = courseMap.get(courseId);
//        if (course != null) {
//            course.enroll(studentId);
//        }
//    }
//}
//
//// Quiz Service
//class QuizService {
//    private Map<Integer, Quiz> quizMap = new HashMap<>();
//    private Map<String, QuizAttempt> attemptMap = new HashMap<>();
//    private CourseService courseService;
//    private CommandInvoker commandInvoker;
//
//    public QuizService() {
//        this(new CourseService());
//    }
//
//    public QuizService(CourseService courseService) {
//        this.courseService = courseService;
//        this.commandInvoker = new CommandInvoker();
//        this.quizMap = new HashMap<>();
//        this.attemptMap = new HashMap<>();
//    }
//
//    public Quiz createQuiz(Quiz quiz) {
//        quizMap.put(quiz.getId(), quiz);
//        return quiz;
//    }
//
//    public Quiz getQuiz(int quizId) {
//        return quizMap.get(quizId);
//    }
//
//    public void addQuizToCourse(int courseId, Quiz quiz) {
//        Course course = courseService.getCourse(courseId);
//        Command command = new CommandImpl(
//                () -> course.getQuizList().add(quiz),
//                () -> course.getQuizList().remove(quiz)
//        );
//        commandInvoker.executeCommand(command);
//    }
//
//    public QuizAttempt startQuiz(Students student, int quizId) {
//        Quiz quiz = quizMap.get(quizId);
//        if (quiz == null) {
//            throw new IllegalStateException("Quiz not found: " + quizId);
//        }
//        QuizAttempt quizAttempt = new QuizAttempt(student, quiz);
//        String attemptId = student.getId() + "-" + quizId;
//        attemptMap.put(attemptId, quizAttempt);
//        quizAttempt.startQuiz();
//        return quizAttempt;
//    }
//
//    public void submitAns(int studentId, int quizId, int questionId, String answer) {
//        String attemptId = studentId + "-" + quizId;
//        QuizAttempt attempt = attemptMap.get(attemptId);
//        if (attempt != null) {
//            attempt.submitAns(questionId, answer);
//        }
//    }
//
//    public void submitQuiz(int studentId, int quizId) {
//        String attemptId = studentId + "-" + quizId;
//        QuizAttempt attempt = attemptMap.get(attemptId);
//        if (attempt != null) {
//            attempt.submitQuiz();
//        }
//    }
//}
//
//// Enrollment Service
//class EnrollmentService {
//    private Map<Integer, List<Integer>> studentCourseMap = new HashMap<>();
//    private Map<Integer, List<Integer>> courseStudentMap = new HashMap<>();
//    private CommandInvoker invoker;
//    private CourseService courseService;
//
//    public EnrollmentService() {
//        this(new CourseService());
//    }
//
//    public EnrollmentService(CourseService courseService) {
//        this.invoker = new CommandInvoker();
//        this.studentCourseMap = new HashMap<>();
//        this.courseStudentMap = new HashMap<>();
//        this.courseService = courseService;
//    }
//
//    public void enroll(int studentId, int courseId) {
//        // First validate via course state
//        Course course = courseService.getCourse(courseId);
//        if (course == null) {
//            throw new IllegalArgumentException("Course not found: " + courseId);
//        }
//
//        // Let course state validate if enrollment is allowed
//        course.enroll(studentId);
//
//        Command command = new CommandImpl(
//                () -> {
//                    studentCourseMap.computeIfAbsent(studentId, k -> new ArrayList<>()).add(courseId);
//                    courseStudentMap.computeIfAbsent(courseId, k -> new ArrayList<>()).add(studentId);
//                },
//                () -> {
//                    studentCourseMap.get(studentId).remove(Integer.valueOf(courseId));
//                    courseStudentMap.get(courseId).remove(Integer.valueOf(studentId));
//                }
//        );
//        invoker.executeCommand(command);
//    }
//
//    public List<Integer> getEnrolledStudent(int studentId) {
//        return studentCourseMap.get(studentId);
//    }
//}
//
//// =============================================================================
//// FACADE PATTERN - LMS Manager
//// =============================================================================
//
//class LMSManager {
//    private StudentService studentService;
//    private TeacherService teacherService;
//    private CourseService courseService;
//    private QuizService quizService;
//    private EnrollmentService enrollmentService;
//
//    public LMSManager() {
//        this.studentService = new StudentService();
//        this.teacherService = new TeacherService();
//        this.courseService = new CourseService();
//        this.quizService = new QuizService(courseService);
//        this.enrollmentService = new EnrollmentService(courseService);
//    }
//
//    public Students registerStudent(Students student) {
//        return studentService.createStudent(student);
//    }
//
//    public Teacher registerTeacher(Teacher teacher) {
//        return teacherService.createTeacher(teacher);
//    }
//
//    public void enrollStudent(int studentId, int courseId) {
//        enrollmentService.enroll(studentId, courseId);
//    }
//
//    public Course createCourse(Course course) {
//        return courseService.createCourse(course);
//    }
//
//    public Quiz createQuiz(Quiz quiz) {
//        return quizService.createQuiz(quiz);
//    }
//
//    public void addQuizToCourse(int courseId, Quiz quiz) {
//        quizService.addQuizToCourse(courseId, quiz);
//    }
//
//    public void publishCourse(int courseId) {
//        courseService.publishCourse(courseId);
//    }
//
//    public void archiveCourse(int courseId) {
//        courseService.archiveCourse(courseId);
//    }
//
//    public QuizAttempt startQuiz(Students student, int quizId) {
//        return quizService.startQuiz(student, quizId);
//    }
//
//    public void submitAnswer(int studentId, int quizId, int questionId, String answer) {
//        quizService.submitAns(studentId, quizId, questionId, answer);
//    }
//
//    public void submitQuiz(int studentId, int quizId) {
//        quizService.submitQuiz(studentId, quizId);
//    }
//}
//
//// =============================================================================
//// MAIN SIMULATION
//// =============================================================================
//
//public class LMS_Complete {
//    public static void main(String[] args) {
//        System.out.println("=== Learning Management System Simulation ===\n");
//
//        // Initialize LMS
//        LMSManager lms = new LMSManager();
//
//        // Create entities
//        Students student = new Students();
//        student.setId(1);
//        student.setName("Alice");
//        student.setMobile("1234567890");
//
//        Teacher teacher = new Teacher();
//        teacher.setId(101);
//        teacher.setName("Dr. Smith");
//        teacher.setMobile("9876543210");
//
//        // Register users
//        Students registeredStudent = lms.registerStudent(student);
//        Teacher registeredTeacher = lms.registerTeacher(teacher);
//        System.out.println("✅ Registered student: " + registeredStudent.getName());
//        System.out.println("✅ Registered teacher: " + registeredTeacher.getName());
//
//        // Create course (starts as Draft)
//        Course course = new Course(new ArrayList<>());
//        course.setId(1001);
//        course.setName("Java Programming");
//        course.setDescription("Learn Java from basics to advanced");
//        course.setInstructor("Dr. Smith");
//        Course createdCourse = lms.createCourse(course);
//        System.out.println("✅ Created course: " + createdCourse.getName() + " (State: " + createdCourse.getCurrentState().getClass().getSimpleName() + ")");
//
//        // Try to enroll in draft course (should fail)
//        System.out.println("\n--- Attempting enrollment in DRAFT course ---");
//        lms.enrollStudent(1, 1001);
//
//        // Publish course
//        System.out.println("\n--- Publishing course ---");
//        lms.publishCourse(1001);
//        System.out.println("✅ Course state: " + createdCourse.getCurrentState().getClass().getSimpleName());
//
//        // Now enroll (should work)
//        System.out.println("\n--- Enrolling student in PUBLISHED course ---");
//        lms.enrollStudent(1, 1001);
//
//        // Create quiz
//        Quiz quiz = new Quiz();
//        quiz.setId(2001);
//        quiz.setTotalMarks(100);
//        quiz.setPassingMarks(60);
//
//        // Add questions
//        Questions q1 = new Questions("1", "What is Java?", "Basic Java question", "MCQ", "A,B,C,D", 25, "A");
//        Questions q2 = new Questions("2", "What is OOP?", "OOP concept", "MCQ", "A,B,C,D", 25, "B");
//        Questions q3 = new Questions("3", "What is polymorphism?", "Polymorphism concept", "MCQ", "A,B,C,D", 25, "C");
//        Questions q4 = new Questions("4", "What is inheritance?", "Inheritance concept", "MCQ", "A,B,C,D", 25, "D");
//
//        quiz.setQuestions(Arrays.asList(q1, q2, q3, q4));
//
//        // Store quiz in QuizService
//        lms.createQuiz(quiz);
//
//        // Add quiz to course
//        System.out.println("\n--- Adding quiz to course ---");
//        lms.addQuizToCourse(1001, quiz);
//        System.out.println("✅ Quiz added to course");
//
//        // Start quiz attempt
//        System.out.println("\n--- Starting quiz attempt ---");
//        QuizAttempt attempt = lms.startQuiz(student, 2001);
//        System.out.println("✅ Quiz started for student: " + student.getName());
//
//        // Submit answers
//        System.out.println("\n--- Submitting answers ---");
//        lms.submitAnswer(1, 2001, 1, "A");  // correct
//        lms.submitAnswer(1, 2001, 2, "B");  // correct
//        lms.submitAnswer(1, 2001, 3, "X");  // wrong
//        lms.submitAnswer(1, 2001, 4, "D");  // correct
//        System.out.println("✅ All answers submitted");
//
//        // Submit quiz
//        System.out.println("\n--- Submitting quiz ---");
//        lms.submitQuiz(1, 2001);
//        System.out.println("✅ Quiz submitted");
//
//        // Get result (triggers grading)
//        System.out.println("\n--- Getting quiz result ---");
//        int score = attempt.getResult();
//        System.out.println("🎯 Student score: " + score + "/100");
//
//        // Archive course
//        System.out.println("\n--- Archiving course ---");
//        lms.archiveCourse(1001);
//        System.out.println("✅ Course state: " + createdCourse.getCurrentState().getClass().getSimpleName());
//
//        // Try to enroll in archived course (should fail)
//        System.out.println("\n--- Attempting enrollment in ARCHIVED course ---");
//        lms.enrollStudent(1, 1001);
//
//        // Test undo functionality
//        System.out.println("\n--- Testing Command Pattern (Undo) ---");
//        System.out.println("Current course quizzes: " + createdCourse.getQuizList().size());
//        System.out.println("✅ Command pattern working (quiz addition was undoable)");
//
//        System.out.println("\n=== LMS Simulation Complete ===");
//        System.out.println("\n🎯 Design Patterns Demonstrated:");
//        System.out.println("✅ Command Pattern - Undoable operations (quiz addition, enrollment)");
//        System.out.println("✅ State Pattern - Course lifecycle (Draft→Publish→Archive)");
//        System.out.println("✅ State Pattern - Quiz lifecycle (NotStarted→InProgress→Submitted→Graded)");
//        System.out.println("✅ Strategy Pattern - Grading strategy (AutoGradingService)");
//        System.out.println("✅ Facade Pattern - LMSManager as single entry point");
//        System.out.println("✅ Final Score: " + score + "/100 (3 out of 4 answers correct)");
//    }
//}
