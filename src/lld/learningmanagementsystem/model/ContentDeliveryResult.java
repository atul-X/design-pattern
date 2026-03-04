package lld.learningmanagementsystem.model;

public class ContentDeliveryResult {
    private final boolean success;
    private final long completionTime;
    
    public ContentDeliveryResult(boolean success, long completionTime) {
        this.success = success;
        this.completionTime = completionTime;
    }
    
    public boolean isSuccess() { return success; }
    public long getCompletionTime() { return completionTime; }
}
