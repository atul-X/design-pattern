package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Quiz;
import lld.learningmanagementsystem.model.Students;
import lld.learningmanagementsystem.model.Teacher;

public class LMSManager {
    private StudentService studentService;
    private TeacherService teacherService;
    private CourseService courseService;
    private QuizService quizService;
    private EnrollmentService enrollmentService;

    public LMSManager() {
        this.studentService=new StudentService();
        this.teacherService=new TeacherService();
        this.courseService=new CourseService();
        this.quizService=new QuizService(courseService);
        this.enrollmentService=new EnrollmentService(courseService);
    }
    public Students registerStudent(Students students){
        return studentService.createStudent(students);
    }
    public Teacher registerTeacher(Teacher teacher){
        return teacherService.createTeacher(teacher);
    }
    public void enrollStudent(int studentId,int courseId){
        enrollmentService.enroll(studentId,courseId);
    }
    public Course createCourse(Course course){
        return courseService.createCourse(course);
    }
    public Quiz createQuiz(Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    public void addQuizToCourse(int courseId, Quiz quiz){
        quizService.addQuizToCourse(courseId,quiz);
    }
    
    public void addQuizToLesson(int courseId, int moduleId, int lessonId, Quiz quiz){
        quizService.addQuizToLesson(courseId, moduleId, lessonId, quiz);
    }
    public QuizAttempt startQuiz(Students students,int quizId){
        return quizService.startQuiz(students,quizId);
    }
    public void submitAnswer(int studentId, int quizId, int questionId, String answer) {
        quizService.submitAns(studentId, quizId, questionId, answer);
    }

    public void submitQuiz(int studentId, int quizId) {
        quizService.submitQuiz(studentId, quizId);
    }

    public void publishCourse(int courseId) {
        courseService.publishCourse(courseId);
    }

    public void archiveCourse(int courseId) {
        courseService.archiveCourse(courseId);
    }

    // Command pattern management methods
    public void undoLastCourseAction() {
        courseService.undoLastAction();
    }

    public void undoAllCourseActions() {
        courseService.undoAllActions();
    }

    public void undoLastQuizAction() {
        quizService.getCommandInvoker().undo();
    }

    public void undoAllQuizActions() {
        while (quizService.getCommandInvoker().hasUndoableActions()) {
            quizService.getCommandInvoker().undo();
        }
    }

    public void undoLastEnrollmentAction() {
        enrollmentService.getCommandInvoker().undo();
    }

    public void undoAllEnrollmentActions() {
        while (enrollmentService.getCommandInvoker().hasUndoableActions()) {
            enrollmentService.getCommandInvoker().undo();
        }
    }

    public void undoLastSystemAction() {
        // Try to undo from all services in reverse order of operations
        if (enrollmentService.getCommandInvoker().hasUndoableActions()) {
            enrollmentService.getCommandInvoker().undo();
        } else if (quizService.getCommandInvoker().hasUndoableActions()) {
            quizService.getCommandInvoker().undo();
        } else if (courseService.getCommandInvoker().hasUndoableActions()) {
            courseService.undoLastAction();
        }
    }

    public int getTotalCommandHistorySize() {
        return courseService.getCommandHistorySize() + 
               quizService.getCommandInvoker().getHistorySize() + 
               enrollmentService.getCommandInvoker().getHistorySize();
    }

    public void clearAllCommandHistory() {
        courseService.clearCommandHistory();
        quizService.getCommandInvoker().clearHistory();
        enrollmentService.getCommandInvoker().clearHistory();
    }

    // Legacy methods for backward compatibility (non-undoable)
    public void publishCourseDirect(int courseId) {
        courseService.publishCourseDirect(courseId);
    }

    public void archiveCourseDirect(int courseId) {
        courseService.archiveCourseDirect(courseId);
    }

    // Service getters for debugging and advanced usage
    public CourseService getCourseService() {
        return courseService;
    }

    public QuizService getQuizService() {
        return quizService;
    }

    public EnrollmentService getEnrollmentService() {
        return enrollmentService;
    }

}
