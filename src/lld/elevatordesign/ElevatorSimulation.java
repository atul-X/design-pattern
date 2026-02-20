package lld.elevatordesign;

public class ElevatorSimulation {
    private Building building;
    private ElevatorController controller;

    public ElevatorSimulation(String buildingName, int totalFloors, int totalElevators) {
        this.building = new Building(buildingName, totalFloors, totalElevators);
        this.controller = building.getElevatorController();
    }

    public void runSimulation() {
        System.out.println("=== Elevator System Simulation ===");
        
        System.out.println("\n1. External requests:");
        // Person on floor 3 wants to go up
        controller.requestElevator(0, 3, Direction.UP);
        
        // Person on floor 7 wants to go down  
        controller.requestElevator(1, 7, Direction.DOWN);
        
        System.out.println("\n2. Internal requests:");
        // Someone in elevator 0 wants to go to floor 5
        controller.requestFloor(0, 5);
        
        // Someone in elevator 1 wants to go to floor 2
        controller.requestFloor(1, 2);
        
        System.out.println("\n3. Running simulation steps:");
        // Run simulation for several steps
        for (int i = 1; i <= 8; i++) {
            System.out.println("\n--- Step " + i + " ---");
            controller.step();
        }
        
        System.out.println("\n=== Simulation Complete ===");
    }

    public static void main(String[] args) {
        ElevatorSimulation simulation = new ElevatorSimulation("Office Tower", 10, 2);
        simulation.runSimulation();
    }
}
