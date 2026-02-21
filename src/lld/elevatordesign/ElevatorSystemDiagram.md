# Elevator System Class Diagram

## ASCII Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                            ELEVATOR SYSTEM                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│   ElevatorSystem    │    │    Building         │    │ ElevatorSimulation  │
│─────────────────────│    │─────────────────────│    │─────────────────────│
│ +main(args[])      │    │ -name: String      │    │ -building: Building │
│                    │    │ -totalFloors: int  │    │ -controller: Ctrl   │
└─────────────────────┘    │ -totalElevators: int│    │─────────────────────│
         │                │ -controller: Ctrl   │    │ +runSimulation()    │
         │                │─────────────────────│    └─────────────────────┘
         │                │ +getController()    │              │
         │                └─────────────────────┘              │
         │                         │                         │
         │                         │                         │
         ▼                         ▼                         ▼

┌─────────────────────────────────────────────────────────────────────────────────────┐
│                        ElevatorController                                       │
│─────────────────────────────────────────────────────────────────────────────────────│
│ -elevators: List<Elevator>                                                    │
│ -schedulingStrategy: SchedulingStrategy                                          │
│─────────────────────────────────────────────────────────────────────────────────────│
│ +ElevatorController(numElevators)                                              │
│ +requestElevator(id, floor, direction)                                         │
│ +requestFloor(id, floor)                                                       │
│ +getElevatorById(id)                                                          │
│ +step()                                                                       │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ uses
                                    ▼
┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│     Elevator        │    │  ElevatorRequest   │    │ SchedulingStrategy  │
│─────────────────────│    │─────────────────────│    │─────────────────────│
│ -id: int           │    │ -floorNumber: int  │    │ +getNextStop(e)     │
│ -currentFloor: int │    │ -requestDirection  │    └─────────────────────┘
│ -direction: Dir    │    │ -elevatorId: int   │              ▲
│ -state: State      │    │ -isInternal: bool  │              │
│ -requests: Queue   │    │ -controller: Ctrl  │              │ implements
│ -observers: List   │    │─────────────────────│              │
│─────────────────────│    │ +execute()         │    ┌─────────────────────┐
│ +Elevator(id)      │    │ +getFloorNumber()  │    │   FIFOScheduling   │
│ +addRequest(req)   │    │ +getRequestDir()   │    │─────────────────────│
│ +moveToNextFloor() │    │ +getElevatorId()   │    │ +getNextStop(e)     │
│ +completeArrival() │    │ +isInternal()      │    └─────────────────────┘
│ +addObserver(obs)   │    └─────────────────────┘
└─────────────────────┘              │ implements
                                    ▼
                          ┌─────────────────────┐
                          │  ElevatorCommand   │
                          │─────────────────────│
                          │ +execute()         │
                          └─────────────────────┘

┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│    Direction        │    │   ElevatorState    │    │ ElevatorObserver   │
│─────────────────────│    │─────────────────────│    │─────────────────────│
│ UP                 │    │ RUNNING            │    │ +onStateChange()   │
│ DOWN               │    │ STOPPED            │    │ +onFloorChange()   │
│ IDLE               │    │ MAINTENANCE        │    └─────────────────────┘
└─────────────────────┘    └─────────────────────┘
```

## Mermaid Class Diagram

```mermaid
classDiagram
    %% Enums
    class Direction {
        <<enumeration>>
        UP
        DOWN
        IDLE
    }
    
    class ElevatorState {
        <<enumeration>>
        RUNNING
        STOPPED
        MAINTENANCE
    }
    
    %% Interfaces
    class ElevatorCommand {
        <<interface>>
        +execute()
    }
    
    class ElevatorObserver {
        <<interface>>
        +onElevatorStateChange(elevator: Elevator, state: ElevatorState)
        +onElevatorFloorChange(elevator: Elevator, floor: int)
    }
    
    class SchedulingStrategy {
        <<interface>>
        +getNextStop(elevator: Elevator): int
    }
    
    %% Classes
    class ElevatorSystem {
        +main(args: String[])
    }
    
    class Building {
        -name: String
        -totalFloors: int
        -totalElevators: int
        -elevatorController: ElevatorController
        +Building(name: String, totalFloors: int, totalElevators: int)
        +getElevatorController(): ElevatorController
    }
    
    class ElevatorSimulation {
        -building: Building
        -controller: ElevatorController
        +ElevatorSimulation(buildingName: String, totalFloors: int, totalElevators: int)
        +runSimulation()
    }
    
    class ElevatorController {
        -elevators: List~Elevator~
        -schedulingStrategy: SchedulingStrategy
        +ElevatorController(numberOfElevators: int)
        +requestElevator(elevatorId: int, floor: int, requestDirection: Direction)
        +requestFloor(elevatorId: int, floor: int)
        +getElevatorById(id: int): Elevator
        +step()
    }
    
    class Elevator {
        -id: int
        -currentFloor: int
        -direction: Direction
        -state: ElevatorState
        -requests: Queue~ElevatorRequest~
        -observers: List~ElevatorObserver~
        +Elevator(id: int)
        +getRequests(): Queue~ElevatorRequest~
        +addRequest(elevatorRequest: ElevatorRequest)
        +moveToNextFloor(nextStop: int)
        +completeArrival()
        +getId(): int
        +getCurrentFloor(): int
        +setCurrentFloor(currentFloor: int)
        +getDirection(): Direction
        +setDirection(direction: Direction)
        +getState(): ElevatorState
        +setState(state: ElevatorState)
    }
    
    class ElevatorRequest {
        -floorNumber: int
        -requestDirection: Direction
        -elevatorId: int
        -isInternalRequest: boolean
        -controller: ElevatorController
        +ElevatorRequest(floorNumber: int, requestDirection: Direction, elevatorId: int, isInternalRequest: boolean, controller: ElevatorController)
        +execute()
        +getFloorNumber(): int
        +getRequestDirection(): Direction
        +getElevatorId(): int
        +isInternalRequest(): boolean
    }
    
    class FIFOScheduling {
        +getNextStop(elevator: Elevator): int
    }
    
    class ScanScheduling {
        +getNextStop(elevator: Elevator): int
    }
    
    %% Relationships
    ElevatorSystem --> ElevatorSimulation : creates
    ElevatorSimulation --> Building : creates
    Building --> ElevatorController : contains
    ElevatorController --> Elevator : manages
    ElevatorController --> SchedulingStrategy : uses
    ElevatorController --> ElevatorRequest : creates
    Elevator *-- ElevatorRequest : contains
    Elevator *-- Direction : uses
    Elevator *-- ElevatorState : uses
    ElevatorRequest ..|> ElevatorCommand : implements
    FIFOScheduling ..|> SchedulingStrategy : implements
    ScanScheduling ..|> SchedulingStrategy : implements
    ElevatorObserver <|.. Elevator : notifies
    
    %% Notes
    note for ElevatorController "Manages multiple elevators\nand handles request scheduling"
    note for Elevator "Individual elevator with\nstate management and\nmovement logic"
    note for ElevatorRequest "Command pattern implementation\nfor elevator operations"
    note for FIFOScheduling "Strategy pattern for\nFIFO scheduling algorithm"
    note for ScanScheduling "Strategy pattern for\nSCAN/LOOK scheduling algorithm\n(default implementation)"
```

## PlantUML Class Diagram

```plantuml
@startuml ElevatorSystem

' Enums
enum Direction {
    UP
    DOWN
    IDLE
}

enum ElevatorState {
    RUNNING
    STOPPED
    MAINTENANCE
}

' Interfaces
interface ElevatorCommand {
    +execute()
}

interface ElevatorObserver {
    +onElevatorStateChange(elevator: Elevator, state: ElevatorState)
    +onElevatorFloorChange(elevator: Elevator, floor: int)
}

interface SchedulingStrategy {
    +getNextStop(elevator: Elevator): int
}

' Classes
class ElevatorSystem {
    +main(args: String[])
}

class Building {
    -name: String
    -totalFloors: int
    -totalElevators: int
    -elevatorController: ElevatorController
    +Building(name: String, totalFloors: int, totalElevators: int)
    +getElevatorController(): ElevatorController
}

class ElevatorSimulation {
    -building: Building
    -controller: ElevatorController
    +ElevatorSimulation(buildingName: String, totalFloors: int, totalElevators: int)
    +runSimulation()
}

class ElevatorController {
    -elevators: List<Elevator>
    -schedulingStrategy: SchedulingStrategy
    +ElevatorController(numberOfElevators: int)
    +requestElevator(elevatorId: int, floor: int, requestDirection: Direction)
    +requestFloor(elevatorId: int, floor: int)
    +getElevatorById(id: int): Elevator
    +step()
}

class Elevator {
    -id: int
    -currentFloor: int
    -direction: Direction
    -state: ElevatorState
    -requests: Queue<ElevatorRequest>
    -observers: List<ElevatorObserver>
    +Elevator(id: int)
    +getRequests(): Queue<ElevatorRequest>
    +addRequest(elevatorRequest: ElevatorRequest)
    +moveToNextFloor(nextStop: int)
    +completeArrival()
    +getId(): int
    +getCurrentFloor(): int
    +setCurrentFloor(currentFloor: int)
    +getDirection(): Direction
    +setDirection(direction: Direction)
    +getState(): ElevatorState
    +setState(state: ElevatorState)
}

class ElevatorRequest {
    -floorNumber: int
    -requestDirection: Direction
    -elevatorId: int
    -isInternalRequest: boolean
    -controller: ElevatorController
    +ElevatorRequest(floorNumber: int, requestDirection: Direction, elevatorId: int, isInternalRequest: boolean, controller: ElevatorController)
    +execute()
    +getFloorNumber(): int
    +getRequestDirection(): Direction
    +getElevatorId(): int
    +isInternalRequest(): boolean
}

class FIFOScheduling {
    +getNextStop(elevator: Elevator): int
}

' Relationships
ElevatorSystem --> ElevatorSimulation : creates
ElevatorSimulation --> Building : creates
Building --> ElevatorController : creates
ElevatorController --> Elevator : manages
ElevatorController --> SchedulingStrategy : uses
ElevatorController --> ElevatorRequest : creates
Elevator --> ElevatorRequest : contains
Elevator --> ElevatorObserver : notifies
Elevator --> Direction : uses
Elevator --> ElevatorState : uses
ElevatorRequest --> ElevatorCommand : implements
FIFOScheduling --> SchedulingStrategy : implements

@enduml
```

## Design Patterns Used

### 1. **Command Pattern**
- **Command Interface**: `ElevatorCommand`
- **Concrete Command**: `ElevatorRequest`
- **Invoker**: `ElevatorController`
- **Receiver**: `Elevator`

### 2. **Strategy Pattern**
- **Strategy Interface**: `SchedulingStrategy`
- **Concrete Strategy**: `FIFOScheduling`
- **Context**: `ElevatorController`

### 3. **Observer Pattern**
- **Subject**: `Elevator`
- **Observer Interface**: `ElevatorObserver`
- **Concrete Observers**: Can be implemented for UI, logging, etc.

### 4. **State Pattern**
- **State Enum**: `ElevatorState`
- **Context**: `Elevator`

## Key Relationships

1. **Composition**: `Building` has `ElevatorController`
2. **Aggregation**: `ElevatorController` manages multiple `Elevator` objects
3. **Association**: `Elevator` processes `ElevatorRequest` objects
4. **Implementation**: `FIFOScheduling` implements `SchedulingStrategy`
5. **Dependency**: `ElevatorController` depends on `SchedulingStrategy`

## Data Flow

1. **External Request** → `ElevatorController.requestElevator()`
2. **Internal Request** → `ElevatorController.requestFloor()`
3. **Request Processing** → `Elevator.addRequest()`
4. **Scheduling** → `FIFOScheduling.getNextStop()`
5. **Movement** → `Elevator.moveToNextFloor()`
6. **State Updates** → `ElevatorObserver` notifications
