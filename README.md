# Design Patterns Implementation in Java

This project demonstrates the implementation of **6 fundamental design patterns** in Java, showcasing both the problems they solve and their practical applications. Each pattern includes comparison implementations to highlight the benefits of using design patterns.

## 📁 Project Structure

```
src/
├── command/           # Command Pattern Implementation
├── memento/           # Memento Pattern Implementation
├── observer/          # Observer Pattern Implementation  
├── strategy/          # Strategy Pattern Implementation
├── template/          # Template Method Pattern Implementation
├── iterator/          # Iterator Pattern Implementation
└── ...                # Other supporting classes
```

## 🔍 **What Are Design Patterns?**

Design patterns are **reusable solutions** to commonly occurring problems in software design. They represent best practices evolved over time by experienced developers and provide a **common vocabulary** for discussing design solutions.

## 🎯 Design Patterns Implemented

### 1. Command Pattern 🎮

**Location:** `src/command/`

**Core Purpose:** Encapsulates a request as an object, allowing you to parameterize clients with different requests, queue operations, and support undo functionality.

#### UML Class Diagram

```
┌─────────────────┐
│   <<interface>>  │
│     Command     │
├─────────────────┤
│ + execute()     │
└─────────────────┘
         △
         │
    ┌────┴────┬────────────┬──────────────┬─────────────────┐
    │         │            │              │                 │
┌───▽───┐ ┌──▽────┐ ┌─────▽──────┐ ┌────▽─────────────┐
│BoldCmd│ │ItalicCmd│ │UnderlineCmd│ │ChangeColorCmd    │
├───────┤ ├─────────┤ ├────────────┤ ├──────────────────┤
│-editor│ │-editor  │ │-editor     │ │-editor           │
├───────┤ ├─────────┤ ├────────────┤ ├──────────────────┤
│+exec()│ │+exec()  │ │+exec()     │ │+exec()           │
└───┬───┘ └────┬────┘ └─────┬──────┘ └────┬─────────────┘
    │          │            │             │
    └──────────┼────────────┼─────────────┘
               │            │
         ┌─────▽────────────▽─────┐
         │    TextEditorII        │
         ├────────────────────────┤
         │ + boldText()           │
         │ + italicizeText()      │
         │ + underlineText()      │
         │ + changeColor()        │
         └────────────────────────┘

┌─────────────────┐      ┌─────────────────┐
│     Button      │────▶ │   <<interface>> │
├─────────────────┤      │     Command     │
│ - command       │      └─────────────────┘
├─────────────────┤
│ + setCommand()  │
│ + click()       │
└─────────────────┘
```

#### Key Components:
- **Command Interface:** `Command.java` - Defines the execute() method
- **Concrete Commands:** `BoldCommand`, `ItalicCommand`, `UnderlineCommand`, `ChangeColorCommand`
- **Receiver:** `TextEditorII` - Performs the actual work
- **Invoker:** `Button` - Triggers command execution

#### Key Benefits:
- **Undo/Redo Operations:** Store command history for reversible actions
- **Macro Recording:** Combine multiple commands into composite operations
- **Queuing & Logging:** Queue commands for later execution or log for audit trails
- **Remote Execution:** Execute commands on remote systems
- **Decoupling:** Invoker doesn't need to know about receiver implementation
- **Flexibility:** Commands can be changed at runtime
- **Extensibility:** Easy to add new commands without modifying existing code

#### Real-World Use Cases:
- **GUI Applications:** Button clicks, menu selections
- **Remote Controls:** TV, AC, Smart Home devices
- **Macro Recording:** Recording and replaying user actions
- **Undo/Redo Operations:** Text editors, image editors
- **Queuing Systems:** Job scheduling, task management
- **Transaction Processing:** Banking systems, database operations
- **Wizard Applications:** Step-by-step operations
- **Plugin Architectures:** Dynamic functionality addition

#### When to Use:
- ✅ GUI applications with buttons and menu items
- ✅ Remote controls and IoT device management
- ✅ Transaction processing systems
- ✅ Wizard-style applications with step-by-step operations
- ✅ Plugin architectures where functionality is added dynamically

#### Avoid When:
- ❌ Simple direct method calls are sufficient
- ❌ No need for undo/redo or queuing functionality
- ❌ Performance is critical and object creation overhead matters

#### Example Usage:
```java
TextEditorII textEditor = new TextEditorII();
Button button = new Button();

// Set different commands dynamically
button.setCommand(new BoldCommand(textEditor));
button.click(); // Executes bold operation

button.setCommand(new ItalicCommand(textEditor));
button.click(); // Executes italic operation
```

---

### 2. Memento Pattern 💾

**Location:** `src/memento/`

**Core Purpose:** Captures and externalizes an object's internal state without violating encapsulation, allowing the object to be restored to this state later.

#### UML Class Diagram

```
┌─────────────────────┐    ┌─────────────────────┐    ┌─────────────────────┐
│    TextEditor       │    │   EditorMemento     │    │     CareTaker       │
│   (Originator)      │    │    (Memento)        │    │   (Caretaker)       │
├─────────────────────┤    ├─────────────────────┤    ├─────────────────────┤
│ - content: String   │    │ - content: String   │    │ - history: Stack    │
├─────────────────────┤    ├─────────────────────┤    ├─────────────────────┤
│ + write(content)    │───▶│ + getContent()      │◀───│ + saveState()       │
│ + save(): Memento   │    │                     │    │ + undo()            │
│ + restore(memento)  │    │                     │    │                     │
│ + getContent()      │    │                     │    │                     │
└─────────────────────┘    └─────────────────────┘    └─────────────────────┘

┌─────────────────────┐    ┌─────────────────────┐
│   GraphicEditor     │    │GraphicEditorMemento │
│   (Originator)      │    │    (Memento)        │
├─────────────────────┤    ├─────────────────────┤
│ - shapeType: String │    │ - shapeType: String │
│ - x: int            │    │ - x: int            │
│ - y: int            │    │ - y: int            │
│ - color: String     │    │ - color: String     │
│ - size: int         │    │ - size: int         │
├─────────────────────┤    ├─────────────────────┤
│ + setShape()        │───▶│ + getShapeType()    │
│ + save(): Memento   │    │ + getX()            │
│ + restore(memento)  │    │ + getY()            │
│ + getShape()        │    │ + getColor()        │
└─────────────────────┘    │ + getSize()         │
                           └─────────────────────┘
```

#### Three Implementations:

**A. Text Editor with Undo (`texteditor/`)**
- **Originator:** `TextEditor` - Creates and restores mementos
- **Memento:** `EditorMemento` - Stores editor state
- **Caretaker:** `CareTaker` - Manages memento history

**B. Graphic Editor with Shape History (`graphiceditor/`)**
- **Originator:** `GraphicEditor` - Manages shape properties (type, position, color, size)
- **Memento:** `EditorMemento` - Stores complete shape state
- **Caretaker:** `Caretaker` - Manages shape history for undo operations
- **Application:** `GraphicEditorApplication` - Interactive demo with user input

**C. Transaction Rollback System (`tnxrollback/`)**
- **Database-like Operations:** Save, update, delete with rollback capability
- **Transaction Management:** Begin, commit, rollback operations

#### Key Benefits:
- **State Preservation:** Save object state at specific points in time
- **Rollback Capability:** Restore to previous states when needed
- **Encapsulation Maintenance:** Access internal state without exposing it
- **History Management:** Maintain multiple snapshots for complex undo systems

#### Real-World Use Cases:
- **Text Editors:** Undo/Redo functionality (MS Word, VS Code)
- **Graphic Design Software:** Shape manipulation history (Adobe Illustrator, Figma)
- **Database Systems:** Transaction rollback, savepoints
- **Game Development:** Save/load game states, checkpoints
- **Version Control:** Git commits, branching
- **Configuration Management:** System restore points

#### When to Use:
- ✅ Text editors with undo/redo functionality
- ✅ Database transaction systems with rollback
- ✅ Game save/load mechanisms
- ✅ Configuration management with restore points
- ✅ Version control systems
- ✅ Graphic editors with shape manipulation history

#### Avoid When:
- ❌ Memory constraints are severe (snapshots consume memory)
- ❌ Object state is simple and easily recreated
- ❌ State changes are infrequent

#### Example Usage:
```java
// Text Editor Example
TextEditor editor = new TextEditor();
CareTaker careTaker = new CareTaker();

editor.write("Hello World");
careTaker.saveState(editor);  // Save current state

editor.write("Modified Text");
careTaker.undo(editor);       // Restore to "Hello World"

// Graphic Editor Example
GraphicEditor graphicEditor = new GraphicEditor();
Caretaker caretaker = new CareTaker();

graphicEditor.setShape("circle", 10, 20, "red", 5);
caretaker.saveState(graphicEditor);  // Save shape state

graphicEditor.setShape("rectangle", 30, 40, "blue", 10);
caretaker.undo(graphicEditor);       // Restore to circle
```

---

### 3. Observer Pattern 👁️

**Location:** `src/observer/weather/`

**Core Purpose:** Defines a one-to-many dependency between objects so that when one object changes state, all dependents are notified automatically.

#### UML Class Diagram

```
┌─────────────────────┐                    ┌─────────────────────┐
│   <<interface>>     │                    │   <<interface>>     │
│      Subject        │                    │     Observer        │
├─────────────────────┤                    ├─────────────────────┤
│ + attach(observer)  │                    │ + update(temp)      │
│ + detach(observer)  │                    └─────────────────────┘
│ + notifyObservers() │                             △
└─────────────────────┘                             │
         △                                          │
         │                               ┌─────────┴─────────┐
┌────────▽─────────┐                     │                   │
│     Weather      │                ┌────▽────┐      ┌─────▽──────┐
├──────────────────┤                │Display  │      │Mobile      │
│ - temperature    │                │Devices  │      │Device      │
│ - observers[]    │                ├─────────┤      ├────────────┤
├──────────────────┤                │ - name  │      │ - name     │
│ + attach()       │◆──────────────▶├─────────┤      ├────────────┤
│ + detach()       │                │ + update│      │ + update() │
│ + notify()       │                └─────────┘      └────────────┘
│ + setTemp()      │
└──────────────────┘
```

#### Key Components:
- **Subject Interface:** `Subject` - Attach, detach, notify observers
- **Observer Interface:** `Observer` - Update method for notifications
- **Concrete Subject:** `Weather` - Maintains state and observer list
- **Concrete Observers:** `DisplayDevices`, `MobileDevice` - React to state changes

#### Key Benefits:
- **Loose Coupling:** Subject and observers are independent
- **Dynamic Relationships:** Add/remove observers at runtime
- **Broadcast Communication:** Notify multiple objects simultaneously
- **Event-Driven Architecture:** React to state changes automatically

#### Real-World Use Cases:
- **GUI Applications:** Model-View architectures (MVC, MVP, MVVM)
- **Event Systems:** DOM events, custom event handlers
- **Stock Market:** Price change notifications to multiple displays
- **Social Media:** Notification systems for followers
- **IoT Systems:** Sensor data broadcasting to multiple devices
- **News Feeds:** Publishing updates to subscribers

#### When to Use:
- ✅ Model-View architectures (MVC, MVP, MVVM)
- ✅ Event handling systems
- ✅ Real-time data feeds (stock prices, weather updates)
- ✅ Social media notification systems
- ✅ IoT sensor networks
- ✅ Publish-subscribe messaging systems

#### Avoid When:
- ❌ Only one or few observers exist
- ❌ Tight coupling between subject and observers is acceptable
- ❌ Simple callback mechanisms suffice

#### Example Usage:
```java
Weather weather = new Weather();
DisplayDevices lcd = new DisplayDevices("Samsung LCD");
MobileDevice phone = new MobileDevice("iPhone");

weather.attach(lcd);
weather.attach(phone);

weather.setTemperature(25.5f); // Both devices get notified
```

---

### 4. Strategy Pattern 🎯

**Location:** `src/strategy/payment/`

**Core Purpose:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable at runtime.

#### UML Class Diagram

```
┌─────────────────────┐      ┌─────────────────────┐
│  PaymentService     │────▶ │   <<interface>>     │
│    (Context)        │      │  PaymentStrategy    │
├─────────────────────┤      ├─────────────────────┤
│ - strategy          │      │ + processPayment()  │
├─────────────────────┤      └─────────────────────┘
│ + setStrategy()     │               △
│ + pay()             │               │
└─────────────────────┘      ┌────────┴────────┐
                             │                 │
                      ┌──────▽──────┐   ┌─────▽──────┐
                      │ CreditCard  │   │ DebitCard  │
                      ├─────────────┤   ├────────────┤
                      │ + process() │   │ + process()│
                      └─────────────┘   └────────────┘
```

#### Key Components:
- **Strategy Interface:** `PaymentStrategy` - Common interface for all algorithms
- **Concrete Strategies:** `CreditCard`, `DebitCard` - Different payment methods
- **Context:** `PaymentService` - Uses strategy to perform operations

#### Key Benefits:
- **Runtime Algorithm Selection:** Choose algorithms dynamically
- **Open/Closed Principle:** Add new algorithms without modifying existing code
- **Eliminate Conditionals:** Replace large if-else chains
- **Algorithm Isolation:** Test and maintain algorithms independently

#### Real-World Use Cases:
- **Payment Processing:** Credit card, PayPal, cryptocurrency, bank transfer
- **Sorting Algorithms:** QuickSort, MergeSort, BubbleSort selection
- **Compression:** ZIP, RAR, 7Z different compression algorithms
- **Navigation Apps:** Fastest route, shortest route, scenic route
- **Pricing Strategies:** Regular, premium, discount pricing
- **Authentication:** OAuth, LDAP, database authentication

#### When to Use:
- ✅ Multiple ways to perform the same task
- ✅ Payment processing with different methods
- ✅ Sorting algorithms selection based on data size
- ✅ Compression algorithms for different file types
- ✅ Navigation routing (fastest, shortest, scenic)
- ✅ Pricing strategies (regular, premium, discount)

#### Avoid When:
- ❌ Only one algorithm exists
- ❌ Algorithms rarely change
- ❌ Simple conditional logic is sufficient

#### Example Usage:
```java
PaymentService paymentService = new PaymentService();

// Use credit card payment
paymentService.setPaymentStrategy(new CreditCard());
paymentService.pay();

// Switch to debit card payment
paymentService.setPaymentStrategy(new DebitCard());
paymentService.pay();
```

---

### 5. Template Method Pattern 📋

**Location:** `src/template/`

**Core Purpose:** Defines the skeleton of an algorithm in a base class, letting subclasses override specific steps without changing the algorithm's structure.

#### UML Class Diagram

```
┌─────────────────────────┐
│    <<abstract>>         │
│      DataParser         │
├─────────────────────────┤
│ + parse() {final}       │  ← Template Method
│ # openFile()            │
│ # closeFile()           │
│ # parseData() {abstract}│  ← Hook Method
└─────────────────────────┘
            △
            │
    ┌───────┴───────┐
    │               │
┌───▽─────────────────┐    ┌──────────────┐
│   CSVParserII      │    │     JSONParser│
├─────────────────────┤    ├──────────────┤
│ + parseData()      │    │ + parseData() │
└─────────────────────┘    └──────────────┘

Algorithm Flow:
1. openFile()    ← Common
2. parseData()   ← Varies by subclass  
3. closeFile()   ← Common
```

#### Key Components:
- **Abstract Class:** `DataParser` - Defines template method and common steps
- **Template Method:** `parse()` - Defines algorithm skeleton
- **Concrete Classes:** `CSVParserII`, `JSONParserII` - Implement specific steps

#### Algorithm Steps:
1. **openFile()** - Common implementation
2. **parseData()** - Abstract method (varies by file type)
3. **closeFile()** - Common implementation

#### Key Benefits:
- **Code Reuse:** Share common algorithm structure
- **Controlled Extension:** Subclasses can only modify specific steps
- **Consistency:** Ensure algorithm flow remains consistent
- **Framework Design:** Create extensible frameworks

#### Real-World Use Cases:
- **Data Processing:** File parsing (CSV, JSON, XML, Excel)
- **Web Frameworks:** Request processing pipeline (authentication, validation, response)
- **Game Development:** Game loop (input, update, render)
- **Testing Frameworks:** Test execution (setup, execute, teardown)
- **Build Systems:** Compilation process (compile, link, package)
- **Report Generation:** Different report formats with common structure

#### When to Use:
- ✅ Multiple classes with similar algorithms but different implementations
- ✅ Framework development with customizable steps
- ✅ Data processing pipelines with common structure
- ✅ Testing frameworks (setup, execute, teardown)
- ✅ Web request processing (authenticate, validate, process, respond)
- ✅ Report generation with different formats

#### Avoid When:
- ❌ Algorithms are completely different
- ❌ No common structure exists between implementations
- ✅ Flexibility to change algorithm structure is needed

#### Example Usage:
```java
DataParser csvParser = new CSVParserII();
csvParser.parse(); // Uses template method

DataParser jsonParser = new JSONParserII();
jsonParser.parse(); // Same structure, different parsing logic
```

---

### 6. Iterator Pattern 🔍

**Location:** `src/iterator/`

**Core Purpose:** Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation.

#### UML Class Diagram

```
┌─────────────────────┐
│   <<interface>>     │
│      Iterator<T>    │
├─────────────────────┤
│ + hasNext(): boolean│
│ + next(): T         │
└─────────────────────┘
         △
         │
    ┌────┴────┐
    │         │
┌───▽─────────────────┐    ┌──────────────┐
│   BookIterator      │    │     Book     │
├─────────────────────┤    ├──────────────┤
│ - books: List<Book> │◆──▷│ - title: String│
│ - position: int     │    │ + getTitle() │
├─────────────────────┤    │ + toString() │
│ + hasNext(): boolean│    └──────────────┘
│ + next(): Book      │
└─────────────────────┘
         △
         │
┌────────┴─────────────┐
│  BookCollectionV2    │
├──────────────────────┤
│ - books: List<Book>  │
├──────────────────────┤
│ + addBook(Book)      │
│ + getBooks()         │
│ + createIterator()   │
└──────────────────────┘
```

#### Key Components:
- **Iterator Interface:** `Iterator<T>` - Defines iteration methods (hasNext(), next())
- **Concrete Iterator:** `BookIterator` - Implements iteration over book collection
- **Aggregate:** `BookCollectionV2` - Manages book collection and provides iterator
- **Element:** `Book` - Individual items in the collection

#### Key Benefits:
- **Encapsulation:** Internal collection structure remains hidden from clients
- **Uniform Interface:** Same iteration interface for different collection types
- **Flexibility:** Multiple iterators can traverse the same collection simultaneously
- **Decoupling:** Iterator logic is separate from collection implementation
- **Safety:** Prevents direct manipulation of collection internals

#### Real-World Use Cases:
- **Database Query Results:** Iterate over large result sets without loading all data
- **File System Navigation:** Traverse directories and files
- **Social Media Feeds:** Iterate over posts, comments, and user content
- **Game Development:** Iterate over game objects, inventory items, player lists
- **Data Processing:** Process large datasets element by element
- **Web Scraping:** Iterate over web pages and extracted data

#### When to Use:
- ✅ Need to traverse a collection without exposing its internal structure
- ✅ Want to provide multiple ways to traverse the same collection
- ✅ Need to iterate over complex data structures
- ✅ Want to decouple iteration logic from collection implementation
- ✅ Working with large collections where memory efficiency matters

#### Avoid When:
- ❌ Collection is simple and direct access is sufficient
- ❌ Only need to access specific elements (not sequential access)
- ❌ Performance overhead of iterator pattern is not acceptable
- ❌ Collection structure is unlikely to change

#### Example Usage:
```java
// Create book collection
BookCollectionV2 bookCollection = new BookCollectionV2();
bookCollection.addBook(new Book("Java Design Patterns"));
bookCollection.addBook(new Book("Clean Code"));
bookCollection.addBook(new Book("Effective Java"));

// Iterate using custom iterator
Iterator<Book> iterator = bookCollection.createIterator();
while (iterator.hasNext()) {
    Book book = iterator.next();
    System.out.println("Reading: " + book.getTitle());
}

// Benefits: Client doesn't know about internal List structure
// If BookCollectionV2 changes from List to Array, client code remains unchanged
```

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

## 🔧 Extension Ideas

- Add **Undo functionality** to Command Pattern
- Implement **Composite Pattern** for hierarchical structures
- Add **Factory Pattern** for object creation
- Implement **Decorator Pattern** for feature enhancement
- Create **Facade Pattern** for simplified interfaces
- Add **Bidirectional Iterator** to Iterator Pattern
- Implement **External Iterator** variations

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

*This project demonstrates practical implementations of 6 fundamental design patterns that you'll encounter in real-world software development.*
