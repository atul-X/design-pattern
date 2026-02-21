package lld.elevatordesign;

import java.util.*;

/**
 * Complete Elevator System in a Single File
 * Demonstrates elevator design patterns including:
 * - Command Pattern (ElevatorRequest)
 * - Strategy Pattern (SchedulingStrategy)
 * - Observer Pattern (ElevatorObserver)
 * - State Pattern (ElevatorState)
 */
public class ElevatorSystem {

    // Enums
    public enum Direction {
        UP, DOWN, IDLE
    }

    public enum ElevatorState {
        RUNNING, STOPPED, MAINTENANCE
    }

    // Observer Interface
    public interface ElevatorObserver {
        void onElevatorStateChange(Elevator elevator, ElevatorState state);
        void onElevatorFloorChange(Elevator elevator, int floor);
    }

    // Command Interface
    public interface ElevatorCommand {
        void execute();
    }

    // Scheduling Strategy Interface
    public interface SchedulingStrategy {
        int getNextStop(Elevator elevator);
    }

    // FIFO Scheduling Implementation
    public static class FIFOScheduling implements SchedulingStrategy {
        @Override
        public int getNextStop(Elevator elevator) {
            int currentFloor = elevator.getCurrentFloor();
            Direction direction = elevator.getDirection();
            Queue<ElevatorRequest> requests = elevator.getRequests();
            
            if (requests.isEmpty()) {
                return currentFloor;
            }
            
            int nextStop = requests.peek().getFloorNumber();
            if (nextStop == currentFloor) {
                return currentFloor;
            }
            
            if (elevator.getDirection() == Direction.IDLE) {
                elevator.setDirection(currentFloor < nextStop ? Direction.UP : Direction.DOWN);
            } else if (elevator.getDirection() == Direction.UP && currentFloor > nextStop) {
                elevator.setDirection(Direction.DOWN);
            } else if (nextStop < currentFloor) {
                elevator.setDirection(Direction.UP);
            }
            
            return nextStop;
        }
    }

    // SCAN Scheduling Implementation (Elevator/LOOK Algorithm)
    public static class ScanScheduling implements SchedulingStrategy {
        @Override
        public int getNextStop(Elevator elevator) {
            Queue<ElevatorRequest> requests = elevator.getRequests();
            int currentFloor = elevator.getCurrentFloor();
            Direction direction = elevator.getDirection();
            
            if (requests.isEmpty()) {
                return currentFloor;
            }
            
            Map<String, List<ElevatorRequest>> separatedRequests = separateRequestsByDirection(requests, currentFloor);
            List<ElevatorRequest> upRequests = sortUpRequests(separatedRequests.get("up"));
            List<ElevatorRequest> downRequests = sortDownRequests(separatedRequests.get("down"));
            
            return determineNextStop(elevator, direction, upRequests, downRequests, currentFloor);
        }
        
        private Map<String, List<ElevatorRequest>> separateRequestsByDirection(Queue<ElevatorRequest> requests, int currentFloor) {
            List<ElevatorRequest> upQueue = new LinkedList<>();
            List<ElevatorRequest> downQueue = new LinkedList<>();
            
            for(ElevatorRequest request : requests) {
                if (request.getFloorNumber() > currentFloor) {
                    upQueue.add(request);
                } else if (request.getFloorNumber() < currentFloor) {
                    downQueue.add(request);
                }
            }
            
            Map<String, List<ElevatorRequest>> result = new HashMap<>();
            result.put("up", upQueue);
            result.put("down", downQueue);
            return result;
        }
        
        private List<ElevatorRequest> sortUpRequests(List<ElevatorRequest> upRequests) {
            List<ElevatorRequest> sorted = new ArrayList<>(upRequests);
            sorted.sort(Comparator.comparingInt(req -> req.getFloorNumber()));
            return sorted;
        }
        
        private List<ElevatorRequest> sortDownRequests(List<ElevatorRequest> downRequests) {
            List<ElevatorRequest> sorted = new ArrayList<>(downRequests);
            sorted.sort(Comparator.comparingInt(req -> -req.getFloorNumber()));
            return sorted;
        }
        
        private int determineNextStop(Elevator elevator, Direction direction, 
                                     List<ElevatorRequest> upRequests, List<ElevatorRequest> downRequests, 
                                     int currentFloor) {
            if (direction == Direction.UP && !upRequests.isEmpty()) {
                return upRequests.get(0).getFloorNumber();
            } else if (direction == Direction.DOWN && !downRequests.isEmpty()) {
                return downRequests.get(0).getFloorNumber();
            } else if (!upRequests.isEmpty()) {
                elevator.setDirection(Direction.DOWN);
                return downRequests.isEmpty() ? currentFloor : downRequests.get(0).getFloorNumber();
            } else if (!downRequests.isEmpty()) {
                elevator.setDirection(Direction.UP);
                return upRequests.isEmpty() ? currentFloor : upRequests.get(0).getFloorNumber();
            } else {
                return currentFloor;
            }
        }
    }

    // Request Class
    public static class ElevatorRequest implements ElevatorCommand {
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
            if (isInternalRequest) {
                controller.requestFloor(elevatorId, floorNumber);
            } else {
                controller.requestElevator(elevatorId, floorNumber, requestDirection);
            }
        }

        public int getFloorNumber() { return floorNumber; }
        public Direction getRequestDirection() { return requestDirection; }
        public int getElevatorId() { return elevatorId; }
        public boolean isInternalRequest() { return isInternalRequest; }
    }

    // Elevator Class
    public static class Elevator {
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

        public Queue<ElevatorRequest> getRequests() { return requests; }

        public void addRequest(ElevatorRequest elevatorRequest) {
            if (!isValidRequest(elevatorRequest)) {
                return;
            }
            
            requests.add(elevatorRequest);
            updateElevatorStateForNewRequest(elevatorRequest);
        }
        
        private boolean isValidRequest(ElevatorRequest elevatorRequest) {
            return !requests.contains(elevatorRequest);
        }
        
        private void updateElevatorStateForNewRequest(ElevatorRequest elevatorRequest) {
            if (state == ElevatorState.STOPPED && !requests.isEmpty()) {
                setDirectionForRequest(elevatorRequest);
                setState(ElevatorState.RUNNING);
            }
        }
        
        private void setDirectionForRequest(ElevatorRequest elevatorRequest) {
            int requestedFloor = elevatorRequest.getFloorNumber();
            if (requestedFloor > currentFloor) {
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        }

        public void moveToNextFloor(int nextStop) {
            if (state != ElevatorState.RUNNING) {
                System.out.println("Elevator " + id + " is not running (state: " + state + ")");
                return;
            }
            
            System.out.println("Elevator " + id + " moving from floor " + currentFloor + " to floor " + nextStop + " (direction: " + direction + ")");
            
            moveFloorByFloor(nextStop);
            
            if (currentFloor == nextStop) {
                completeArrival();
            }
        }
        
        private void moveFloorByFloor(int targetFloor) {
            while (currentFloor != targetFloor) {
                incrementFloor();
                System.out.println("Elevator " + id + " now at floor " + currentFloor);
                notifyFloorChange();
            }
        }
        
        private void incrementFloor() {
            if (direction == Direction.UP) {
                currentFloor++;
            } else {
                currentFloor--;
            }
        }

        public void completeArrival() {
            setState(ElevatorState.STOPPED);
            requests.removeIf(r -> r.getFloorNumber() == currentFloor);
            if (requests.isEmpty()) {
                setState(ElevatorState.STOPPED);
                setDirection(Direction.IDLE);
            } else {
                setState(ElevatorState.RUNNING);
            }
        }

        private void notifyStateChange() {
            for (ElevatorObserver elevatorObserver : observers) {
                elevatorObserver.onElevatorStateChange(this, state);
            }
        }

        private void notifyFloorChange() {
            for (ElevatorObserver elevatorObserver : observers) {
                elevatorObserver.onElevatorFloorChange(this, currentFloor);
            }
        }

        // Getters and Setters
        public int getId() { return id; }
        public int getCurrentFloor() { return currentFloor; }
        public void setCurrentFloor(int currentFloor) { 
            notifyFloorChange();
            this.currentFloor = currentFloor; 
        }
        public Direction getDirection() { return direction; }
        public void setDirection(Direction direction) { this.direction = direction; }
        public ElevatorState getState() { return state; }
        public void setState(ElevatorState state) { 
            notifyStateChange();
            this.state = state; 
        }
    }

    // Controller Class
    public static class ElevatorController {
        private List<Elevator> elevators;
        private SchedulingStrategy schedulingStrategy;

        public ElevatorController(int numberOfElevators) {
            this.elevators = new ArrayList<>();
            this.schedulingStrategy = new ScanScheduling(); // Using SCAN scheduling by default
            for (int i = 0; i < numberOfElevators; i++) {
                Elevator elevator = new Elevator(i);
                elevators.add(elevator);
            }
        }

        void requestElevator(int elevatorId, int floor, Direction requestDirection) {
            System.out.println("External Requesting elevator " + elevatorId + " to floor " + floor + " in direction " + requestDirection);
            Elevator elevator = getElevatorById(elevatorId);
            if (elevator == null) {
                System.out.println("Elevator not found");
                return;
            }
            elevator.addRequest(new ElevatorRequest(floor, requestDirection, elevatorId, false, this));
            System.out.println("Assigning elevator " + elevator.getId() + " to floor " + floor + " in direction " + requestDirection);
        }

        void requestFloor(int elevatorId, int floor) {
            System.out.println("Internal Requesting elevator " + elevatorId + " to floor " + floor);
            Elevator elevator = getElevatorById(elevatorId);
            if (elevator == null) {
                System.out.println("Elevator not found");
                return;
            }
            Direction direction = floor > elevator.getCurrentFloor() ? Direction.UP : Direction.DOWN;
            elevator.addRequest(new ElevatorRequest(floor, direction, elevatorId, true, this));
            System.out.println("Assigning elevator " + elevator.getId() + " to floor " + floor + " in direction " + direction);
        }

        public Elevator getElevatorById(int id) {
            for (Elevator elevator : elevators) {
                if (elevator.getId() == id) {
                    return elevator;
                }
            }
            return null;
        }

        public void step() {
            for (Elevator elevator : elevators) {
                if (!elevator.getRequests().isEmpty()) {
                    int nextFloor = schedulingStrategy.getNextStop(elevator);
                    if (elevator.getCurrentFloor() != nextFloor) {
                        elevator.moveToNextFloor(nextFloor);
                    }
                }
            }
        }
    }

    // Building Class
    public static class Building {
        private String name;
        private int totalFloors;
        private int totalElevators;
        private ElevatorController elevatorController;

        public Building(String name, int totalFloors, int totalElevators) {
            this.name = name;
            this.totalFloors = totalFloors;
            this.totalElevators = totalElevators;
            this.elevatorController = new ElevatorController(totalElevators);
        }

        public ElevatorController getElevatorController() {
            return elevatorController;
        }
    }

    // Simulation Class
    public static class ElevatorSimulation {
        private Building building;
        private ElevatorController controller;

        public ElevatorSimulation(String buildingName, int totalFloors, int totalElevators) {
            this.building = new Building(buildingName, totalFloors, totalElevators);
            this.controller = building.getElevatorController();
        }

        public void runSimulation() {
            System.out.println("=== Elevator System Simulation (SCAN Scheduling) ===");
            
            setupExternalRequests();
            setupInternalRequests();
            runSimulationSteps();
            
            System.out.println("\n=== Simulation Complete ===");
        }
        
        private void setupExternalRequests() {
            System.out.println("\n1. External requests:");
            // Person on floor 3 wants to go up
            controller.requestElevator(0, 3, Direction.UP);
            
            // Person on floor 7 wants to go down  
            controller.requestElevator(1, 7, Direction.DOWN);
        }
        
        private void setupInternalRequests() {
            System.out.println("\n2. Internal requests:");
            // Someone in elevator 0 wants to go to floor 5
            controller.requestFloor(0, 5);
            
            // Someone in elevator 1 wants to go to floor 2
            controller.requestFloor(1, 2);
        }
        
        private void runSimulationSteps() {
            System.out.println("\n3. Running simulation steps:");
            // Run simulation for several steps
            for (int i = 1; i <= 8; i++) {
                System.out.println("\n--- Step " + i + " ---");
                controller.step();
            }
        }
    }

    // Main Method
    public static void main(String[] args) {
        ElevatorSimulation simulation = new ElevatorSimulation("Office Tower", 10, 2);
        simulation.runSimulation();
    }
}