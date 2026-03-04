package lld.learningmanagementsystem.model;

import java.util.ArrayList;

/**
 * Concrete implementation of Lesson for video content.
 * Demonstrates Open/Closed Principle - new lesson type without modifying existing code.
 */
public class VideoLesson extends Lesson {
    
    public VideoLesson() {
        super();
    }
    
    public VideoLesson(int id, String name, String description, String videoUrl, int moduleId) {
        setId(id);
        setName(name);
        setDescription(description);
        setVideoUrl(videoUrl);
        setModuleId(moduleId);
        setQuizList(new ArrayList<>()); // Initialize quiz list
    }
    
    @Override
    protected ContentDeliveryResult deliverContent(Students student) {
        System.out.println("Delivering video lesson: " + getName());
        System.out.println("Streaming from: " + getVideoUrl());
        System.out.println("DEBUG: Lesson ID = " + getId());
        System.out.println("DEBUG: Description = " + getDescription());
        System.out.println("DEBUG: Module ID = " + getModuleId());
        
        // Simulate video streaming
        long startTime = System.currentTimeMillis();
        
        try {
            // Simulate video delivery time
            Thread.sleep(1000);
            
            // In real implementation, this would:
            // 1. Stream video content
            // 2. Track viewing progress
            // 3. Handle buffering/errors
            // 4. Log viewing analytics
            
            long endTime = System.currentTimeMillis();
            return new ContentDeliveryResult(true, endTime - startTime);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ContentDeliveryResult(false, 0);
        }
    }
    
    @Override
    protected void validatePrerequisites(Students student) {
        System.out.println("Validating prerequisites for video lesson: " + getName());
        
        // Check if student has completed previous lessons
        // Check if student has access to this course
        // Check technical requirements (internet bandwidth, etc.)
        
        if (getVideoUrl() == null || getVideoUrl().trim().isEmpty()) {
            throw new IllegalStateException("Video URL is required for video lessons");
        }
        
        System.out.println("Prerequisites validated for student: " + student.getName());
    }
}
