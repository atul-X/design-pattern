package lld.elevatordesign;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ElevatorDisplays - Observer implementation for elevator status display
 * 
 * This class implements the Observer pattern to provide real-time visual feedback
 * about elevator operations. It displays state changes and floor movements with
 * timestamps for monitoring and debugging purposes.
 * 
 * Design Patterns:
 * - Observer Pattern: Implements ElevatorObserver interface
 * - Singleton Pattern: Could be extended for display management
 * 
 * Thread Safety:
 * - This class is thread-safe as it only performs read operations
 * - All method calls are atomic and don't modify shared state
 * 
 * @author Elevator System Team
 * @version 2.0
 * @since 1.0
 */
public class ElevatorDisplays implements ElevatorObserver {
    
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
    
    private final String displayId;
    private boolean enabled;
    
    /**
     * Constructor for ElevatorDisplays
     * 
     * @param displayId Unique identifier for this display instance
     * @throws IllegalArgumentException if displayId is null or empty
     */
    public ElevatorDisplays(String displayId) {
        validateDisplayId(displayId);
        this.displayId = displayId;
        this.enabled = true;
        
        System.out.println(formatMessage("Display initialized: " + displayId));
    }
    
    /**
     * Default constructor with auto-generated display ID
     */
    public ElevatorDisplays() {
        this("DISPLAY-" + System.currentTimeMillis());
    }
    
    /**
     * Called when elevator state changes
     * 
     * @param elevator The elevator that changed state
     * @param state The new state of the elevator
     * @throws IllegalArgumentException if elevator or state is null
     */
    @Override
    public void onElevatorStateChange(Elevator elevator, ElevatorState state) {
        try {
            validateParameters(elevator, state);
            
            if (!enabled) {
                return; // Silently ignore if display is disabled
            }
            
            String message = String.format("Elevator %d state transition: %s -> %s", 
                elevator.getId(), 
                getPreviousState(elevator), 
                state);
            
            System.out.println(formatMessage(message));
            
            // Additional logging for critical states
            if (state == ElevatorState.MAINTENANCE) {
                System.out.println(formatMessage("WARNING: Elevator " + elevator.getId() + " entered MAINTENANCE mode"));
            }
            
        } catch (Exception e) {
            handleDisplayError("State change notification failed", e);
        }
    }
    
    /**
     * Called when elevator floor changes
     * 
     * @param elevator The elevator that moved
     * @param floor The current floor of the elevator
     * @throws IllegalArgumentException if elevator is null or floor is invalid
     */
    @Override
    public void onElevatorFloorChange(Elevator elevator, int floor) {
        try {
            validateFloorChange(elevator, floor);
            
            if (!enabled) {
                return; // Silently ignore if display is disabled
            }
            
            String message = String.format("Elevator %d floor update: %d", 
                elevator.getId(), 
                floor);
            
            System.out.println(formatMessage(message));
            
            // Highlight arrival at destination floors
            if (isDestinationFloor(elevator, floor)) {
                System.out.println(formatMessage("INFO: Elevator " + elevator.getId() + " arrived at destination floor " + floor));
            }
            
        } catch (Exception e) {
            handleDisplayError("Floor change notification failed", e);
        }
    }
    
    /**
     * Enable or disable the display
     * 
     * @param enabled true to enable, false to disable
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        System.out.println(formatMessage("Display " + displayId + " " + (enabled ? "ENABLED" : "DISABLED")));
    }
    
    /**
     * Check if display is currently enabled
     * 
     * @return true if enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Get display identifier
     * 
     * @return the display ID
     */
    public String getDisplayId() {
        return displayId;
    }
    
    // Private helper methods
    
    private String formatMessage(String message) {
        return String.format("[%s] [%s] %s", 
            LocalDateTime.now().format(formatter),
            displayId,
            message);
    }
    
    private void validateDisplayId(String displayId) {
        if (displayId == null || displayId.trim().isEmpty()) {
            throw new IllegalArgumentException("Display ID cannot be null or empty");
        }
    }
    
    private void validateParameters(Elevator elevator, ElevatorState state) {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator cannot be null");
        }
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }
    }
    
    private void validateFloorChange(Elevator elevator, int floor) {
        if (elevator == null) {
            throw new IllegalArgumentException("Elevator cannot be null");
        }
        if (floor < 1) {
            throw new IllegalArgumentException("Floor number must be positive: " + floor);
        }
    }
    
    private String getPreviousState(Elevator elevator) {
        // This would ideally be tracked by the elevator itself
        // For now, return a placeholder
        return "UNKNOWN";
    }
    
    private boolean isDestinationFloor(Elevator elevator, int floor) {
        // Check if this floor is in the elevator's request queue
        return elevator.getRequests().stream()
            .anyMatch(request -> request.getFloorNumber() == floor);
    }
    
    private void handleDisplayError(String operation, Exception e) {
        System.err.println(formatMessage("ERROR: " + operation + " - " + e.getMessage()));
        // In a production system, this would log to a proper logging framework
        // and potentially trigger alerts for monitoring systems
    }
}
