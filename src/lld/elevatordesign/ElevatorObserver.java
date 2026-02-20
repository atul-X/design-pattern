package lld.elevatordesign;

public interface ElevatorObserver {
    void onElevatorStateChange(Elevator elevator,ElevatorState state);
    void onElevatorFloorChange(Elevator elevator,int floor);
}
