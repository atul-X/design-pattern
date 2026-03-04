package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Quiz;

public class DraftCourse implements CourseState{
    @Override
    public void publish(Course course) {
        System.out.println("Course published!");
        course.setCurrentState(new PublishCourse());
    }

    @Override
    public void archive(Course course) {
        System.out.println("Cannot archive — course not published yet.");
    }

    @Override
    public void addQuiz(Course course, Quiz quiz) {
        System.out.println("Quiz added to draft course - should be added to a lesson within a module.");
        // In the new architecture, quizzes belong to lessons, not directly to courses
        // This method is kept for backward compatibility but should be refactored
        System.out.println("Note: Consider adding quiz to a specific lesson instead");
    }

    @Override
    public void enroll(Course course, int studentId) {
        System.out.println("Cannot enroll — course not published yet.");
    }
}
