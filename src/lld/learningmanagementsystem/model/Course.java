package lld.learningmanagementsystem.model;

import lld.learningmanagementsystem.service.CourseState;
import lld.learningmanagementsystem.service.DraftCourse;

import java.util.List;

public class Course {
    private  int id;
    private String name;
    private String description;
    private int duration;
    private double price;
    private String category;
    private String level;
    private String instructor;
    private String language;
    private String status;
    private List<Quiz> quizList;
    private CourseState currentState;

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    public CourseState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CourseState currentState) {
        this.currentState = currentState;
    }

    public Course(List<Quiz> quizList) {
        this.quizList = quizList;
        this.currentState = new DraftCourse();  // all courses start as draft
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public void publish() { currentState.publish(this); }
    public void archive() { currentState.archive(this); }
    public void addQuiz(Quiz quiz) { currentState.addQuiz(this, quiz); }
    public void enroll(int studentId) { currentState.enroll(this, studentId); }
}
