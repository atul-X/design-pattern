# Elevator System Architecture - Mermaid Diagram

## Class Diagram

```mermaid
classDiagram
    %% Main System Class
    class ElevatorSystem {
        <<enumeration>>
        Direction
        ElevatorState
    }
    
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
    class ElevatorObserver {
        <<interface>>
        +onElevatorStateChange(Elevator, ElevatorState)
        +onElevatorFloorChange(Elevator, int)
    }
    
    class ElevatorCommand {
        <<interface>>
        +execute()
    }
    
    class SchedulingStrategy {
        <<interface>>
        +getNextStop(Elevator) int
    }
    
    %% Scheduling Strategy Implementations
    class FIFOScheduling {
        +getNextStop(Elevator) int
    }
    
    class ScanScheduling {
        +getNextStop(Elevator) int
        -separateRequestsByDirection(Queue, int) Map
        -sortUpRequests(List) List
        -sortDownRequests(List) List
        -determineNextStop(Elevator, Direction, List, List, int) int
    }
    
    %% Request Class
    class ElevatorRequest {
        -int floorNumber
        -Direction requestDirection
        -int elevatorId
        -boolean isInternalRequest
        -ElevatorController controller
        +execute()
        +getFloorNumber() int
        +getRequestDirection() Direction
        +getElevatorId() int
        +isInternalRequest() boolean
    }
    
    %% Elevator Class
    class Elevator {
        -int id
        -int currentFloor
        -Direction direction
        -ElevatorState state
        -Queue~ElevatorRequest~ requests
        -List~ElevatorObserver~ observers
        +addRequest(ElevatorRequest)
        +moveToNextFloor(int)
        +completeArrival()
        -isValidRequest(ElevatorRequest) boolean
        -updateElevatorStateForNewRequest(ElevatorRequest)
        -setDirectionForRequest(ElevatorRequest)
        -moveFloorByFloor(int)
        -incrementFloor()
        -notifyStateChange()
        -notifyFloorChange()
    }
    
    %% Controller Class
    class ElevatorController {
        -List~Elevator~ elevators
        -SchedulingStrategy schedulingStrategy
        +requestElevator(int, int, Direction)
        +requestFloor(int, int)
        +getElevatorById(int) Elevator
        +step()
    }
    
    %% Building Class
    class Building {
        -String name
        -int totalFloors
        -int totalElevators
        -ElevatorController elevatorController
        +getElevatorController() ElevatorController
    }
    
    %% Simulation Class
    class ElevatorSimulation {
        -Building building
        -ElevatorController controller
        +runSimulation()
        -setupExternalRequests()
        -setupInternalRequests()
        -runSimulationSteps()
    }
    
    %% Relationships
    SchedulingStrategy <|.. FIFOScheduling
    SchedulingStrategy <|.. ScanScheduling
    ElevatorCommand <|.. ElevatorRequest
    ElevatorRequest --> ElevatorController
    Elevator --> ElevatorRequest
    Elevator --> ElevatorObserver
    ElevatorController --> Elevator
    ElevatorController --> SchedulingStrategy
    Building --> ElevatorController
    ElevatorSimulation --> Building
    ElevatorSimulation --> ElevatorController
```

## Sequence Diagram - Request Processing

```mermaid
sequenceDiagram
    participant User
    participant ElevatorSimulation
    participant ElevatorController
    participant Elevator
    participant ScanScheduling
    participant ElevatorObserver
    
    User->>ElevatorSimulation: runSimulation()
    ElevatorSimulation->>ElevatorSimulation: setupExternalRequests()
    ElevatorSimulation->>ElevatorController: requestElevator(elevatorId, floor, direction)
    ElevatorController->>Elevator: addRequest(request)
    Elevator->>Elevator: isValidRequest(request)
    Elevator->>Elevator: updateElevatorStateForNewRequest(request)
    Elevator->>ElevatorObserver: onElevatorStateChange()
    
    ElevatorSimulation->>ElevatorSimulation: runSimulationSteps()
    ElevatorSimulation->>ElevatorController: step()
    ElevatorController->>ScanScheduling: getNextStop(elevator)
    ScanScheduling->>ScanScheduling: separateRequestsByDirection()
    ScanScheduling->>ScanScheduling: sortUpRequests()
    ScanScheduling->>ScanScheduling: sortDownRequests()
    ScanScheduling->>ScanScheduling: determineNextStop()
    ScanScheduling-->>ElevatorController: nextFloor
    
    ElevatorController->>Elevator: moveToNextFloor(nextFloor)
    Elevator->>Elevator: moveFloorByFloor(targetFloor)
    loop Each floor
        Elevator->>Elevator: incrementFloor()
        Elevator->>ElevatorObserver: onElevatorFloorChange()
    end
    Elevator->>Elevator: completeArrival()
    Elevator->>ElevatorObserver: onElevatorStateChange()
```

## State Diagram - Elevator States

```mermaid
stateDiagram-v2
    [*] --> STOPPED
    STOPPED --> RUNNING: addRequest() && requests not empty
    RUNNING --> STOPPED: completeArrival()
    STOPPED --> MAINTENANCE: maintenance mode
    MAINTENANCE --> STOPPED: maintenance complete
    RUNNING --> MAINTENANCE: emergency stop
    
    note right of RUNNING
        Elevator moves floor by floor
        Notifies observers on floor changes
    end note
    
    note right of STOPPED
        Can accept new requests
        Sets direction based on request
    end note
```

## Component Diagram - System Architecture

```mermaid
graph TB
    subgraph "Elevator System"
        subgraph "Core Components"
            ElevatorController[Controller]
            Elevator[Elevator]
            Scheduling[Scheduling Strategy]
        end
        
        subgraph "Request Management"
            Request[Elevator Request]
            Command[Command Interface]
        end
        
        subgraph "Observation System"
            Observer[Observer Interface]
            Display[Display System]
            Logger[Logger System]
        end
        
        subgraph "Simulation"
            Simulation[Elevator Simulation]
            Building[Building]
        end
    end
    
    %% Connections
    ElevatorController --> Elevator
    ElevatorController --> Scheduling
    Request --> Command
    Elevator --> Request
    Elevator --> Observer
    Observer --> Display
    Observer --> Logger
    Simulation --> Building
    Simulation --> ElevatorController
    
    %% Style
    classDef core fill:#e1f5fe
    classDef request fill:#f3e5f5
    classDef observation fill:#e8f5e8
    classDef simulation fill:#fff3e0
    
    class ElevatorController,Elevator,Scheduling core
    class Request,Command request
    class Observer,Display,Logger observation
    class Simulation,Building simulation
```

## Activity Diagram - Request Processing Flow

```mermaid
flowchart TD
    A[Request Received] --> B{Request Type}
    B -->|External| C[requestElevator]
    B -->|Internal| D[requestFloor]
    
    C --> E[Create ElevatorRequest]
    D --> E
    
    E --> F[addRequest to Elevator]
    F --> G{Valid Request?}
    G -->|No| H[Reject Request]
    G -->|Yes| I[Add to Queue]
    
    I --> J{Elevator Stopped?}
    J -->|No| K[Keep Current State]
    J -->|Yes| L[Set Direction]
    L --> M[Set State to RUNNING]
    
    M --> N[Schedule Next Stop]
    N --> O[Move Floor by Floor]
    O --> P{Reached Target?}
    P -->|No| O
    P -->|Yes| Q[Complete Arrival]
    
    Q --> R{More Requests?}
    R -->|Yes| N
    R -->|No| S[Set State to STOPPED]
    S --> T[Set Direction to IDLE]
    
    K --> U[End]
    T --> U
    H --> U
```

## Design Patterns Used

```mermaid
mindmap
  root((Elevator System))
    Command Pattern
      ElevatorRequest implements ElevatorCommand
      execute() method
    Strategy Pattern
      SchedulingStrategy interface
      FIFO implementation
      SCAN implementation
    Observer Pattern
      ElevatorObserver interface
      State change notifications
      Floor change notifications
    State Pattern
      ElevatorState enum
      RUNNING, STOPPED, MAINTENANCE
      State transitions
```
