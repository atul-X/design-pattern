package lld.learningmanagementsystem.service;

public class GradedState implements QuizState {
    GradingService gradingService;

    public GradedState(GradingService gradingService) {
        this.gradingService = gradingService;
    }

    @Override
    public void startQuiz(QuizAttempt quizAttempt) {
        System.out.println("Quiz already graded, cannot restart.");
    }

    @Override
    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
        System.out.println("Quiz already graded, cannot add answers.");
    }

    @Override
    public void submitQuiz(QuizAttempt attempt) {
        System.out.println("Quiz already graded, cannot resubmit.");
    }

    @Override
    public int getResult(QuizAttempt attempt) {
        return gradingService.calculateMarks(attempt);
    }
}
