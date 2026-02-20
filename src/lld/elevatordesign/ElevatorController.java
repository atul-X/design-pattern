package lld.elevatordesign;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private List<Elevator> elevators;
    private List<Floor> floors;
    private SchedulingStrategy schedulingStrategy;
    private int currentElevatorId;



    public ElevatorController(int numberOfElevators, int numberOfFloors) {
        this.elevators = new ArrayList<>();
        this.floors = new ArrayList<>();
        this.schedulingStrategy = new FIFOSecheduling();
        for (int i=0;i<numberOfElevators;i++){
            Elevator elevator = new Elevator(i);
            elevators.add(elevator);
        }
        for (int i=0;i<numberOfFloors;i++){
            Floor floor = new Floor(i);
            floors.add(floor);
        }
    }
    void requestElevator(int elevatorId,int floor,Direction requestDirection){
        System.out.println("External Requesting elevator " + elevatorId + " to floor " + floor + " in direction " + requestDirection);
        Elevator elevator= getElevatorById(elevatorId);
        if (elevator==null){
            System.out.println("Elevator not found");
            return;
        }
        elevator.addRequest(new ElevatorRequest(floor,requestDirection,elevatorId,false,this));
        System.out.println("Assigning  elevator " + elevator.getId() + " to floor " + floor + " in direction " + requestDirection);
    }
    void requestFloor(int elevatorId, int floor){
        System.out.println("Internal Requesting elevator " + elevatorId + " to floor " + floor);
        Elevator elevator=getElevatorById(elevatorId);
        if (elevator==null){
            System.out.println("Elevator not found");
            return;
        }
        Direction direction=floor>elevator.getCurrentFloor()?Direction.UP:Direction.DOWN;
        elevator.addRequest(new ElevatorRequest(floor,direction,elevatorId,true,this));
        System.out.println("Assigning  elevator " + elevator.getId() + " to floor " + floor + " in direction " + direction);
    }
    public Elevator getElevatorById(int id){
        for (Elevator elevator:elevators){
            if (elevator.getId()==id){
                return elevator;
            }
        }
        return null;
    }

    public void  step(){
        for (Elevator elevator:elevators){
            if(!elevator.getRequests().isEmpty()) {
                int nextFloor = schedulingStrategy.getNextStop(elevator);
                if (elevator.getCurrentFloor() != nextFloor) {
                    elevator.moveToNextFloor(nextFloor);
                }
            }
        }
    }
}
