package lld.learningmanagementsystem.model;

import lld.learningmanagementsystem.service.CourseState;
import lld.learningmanagementsystem.service.DraftCourse;

import java.util.ArrayList;
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
    private List<Module> moduleList;
    private CourseState currentState;

    public CourseState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CourseState currentState) {
        this.currentState = currentState;
    }

    public Course() {
        this.moduleList = new ArrayList<>();
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

    public List<Module> getModuleList() {
        return new ArrayList<>(moduleList);
    }
    
    public void addModule(Module module) {
        if (module != null && !moduleList.contains(module)) {
            moduleList.add(module);
            module.setCourseId(this.id);
        }
    }
    
    public void removeModule(Module module) {
        if (moduleList.remove(module)) {
            module.setCourseId(-1); // Reset course ID
        }
    }
    
    public Module getModule(int moduleId) {
        return moduleList.stream()
                .filter(module -> module.getId() == moduleId)
                .findFirst()
                .orElse(null);
    }

    public List<Quiz> getQuizList() {
        // Collect all quizzes from all lessons in all modules
        List<Quiz> allQuizzes = new ArrayList<>();
        for (Module module : moduleList) {
            for (Lesson lesson : module.getLessonList()) {
                allQuizzes.addAll(lesson.getQuizList());
            }
        }
        return allQuizzes;
    }

    public List<Integer> getEnrolledStudents() {
        // This would typically come from EnrollmentService
        // For now, return empty list - this would be populated by EnrollmentService
        return new ArrayList<>();
    }

    public void publish() { currentState.publish(this); }
    public void archive() { currentState.archive(this); }
    public void addQuiz(Quiz quiz) { currentState.addQuiz(this, quiz); }
    public void enroll(int studentId) { currentState.enroll(this, studentId); }
}
