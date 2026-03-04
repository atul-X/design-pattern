package lld.learningmanagementsystem.model;

public class LearningResult {
    private final boolean completed;
    private final long completionTime;
    
    public LearningResult(boolean completed, long completionTime) {
        this.completed = completed;
        this.completionTime = completionTime;
    }
    
    public boolean isCompleted() { return completed; }
    public long getCompletionTime() { return completionTime; }
}
