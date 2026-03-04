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

}
