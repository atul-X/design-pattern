# 🎯 LLD Manager Pattern - When to Use

## 📋 Table of Contents
- [Core Principle](#core-principle)
- [When to Create Manager](#when-to-create-manager)
- [When NOT to Create Manager](#when-not-to-create-manager)
- [Real Examples](#real-examples)
- [Decision Guide](#decision-guide)
- [Implementation Pattern](#implementation-pattern)
- [Common Mistakes](#common-mistakes)

---

## 🎯 Core Principle

### **✅ Fundamental Rule:**
> **"Create Manager when objects need to interact with each other"**

### **🎯 Manager = Object Interaction Coordinator**

#### **✅ WITH Manager (Objects Interact)**
```
User → Manager → ServiceA
              ↓
              → ServiceB
              ↓
              → ServiceC

Manager coordinates object interactions!
```

#### **❌ WITHOUT Manager (Objects Work Alone)**
```
User → ServiceA
User → ServiceB
User → ServiceC

Objects don't interact with each other
```

---

## 🎯 When to Create Manager

### **✅ Clear Indicators:**

#### **🎯 1. Multiple Services Working Together**
```java
// ✅ Create Manager when you have 3+ services
InventoryService + PaymentService + ChangeService + NotificationService
→ ✅ VendingMachineManager

SpotService + PaymentService + PricingService + TicketService
→ ✅ ParkingLotManager

BookingService + PaymentService + RoomService + NotificationService
→ ✅ HotelManager
```

#### **🎯 2. Complex Workflows**
```java
// ✅ Complex workflow needs coordination
public PurchaseResult purchaseItem(String productId, double payment) {
    // Step 1: Check inventory
    if (!inventoryService.isAvailable(productId)) {
        return PurchaseResult.outOfStock();
    }
    
    // Step 2: Process payment
    if (!paymentService.processPayment(payment)) {
        return PurchaseResult.paymentFailed();
    }
    
    // Step 3: Dispense item
    inventoryService.dispenseItem(productId);
    
    // Step 4: Calculate change
    Map<Coin, Integer> change = changeService.calculateChange(payment);
    
    // ✅ Multiple coordinated steps = need Manager
    return PurchaseResult.success(productId, change);
}
```

#### **🎯 3. Common Operations Need Coordination**
```java
// ✅ Frequent operation that uses multiple services
public Ticket parkVehicle(Vehicle vehicle) {
    // Coordination needed
    Spot spot = spotService.findAvailableSpot(vehicle.getType());
    spotService.occupySpot(spot, vehicle);
    Ticket ticket = paymentService.generateTicket(spot);
    pricingService.setInitialRate(ticket);
    
    return ticket; // ✅ Coordinated workflow
}
```

#### **🎯 4. Services Need to Share Data**
```java
// ✅ Services need to communicate
public class OrderManager {
    public OrderResult processOrder(Order order) {
        // Service A validates
        validationService.validateOrder(order);
        
        // Service B processes payment (needs validation result)
        Payment payment = paymentService.processPayment(order);
        
        // Service C updates inventory (needs payment result)
        inventoryService.updateStock(order);
        
        // Service D sends notification (needs order status)
        notificationService.sendConfirmation(order);
        
        // ✅ Services interact through Manager
    }
}
```

---

## ❌ When NOT to Create Manager

### **❌ Clear Indicators:**

#### **🎯 1. Single Service Handles Everything**
```java
// ❌ Only one service - no Manager needed
public class LibraryService {
    public Book addBook(String title, String author) { ... }
    public Book borrowBook(int bookId, int memberId) { ... }
    public void returnBook(int bookId) { ... }
    public List<Book> searchBooks(String query) { ... }
}

// ❌ No LibraryManager needed - service is already simple
```

#### **🎯 2. Simple Operations**
```java
// ❌ Simple operations don't need coordination
public class UserService {
    public User createUser(String name, String email) {
        return new User(name, email); // Single step
    }
    
    public User getUser(int id) {
        return users.get(id); // Single step
    }
}

// ❌ No UserManager needed
```

#### **🎯 3. Independent Operations**
```java
// ❌ Operations don't depend on each other
public class ATMService {
    public Account validateCard(String cardNumber, String pin) { ... }
    public Cash withdraw(Account account, int amount) { ... }
    public void deposit(Account account, int amount) { ... }
}

// ❌ Each operation is independent - no coordination needed
```

#### **🎯 4. Client Needs Fine-Grained Control**
```java
// ❌ Client needs individual control
public class OrderService {
    public Order createOrder(OrderRequest request) { ... }
    public void addItem(Order order, Item item) { ... }
    public void removeItem(Order order, Item item) { ... }
    public void applyDiscount(Order order, Discount discount) { ... }
}

// ❌ Don't force facade if client needs individual calls
```

---

## 🎯 Real Examples

### **✅ Examples That NEED Manager**

#### **🎯 Vending Machine System**
```java
// Multiple services that must interact
public class InventoryService {
    public boolean isAvailable(String productId) { ... }
    public void dispenseItem(String productId) { ... }
}

public class PaymentService {
    public boolean processPayment(double amount) { ... }
    public void refund(double amount) { ... }
}

public class ChangeService {
    public Map<Coin, Integer> calculateChange(double amount) { ... }
}

// ✅ Manager coordinates interactions
public class VendingMachineManager {
    private InventoryService inventoryService;
    private PaymentService paymentService;
    private ChangeService changeService;
    
    public PurchaseResult purchaseItem(String productId, double paymentAmount) {
        // Interaction 1: Check inventory
        if (!inventoryService.isAvailable(productId)) {
            return PurchaseResult.outOfStock();
        }
        
        // Interaction 2: Process payment
        if (!paymentService.processPayment(paymentAmount)) {
            return PurchaseResult.paymentFailed();
        }
        
        // Interaction 3: Dispense item
        inventoryService.dispenseItem(productId);
        
        // Interaction 4: Calculate change
        Map<Coin, Integer> change = changeService.calculateChange(paymentAmount);
        
        return PurchaseResult.success(productId, change);
    }
}
```

#### **🎯 Parking Lot System**
```java
// Services that need coordination
public class SpotService {
    public Spot findAvailableSpot(VehicleType type) { ... }
    public void occupySpot(Spot spot, Vehicle vehicle) { ... }
    public void vacateSpot(Spot spot) { ... }
}

public class PaymentService {
    public Ticket generateTicket(Spot spot) { ... }
    public Payment processPayment(Ticket ticket) { ... }
}

public class PricingService {
    public double calculatePrice(Ticket ticket) { ... }
}

// ✅ Manager coordinates the workflow
public class ParkingLotManager {
    private SpotService spotService;
    private PaymentService paymentService;
    private PricingService pricingService;
    
    public Ticket parkVehicle(Vehicle vehicle) {
        // Interaction 1: Find spot
        Spot spot = spotService.findAvailableSpot(vehicle.getType());
        
        // Interaction 2: Occupy spot
        spotService.occupySpot(spot, vehicle);
        
        // Interaction 3: Generate ticket
        Ticket ticket = paymentService.generateTicket(spot);
        
        return ticket;
    }
    
    public Payment unparkVehicle(Ticket ticket) {
        // Interaction 1: Calculate price
        double price = pricingService.calculatePrice(ticket);
        
        // Interaction 2: Process payment
        Payment payment = paymentService.processPayment(price);
        
        // Interaction 3: Vacate spot
        spotService.vacateSpot(ticket.getSpot());
        
        return payment;
    }
}
```

#### **🎯 Hotel Booking System**
```java
// Complex booking workflow
public class BookingService {
    public Booking createBooking(Room room, Guest guest) { ... }
    public void cancelBooking(Booking booking) { ... }
}

public class PaymentService {
    public Payment processPayment(Booking booking, PaymentInfo info) { ... }
    public Refund refundPayment(Payment payment) { ... }
}

public class RoomService {
    public Room findAvailableRoom(RoomType type, DateRange dates) { ... }
    public void reserveRoom(Room room, Booking booking) { ... }
}

public class NotificationService {
    public void sendBookingConfirmation(Booking booking) { ... }
    public void sendPaymentConfirmation(Payment payment) { ... }
}

// ✅ Manager handles complex booking workflow
public class HotelManager {
    private BookingService bookingService;
    private PaymentService paymentService;
    private RoomService roomService;
    private NotificationService notificationService;
    
    public BookingResult bookRoom(RoomType type, DateRange dates, Guest guest, PaymentInfo payment) {
        // Interaction 1: Find available room
        Room room = roomService.findAvailableRoom(type, dates);
        if (room == null) {
            return BookingResult.noRoomsAvailable();
        }
        
        // Interaction 2: Create booking
        Booking booking = bookingService.createBooking(room, guest);
        
        // Interaction 3: Process payment
        Payment paymentResult = paymentService.processPayment(booking, payment);
        if (!paymentResult.isSuccess()) {
            bookingService.cancelBooking(booking);
            return BookingResult.paymentFailed();
        }
        
        // Interaction 4: Reserve room
        roomService.reserveRoom(room, booking);
        
        // Interaction 5: Send notifications
        notificationService.sendBookingConfirmation(booking);
        notificationService.sendPaymentConfirmation(paymentResult);
        
        return BookingResult.success(booking, room, paymentResult);
    }
}
```

### **❌ Examples That DON'T Need Manager**

#### **🎯 Library Management**
```java
// Single service handles everything
public class LibraryService {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();
    private Map<Integer, Loan> loans = new HashMap<>();
    
    public Book addBook(String title, String author, String isbn) {
        Book book = new Book(title, author, isbn);
        books.put(book.getId(), book);
        return book;
    }
    
    public Loan borrowBook(int bookId, int memberId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);
        
        if (book == null || member == null || !book.isAvailable()) {
            throw new IllegalArgumentException("Cannot borrow book");
        }
        
        Loan loan = new Loan(book, member);
        book.setAvailable(false);
        loans.put(loan.getId(), loan);
        
        return loan;
    }
    
    public void returnBook(int loanId) {
        Loan loan = loans.remove(loanId);
        if (loan != null) {
            loan.getBook().setAvailable(true);
        }
    }
    
    public List<Book> searchBooks(String query) {
        return books.values().stream()
            .filter(book -> book.getTitle().contains(query) || 
                           book.getAuthor().contains(query))
            .collect(Collectors.toList());
    }
}

// ❌ No LibraryManager needed - service is already simple
```

#### **🎯 ATM System**
```java
// Single service handles all ATM operations
public class ATMService {
    private Map<String, Account> accounts = new HashMap<>();
    private Map<Integer, Integer> cashDenominations = new HashMap<>();
    
    public Account validateCard(String cardNumber, String pin) {
        Account account = accounts.get(cardNumber);
        if (account != null && account.validatePin(pin)) {
            return account;
        }
        return null;
    }
    
    public Cash withdraw(Account account, int amount) {
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException();
        }
        
        if (!hasEnoughCash(amount)) {
            throw new ATMOutOfCashException();
        }
        
        account.withdraw(amount);
        dispenseCash(amount);
        
        return new Cash(amount);
    }
    
    public void deposit(Account account, int amount) {
        account.deposit(amount);
        updateCashDenominations(amount);
    }
    
    public Balance checkBalance(Account account) {
        return new Balance(account.getBalance());
    }
    
    // Private helper methods
    private boolean hasEnoughCash(int amount) { ... }
    private void dispenseCash(int amount) { ... }
    private void updateCashDenominations(int amount) { ... }
}

// ❌ No ATMManager needed - service handles everything
```

#### **🎯 Todo List Management**
```java
// Simple CRUD operations - no coordination needed
public class TodoService {
    private Map<Integer, Todo> todos = new HashMap<>();
    private AtomicInteger idGenerator = new AtomicInteger(1);
    
    public Todo createTodo(String title, String description) {
        Todo todo = new Todo(idGenerator.getAndIncrement(), title, description);
        todos.put(todo.getId(), todo);
        return todo;
    }
    
    public Todo getTodo(int id) {
        return todos.get(id);
    }
    
    public Todo updateTodo(int id, String title, String description) {
        Todo todo = todos.get(id);
        if (todo != null) {
            todo.setTitle(title);
            todo.setDescription(description);
        }
        return todo;
    }
    
    public void deleteTodo(int id) {
        todos.remove(id);
    }
    
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos.values());
    }
    
    public List<Todo> getCompletedTodos() {
        return todos.values().stream()
            .filter(Todo::isCompleted)
            .collect(Collectors.toList());
    }
}

// ❌ No TodoManager needed - simple operations only
```

---

## 🎯 Decision Guide

### **✅ Quick Decision Flow**

#### **🎯 Step 1: Count Services**
```
How many services/classes are involved?
├─ 1 service → ❌ No Manager needed
├─ 2 services → 🤔 Maybe Manager
└─ 3+ services → ✅ Definitely Manager
```

#### **🎯 Step 2: Check Workflow Complexity**
```
How many steps in the operation?
├─ 1-2 steps → ❌ No Manager needed
├─ 3+ steps → ✅ Consider Manager
└─ Complex coordination → ✅ Definitely Manager
```

#### **🎯 Step 3: Check Interaction Needs**
```
Do services need to share data/state?
├─ No → ❌ No Manager needed
├─ Yes → ✅ Manager needed
└─ Complex data flow → ✅ Definitely Manager
```

### **✅ Decision Matrix**

| Scenario | Services | Steps | Interactions | Manager Needed |
|----------|----------|-------|--------------|----------------|
| Vending Machine | 4+ | 4+ | Yes | ✅ YES |
| Parking Lot | 3+ | 3+ | Yes | ✅ YES |
| Hotel Booking | 4+ | 5+ | Yes | ✅ YES |
| Library | 1 | 1-2 | No | ❌ NO |
| ATM | 1 | 1-2 | No | ❌ NO |
| Todo List | 1 | 1 | No | ❌ NO |
| E-commerce Checkout | 5+ | 6+ | Yes | ✅ YES |
| Social Media Post | 3+ | 4+ | Yes | ✅ YES |

---

## 🎯 Implementation Pattern

### **✅ Standard Manager Structure**

#### **🎯 1. Manager Class**
```java
public class SystemManager {
    // Services that need coordination
    private ServiceA serviceA;
    private ServiceB serviceB;
    private ServiceC serviceC;
    
    // Constructor with dependency injection
    public SystemManager(ServiceA serviceA, ServiceB serviceB, ServiceC serviceC) {
        this.serviceA = serviceA;
        this.serviceB = serviceB;
        this.serviceC = serviceC;
    }
    
    // Coordinated operations
    public Result performComplexOperation(Input input) {
        // Step 1: Use ServiceA
        IntermediateResult result1 = serviceA.process(input);
        
        // Step 2: Use ServiceB (with result from A)
        IntermediateResult result2 = serviceB.process(result1);
        
        // Step 3: Use ServiceC (with result from B)
        FinalResult result = serviceC.process(result2);
        
        return result;
    }
}
```

#### **🎯 2. Service Classes**
```java
// Individual services - single responsibility
public class ServiceA {
    public IntermediateResult process(Input input) {
        // Single responsibility processing
        return new IntermediateResult();
    }
}

public class ServiceB {
    public IntermediateResult process(IntermediateResult input) {
        // Single responsibility processing
        return new IntermediateResult();
    }
}

public class ServiceC {
    public FinalResult process(IntermediateResult input) {
        // Single responsibility processing
        return new FinalResult();
    }
}
```

#### **🎯 3. Client Usage**
```java
public class Client {
    public static void main(String[] args) {
        // Initialize services
        ServiceA serviceA = new ServiceA();
        ServiceB serviceB = new ServiceB();
        ServiceC serviceC = new ServiceC();
        
        // Create manager
        SystemManager manager = new SystemManager(serviceA, serviceB, serviceC);
        
        // Use coordinated operation
        Result result = manager.performComplexOperation(new Input());
        
        System.out.println("Result: " + result);
    }
}
```

---

## ❌ Common Mistakes

### **❌ Mistake 1: Manager as Object Factory**

#### **❌ WRONG: Manager Creates Objects**
```java
public class RestaurantManager {
    public void simulateSystem() {
        // ❌ Don't create objects inside manager
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizza Palace");
        restaurant.setPrice(100);
        
        Rating rating = new Rating();
        rating.setScore(4.5);
        
        // This should be in demo/test class, not manager
    }
}
```

#### **✅ CORRECT: Manager Coordinates Services**
```java
public class RestaurantManager {
    private RestaurantService restaurantService;
    private RatingService ratingService;
    
    // ✅ Manager coordinates, doesn't create
    public Restaurant addRestaurantWithRating(String name, int price, int rating) {
        Restaurant restaurant = restaurantService.addRestaurant(name, price);
        ratingService.addRating(restaurant.getId(), rating);
        return restaurant;
    }
}
```

### **❌ Mistake 2: Manager for Simple Operations**

#### **❌ WRONG: Unnecessary Manager**
```java
// ❌ Don't create manager for simple operations
public class LibraryManager {
    private LibraryService libraryService;
    
    public Book addBook(String title, String author) {
        // ❌ Just forwarding to service - no coordination needed
        return libraryService.addBook(title, author);
    }
    
    public Book borrowBook(int bookId, int memberId) {
        // ❌ Just forwarding - no coordination needed
        return libraryService.borrowBook(bookId, memberId);
    }
}

// ✅ Just use the service directly
LibraryService libraryService = new LibraryService();
Book book = libraryService.addBook("Title", "Author");
```

### **❌ Mistake 3: Manager with Single Responsibility**

#### **❌ WRONG: Manager Does One Thing**
```java
// ❌ Manager that only does one thing - should be a service
public class UserManager {
    public User createUser(String name, String email) {
        return new User(name, email); // Single operation
    }
}

// ✅ Should be a service, not a manager
public class UserService {
    public User createUser(String name, String email) {
        return new User(name, email);
    }
}
```

### **❌ Mistake 4: Manager Hides Too Much**

#### **❌ WRONG: Manager Over-Abstracts**
```java
// ❌ Manager hides everything - client loses control
public class OrderManager {
    public void placeOrder(OrderRequest request) {
        // Client has no control over individual steps
        // Can't customize validation, payment, or notification
    }
}

// ✅ Provide both options
public class OrderManager {
    // Simple version for common cases
    public Order placeSimpleOrder(OrderRequest request) { ... }
    
    // Detailed version for complex cases
    public Order placeDetailedOrder(OrderRequest request, 
                                   ValidationStrategy validation,
                                   PaymentStrategy payment,
                                   NotificationStrategy notification) { ... }
}
```

---

## 🎯 Best Practices Summary

### **✅ DO Create Manager When:**

1. **3+ services need coordination**
2. **Complex workflow with multiple steps**
3. **Services need to share data/state**
4. **Common operation that uses multiple services**
5. **Want to simplify complex API for client**

### **❌ DON'T Create Manager When:**

1. **Only 1-2 services involved**
2. **Simple operations (1-2 steps)**
3. **Services work independently**
4. **Client needs fine-grained control**
5. **Operation is straightforward**

### **🎯 Key Principle:**

> **"Manager = Facade for Coordinating Object Interactions"**

### **🎯 Quick Test:**

```
Ask yourself:
1. Do objects need to interact with each other? YES → Manager
2. Is the workflow complex? YES → Manager
3. Are multiple services involved? YES → Manager
4. Is it a simple operation? NO → No Manager
```

**Remember: Manager coordinates object interactions - that's the key insight!** 🎯
