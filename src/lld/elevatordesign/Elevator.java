package lld.elevatordesign;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Elevator {
    private int id;
    private int currentFloor;
    private Direction direction;
    private ElevatorState state;
    private Queue<ElevatorRequest> requests;
    private List<ElevatorObserver> observers;

    public Elevator(int id) {
        this.id = id;
        this.currentFloor = 1;
        this.direction = Direction.IDLE;
        this.state = ElevatorState.STOPPED;
        this.requests = new LinkedList<>();
        this.observers = new ArrayList<>();
    }

    public Queue<ElevatorRequest> getRequests() {
        return requests;
    }

    public void removeRequest(ElevatorRequest request){
        requests.remove(request);
    }
    private void notifyStateChange(){
        for(ElevatorObserver elevatorObserver:observers){
            elevatorObserver.onElevatorStateChange(this,state);
        }
    }
    private void notifyFloorChange(){
        for(ElevatorObserver elevatorObserver:observers){
            elevatorObserver.onElevatorFloorChange(this,currentFloor);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        notifyFloorChange();
        this.currentFloor = currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ElevatorState getState() {
        return state;
    }

    public void setState(ElevatorState state) {
        notifyStateChange();
        this.state = state;
    }

    public void addRequest(ElevatorRequest elevatorRequest){
        if (!requests.contains(elevatorRequest)){
            requests.add(elevatorRequest);
        }
        int requestedFloor=elevatorRequest.getFloorNumber();
        if (state==ElevatorState.STOPPED && !requests.isEmpty()){
            if (requestedFloor>currentFloor){
                direction=Direction.UP;
            }else{
                direction=Direction.DOWN;
            }
            setState(ElevatorState.RUNNING);
        }
    }
    public void moveToNextFloor(int nextStop){
        if (state!=ElevatorState.RUNNING){
            System.out.println("Elevator " + id + " is not running (state: " + state + ")");
            return;
        }
        System.out.println("Elevator " + id + " moving from floor " + currentFloor + " to floor " + nextStop + " (direction: " + direction + ")");
        while (currentFloor!=nextStop){
            if (direction==Direction.UP){
                currentFloor++;
            }else{
                currentFloor--;
            }
            System.out.println("Elevator " + id + " now at floor " + currentFloor);
            notifyFloorChange();
        }
        if (currentFloor==nextStop){
            completeArrival();
        }
    }
    public void completeArrival(){
        setState(ElevatorState.STOPPED);
        requests.removeIf(r->r.getFloorNumber()==currentFloor);
        if (requests.isEmpty()){
            setState(ElevatorState.STOPPED);
            setDirection(Direction.IDLE);
        }else {
            setState(ElevatorState.RUNNING);
        }
    }


}
