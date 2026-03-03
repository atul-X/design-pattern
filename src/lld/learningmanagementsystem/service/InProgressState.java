package lld.learningmanagementsystem.service;

public class InProgressState implements QuizState{
    @Override
    public void startQuiz(QuizAttempt quizAttempt) {
        System.out.println("Quiz already started");
    }

    @Override
    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
        attempt.getAnswers().put(questionId,answer);
    }

    @Override
    public void submitQuiz(QuizAttempt attempt) {
        System.out.println("Quiz Submitted !");
        attempt.setCurrentState(new SubmittedState());
    }

    @Override
    public int getResult(QuizAttempt attempt) {
        System.out.println("Quiz is Running");
        return -1;
    }
}
