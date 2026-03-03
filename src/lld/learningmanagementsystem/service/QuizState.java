package lld.learningmanagementsystem.service;

public interface QuizState {
    void startQuiz(QuizAttempt quizAttempt);
    void submitAnswer(QuizAttempt attempt,int questionId,String answer);
    void submitQuiz(QuizAttempt attempt);
    int getResult(QuizAttempt attempt);

}
