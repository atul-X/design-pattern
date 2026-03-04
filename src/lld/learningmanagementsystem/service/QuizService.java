package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Module;
import lld.learningmanagementsystem.model.Quiz;
import lld.learningmanagementsystem.model.Students;
import lld.learningmanagementsystem.model.Lesson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizService {
    Map<Integer, Quiz> quizMap;
    Map<String,QuizAttempt> attemptMap;
    CourseService courseService;
    CommandInvoker commandInvoker;
    public QuizService() {
        this(new CourseService());
    }

    public QuizService(CourseService courseService) {
        this.courseService = courseService;
        this.commandInvoker = new CommandInvoker();
        this.quizMap = new HashMap<>();
        this.attemptMap = new HashMap<>();
    }
    public Quiz createQuiz(Quiz quiz){
        quizMap.put(quiz.getId(),quiz);
        return quiz;
    }
    public Quiz getQuiz(int quizId){
        return quizMap.get(quizId);
    }

    public void addQuizToCourse(int courseId, Quiz quiz){
        // Legacy method - adds to first lesson of first module
        addQuizToLesson(courseId, 1, 1, quiz); // Default to first module and first lesson
    }
    
    public void addQuizToLesson(int courseId, int moduleId, int lessonId, Quiz quiz){
        Course course=courseService.getCourse(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found: " + courseId);
        }
        
        lld.learningmanagementsystem.model.Module module = course.getModule(moduleId);
        if (module == null) {
            throw new IllegalArgumentException("Module not found: " + moduleId);
        }
        
        Lesson lesson = module.getLesson(lessonId);
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson not found: " + lessonId);
        }
        
        Command command= new CommandImpl(
                ()->lesson.getQuizList().add(quiz),
                ()->lesson.getQuizList().remove(quiz));
        commandInvoker.executeCommand(command);
        
        System.out.println("Quiz added to lesson " + lessonId + " in module " + moduleId);
    }

    public QuizAttempt startQuiz(Students student,int quizId){
        Quiz quiz=quizMap.get(quizId);
        if(quiz==null){
            throw new IllegalStateException();
        }
        QuizAttempt quizAttempt=new QuizAttempt(student,quiz);
        String attemptIs=student.getId()+"-"+quizId;
        attemptMap.put(attemptIs,quizAttempt);
        quizAttempt.startQuiz();
        return quizAttempt;
    }
    public void submitAns(int studentId,int quizId,int questionId,String ans){
        String attemptId=studentId+"-"+quizId;
        QuizAttempt quizAttempt=attemptMap.get(attemptId);
        quizAttempt.submitAns(questionId,ans);
    }
    public void submitQuiz(int studentId,int quizId){
        String attemptId=studentId+"-"+quizId;
        QuizAttempt quizAttempt=attemptMap.get(attemptId);
        quizAttempt.submitQuiz();
    }

}
