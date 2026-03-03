package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Course;
import lld.learningmanagementsystem.model.Quiz;

public interface CourseState {
    void publish(Course course);
    void archive(Course course);
    void addQuiz(Course course, Quiz quiz);
    void enroll(Course course, int studentId);
}
