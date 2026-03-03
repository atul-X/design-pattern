package lld.learningmanagementsystem.service;

public class SubmittedState implements QuizState{
    @Override
    public void startQuiz(QuizAttempt quizAttempt) {
        System.out.println("Quiz already Ended");
    }

    @Override
    public void submitAnswer(QuizAttempt attempt, int questionId, String answer) {
        System.out.println("Quiz already Ended");

    }

    @Override
    public void submitQuiz(QuizAttempt attempt) {
        System.out.println("Quiz already Ended");

    }

    @Override
    public int getResult(QuizAttempt attempt) {
        System.out.println("Not Graded yet");
        GradingService service = new GradingService(new AutoGradeingService());
        attempt.setCurrentState(new GradedState(service));
        return attempt.getResult();
    }
}
