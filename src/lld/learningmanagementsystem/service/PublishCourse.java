package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Quiz;

public class PublishCourse implements CourseState{

    @Override
    public void publish(Course course) {
        System.out.println("Course is published");
    }

    @Override
    public void archive(Course course) {
        System.out.println("Course archived!");
        course.setCurrentState(new ArchiveCourse());
    }

    @Override
    public void addQuiz(Course course, Quiz quiz) {
        System.out.println("Quiz added to published course - should be added to a lesson within a module.");
        // In the new architecture, quizzes belong to lessons, not directly to courses
        System.out.println("Note: Consider adding quiz to a specific lesson instead");
    }

    @Override
    public void enroll(Course course, int studentId) {
        System.out.println("Student " + studentId + " enrolled in course " + course.getId());
    }
}
