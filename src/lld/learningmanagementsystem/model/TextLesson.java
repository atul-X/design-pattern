package lld.learningmanagementsystem.model;

/**
 * Concrete implementation of Lesson for text-based content.
 * Demonstrates Open/Closed Principle - new lesson type without modifying existing code.
 */
public class TextLesson extends Lesson {
    
    public TextLesson() {
        super();
    }
    
    public TextLesson(int id, String name, String description, String videoUrl, int moduleId) {
        setId(id);
        setName(name);
        setDescription(description);
        setVideoUrl(videoUrl); // Could be text content URL
        setModuleId(moduleId);
    }
    
    @Override
    protected ContentDeliveryResult deliverContent(Students student) {
        System.out.println("Delivering text lesson: " + getName());
        System.out.println("Content URL: " + getVideoUrl());
        
        // Simulate text content delivery
        long startTime = System.currentTimeMillis();
        
        try {
            // Simulate text loading time
            Thread.sleep(500);
            
            // In real implementation, this would:
            // 1. Load text content from database/file
            // 2. Format content for display
            // 3. Handle images/links in text
            // 4. Track reading progress
            // 5. Support bookmarks/highlights
            
            long endTime = System.currentTimeMillis();
            return new ContentDeliveryResult(true, endTime - startTime);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ContentDeliveryResult(false, 0);
        }
    }
    
    @Override
    protected void validatePrerequisites(Students student) {
        System.out.println("Validating prerequisites for text lesson: " + getName());
        
        // Check if student has completed previous lessons
        // Check if student has access to this course
        // Check reading level requirements
        // Check accessibility needs
        
        if (getDescription() == null || getDescription().trim().isEmpty()) {
            throw new IllegalStateException("Description is required for text lessons");
        }
        
        System.out.println("Prerequisites validated for student: " + student.getName());
    }
}
