# Design Patterns Implementation in Java

This project demonstrates the implementation of **fundamental design patterns** in Java, showcasing both the problems they solve and their practical applications. Each pattern includes comparison implementations to highlight the benefits of using design patterns.

## 📁 Project Structure

```
src/
├── behavioural/                    # Behavioral Design Patterns
│   ├── command/                    # Command Pattern Implementation
│   ├── iterator/                   # Iterator Pattern Implementation
│   │   ├── book/                   # Simple book collection example
│   │   └── notificationmanagement/ # Comprehensive notification system
│   ├── mediator/                   # Mediator Pattern Implementation
│   ├── memento/                    # Memento Pattern Implementation
│   ├── observer/                   # Observer Pattern Implementation  
│   ├── state/                      # State Pattern Implementation
│   ├── strategy/                   # Strategy Pattern Implementation
│   └── template/                   # Template Method Pattern Implementation
├── creational/                     # Creational Design Patterns (Future)
│   ├── factory/                    # Factory Pattern (Planned)
│   ├── builder/                    # Builder Pattern (Planned)
│   ├── singleton/                  # Singleton Pattern (Planned)
│   ├── prototype/                  # Prototype Pattern (Planned)
│   └── abstractfactory/            # Abstract Factory Pattern (Planned)
└── Main.java                       # Main entry point
```

## 🎯 **Design Pattern Categories**

Design patterns are categorized into three main types based on their purpose:

### 🎭 **Behavioral Patterns** (Current Implementation)
**Focus:** Communication between objects and the assignment of responsibilities
- Define how objects interact and communicate with each other
- Concerned with algorithms and assignment of responsibilities between objects
- Help in defining the communication patterns between objects

### 🏭 **Creational Patterns** (Future Implementation)
**Focus:** Object creation mechanisms
- Deal with object creation in a manner suitable to the situation
- Provide flexibility in deciding which objects need to be created for a given use case
- Make the system independent of how its objects are created, composed, and represented

### 🏗️ **Structural Patterns** (Future Consideration)
**Focus:** Object composition and relationships
- Deal with object composition to form larger structures
- Describe ways to compose objects to realize new functionality
- Help ensure that when one part changes, the entire structure doesn't need to change

---

## 🔍 **What Are Design Patterns?**

Design patterns are **reusable solutions** to commonly occurring problems in software design. They represent best practices evolved over time by experienced developers and provide a **common vocabulary** for discussing design solutions.

### 🎯 **Key Characteristics:**
- **Proven Solutions:** Time-tested approaches to common problems
- **Language Independent:** Concepts applicable across programming languages
- **Communication Tool:** Shared vocabulary for developers
- **Best Practices:** Encapsulate expert knowledge and experience
- **Flexibility:** Adaptable to specific contexts and requirements

---

## 🎯 **Behavioral Design Patterns Overview**

**Behavioral patterns** are design patterns that identify common communication patterns between objects and realize these patterns. These patterns increase flexibility in carrying out communication by characterizing the ways in which classes or objects interact and distribute responsibility.

### 🎯 **Core Characteristics of Behavioral Patterns:**

#### **1. Communication Focus** 📡
- **Inter-object Communication:** Define how objects talk to each other
- **Message Passing:** Establish protocols for object interaction
- **Event Handling:** Manage how objects respond to events and state changes
- **Notification Systems:** Implement observer-subscriber relationships

#### **2. Responsibility Distribution** ⚖️
- **Role Assignment:** Clearly define what each object is responsible for
- **Separation of Concerns:** Divide complex behaviors into manageable parts
- **Delegation:** Allow objects to delegate tasks to appropriate handlers
- **Chain of Responsibility:** Pass requests through a chain of potential handlers

#### **3. Algorithm Encapsulation** 🔒
- **Strategy Encapsulation:** Wrap algorithms in interchangeable objects
- **Template Definition:** Define algorithm skeletons with customizable steps
- **Command Encapsulation:** Package requests as objects for flexibility
- **State Management:** Encapsulate state-specific behavior in separate objects

#### **4. Flexibility and Extensibility** 🔄
- **Runtime Behavior Changes:** Modify object behavior during execution
- **Loose Coupling:** Reduce dependencies between communicating objects
- **Open/Closed Principle:** Open for extension, closed for modification
- **Dynamic Composition:** Compose behaviors at runtime

### 🎪 **Types of Behavioral Patterns Implemented:**

#### **A. Object Behavioral Patterns** 🎯
Use object composition to distribute behavior among objects:

- **🎮 Command Pattern:** Encapsulate requests as objects
- **👁️ Observer Pattern:** Define one-to-many dependencies between objects
- **🎯 Strategy Pattern:** Define family of algorithms and make them interchangeable
- **🔍 Iterator Pattern:** Provide sequential access to elements of an aggregate
- **💾 Memento Pattern:** Capture and restore object state without violating encapsulation

#### **B. Class Behavioral Patterns** 📋
Use inheritance to distribute behavior between classes:

- **📋 Template Method Pattern:** Define algorithm skeleton, let subclasses override specific steps

### 🌟 **Benefits of Behavioral Patterns:**

#### **1. Enhanced Communication** 📞
```java
// Before: Tight coupling
class WeatherStation {
    private Display display;
    private MobileApp app;
    
    public void updateTemperature(float temp) {
        display.update(temp);  // Direct coupling
        app.notify(temp);      // Hard to extend
    }
}

// After: Observer Pattern - Loose coupling
class WeatherStation implements Subject {
    private List<Observer> observers = new ArrayList<>();
    
    public void updateTemperature(float temp) {
        notifyObservers(temp);  // Flexible, extensible
    }
}
```

#### **2. Flexible Algorithm Selection** 🎛️
```java
// Before: Rigid algorithm selection
class PaymentProcessor {
    public void processPayment(String type, double amount) {
        if (type.equals("credit")) {
            // Credit card logic
        } else if (type.equals("debit")) {
            // Debit card logic
        }
        // Hard to add new payment methods
    }
}

// After: Strategy Pattern - Dynamic algorithm selection
class PaymentProcessor {
    private PaymentStrategy strategy;
    
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void processPayment(double amount) {
        strategy.processPayment(amount);  // Flexible, extensible
    }
}
```

#### **3. Undo/Redo Capabilities** ↩️
```java
// Command Pattern enables powerful undo/redo systems
class TextEditor {
    private Stack<Command> history = new Stack<>();
    
    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }
    
    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();  // Powerful undo capability
        }
    }
}
```

### 🎯 **When to Use Behavioral Patterns:**

#### **✅ Use Behavioral Patterns When:**
- Multiple objects need to communicate efficiently
- You need to change object behavior at runtime
- Algorithms need to be interchangeable
- You want to implement undo/redo functionality
- Complex workflows need to be managed
- Event-driven architectures are required
- You need to iterate over collections uniformly

#### **❌ Avoid Behavioral Patterns When:**
- Simple direct method calls are sufficient
- Object interactions are minimal and unlikely to change
- Performance overhead is critical
- The system is small and unlikely to grow

### 🏭 **Future Expansion: Creational Design Patterns**

The next phase of this project will implement **Creational Design Patterns** that focus on object creation mechanisms:

#### **🔮 Planned Creational Patterns:**

##### **1. Factory Method Pattern** 🏭
- **Purpose:** Create objects without specifying exact classes
- **Use Case:** Database connection factories, UI component creation
- **Benefit:** Loose coupling between creator and concrete products

##### **2. Builder Pattern** 🔨
- **Purpose:** Construct complex objects step by step
- **Use Case:** SQL query builders, configuration objects
- **Benefit:** Flexible object construction with optional parameters

##### **3. Singleton Pattern** 👑
- **Purpose:** Ensure only one instance of a class exists
- **Use Case:** Database connections, logging, caching
- **Benefit:** Controlled access to shared resources

##### **4. Prototype Pattern** 🧬
- **Purpose:** Create objects by cloning existing instances
- **Use Case:** Game object creation, document templates
- **Benefit:** Avoid expensive object creation operations

##### **5. Abstract Factory Pattern** 🏗️
- **Purpose:** Create families of related objects
- **Use Case:** Cross-platform UI components, database drivers
- **Benefit:** Ensure compatibility between related objects

#### **🔄 Parallel Structure Design:**

The project will maintain parallel organization between Behavioral and Creational patterns:

```
Behavioral Patterns (Current)     →     Creational Patterns (Future)
├── Command (Actions)            →     ├── Factory Method (Object Creation)
├── Observer (Notifications)     →     ├── Builder (Complex Construction)
├── Strategy (Algorithms)        →     ├── Singleton (Instance Control)
├── Iterator (Collection Access) →     ├── Prototype (Object Cloning)
├── Memento (State Management)   →     ├── Abstract Factory (Family Creation)
└── Template Method (Workflows)  →     └── [Additional patterns as needed]
```

#### **🎯 Integration Opportunities:**

Future patterns will demonstrate powerful combinations:
- **Factory + Strategy:** Create strategy objects dynamically
- **Builder + Command:** Build complex command objects
- **Singleton + Observer:** Global event management systems
- **Prototype + Memento:** Efficient state cloning and restoration

{{ ... }}

---

## 🎯 **Behavioral Design Patterns Implemented**

### 1. Command Pattern 🎮

**Location:** `src/behavioural/command/`

**Core Purpose:** Encapsulates a request as an object, allowing you to parameterize clients with different requests, queue operations, and support undo functionality.

#### Key Components:
- **Command Interface:** Defines the execute() method
- **Concrete Commands:** Specific command implementations
- **Receiver:** Performs the actual work
- **Invoker:** Triggers command execution

#### Real-World Use Cases:
- **GUI Applications:** Button clicks, menu selections
- **Remote Controls:** TV, AC, Smart Home devices
- **Undo/Redo Operations:** Text editors, image editors
- **Transaction Processing:** Banking systems, database operations

---

### 2. Memento Pattern 💾

**Location:** `src/behavioural/memento/`

**Core Purpose:** Captures and externalizes an object's internal state without violating encapsulation, allowing the object to be restored to this state later.

#### Three Implementations:
- **Text Editor with Undo:** Basic undo/redo functionality
- **Graphic Editor:** Shape manipulation with history
- **Transaction Rollback:** Database-like operations with rollback

#### Real-World Use Cases:
- **Text Editors:** Undo/Redo functionality (MS Word, VS Code)
- **Database Systems:** Transaction rollback, savepoints
- **Game Development:** Save/load game states, checkpoints
- **Version Control:** Git commits, branching

---

### 3. Observer Pattern 👁️

**Location:** `src/behavioural/observer/weather/`

**Core Purpose:** Defines a one-to-many dependency between objects so that when one object changes state, all dependents are notified automatically.

#### Key Components:
- **Subject Interface:** Attach, detach, notify observers
- **Observer Interface:** Update method for notifications
- **Concrete Subject:** Maintains state and observer list
- **Concrete Observers:** React to state changes

#### Real-World Use Cases:
- **GUI Applications:** Model-View architectures (MVC, MVP, MVVM)
- **Event Systems:** DOM events, custom event handlers
- **Stock Market:** Price change notifications
- **Social Media:** Notification systems for followers

---

### 4. Strategy Pattern 🎯

**Location:** `src/behavioural/strategy/payment/`

**Core Purpose:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable at runtime.

#### Key Components:
- **Strategy Interface:** Common interface for all algorithms
- **Concrete Strategies:** Different algorithm implementations
- **Context:** Uses strategy to perform operations

#### Real-World Use Cases:
- **Payment Processing:** Credit card, PayPal, cryptocurrency
- **Sorting Algorithms:** QuickSort, MergeSort, BubbleSort
- **Navigation Apps:** Fastest route, shortest route, scenic route
- **Authentication:** OAuth, LDAP, database authentication

---

### 5. Template Method Pattern 📋

**Location:** `src/behavioural/template/`

**Core Purpose:** Defines the skeleton of an algorithm in a base class, letting subclasses override specific steps without changing the algorithm's structure.

#### Key Components:
- **Abstract Class:** Defines template method and common steps
- **Template Method:** Defines algorithm skeleton
- **Concrete Classes:** Implement specific steps

#### Real-World Use Cases:
- **Data Processing:** File parsing (CSV, JSON, XML, Excel)
- **Web Frameworks:** Request processing pipeline
- **Game Development:** Game loop (input, update, render)
- **Testing Frameworks:** Test execution (setup, execute, teardown)

---

### 6. Iterator Pattern 🔍

**Location:** `src/behavioural/iterator/`

**Core Purpose:** Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation.

#### Comprehensive Implementation: Notification Management System

This implementation demonstrates the Iterator pattern through a **real-world notification management system** that handles three different types of notifications, each using optimal data structures:

**A. Core Components:**

1. **Notification.java** - Basic notification entity with message content
2. **NotificationCollection.java** - Interface defining `createIterator()` method
3. **NotificationManager.java** - Central coordinator managing all notification types
4. **NotificationApp.java** - Interactive application with user input and demonstration

**B. Three Notification Types with Different Data Structures:**

1. **EmailNotification.java** 
   - Uses `ArrayList<Notification>` for indexed access
   - Implements `EmailNotificationIterator` for sequential traversal
   - Ideal for: Ordered email queues, batch processing

2. **SMSNotification.java**
   - Uses `ArrayDeque<Notification>` (Queue) for FIFO processing
   - Implements `SMSNotificationIterator` for queue-based iteration
   - Ideal for: Real-time SMS delivery, priority messaging

3. **PushNotification.java**
   - Uses `LinkedHashSet<Notification>` for unique, ordered notifications
   - Implements `PushNotificationIterator` for set-based iteration
   - Ideal for: Preventing duplicate push notifications, maintaining order

#### Key Benefits Demonstrated:

- **Uniform Interface:** Same iterator pattern across different data structures (List, Queue, Set)
- **Encapsulation:** Internal data structure completely hidden from NotificationManager
- **Flexibility:** Each notification type uses the most appropriate data structure
- **Polymorphism:** All notification types implement the same NotificationCollection interface
- **Scalability:** Easy to add new notification types without changing existing code
- **Data Structure Independence:** Client code works regardless of underlying collection type

#### Real-World Use Cases:
- **Notification Systems:** Mobile apps, web applications, desktop software
- **Message Queuing:** Email servers, SMS gateways, push notification services
- **Event Processing:** Real-time analytics, logging systems, audit trails
- **Social Media Platforms:** Feed updates, friend requests, activity notifications
- **E-commerce:** Order updates, shipping notifications, promotional messages
- **Enterprise Applications:** System alerts, user notifications, workflow updates
- **IoT Systems:** Device status updates, sensor alerts, system monitoring
- **Gaming Platforms:** Achievement notifications, friend activities, game invites

---

## 🏗️ **Pattern Relationships & Combinations**

| Pattern Combination | Use Case | Example |
|-------------------|----------|---------|
| **Command + Memento** | Undo/Redo with state restoration | Advanced text editors |
| **Observer + Strategy** | Dynamic algorithm notification | Real-time trading systems |
| **Template Method + Strategy** | Pluggable algorithm frameworks | Data processing pipelines |
| **Command + Observer** | Event-driven command execution | GUI event handling |
| **Iterator + Composite** | Traversing hierarchical structures | File system navigation |
| **Iterator + Visitor** | Processing collections with different operations | Data analysis pipelines |

## 🎯 **Choosing the Right Pattern**

**Ask These Questions:**

1. **Need to undo operations?** → **Command Pattern**
2. **Need to save/restore state?** → **Memento Pattern**  
3. **Multiple objects need notifications?** → **Observer Pattern**
4. **Multiple algorithms for same task?** → **Strategy Pattern**
5. **Common algorithm with variations?** → **Template Method Pattern**
6. **Need to access aggregate elements sequentially?** → **Iterator Pattern**

## 🚀 **Real-World Industry Applications**

#### **Enterprise Applications:**
- **Banking Systems:** Command (transactions), Memento (rollback), Observer (account notifications), Iterator (transaction history)
- **E-commerce:** Strategy (payment methods), Observer (inventory updates), Template Method (order processing), Iterator (product catalogs)
- **Content Management:** Command (content operations), Memento (version control), Template Method (publishing workflow), Iterator (content browsing)

#### **Game Development:**
- **Game Engines:** Command (input handling), Memento (save states), Observer (event systems), Iterator (game object collections)
- **Mobile Games:** Strategy (difficulty levels), Template Method (game loops), Iterator (inventory systems)

#### **Web Development:**
- **Frameworks:** Template Method (request lifecycle), Observer (event listeners), Strategy (routing), Iterator (data pagination)
- **Frontend:** Observer (reactive programming), Command (user actions), Iterator (component rendering)

## 🚀 How to Run

### Prerequisites
- Java 8 or higher
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Running Examples

Each pattern has its own main class for demonstration:

```bash
# Command Pattern
java behavioural.command.WithCommondPattern

# Iterator Pattern - Notification Management System
java behavioural.iterator.notificationmanagement.NotificationApp

# Memento Pattern
java behavioural.memento.texteditor.TextEditorMain
java behavioural.memento.graphiceditor.GraphicEditorMain
java behavioural.memento.tnxrollback.Solution

# Observer Pattern
java behavioural.observer.weather.ObserverPatternExample

# Strategy Pattern
java behavioural.strategy.payment.StrategyPattern

# Template Method Pattern
java behavioural.template.WithTemplatePattern
```

#### Interactive Examples:

**Iterator Pattern - Notification Management Demo:**
```bash
java behavioural.iterator.notificationmanagement.NotificationApp
# Interactive demo that allows you to:
# 1. Add Email notifications (stored in ArrayList)
# 2. Add SMS notifications (stored in ArrayDeque) 
# 3. Add Push notifications (stored in LinkedHashSet)
# 4. Display all notifications using uniform iterator interface
# 5. See how different data structures are handled transparently
# 
# Example interaction:
# Enter Email notification: Welcome to our platform!
# Enter SMS notification: Your verification code: 123456  
# Enter Push notification: New message received
# Enter Email notification: Account updated successfully
# Enter SMS notification: Payment confirmation: $50.00
# Enter Push notification: Friend request from John
# 
# The program demonstrates:
# - Uniform iteration across ArrayList, ArrayDeque, LinkedHashSet
# - Encapsulation of different data structures
# - Real-world notification management scenario
```

**Graphic Editor Memento Demo:**
```bash
java behavioural.memento.graphiceditor.GraphicEditorMain
# Input format: shapeType x y color size
# Example inputs:
# circle 10 20 red 5
# rectangle 30 40 blue 10  
# triangle 50 60 green 15
# The program will undo the last shape and show the restored state
```

## 🔍 Pattern Comparison

Each pattern includes "WithoutPattern" examples showing:
- **Problems** without using the pattern
- **Code duplication** and tight coupling issues
- **Maintenance difficulties** in traditional approaches

## 🛠️ Key Benefits of Design Patterns

1. **Reusability:** Proven solutions to common problems
2. **Maintainability:** Easier to modify and extend code
3. **Communication:** Common vocabulary for developers
4. **Best Practices:** Industry-standard approaches
5. **Flexibility:** Adaptable to changing requirements

## 📚 Learning Path

1. **Start with Strategy Pattern** - Easiest to understand
2. **Move to Observer Pattern** - Common in GUI applications
3. **Learn Command Pattern** - Powerful for undo/redo systems
4. **Study Template Method** - Great for framework design
5. **Master Memento Pattern** - Essential for state management
6. **Understand Iterator Pattern** - Fundamental for collection traversal

## 🎓 When to Use Each Pattern

| Pattern | Use When | Avoid When |
|---------|----------|------------|
| **Command** | Need undo/redo, queuing, logging | Simple direct method calls suffice |
| **Iterator** | Need to traverse collections sequentially | Collection is simple, direct access sufficient |
| **Memento** | Need state restoration, checkpoints | Memory constraints, simple state |
| **Observer** | One-to-many notifications needed | Few observers, tight coupling acceptable |
| **Strategy** | Multiple algorithms for same task | Only one algorithm, no runtime switching |
| **Template Method** | Common algorithm structure with variations | Completely different algorithms |

## 🔧 Extension Ideas

- Add **Undo functionality** to Command Pattern
- Implement **Composite Pattern** for hierarchical structures
- Add **Factory Pattern** for object creation
- Implement **Decorator Pattern** for feature enhancement
- Create **Facade Pattern** for simplified interfaces
- Add **Bidirectional Iterator** to Iterator Pattern
- Implement **External Iterator** variations
- Add **Filtering Iterator** to Notification System
- Implement **Priority-based Iterator** for notifications
- Create **Composite Iterator** for hierarchical notification structures
- Add **Concurrent Iterator** for thread-safe notification processing

## 📖 Additional Resources

- [Design Patterns: Elements of Reusable Object-Oriented Software](https://en.wikipedia.org/wiki/Design_Patterns) - Gang of Four
- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)

### 📊 UML Relationship Legend

```
Relationships:
─────▶  Association (uses)
◆─────▶ Composition (has-a, strong)
◇─────▶ Aggregation (has-a, weak)  
─────△  Inheritance (is-a)
- - -△  Implementation (realizes)
```

---

**Happy Coding! 🚀**

*This project demonstrates practical implementations of fundamental behavioral design patterns that you'll encounter in real-world software development, with future expansion planned for creational design patterns.*
