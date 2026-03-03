package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Quiz;

public interface GradingStrategy {
    public int calculateGrade(QuizAttempt quizAttempt);
}
