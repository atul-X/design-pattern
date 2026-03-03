package lld.learningmanagementsystem.service;

import lld.learningmanagementsystem.model.Questions;
import lld.learningmanagementsystem.model.Quiz;

import java.util.List;
import java.util.Map;

public class AutoGradeingService implements GradingStrategy{
    @Override
    public int calculateGrade(QuizAttempt quizAttempt) {
        Map<Integer,String> ans= quizAttempt.getAnswers();
        List<Questions> questions=quizAttempt.getQuiz().getQuestions();
        int marks=0;
        for(Questions question:questions){
            Integer questionId = Integer.parseInt(question.getId());
            if(ans.containsKey(questionId)){
                if(question.getAns().equals(ans.get(questionId))){
                    marks+=question.getMarks();
                }
            }else{
                marks+=0;
            }
        }
        return marks;
    }
}
