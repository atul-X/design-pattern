package lld.learningmanagementsystem.model;

import java.util.List;

public abstract class Lesson {
    private int id;
    private String name;
    private String description;
    private String videoUrl;
    private int moduleId;
    private List<Quiz> quizList;

    // Template Method - defines the algorithm structure
    public final LearningResult deliver(Students student) {
        validatePrerequisites(student);
        ContentDeliveryResult result = deliverContent(student);
        return processResult(result);
    }
    
    // Open/Closed: New lesson types via inheritance
    protected abstract ContentDeliveryResult deliverContent(Students student);
    protected abstract void validatePrerequisites(Students student);
    
    // Common processing for all lesson types
    private LearningResult processResult(ContentDeliveryResult result) {
        // Common result processing logic
        return new LearningResult(result.isSuccess(), result.getCompletionTime());
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }
    
    public List<Quiz> getQuizList() { return quizList; }
    public void setQuizList(List<Quiz> quizList) { this.quizList = quizList; }
}
