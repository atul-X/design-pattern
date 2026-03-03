package lld.learningmanagementsystem.service;

public class GradingService {
    public GradingStrategy gradingStrategy;

    public GradingService(GradingStrategy gradingStrategy) {
        this.gradingStrategy = gradingStrategy;
    }

    public int calculateMarks(QuizAttempt quizAttempt){
        return gradingStrategy.calculateGrade(quizAttempt);
    }
}
