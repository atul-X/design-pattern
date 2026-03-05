# 🎯 Command Pattern - Real-World Implementation Guide

## 📋 Table of Contents
- [Real-World Scenario](#real-world-scenario)
- [System Architecture](#system-architecture)
- [Step-by-Step Implementation](#step-by-step-implementation)
- [Production Considerations](#production-considerations)
- [Scaling Strategies](#scaling-strategies)
- [Testing Approach](#testing-approach)
- [Deployment Guide](#deployment-guide)

---

## 🌍 Real-World Scenario

### **E-Commerce Order Management System**

Let's implement a Command pattern for a real e-commerce system like Amazon or Flipkart where:
- Customers can place orders
- Orders can be cancelled
- Inventory must be updated
- Payments need to be processed
- Notifications must be sent
- Every operation should be undoable

### **Business Requirements**
```
📦 Order Management:
✓ Place Order (inventory check, payment, notification)
✓ Cancel Order (refund, restore inventory, notification)
✓ Update Order (modify items, recalculate total)
✓ Return Order (refund, restock, quality check)

🔒 System Requirements:
✓ All operations must be undoable (Ctrl+Z style)
✓ Operations must be logged for audit trail
✓ System must handle concurrent operations
✓ Operations must be persistent across restarts
✓ Performance must handle 1000+ operations/second
```

---

## 🏗️ System Architecture

### **Component Overview**
```
┌─────────────────────────────────────────────────────────────┐
│                    E-Commerce System                        │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   Web UI    │  │  Mobile App │  │   Admin UI  │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────────────────────────────────────────────┐ │
│  │              API Gateway / Load Balancer               │ │
│  └─────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │OrderService │  │PaymentService│  │InventoryService│      │
│  │             │  │             │  │               │      │
│  │CommandInvoker│  │CommandInvoker│  │CommandInvoker │      │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   Database  │  │    Redis    │  │Message Queue │        │
│  │             │  │   (Cache)   │  │   (Events)   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
```

### **Command Pattern Integration**
```
🔄 Command Flow:
UI → API → Service → Command → Invoker → Database
                    ↓
                 Undo Support
                    ↓
              Audit Logging
```

---

## 🚀 Step-by-Step Implementation

### **Step 1: Define Core Command Interface**

```java
// Base command interface
public interface Command {
    void execute() throws CommandException;
    void undo() throws CommandException;
    String getCommandId();
    long getTimestamp();
    CommandMetadata getMetadata();
}

// Command metadata for auditing
public class CommandMetadata {
    private String userId;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    private Map<String, Object> context;
    
    // Constructors, getters, setters
}

// Custom exception for command failures
public class CommandException extends Exception {
    private String commandId;
    private ErrorCode errorCode;
    
    public CommandException(String commandId, ErrorCode errorCode, String message) {
        super(message);
        this.commandId = commandId;
        this.errorCode = errorCode;
    }
}
```

### **Step 2: Create Abstract Base Command**

```java
// Abstract base command with common functionality
public abstract class BaseCommand implements Command {
    protected String commandId;
    protected long timestamp;
    protected CommandMetadata metadata;
    protected CommandState state;
    
    public BaseCommand(CommandMetadata metadata) {
        this.commandId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.metadata = metadata;
        this.state = CommandState.CREATED;
    }
    
    @Override
    public final void execute() throws CommandException {
        if (state != CommandState.CREATED) {
            throw new CommandException(commandId, ErrorCode.INVALID_STATE, 
                "Command can only be executed once");
        }
        
        try {
            // Pre-execution validation
            validate();
            
            // Capture state for undo
            captureState();
            
            // Execute the command
            doExecute();
            
            // Update state
            state = CommandState.EXECUTED;
            
            // Log execution
            logExecution();
            
        } catch (Exception e) {
            state = CommandState.FAILED;
            throw new CommandException(commandId, ErrorCode.EXECUTION_FAILED, 
                "Command execution failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public final void undo() throws CommandException {
        if (state != CommandState.EXECUTED) {
            throw new CommandException(commandId, ErrorCode.INVALID_STATE, 
                "Only executed commands can be undone");
        }
        
        try {
            // Pre-undo validation
            validateUndo();
            
            // Perform undo
            doUndo();
            
            // Update state
            state = CommandState.UNDONE;
            
            // Log undo
            logUndo();
            
        } catch (Exception e) {
            throw new CommandException(commandId, ErrorCode.UNDO_FAILED, 
                "Command undo failed: " + e.getMessage(), e);
        }
    }
    
    // Abstract methods for concrete commands
    protected abstract void validate() throws CommandException;
    protected abstract void captureState();
    protected abstract void doExecute() throws CommandException;
    protected abstract void validateUndo() throws CommandException;
    protected abstract void doUndo() throws CommandException;
    
    // Common methods
    private void logExecution() {
        CommandLogger.logExecution(this);
    }
    
    private void logUndo() {
        CommandLogger.logUndo(this);
    }
    
    // Getters
    @Override
    public String getCommandId() { return commandId; }
    @Override
    public long getTimestamp() { return timestamp; }
    @Override
    public CommandMetadata getMetadata() { return metadata; }
    public CommandState getState() { return state; }
}

// Command states
public enum CommandState {
    CREATED, EXECUTED, UNDONE, FAILED
}
```

### **Step 3: Implement Concrete Commands**

```java
// Place Order Command
public class PlaceOrderCommand extends BaseCommand {
    private Order order;
    private OrderService orderService;
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private NotificationService notificationService;
    
    // State captured for undo
    private Order previousOrderState;
    private PaymentResult paymentResult;
    private List<InventoryReservation> reservations;
    private boolean notificationsSent;
    
    public PlaceOrderCommand(Order order, 
                             OrderService orderService,
                             PaymentService paymentService,
                             InventoryService inventoryService,
                             NotificationService notificationService,
                             CommandMetadata metadata) {
        super(metadata);
        this.order = order;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
    }
    
    @Override
    protected void validate() throws CommandException {
        // Validate order
        if (order == null || order.getItems().isEmpty()) {
            throw new CommandException(commandId, ErrorCode.INVALID_INPUT, 
                "Order cannot be null or empty");
        }
        
        // Validate customer
        if (order.getCustomerId() == null) {
            throw new CommandException(commandId, ErrorCode.INVALID_INPUT, 
                "Customer ID is required");
        }
        
        // Check inventory availability
        for (OrderItem item : order.getItems()) {
            if (!inventoryService.isAvailable(item.getProductId(), item.getQuantity())) {
                throw new CommandException(commandId, ErrorCode.INVENTORY_UNAVAILABLE, 
                    "Product " + item.getProductId() + " is not available");
            }
        }
        
        // Validate payment method
        if (order.getPaymentMethod() == null) {
            throw new CommandException(commandId, ErrorCode.INVALID_INPUT, 
                "Payment method is required");
        }
    }
    
    @Override
    protected void captureState() {
        // Capture current state for potential undo
        previousOrderState = orderService.getOrder(order.getId());
        reservations = new ArrayList<>();
        notificationsSent = false;
    }
    
    @Override
    protected void doExecute() throws CommandException {
        try {
            // Step 1: Reserve inventory
            for (OrderItem item : order.getItems()) {
                InventoryReservation reservation = inventoryService.reserve(
                    item.getProductId(), item.getQuantity());
                reservations.add(reservation);
            }
            
            // Step 2: Process payment
            paymentResult = paymentService.processPayment(
                order.getTotalAmount(), 
                order.getPaymentMethod(),
                order.getCustomerId()
            );
            
            if (!paymentResult.isSuccess()) {
                throw new CommandException(commandId, ErrorCode.PAYMENT_FAILED, 
                    "Payment failed: " + paymentResult.getErrorMessage());
            }
            
            // Step 3: Create order
            order.setStatus(OrderStatus.CONFIRMED);
            order.setPaymentId(paymentResult.getPaymentId());
            order.setConfirmationDate(new Date());
            order = orderService.createOrder(order);
            
            // Step 4: Send notifications
            notificationService.sendOrderConfirmation(order);
            notificationService.sendPaymentConfirmation(order, paymentResult);
            notificationsSent = true;
            
        } catch (Exception e) {
            // Cleanup on failure
            cleanup();
            throw new CommandException(commandId, ErrorCode.EXECUTION_FAILED, 
                "Order placement failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void validateUndo() throws CommandException {
        if (order == null || order.getId() == null) {
            throw new CommandException(commandId, ErrorCode.INVALID_STATE, 
                "Cannot undo: Order not properly created");
        }
        
        Order currentOrder = orderService.getOrder(order.getId());
        if (currentOrder == null || currentOrder.getStatus() != OrderStatus.CONFIRMED) {
            throw new CommandException(commandId, ErrorCode.INVALID_STATE, 
                "Cannot undo: Order is not in confirmed state");
        }
    }
    
    @Override
    protected void doUndo() throws CommandException {
        try {
            // Step 1: Refund payment
            if (paymentResult != null && paymentResult.isSuccess()) {
                paymentService.refundPayment(paymentResult.getPaymentId(), 
                    order.getTotalAmount(), "Order cancellation");
            }
            
            // Step 2: Cancel order
            orderService.cancelOrder(order.getId(), "System undo");
            
            // Step 3: Release inventory reservations
            for (InventoryReservation reservation : reservations) {
                inventoryService.releaseReservation(reservation);
            }
            
            // Step 4: Send cancellation notifications
            if (notificationsSent) {
                notificationService.sendOrderCancellation(order);
                notificationService.sendPaymentRefund(order, paymentResult);
            }
            
        } catch (Exception e) {
            throw new CommandException(commandId, ErrorCode.UNDO_FAILED, 
                "Order undo failed: " + e.getMessage(), e);
        }
    }
    
    private void cleanup() {
        // Release reservations on failure
        for (InventoryReservation reservation : reservations) {
            try {
                inventoryService.releaseReservation(reservation);
            } catch (Exception e) {
                // Log cleanup failure but don't throw
                CommandLogger.logCleanupFailure(commandId, reservation, e);
            }
        }
    }
}

// Cancel Order Command
public class CancelOrderCommand extends BaseCommand {
    private Order order;
    private String reason;
    private OrderService orderService;
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private NotificationService notificationService;
    
    // State for undo
    private Order previousOrderState;
    private RefundResult refundResult;
    private List<InventoryRestoration> restorations;
    
    public CancelOrderCommand(String orderId, String reason,
                              OrderService orderService,
                              PaymentService paymentService,
                              InventoryService inventoryService,
                              NotificationService notificationService,
                              CommandMetadata metadata) {
        super(metadata);
        this.order = orderService.getOrder(orderId);
        this.reason = reason;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
    }
    
    @Override
    protected void validate() throws CommandException {
        if (order == null) {
            throw new CommandException(commandId, ErrorCode.INVALID_INPUT, 
                "Order not found");
        }
        
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new CommandException(commandId, ErrorCode.INVALID_STATE, 
                "Only confirmed orders can be cancelled");
        }
        
        // Check cancellation window (e.g., 24 hours)
        long hoursSinceConfirmation = ChronoUnit.HOURS.between(
            order.getConfirmationDate().toInstant(), 
            Instant.now()
        );
        
        if (hoursSinceConfirmation > 24) {
            throw new CommandException(commandId, ErrorCode.CANCELLATION_WINDOW_EXPIRED, 
                "Order can only be cancelled within 24 hours");
        }
    }
    
    @Override
    protected void captureState() {
        previousOrderState = cloneOrder(order);
        restorations = new ArrayList<>();
    }
    
    @Override
    protected void doExecute() throws CommandException {
        try {
            // Step 1: Process refund
            if (order.getPaymentId() != null) {
                refundResult = paymentService.refundPayment(
                    order.getPaymentId(), 
                    order.getTotalAmount(), 
                    reason
                );
                
                if (!refundResult.isSuccess()) {
                    throw new CommandException(commandId, ErrorCode.REFUND_FAILED, 
                        "Refund failed: " + refundResult.getErrorMessage());
                }
            }
            
            // Step 2: Restore inventory
            for (OrderItem item : order.getItems()) {
                InventoryRestoration restoration = inventoryService.restoreInventory(
                    item.getProductId(), item.getQuantity());
                restorations.add(restoration);
            }
            
            // Step 3: Update order status
            order.setStatus(OrderStatus.CANCELLED);
            order.setCancellationReason(reason);
            order.setCancellationDate(new Date());
            orderService.updateOrder(order);
            
            // Step 4: Send notifications
            notificationService.sendOrderCancellation(order);
            if (refundResult != null) {
                notificationService.sendPaymentRefund(order, refundResult);
            }
            
        } catch (Exception e) {
            throw new CommandException(commandId, ErrorCode.EXECUTION_FAILED, 
                "Order cancellation failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    protected void validateUndo() throws CommandException {
        Order currentOrder = orderService.getOrder(order.getId());
        if (currentOrder == null || currentOrder.getStatus() != OrderStatus.CANCELLED) {
            throw new CommandException(commandId, ErrorCode.INVALID_STATE, 
                "Cannot undo: Order is not in cancelled state");
        }
    }
    
    @Override
    protected void doUndo() throws CommandException {
        try {
            // Step 1: Reverse refund (charge again)
            if (refundResult != null && refundResult.isSuccess()) {
                PaymentResult chargeResult = paymentService.chargePayment(
                    order.getPaymentMethod(),
                    order.getTotalAmount(),
                    order.getCustomerId(),
                    "Order cancellation reversal"
                );
                
                if (!chargeResult.isSuccess()) {
                    throw new CommandException(commandId, ErrorCode.PAYMENT_FAILED, 
                        "Failed to reverse refund: " + chargeResult.getErrorMessage());
                }
            }
            
            // Step 2: Remove inventory restorations
            for (InventoryRestoration restoration : restorations) {
                inventoryService.reverseRestoration(restoration);
            }
            
            // Step 3: Restore order status
            order.setStatus(OrderStatus.CONFIRMED);
            order.setCancellationReason(null);
            order.setCancellationDate(null);
            orderService.updateOrder(order);
            
            // Step 4: Send restoration notifications
            notificationService.sendOrderRestoration(order);
            
        } catch (Exception e) {
            throw new CommandException(commandId, ErrorCode.UNDO_FAILED, 
                "Order cancellation undo failed: " + e.getMessage(), e);
        }
    }
    
    private Order cloneOrder(Order original) {
        // Deep clone order for state capture
        Order cloned = new Order();
        cloned.setId(original.getId());
        cloned.setCustomerId(original.getCustomerId());
        cloned.setStatus(original.getStatus());
        cloned.setTotalAmount(original.getTotalAmount());
        cloned.setPaymentId(original.getPaymentId());
        cloned.setConfirmationDate(original.getConfirmationDate());
        cloned.setItems(new ArrayList<>(original.getItems()));
        return cloned;
    }
}
```

### **Step 4: Production-Ready Command Invoker**

```java
// Production-ready CommandInvoker with advanced features
@Component
public class CommandInvoker {
    private static final Logger logger = LoggerFactory.getLogger(CommandInvoker.class);
    
    // Command storage
    private final Map<String, Command> commandStore = new ConcurrentHashMap<>();
    private final Deque<String> executionStack = new ArrayDeque<>();
    private final Map<String, Stack<String>> userCommandHistory = new ConcurrentHashMap<>();
    
    // Configuration
    private final int maxHistorySize;
    private final int maxUserHistorySize;
    private final boolean enablePersistence;
    private final boolean enableAsyncExecution;
    
    // Services
    private final CommandPersistenceService persistenceService;
    private final CommandAuditService auditService;
    private final ExecutorService asyncExecutor;
    
    // Metrics
    private final MeterRegistry meterRegistry;
    private final Counter commandExecutionCounter;
    private final Counter commandUndoCounter;
    private final Timer commandExecutionTimer;
    
    public CommandInvoker(CommandInvokerConfig config,
                          CommandPersistenceService persistenceService,
                          CommandAuditService auditService,
                          MeterRegistry meterRegistry) {
        this.maxHistorySize = config.getMaxHistorySize();
        this.maxUserHistorySize = config.getMaxUserHistorySize();
        this.enablePersistence = config.isEnablePersistence();
        this.enableAsyncExecution = config.isEnableAsyncExecution();
        this.persistenceService = persistenceService;
        this.auditService = auditService;
        this.meterRegistry = meterRegistry;
        
        // Initialize metrics
        this.commandExecutionCounter = Counter.builder("command.execution")
            .description("Number of command executions")
            .register(meterRegistry);
        this.commandUndoCounter = Counter.builder("command.undo")
            .description("Number of command undos")
            .register(meterRegistry);
        this.commandExecutionTimer = Timer.builder("command.execution.time")
            .description("Command execution time")
            .register(meterRegistry);
        
        // Initialize async executor if enabled
        this.asyncExecutor = enableAsyncExecution ? 
            Executors.newFixedThreadPool(config.getAsyncThreads()) : null;
        
        // Load persisted commands on startup
        if (enablePersistence) {
            loadPersistedCommands();
        }
    }
    
    /**
     * Execute a command synchronously
     */
    public CommandResult executeCommand(Command command) throws CommandException {
        return executeCommand(command, false);
    }
    
    /**
     * Execute a command (sync or async based on parameter)
     */
    public CommandResult executeCommand(Command command, boolean async) throws CommandException {
        String commandId = command.getCommandId();
        
        try {
            // Record execution start
            Timer.Sample sample = Timer.start(meterRegistry);
            
            // Execute command
            if (async && enableAsyncExecution) {
                return executeAsync(command);
            } else {
                return executeSync(command, sample);
            }
            
        } catch (Exception e) {
            logger.error("Failed to execute command: {}", commandId, e);
            throw new CommandException(commandId, ErrorCode.EXECUTION_FAILED, 
                "Command execution failed", e);
        }
    }
    
    private CommandResult executeSync(Command command, Timer.Sample sample) throws CommandException {
        String commandId = command.getCommandId();
        
        try {
            // Store command
            commandStore.put(commandId, command);
            
            // Execute command
            command.execute();
            
            // Add to execution stack
            executionStack.push(commandId);
            
            // Add to user history
            addToUserHistory(command);
            
            // Persist command if enabled
            if (enablePersistence) {
                persistenceService.saveCommand(command);
            }
            
            // Record metrics
            commandExecutionCounter.increment(Tags.of("type", command.getClass().getSimpleName()));
            sample.stop(commandExecutionTimer);
            
            // Audit log
            auditService.logCommandExecution(command);
            
            logger.info("Command executed successfully: {}", commandId);
            
            return CommandResult.success(commandId, "Command executed successfully");
            
        } catch (CommandException e) {
            // Record failure metrics
            commandExecutionCounter.increment(
                Tags.of("type", command.getClass().getSimpleName(), "status", "failed")
            );
            sample.stop(commandExecutionTimer);
            
            // Audit log failure
            auditService.logCommandFailure(command, e);
            
            throw e;
        }
    }
    
    private CommandResult executeAsync(Command command) throws CommandException {
        String commandId = command.getCommandId();
        
        try {
            // Submit to async executor
            CompletableFuture<CommandResult> future = CompletableFuture.supplyAsync(() -> {
                try {
                    Timer.Sample sample = Timer.start(meterRegistry);
                    return executeSync(command, sample);
                } catch (CommandException e) {
                    throw new RuntimeException(e);
                }
            }, asyncExecutor);
            
            // Return async result
            return CommandResult.async(commandId, future);
            
        } catch (Exception e) {
            throw new CommandException(commandId, ErrorCode.ASYNC_EXECUTION_FAILED, 
                "Async command execution failed", e);
        }
    }
    
    /**
     * Undo the last executed command
     */
    public CommandResult undoLastCommand() throws CommandException {
        if (executionStack.isEmpty()) {
            throw new CommandException("", ErrorCode.NO_COMMAND_TO_UNDO, 
                "No commands to undo");
        }
        
        String lastCommandId = executionStack.pop();
        return undoCommand(lastCommandId);
    }
    
    /**
     * Undo a specific command
     */
    public CommandResult undoCommand(String commandId) throws CommandException {
        Command command = commandStore.get(commandId);
        if (command == null) {
            throw new CommandException(commandId, ErrorCode.COMMAND_NOT_FOUND, 
                "Command not found: " + commandId);
        }
        
        try {
            // Record undo start
            Timer.Sample sample = Timer.start(meterRegistry);
            
            // Undo command
            command.undo();
            
            // Remove from user history
            removeFromUserHistory(command);
            
            // Update persisted command if enabled
            if (enablePersistence) {
                persistenceService.updateCommand(command);
            }
            
            // Record metrics
            commandUndoCounter.increment(Tags.of("type", command.getClass().getSimpleName()));
            sample.stop(commandExecutionTimer);
            
            // Audit log
            auditService.logCommandUndo(command);
            
            logger.info("Command undone successfully: {}", commandId);
            
            return CommandResult.success(commandId, "Command undone successfully");
            
        } catch (CommandException e) {
            // Record failure metrics
            commandUndoCounter.increment(
                Tags.of("type", command.getClass().getSimpleName(), "status", "failed")
            );
            
            // Audit log failure
            auditService.logCommandUndoFailure(command, e);
            
            throw e;
        }
    }
    
    /**
     * Undo commands for a specific user
     */
    public CommandResult undoLastUserCommand(String userId) throws CommandException {
        Stack<String> userHistory = userCommandHistory.get(userId);
        if (userHistory == null || userHistory.isEmpty()) {
            throw new CommandException("", ErrorCode.NO_COMMAND_TO_UNDO, 
                "No commands to undo for user: " + userId);
        }
        
        String lastCommandId = userHistory.pop();
        return undoCommand(lastCommandId);
    }
    
    /**
     * Get command history for a user
     */
    public List<Command> getUserCommandHistory(String userId, int limit) {
        Stack<String> userHistory = userCommandHistory.get(userId);
        if (userHistory == null) {
            return Collections.emptyList();
        }
        
        return userHistory.stream()
            .limit(limit)
            .map(commandStore::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    /**
     * Get command execution statistics
     */
    public CommandStatistics getStatistics() {
        return CommandStatistics.builder()
            .totalCommands(commandStore.size())
            .executionStackSize(executionStack.size())
            .userCount(userCommandHistory.size())
            .build();
    }
    
    // Private helper methods
    private void addToUserHistory(Command command) {
        String userId = command.getMetadata().getUserId();
        if (userId != null) {
            Stack<String> history = userCommandHistory.computeIfAbsent(userId, 
                k -> new ArrayDeque<>());
            history.push(command.getCommandId());
            
            // Limit history size
            while (history.size() > maxUserHistorySize) {
                history.removeLast();
            }
        }
    }
    
    private void removeFromUserHistory(Command command) {
        String userId = command.getMetadata().getUserId();
        if (userId != null) {
            Stack<String> history = userCommandHistory.get(userId);
            if (history != null) {
                history.remove(command.getCommandId());
            }
        }
    }
    
    private void loadPersistedCommands() {
        try {
            List<Command> persistedCommands = persistenceService.loadAllCommands();
            for (Command command : persistedCommands) {
                commandStore.put(command.getCommandId(), command);
                
                // Rebuild execution stack and user history
                if (command.getState() == CommandState.EXECUTED) {
                    executionStack.push(command.getCommandId());
                    addToUserHistory(command);
                }
            }
            
            logger.info("Loaded {} persisted commands", persistedCommands.size());
            
        } catch (Exception e) {
            logger.error("Failed to load persisted commands", e);
        }
    }
    
    @PreDestroy
    public void shutdown() {
        if (asyncExecutor != null) {
            asyncExecutor.shutdown();
            try {
                if (!asyncExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    asyncExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                asyncExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Command result wrapper
public class CommandResult {
    private String commandId;
    private boolean success;
    private String message;
    private CompletableFuture<CommandResult> asyncFuture;
    
    public static CommandResult success(String commandId, String message) {
        return new CommandResult(commandId, true, message, null);
    }
    
    public static CommandResult async(String commandId, CompletableFuture<CommandResult> future) {
        return new CommandResult(commandId, false, "Async execution started", future);
    }
    
    // Constructors, getters
    public boolean isAsync() {
        return asyncFuture != null;
    }
    
    public CommandResult getAsyncResult() throws InterruptedException, ExecutionException {
        if (asyncFuture != null) {
            return asyncFuture.get();
        }
        return this;
    }
}

// Configuration class
@ConfigurationProperties(prefix = "command.invoker")
@Data
public class CommandInvokerConfig {
    private int maxHistorySize = 1000;
    private int maxUserHistorySize = 100;
    private boolean enablePersistence = true;
    private boolean enableAsyncExecution = true;
    private int asyncThreads = 10;
}
```

---

## 🏭 Production Considerations

### **1. Performance Optimization**

```java
// Command caching for frequently used commands
@Component
public class CommandCache {
    private final Cache<String, Command> commandCache;
    
    public CommandCache() {
        this.commandCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();
    }
    
    public Command getCachedCommand(String commandId) {
        return commandCache.getIfPresent(commandId);
    }
    
    public void cacheCommand(Command command) {
        commandCache.put(command.getCommandId(), command);
    }
}

// Batch command execution for high throughput
public class BatchCommandProcessor {
    private final CommandInvoker invoker;
    private final int batchSize;
    
    public CompletableFuture<List<CommandResult>> executeBatch(List<Command> commands) {
        return CompletableFuture.supplyAsync(() -> {
            List<CommandResult> results = new ArrayList<>();
            
            for (List<Command> batch : partition(commands, batchSize)) {
                List<CompletableFuture<CommandResult>> futures = batch.stream()
                    .map(cmd -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return invoker.executeCommand(cmd);
                        } catch (CommandException e) {
                            return CommandResult.failure(cmd.getCommandId(), e.getMessage());
                        }
                    }))
                    .collect(Collectors.toList());
                
                // Wait for batch completion
                futures.forEach(future -> {
                    try {
                        results.add(future.get());
                    } catch (Exception e) {
                        results.add(CommandResult.failure("unknown", e.getMessage()));
                    }
                });
            }
            
            return results;
        });
    }
}
```

### **2. Security & Authorization**

```java
// Command security interceptor
@Component
public class CommandSecurityInterceptor {
    private final AuthorizationService authorizationService;
    
    public void checkPermission(Command command) throws CommandException {
        CommandMetadata metadata = command.getMetadata();
        String userId = metadata.getUserId();
        String permission = getRequiredPermission(command);
        
        if (!authorizationService.hasPermission(userId, permission)) {
            throw new CommandException(command.getCommandId(), ErrorCode.UNAUTHORIZED, 
                "User not authorized to execute this command");
        }
    }
    
    private String getRequiredPermission(Command command) {
        if (command instanceof PlaceOrderCommand) {
            return "order:place";
        } else if (command instanceof CancelOrderCommand) {
            return "order:cancel";
        }
        return "command:execute";
    }
}

// Rate limiting for command execution
@Component
public class CommandRateLimiter {
    private final RateLimiter rateLimiter;
    
    public CommandRateLimiter() {
        this.rateLimiter = RateLimiter.create(100.0); // 100 commands per second
    }
    
    public void checkRateLimit(String userId) throws CommandException {
        if (!rateLimiter.tryAcquire()) {
            throw new CommandException("", ErrorCode.RATE_LIMIT_EXCEEDED, 
                "Rate limit exceeded");
        }
    }
}
```

### **3. Monitoring & Alerting**

```java
// Command monitoring service
@Component
public class CommandMonitoringService {
    private final MeterRegistry meterRegistry;
    private final AlertService alertService;
    
    // Custom metrics
    private final Counter commandFailureCounter;
    private final Gauge commandQueueSize;
    private final Timer commandLatencyTimer;
    
    public CommandMonitoringService(MeterRegistry meterRegistry, AlertService alertService) {
        this.meterRegistry = meterRegistry;
        this.alertService = alertService;
        
        this.commandFailureCounter = Counter.builder("command.failures")
            .description("Number of command failures")
            .register(meterRegistry);
        
        this.commandQueueSize = Gauge.builder("command.queue.size")
            .description("Command queue size")
            .register(meterRegistry, this, CommandMonitoringService::getQueueSize);
        
        this.commandLatencyTimer = Timer.builder("command.latency")
            .description("Command execution latency")
            .register(meterRegistry);
    }
    
    @EventListener
    public void onCommandExecuted(CommandExecutedEvent event) {
        // Record metrics
        commandLatencyTimer.record(event.getExecutionTime());
        
        // Check for performance issues
        if (event.getExecutionTime() > 5000) { // 5 seconds
            alertService.sendAlert(AlertType.PERFORMANCE, 
                "Slow command execution: " + event.getCommandId());
        }
    }
    
    @EventListener
    public void onCommandFailed(CommandFailedEvent event) {
        // Record failure
        commandFailureCounter.increment(
            Tags.of("type", event.getCommandType(), "error", event.getErrorCode())
        );
        
        // Check for high failure rate
        double failureRate = calculateFailureRate();
        if (failureRate > 0.05) { // 5% failure rate
            alertService.sendAlert(AlertType.HIGH_ERROR_RATE, 
                "High command failure rate: " + failureRate);
        }
    }
    
    private double getQueueSize() {
        // Return current queue size
        return 0; // Implementation depends on your queue system
    }
    
    private double calculateFailureRate() {
        // Calculate failure rate over last 5 minutes
        return 0.0; // Implementation depends on your metrics system
    }
}
```

---

## 📈 Scaling Strategies

### **1. Horizontal Scaling**

```java
// Distributed command invoker using Redis
@Component
public class DistributedCommandInvoker {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String nodeId;
    
    private static final String COMMAND_QUEUE = "commands:queue";
    private static final String EXECUTION_STACK = "commands:stack";
    private static final String USER_HISTORY = "commands:users:";
    
    public void executeDistributedCommand(Command command) throws CommandException {
        try {
            // Serialize command
            byte[] serializedCommand = serializeCommand(command);
            
            // Add to distributed queue
            redisTemplate.opsForList().rightPush(COMMAND_QUEUE, serializedCommand);
            
            // Publish execution event
            redisTemplate.convertAndSend("command:execute", command.getCommandId());
            
        } catch (Exception e) {
            throw new CommandException(command.getCommandId(), ErrorCode.DISTRIBUTED_EXECUTION_FAILED, 
                "Distributed command execution failed", e);
        }
    }
    
    @RedisListener(channel = "command:execute")
    public void handleCommandExecution(String commandId) {
        try {
            // Get command from queue
            byte[] serializedCommand = (byte[]) redisTemplate.opsForList().rightPop(COMMAND_QUEUE);
            Command command = deserializeCommand(serializedCommand);
            
            // Execute command
            command.execute();
            
            // Add to distributed execution stack
            redisTemplate.opsForList().rightPush(EXECUTION_STACK, commandId);
            
            // Add to user history
            String userId = command.getMetadata().getUserId();
            redisTemplate.opsForList().rightPush(USER_HISTORY + userId, commandId);
            
        } catch (Exception e) {
            logger.error("Failed to execute distributed command: {}", commandId, e);
        }
    }
    
    public void undoDistributedCommand(String commandId) throws CommandException {
        try {
            // Check if command is at top of execution stack
            String topCommand = (String) redisTemplate.opsForList().rightPeek(EXECUTION_STACK);
            if (!commandId.equals(topCommand)) {
                throw new CommandException(commandId, ErrorCode.INVALID_UNDO_ORDER, 
                    "Can only undo the most recent command");
            }
            
            // Get command from store
            Command command = getCommandFromStore(commandId);
            
            // Undo command
            command.undo();
            
            // Remove from execution stack
            redisTemplate.opsForList().rightPop(EXECUTION_STACK);
            
            // Remove from user history
            String userId = command.getMetadata().getUserId();
            redisTemplate.opsForList().remove(USER_HISTORY + userId, 1, commandId);
            
        } catch (Exception e) {
            throw new CommandException(commandId, ErrorCode.DISTRIBUTED_UNDO_FAILED, 
                "Distributed command undo failed", e);
        }
    }
}
```

### **2. Event-Driven Architecture**

```java
// Event-driven command processing
@Component
public class EventDrivenCommandProcessor {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CommandStore commandStore;
    
    public void publishCommandEvent(Command command) {
        CommandEvent event = CommandEvent.builder()
            .commandId(command.getCommandId())
            .commandType(command.getClass().getSimpleName())
            .userId(command.getMetadata().getUserId())
            .timestamp(System.currentTimeMillis())
            .payload(serializeCommand(command))
            .build();
        
        kafkaTemplate.send("command-events", event);
    }
    
    @KafkaListener(topics = "command-events")
    public void processCommandEvent(CommandEvent event) {
        try {
            Command command = deserializeCommand(event.getPayload());
            
            // Execute command
            command.execute();
            
            // Publish success event
            publishCommandResultEvent(command, true, null);
            
        } catch (Exception e) {
            // Publish failure event
            publishCommandResultEvent(getCommandFromEvent(event), false, e.getMessage());
        }
    }
    
    private void publishCommandResultEvent(Command command, boolean success, String error) {
        CommandResultEvent resultEvent = CommandResultEvent.builder()
            .commandId(command.getCommandId())
            .success(success)
            .error(error)
            .timestamp(System.currentTimeMillis())
            .build();
        
        kafkaTemplate.send("command-results", resultEvent);
    }
}
```

---

## 🧪 Testing Approach

### **1. Unit Testing**

```java
// Command unit tests
@ExtendWith(MockitoExtension.class)
class PlaceOrderCommandTest {
    
    @Mock private OrderService orderService;
    @Mock private PaymentService paymentService;
    @Mock private InventoryService inventoryService;
    @Mock private NotificationService notificationService;
    
    @Test
    void shouldExecutePlaceOrderCommandSuccessfully() throws CommandException {
        // Given
        Order order = createTestOrder();
        PlaceOrderCommand command = new PlaceOrderCommand(
            order, orderService, paymentService, inventoryService, notificationService,
            createTestMetadata()
        );
        
        // Mock dependencies
        when(inventoryService.isAvailable(any(), any())).thenReturn(true);
        when(paymentService.processPayment(any(), any(), any()))
            .thenReturn(createSuccessfulPaymentResult());
        when(orderService.createOrder(any())).thenReturn(order);
        
        // When
        command.execute();
        
        // Then
        assertEquals(CommandState.EXECUTED, command.getState());
        verify(inventoryService).reserve(any(), any());
        verify(paymentService).processPayment(any(), any(), any());
        verify(orderService).createOrder(order);
        verify(notificationService).sendOrderConfirmation(order);
    }
    
    @Test
    void shouldUndoPlaceOrderCommandSuccessfully() throws CommandException {
        // Given
        Order order = createTestOrder();
        PlaceOrderCommand command = new PlaceOrderCommand(
            order, orderService, paymentService, inventoryService, notificationService,
            createTestMetadata()
        );
        
        // Execute first
        when(inventoryService.isAvailable(any(), any())).thenReturn(true);
        when(paymentService.processPayment(any(), any(), any()))
            .thenReturn(createSuccessfulPaymentResult());
        when(orderService.createOrder(any())).thenReturn(order);
        command.execute();
        
        // Mock undo dependencies
        when(paymentService.refundPayment(any(), any(), any()))
            .thenReturn(createSuccessfulRefundResult());
        
        // When
        command.undo();
        
        // Then
        assertEquals(CommandState.UNDONE, command.getState());
        verify(paymentService).refundPayment(any(), any(), any());
        verify(orderService).cancelOrder(any(), any());
        verify(inventoryService).releaseReservation(any());
    }
    
    @Test
    void shouldFailWhenInventoryUnavailable() {
        // Given
        Order order = createTestOrder();
        PlaceOrderCommand command = new PlaceOrderCommand(
            order, orderService, paymentService, inventoryService, notificationService,
            createTestMetadata()
        );
        
        // Mock inventory unavailable
        when(inventoryService.isAvailable(any(), any())).thenReturn(false);
        
        // When & Then
        CommandException exception = assertThrows(CommandException.class, 
            () -> command.execute());
        
        assertEquals(ErrorCode.INVENTORY_UNAVAILABLE, exception.getErrorCode());
        assertEquals(CommandState.FAILED, command.getState());
    }
}
```

### **2. Integration Testing**

```java
// Command invoker integration tests
@SpringBootTest
@TestPropertySource(properties = {
    "command.invoker.enable-persistence=true",
    "command.invoker.enable-async-execution=true"
})
class CommandInvokerIntegrationTest {
    
    @Autowired private CommandInvoker commandInvoker;
    @Autowired private TestEntityManager entityManager;
    
    @Test
    @Transactional
    void shouldExecuteAndUndoCommandWithPersistence() throws CommandException {
        // Given
        PlaceOrderCommand command = createPlaceOrderCommand();
        
        // When
        CommandResult result = commandInvoker.executeCommand(command);
        
        // Then
        assertTrue(result.isSuccess());
        assertEquals(CommandState.EXECUTED, command.getState());
        
        // Verify command is persisted
        Command persistedCommand = commandInvoker.getCommand(command.getCommandId());
        assertNotNull(persistedCommand);
        assertEquals(CommandState.EXECUTED, persistedCommand.getState());
        
        // When
        CommandResult undoResult = commandInvoker.undoLastCommand();
        
        // Then
        assertTrue(undoResult.isSuccess());
        assertEquals(CommandState.UNDONE, command.getState());
        
        // Verify undo is persisted
        Command undoneCommand = commandInvoker.getCommand(command.getCommandId());
        assertEquals(CommandState.UNDONE, undoneCommand.getState());
    }
    
    @Test
    void shouldHandleAsyncCommandExecution() throws CommandException, InterruptedException, ExecutionException {
        // Given
        PlaceOrderCommand command = createPlaceOrderCommand();
        
        // When
        CommandResult result = commandInvoker.executeCommand(command, true);
        
        // Then
        assertTrue(result.isAsync());
        assertNotNull(result.getAsyncFuture());
        
        // Wait for async completion
        CommandResult asyncResult = result.getAsyncResult();
        assertTrue(asyncResult.isSuccess());
        assertEquals(CommandState.EXECUTED, command.getState());
    }
}
```

### **3. Performance Testing**

```java
// Command performance tests
@Test
void shouldHandleHighThroughputCommandExecution() throws InterruptedException {
    // Given
    int numberOfCommands = 10000;
    CountDownLatch latch = new CountDownLatch(numberOfCommands);
    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger failureCount = new AtomicInteger(0);
    
    // When
    long startTime = System.currentTimeMillis();
    
    for (int i = 0; i < numberOfCommands; i++) {
        Command command = createTestCommand(i);
        
        CompletableFuture.runAsync(() -> {
            try {
                commandInvoker.executeCommand(command);
                successCount.incrementAndGet();
            } catch (CommandException e) {
                failureCount.incrementAndGet();
            } finally {
                latch.countDown();
            }
        });
    }
    
    latch.await(30, TimeUnit.SECONDS);
    
    long endTime = System.currentTimeMillis();
    
    // Then
    long totalTime = endTime - startTime;
    double throughput = (double) numberOfCommands / (totalTime / 1000.0);
    
    assertEquals(numberOfCommands, successCount.get() + failureCount.get());
    assertTrue("Throughput should be at least 1000 commands/second", throughput >= 1000);
    assertTrue("Failure rate should be less than 1%", 
        (double) failureCount.get() / numberOfCommands < 0.01);
    
    logger.info("Executed {} commands in {}ms (throughput: {:.2f} commands/sec)", 
        numberOfCommands, totalTime, throughput);
}
```

---

## 🚀 Deployment Guide

### **1. Docker Configuration**

```dockerfile
# Dockerfile for Command Pattern Service
FROM openjdk:17-jre-slim

COPY target/command-service.jar /app/
COPY config/ /app/config/

WORKDIR /app

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "command-service.jar"]
```

```yaml
# docker-compose.yml
version: '3.8'

services:
  command-service:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - COMMAND_INVOKER_ENABLE_PERSISTENCE=true
      - COMMAND_INVOKER_ENABLE_ASYNC_EXECUTION=true
      - COMMAND_INVOKER_ASYNC_THREADS=20
    depends_on:
      - redis
      - postgres
      - kafka
  
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
  
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: command_db
      POSTGRES_USER: command_user
      POSTGRES_PASSWORD: command_pass
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
  
  kafka:
    image: confluentinc/cp-kafka:latest
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper
  
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

volumes:
  redis_data:
  postgres_data:
```

### **2. Kubernetes Deployment**

```yaml
# k8s-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: command-service
  labels:
    app: command-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: command-service
  template:
    metadata:
      labels:
        app: command-service
    spec:
      containers:
      - name: command-service
        image: command-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: COMMAND_INVOKER_ENABLE_PERSISTENCE
          value: "true"
        - name: COMMAND_INVOKER_ENABLE_ASYNC_EXECUTION
          value: "true"
        - name: COMMAND_INVOKER_ASYNC_THREADS
          value: "20"
        - name: REDIS_HOST
          value: "redis-service"
        - name: POSTGRES_HOST
          value: "postgres-service"
        - name: KAFKA_BOOTSTRAP_SERVERS
          value: "kafka-service:9092"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5

---
apiVersion: v1
kind: Service
metadata:
  name: command-service
spec:
  selector:
    app: command-service
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

### **3. Monitoring & Observability**

```yaml
# prometheus-config.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'command-service'
    static_configs:
      - targets: ['command-service:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s

rule_files:
  - "command-alerts.yml"

alerting:
  alertmanagers:
    - static_configs:
        - targets:
          - alertmanager:9093
```

```yaml
# command-alerts.yml
groups:
- name: command-service-alerts
  rules:
  - alert: HighCommandFailureRate
    expr: rate(command_failures_total[5m]) > 0.05
    for: 2m
    labels:
      severity: warning
    annotations:
      summary: "High command failure rate detected"
      description: "Command failure rate is {{ $value | humanizePercentage }}"
  
  - alert: SlowCommandExecution
    expr: histogram_quantile(0.95, rate(command_execution_duration_seconds_bucket[5m])) > 5
    for: 5m
    labels:
      severity: warning
    annotations:
      summary: "Slow command execution detected"
      description: "95th percentile latency is {{ $value }}s"
  
  - alert: CommandQueueBacklog
    expr: command_queue_size > 1000
    for: 1m
    labels:
      severity: critical
    annotations:
      summary: "Command queue backlog detected"
      description: "Queue size is {{ $value }} commands"
```

---

## 📊 Summary

This real-world implementation demonstrates:

### **✅ Production-Ready Features**
- **Persistence**: Commands survive system restarts
- **Async Execution**: High throughput with background processing
- **Distributed Support**: Multi-node deployment with Redis/Kafka
- **Security**: Authorization and rate limiting
- **Monitoring**: Metrics, alerts, and observability
- **Testing**: Comprehensive unit, integration, and performance tests

### **🚀 Scalability Considerations**
- **Horizontal Scaling**: Multiple service instances
- **Event-Driven**: Decoupled command processing
- **Caching**: Frequently accessed commands
- **Batch Processing**: High-volume operations

### **🎯 Real-World Application**
- **E-Commerce**: Order management with undo capability
- **Financial Services**: Transaction management
- **Content Management**: Document operations with versioning
- **IoT Systems**: Device control with rollback

### **💡 Key Takeaways**
1. **State Capture**: Always capture state BEFORE execution
2. **Error Handling**: Comprehensive exception management
3. **Performance**: Async execution for high throughput
4. **Monitoring**: Observability is crucial in production
5. **Testing**: Multiple testing levels ensure reliability

This implementation provides a solid foundation for deploying Command pattern in production systems with enterprise-grade features and scalability considerations.
