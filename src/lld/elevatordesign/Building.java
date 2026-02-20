package lld.elevatordesign;

import java.util.List;

public class Building {
    private String name;
    private int totalFloors;
    private int totalElevators;
    private ElevatorController elevatorController;

    public Building(String name, int totalFloors, int totalElevators) {
        this.name = name;
        this.totalFloors = totalFloors;
        this.totalElevators = totalElevators;
        this.elevatorController=new ElevatorController(totalElevators,totalFloors);
    }

    public ElevatorController getElevatorController() {
        return elevatorController;
    }
}
