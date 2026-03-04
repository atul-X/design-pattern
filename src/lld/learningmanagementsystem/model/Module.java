package lld.learningmanagementsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Module represents a learning unit within a course.
 * Follows Single Responsibility Principle - only manages lesson organization.
 */
public class Module {
    private int id;
    private String name;
    private String description;
    private int courseId;
    private List<Lesson> lessonList;
    
    public Module() {
        this.lessonList = new ArrayList<>();
    }
    
    public Module(int id, String name, String description, int courseId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseId = courseId;
        this.lessonList = new ArrayList<>();
    }
    
    // Single Responsibility: Module only organizes lessons
    public void addLesson(Lesson lesson) {
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson cannot be null");
        }
        if (!lessonList.contains(lesson)) {
            lessonList.add(lesson);
            lesson.setModuleId(this.id);
        }
    }
    
    public void removeLesson(Lesson lesson) {
        if (lessonList.remove(lesson)) {
            lesson.setModuleId(-1); // Reset module ID
        }
    }
    
    public Lesson getLesson(int lessonId) {
        return lessonList.stream()
                .filter(lesson -> lesson.getId() == lessonId)
                .findFirst()
                .orElse(null);
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    
    public List<Lesson> getLessonList() { return new ArrayList<>(lessonList); }
    
    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lessonCount=" + lessonList.size() +
                '}';
    }
}
