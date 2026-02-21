package lld.elevatordesign;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ScanScheduling implements SchedulingStrategy {

    @Override
    public int getNextStop(Elevator elevator) {
        Queue<ElevatorRequest> requests=elevator.getRequests();
        Queue<ElevatorRequest> upQueue=new LinkedList<>();
        Queue<ElevatorRequest> downQueue=new LinkedList<>();
        int currentFloor=elevator.getCurrentFloor();
        Direction direction=elevator.getDirection();

        // Separate requests into up and down queues
        for(ElevatorRequest elevatorRequest:requests){
            if (elevatorRequest.getFloorNumber()>currentFloor){
                upQueue.add(elevatorRequest);
            } else if (elevatorRequest.getFloorNumber()<currentFloor){
                downQueue.add(elevatorRequest);
            }
        }
        
        // Sort upQueue in ascending order (serve floors going up)
        List<ElevatorRequest> upList = new ArrayList<>(upQueue);
        upList.sort(Comparator.comparingInt(req -> req.getFloorNumber()));
        
        // Sort downQueue in descending order (serve floors going down)
        List<ElevatorRequest> downList = new ArrayList<>(downQueue);
        downList.sort(Comparator.comparingInt(req -> -req.getFloorNumber()));
        
        if (direction==Direction.UP && !upList.isEmpty()){
            return upList.get(0).getFloorNumber();
        } else if (direction==Direction.DOWN && !downList.isEmpty()){
            return downList.get(0).getFloorNumber();
        } else if (!upList.isEmpty()){
            // If going up but no up requests, switch to down
            elevator.setDirection(Direction.DOWN);
            return downList.isEmpty() ? currentFloor : downList.get(0).getFloorNumber();
        } else if (!downList.isEmpty()){
            // If going down but no down requests, switch to up
            elevator.setDirection(Direction.UP);
            return upList.isEmpty() ? currentFloor : upList.get(0).getFloorNumber();
        } else {
            // No requests
            return currentFloor;
        }
    }
}
