package lld.learningmanagementsystem.service;

public class NotStatertedState implements QuizState {
    @Override
    public void startQuiz(QuizAttempt quizAttempt) {
        System.out.println("Quiz started");
        quizAttempt.setCurrentState(new InProgressState());
    }

    @Override
    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
        System.out.println("quiz not started");
    }

    @Override
    public void submitQuiz(QuizAttempt attempt) {
        System.out.println("quiz not started");
    }

    @Override
    public int getResult(QuizAttempt attempt) {
        System.out.println("quiz not started");
        return -1;
    }
}
