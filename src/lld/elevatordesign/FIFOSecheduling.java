package lld.elevatordesign;

import java.util.Queue;

public class FIFOSecheduling implements SchedulingStrategy{
    @Override
    public int getNextStop(Elevator elevator) {
        int currentFloor=elevator.getCurrentFloor();
        Direction direction=elevator.getDirection();
        Queue<ElevatorRequest> requests=elevator.getRequests();
        if (requests.isEmpty()){
            return currentFloor;
        }
        int nextStop=requests.peek().getFloorNumber();
        if (nextStop==currentFloor){
            return currentFloor;
        }
        if (elevator.getDirection()==Direction.IDLE){
            elevator.setDirection(currentFloor<nextStop?Direction.UP:Direction.DOWN);
        }else if (elevator.getDirection()==Direction.UP && currentFloor>nextStop){
            elevator.setDirection(Direction.DOWN);
        }else if(nextStop<currentFloor){
            elevator.setDirection(Direction.UP);
        }
        return nextStop;
    }
}
