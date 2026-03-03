package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Quiz;

public class ArchiveCourse implements CourseState {
    @Override
    public void publish(Course course) {
        System.out.println("Cannot publish — course is archived.");
    }

    @Override
    public void archive(Course course) {
        System.out.println("Course already archived.");
    }

    @Override
    public void addQuiz(Course course, Quiz quiz) {
        System.out.println("Cannot add quiz — course is archived.");
    }

    @Override
    public void enroll(Course course, int studentId) {
        System.out.println("Cannot enroll — course is archived.");
    }
}
