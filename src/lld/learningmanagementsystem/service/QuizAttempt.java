package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Quiz;
import lld.learningmanagementsystem.model.Students;

import java.util.HashMap;
import java.util.Map;

public class QuizAttempt {
    private QuizState currentState;
    private Students students;
    private Quiz quiz;
    private Map<Integer,String> answers=new HashMap<>();

    public Quiz getQuiz() {
        return quiz;
    }

    public QuizAttempt(Students students, Quiz quiz) {
        this.currentState = new NotStatertedState();
        this.students = students;
        this.quiz = quiz;
    }

    public void setCurrentState(QuizState currentState) {
        this.currentState = currentState;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void startQuiz() {
        currentState.startQuiz(this);
    }

    public void submitAns(int qid,String ans) {
        currentState.submitAnswer(this,qid,ans);
    }
    public void submitQuiz(){
        currentState.submitQuiz(this);
    }
    public int getResult(){
        return currentState.getResult(this);
    }
}
