package lld.wearehousemangement;

public class WarehouseDemo {
    public static void main(String[] args) {
        // Get inventory manager instance
        InventoryManager inventoryManager = InventoryManager.getInstance();
        
        // Create warehouses using inventory manager methods
        Warehouse mainWarehouse = inventoryManager.createWarehouse(1, "New York", "Main Warehouse");
        Warehouse secondaryWarehouse = inventoryManager.createWarehouse(2, "Los Angeles", "Secondary Warehouse", new BatchReplishment());
        
        // List all warehouses
        inventoryManager.listAllWarehouses();
        
        // Create products using factory
        Product laptop = inventoryManager.productFactory.createProduct("LAP001", "Laptop", ProductCategories.ELECTRONIC, 999.99, 10);
        Product mouse = inventoryManager.productFactory.createProduct("MOU001", "Mouse", ProductCategories.ELECTRONIC, 25.99, 5);
        Product keyboard = inventoryManager.productFactory.createProduct("KEY001", "Keyboard", ProductCategories.ELECTRONIC, 49.99, 8);
        
        // Add products to warehouses
        mainWarehouse.addProduct(laptop, 10);
        mainWarehouse.addProduct(mouse, 5);
        secondaryWarehouse.addProduct(keyboard, 8);
        
        // Check inventory status
        mainWarehouse.checkInventoryStatus();
        secondaryWarehouse.checkInventoryStatus();
        
        // Use replenishment strategies
        System.out.println("\n--- Testing Replenishment Strategies ---");
        System.out.println("Main Warehouse (Automatic):");
        mainWarehouse.triggerReplenishment();
        
        System.out.println("\nSecondary Warehouse (Batch):");
        secondaryWarehouse.triggerReplenishment();
        
        // Switch strategies at runtime
        System.out.println("\n--- Switching Strategies ---");
        mainWarehouse.switchReplenishmentStrategy(new BatchReplishment());
        mainWarehouse.triggerReplenishment();
        
        // Remove products and trigger auto-replenishment
        System.out.println("\n--- Testing Auto-Replenishment on Depletion ---");
        mainWarehouse.removeProduct("LAP001", 10); // This will trigger replenishment
        secondaryWarehouse.removeProduct("KEY001", 8);  // This will trigger replenishment
        
        // Final inventory check
        mainWarehouse.checkInventoryStatus();
        secondaryWarehouse.checkInventoryStatus();
        
        // Test warehouse management methods
        System.out.println("\n--- Testing Warehouse Management ---");
        Warehouse foundWarehouse = inventoryManager.getWarehouseById(1);
        if (foundWarehouse != null) {
            System.out.println("Found warehouse: " + foundWarehouse.name);
        }
        
        // Remove a warehouse
        inventoryManager.removeWarehouse(2);
        inventoryManager.listAllWarehouses();
    }
}
