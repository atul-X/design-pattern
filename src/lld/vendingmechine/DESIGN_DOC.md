# Vending Machine System - Design Document

## 1. System Overview

A vending machine system designed to manage product inventory, handle customer selections, process payments, and dispense products efficiently. The system supports multiple product types, manages inventory availability, handles various payment methods, and provides a seamless purchase experience.

---

## 2. Requirements Summary

### Core Features
- **Multiple Product Types**: Support beverages, snacks, and other categories
- **Inventory Management**: Track product availability and manage stock levels
- **Payment Methods**: Accept coin-based payments, credit cards, and mobile payments
- **State Management**: Handle different machine states during operations
- **Safety Features**: Prevent out-of-stock dispensing, validate payments, maintain audit trails

### Machine States
1. **READY** - Waiting for customer interaction
2. **ITEM_SELECTED** - Customer has selected an item
3. **PAYMENT_PENDING** - Waiting for payment confirmation
4. **DISPENSING** - Dispensing the selected product
5. **MAINTENANCE** - Machine in maintenance mode (no user interaction)

---

## 3. Core Entities & Classes

### 3.1 Product
```
Attributes:
  - skuId: int (Stock Keeping Unit)
  - category: String (BISCUIT, COLD_DRINK, etc.)
  - name: String
  - price: double
  - slotId: int (location in vending machine)
```

### 3.2 Slot
```
Attributes:
  - id: int
  - product: Product
  - capacity: int (maximum items slot can hold)
  - currentQuantity: int (items currently in slot)
  - isOperational: boolean
  - state: SlotState (AVAILABLE, OUT_OF_STOCK, JAMMED)
```

### 3.3 Level
```
Attributes:
  - levelId: int
  - levelName: String
  - type: LevelType (ROW, COLUMN, etc.)
  - slots: List<Slot>
```

### 3.4 VendingMachine
```
Attributes:
  - machineId: int
  - location: String
  - vendingMachineState: State (current state)
  - products: List<Product>
  - transactionHistory: List<Transaction>
  - levels: List<Level>
  - currentBalance: double
  - selectedProduct: Product
```

### 3.5 Transaction
```
Attributes:
  - id: int
  - orderId: int
  - amount: double
  - timestamp: LocalDateTime
  - paymentMethod: String
  - status: String
```

### 3.6 Customer
```
Attributes:
  - customerId: int
  - name: String
  - paymentMethod: PaymentMethod
```

---

## 4. Design Patterns & Services

### 4.1 State Pattern - VendingMachineState
**Purpose**: Manage state transitions and behavior during different machine states

**States**:
- `ReadyState` - Accepts product selection
- `ItemSelectedState` - Displays price, accepts payment
- `PaymentPendingState` - Validates payment
- `DispensingState` - Dispenses product
- `MaintenanceState` - Blocks user interaction

**Interface**:
```
VendingMachineState
  + selectItem(product): void
  + insertMoney(amount): void
  + dispenseProduct(): void
  + cancel(): void
  + returnChange(): void
```

### 4.2 Strategy Pattern - PaymentService
**Purpose**: Support multiple payment methods

**Strategies**:
- `CoinPaymentStrategy` - Process coin payments
- `CardPaymentStrategy` - Process credit/debit card payments
- `MobilePaymentStrategy` - Process mobile payments

**Interface**:
```
PaymentStrategy
  + pay(amount): boolean
  + refund(amount): boolean
  + getPaymentMethod(): String
```

### 4.3 Command Pattern - DispenseCommand
**Purpose**: Encapsulate dispensing operations and support undo

**Commands**:
- `DispenseProductCommand` - Dispense product from slot
- `ReturnChangeCommand` - Return change to customer
- `ReturnProductCommand` - Return product on failure

**Interface**:
```
Command
  + execute(): void
  + undo(): void
```

### 4.4 Observer Pattern - InventoryService
**Purpose**: Notify when inventory levels change

**Observers**:
- `LowStockObserver` - Notified when stock is low
- `OutOfStockObserver` - Notified when stock is depleted

**Interface**:
```
InventoryObserver
  + update(product, quantity): void
  + onOutOfStock(product): void
```

---

## 5. Service Components

### 5.1 InventoryService
**Responsibilities**:
- Track product stock levels
- Update quantities after dispensing
- Notify observers on stock changes
- Prevent dispensing of out-of-stock items

**Key Methods**:
```
- addStock(slotId, quantity): void
- removeStock(slotId): Product
- getAvailableProducts(): List<Product>
- isInStock(slotId): boolean
- subscribe(observer): void
- notifyObservers(): void
```

### 5.2 PaymentService
**Responsibilities**:
- Process payments using selected strategy
- Validate transaction amounts
- Calculate change
- Handle refunds

**Key Methods**:
```
- processPayment(amount, strategy): boolean
- refundPayment(amount, strategy): boolean
- calculateChange(paid, required): double
- validateAmount(amount): boolean
```

### 5.3 PricingService
**Responsibilities**:
- Calculate product prices
- Apply discounts
- Calculate taxes
- Generate final price

**Key Methods**:
```
- getPrice(product): double
- applyDiscount(price, discount): double
- calculateTax(price): double
- getTotalPrice(product, quantity): double
```

### 5.4 DispenseService
**Responsibilities**:
- Execute dispensing operations
- Return change
- Handle failures
- Maintain audit trail

**Key Methods**:
```
- dispenseProduct(slotId): Command
- returnChange(amount): Command
- returnProduct(slotId): Command
- executeCommand(command): void
- undoCommand(command): void
```

### 5.5 VendingMachineManager
**Responsibilities**:
- Manage state transitions
- Coordinate between services
- Handle user interactions
- Maintain transaction history

**Key Methods**:
```
- selectProduct(slotId): void
- insertMoney(amount): void
- completeTransaction(): void
- cancelTransaction(): void
- transitionState(newState): void
```

---

## 6. Workflow Scenarios

### Successful Purchase Flow
```
1. Customer inserts card/coins/mobile payment
2. Machine transitions to READY state
3. Customer selects product (ITEM_SELECTED state)
4. Machine displays price
5. Customer confirms payment (PAYMENT_PENDING state)
6. PaymentService processes transaction
7. Machine transitions to DISPENSING state
8. DispenseService dispenses product
9. Machine calculates and returns change
10. Machine returns to READY state
11. Transaction logged
```

### Out-of-Stock Scenario
```
1. Customer selects product
2. InventoryService checks stock
3. If out-of-stock, display error message
4. Machine remains in READY state
5. Return payment to customer
```

### Payment Failure Scenario
```
1. Customer inserts payment
2. PaymentService validates amount
3. If insufficient, display message
4. DispenseService returns product
5. Machine returns to READY state
```

---

## 7. Class Responsibilities (SOLID Principles)

| Class | Responsibility |
|-------|-----------------|
| Product | Data model for products |
| Slot | Container for product and quantity tracking |
| Level | Organizes multiple slots |
| VendingMachine | Maintains machine metadata |
| Transaction | Records transaction details |
| VendingMachineState | Defines behavior per state |
| PaymentStrategy | Handles payment processing |
| InventoryService | Manages inventory |
| PaymentService | Processes payments |
| PricingService | Calculates prices |
| DispenseService | Manages dispensing operations |
| VendingMachineManager | Orchestrates overall flow |

---

## 8. Design Patterns Applied

| Pattern | Purpose | Implementation |
|---------|---------|-----------------|
| **State** | Manage machine state transitions | VendingMachineState interface with concrete states |
| **Strategy** | Support multiple payment methods | PaymentStrategy interface with concrete strategies |
| **Command** | Encapsulate dispensing operations | Command interface with execute/undo methods |
| **Observer** | Notify on inventory changes | InventoryObserver interface with concrete observers |
| **Singleton** | Ensure single service instances | InventoryService, PaymentService, etc. |
| **Factory** | Create products, slots, levels | Factory classes for object creation |

---

## 9. Error Handling & Edge Cases

1. **Out-of-Stock Items**
   - Check inventory before dispensing
   - Return payment if item unavailable

2. **Insufficient Payment**
   - Validate amount against product price
   - Request additional payment or cancel

3. **Slot Jam**
   - Mark slot as non-operational
   - Notify maintenance team
   - Prevent selection of jammed slot

4. **Payment Failure**
   - Retry payment processing
   - Return payment after max retries
   - Log transaction as failed

5. **Maintenance Mode**
   - Block all user interactions
   - Prevent state transitions
   - Allow only maintenance operations

---

## 10. Extensibility Points

1. **New Payment Methods**
   - Implement PaymentStrategy interface

2. **New Product Categories**
   - Add to product category enum

3. **Discount Mechanisms**
   - Extend PricingService

4. **Audit Trails**
   - Implement logging in services

5. **Machine Monitoring**
   - Add new observers to InventoryService

---

## 11. Implementation Order

1. **Phase 1**: Core entities (Product, Slot, Level, VendingMachine, Transaction)
2. **Phase 2**: State management (VendingMachineState implementations)
3. **Phase 3**: Payment strategies (PaymentStrategy implementations)
4. **Phase 4**: Services (InventoryService, PaymentService, PricingService, DispenseService)
5. **Phase 5**: VendingMachineManager and orchestration
6. **Phase 6**: Testing and edge case handling

---

## 12. Technology Stack (Recommended)

- **Language**: Java
- **Testing**: JUnit 5, Mockito
- **Logging**: SLF4J with Logback
- **Build Tool**: Maven or Gradle
- **IDE**: IntelliJ IDEA or VS Code

---

## 13. Future Enhancements

1. Multi-currency support
2. Real-time inventory sync with backend
3. Mobile app integration
4. Predictive maintenance
5. Analytics and reporting dashboard
6. Machine-to-machine communication (IoT)
7. Subscription/loyalty program support

---

**End of Design Document**
