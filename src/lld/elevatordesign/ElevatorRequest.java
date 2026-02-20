package lld.elevatordesign;

public class ElevatorRequest implements ElevatorCommond{
    private int floorNumber;
    private Direction requestDirection;
    private int elevatorId;
    private boolean isInternalRequest;
    private ElevatorController controller;


    public ElevatorRequest(int floorNumber, Direction requestDirection,
                           int elevatorId, boolean isInternalRequest, ElevatorController controller) {
        this.floorNumber = floorNumber;
        this.requestDirection = requestDirection;
        this.elevatorId = elevatorId;
        this.isInternalRequest = isInternalRequest;
        this.controller = controller;
    }

    @Override
    public void execute() {
        if (isInternalRequest){
            controller.requestFloor(elevatorId,floorNumber);
        }else{
            controller.requestElevator(elevatorId,floorNumber,requestDirection);
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Direction getRequestDirection() {
        return requestDirection;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    public boolean isInternalRequest() {
        return isInternalRequest;
    }
}
