//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//// ==================== ENUMS ====================
//
//enum ProductCategories {
//    ELECTRONIC, RETAIL
//}
//
//// ==================== INTERFACES ====================
//
//interface ReplenishmentStrategy {
//    void replenish();
//}
//
//// ==================== ABSTRACT CLASSES ====================
//
//abstract class Product {
//    String sku;
//    String name;
//    double price;
//    ProductCategories productCategories;
//    int quantity;
//
//    public Product(String name, double price, ProductCategories productCategories, int quantity, String sku) {
//        this.name = name;
//        this.price = price;
//        this.productCategories = productCategories;
//        this.quantity = quantity;
//        this.sku = sku;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public ProductCategories getProductCategories() {
//        return productCategories;
//    }
//
//    public void setProductCategories(ProductCategories productCategories) {
//        this.productCategories = productCategories;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getSku() {
//        return sku;
//    }
//
//    public void setSku(String sku) {
//        this.sku = sku;
//    }
//}
//
//// ==================== CONCRETE PRODUCT CLASSES ====================
//
//class RetailsProducts extends Product {
//    String color;
//    int size;
//
//    public RetailsProducts(String name, double price, ProductCategories productCategories, int quantity, String sku) {
//        super(name, price, productCategories, quantity, sku);
//    }
//
//    public String getColor() {
//        return color;
//    }
//
//    public void setColor(String color) {
//        this.color = color;
//    }
//
//    public int getSize() {
//        return size;
//    }
//
//    public void setSize(int size) {
//        this.size = size;
//    }
//}
//
//class ElectronicProducts extends Product {
//    int warrantyPeriod;
//    String brand;
//
//    public ElectronicProducts(String name, double price, ProductCategories productCategories, int quantity, String sku, String brand, int warrantyPeriod) {
//        super(name, price, productCategories, quantity, sku);
//        this.brand = brand;
//        this.warrantyPeriod = warrantyPeriod;
//    }
//
//    public String getBrand() {
//        return brand;
//    }
//
//    public void setBrand(String brand) {
//        this.brand = brand;
//    }
//
//    public int getWarrantyPeriod() {
//        return warrantyPeriod;
//    }
//
//    public void setWarrantyPeriod(int warrantyPeriod) {
//        this.warrantyPeriod = warrantyPeriod;
//    }
//}
//
//// ==================== REPLENISHMENT STRATEGIES ====================
//
//class AutoMaticReplishment implements ReplenishmentStrategy {
//    @Override
//    public void replenish() {
//        System.out.println("Automatic replenishment triggered - Restocking items to optimal levels");
//    }
//}
//
//class BatchReplishment implements ReplenishmentStrategy {
//    @Override
//    public void replenish() {
//        System.out.println("Batch replenishment triggered - Processing restock in batches");
//    }
//}
//
//// ==================== FACTORY CLASS ====================
//
//class ProductFactory {
//    public Product getProduct(ProductCategories categories, String sku, String name, Double price, int quantity) {
//        switch (categories) {
//            case RETAIL -> {
//                return new RetailsProducts(name, price, categories, quantity, sku);
//            }
//            case ELECTRONIC -> {
//                return new ElectronicProducts(name, price, categories, quantity, sku, null, 0);
//            }
//            default -> throw new IllegalArgumentException();
//        }
//    }
//
//    public Product createProduct(String sku, String name) {
//        return createProduct(sku, name, ProductCategories.RETAIL, 0.0, 1);
//    }
//
//    public Product createProduct(String sku, String name, ProductCategories category, double price, int quantity) {
//        return getProduct(category, sku, name, price, quantity);
//    }
//}
//
//// ==================== SINGLETON INVENTORY MANAGER ====================
//
//class InventoryManager {
//    private static InventoryManager instance;
//    public List<Warehouse> warehouses;
//    public ProductFactory productFactory;
//    public ReplenishmentStrategy replenishmentStrategy;
//
//    private InventoryManager() {
//        warehouses = new ArrayList<>();
//        productFactory = new ProductFactory();
//        replenishmentStrategy = new AutoMaticReplishment();
//    }
//
//    public static synchronized InventoryManager getInstance() {
//        if (instance == null) {
//            instance = new InventoryManager();
//        }
//        return instance;
//    }
//
//    public Warehouse createWarehouse(int id, String location, String name) {
//        Warehouse warehouse = new Warehouse(id, location, name);
//        warehouses.add(warehouse);
//        System.out.println("Warehouse '" + name + "' created and added to inventory manager");
//        return warehouse;
//    }
//
//    public Warehouse createWarehouse(int id, String location, String name, ReplenishmentStrategy strategy) {
//        Warehouse warehouse = new Warehouse(id, location, name);
//        warehouse.switchReplenishmentStrategy(strategy);
//        warehouses.add(warehouse);
//        System.out.println("Warehouse '" + name + "' created with custom strategy and added to inventory manager");
//        return warehouse;
//    }
//
//    public void removeWarehouse(int warehouseId) {
//        warehouses.removeIf(warehouse -> warehouse.id == warehouseId);
//        System.out.println("Warehouse with ID " + warehouseId + " removed from inventory manager");
//    }
//
//    public Warehouse getWarehouseById(int warehouseId) {
//        return warehouses.stream()
//                .filter(warehouse -> warehouse.id == warehouseId)
//                .findFirst()
//                .orElse(null);
//    }
//
//    public void listAllWarehouses() {
//        System.out.println("=== All Warehouses ===");
//        for (Warehouse warehouse : warehouses) {
//            System.out.println("ID: " + warehouse.id + ", Name: " + warehouse.name +
//                             ", Location: " + warehouse.location +
//                             ", Products: " + warehouse.products.size());
//        }
//        System.out.println("========================");
//    }
//}
//
//// ==================== WAREHOUSE CLASS ====================
//
//class Warehouse {
//    int id;
//    String name;
//    String location;
//    Map<String, Product> products;
//    InventoryManager inventoryManager;
//    ReplenishmentStrategy replenishmentStrategy;
//
//    public Warehouse(int id, String location, String name) {
//        this.id = id;
//        this.location = location;
//        this.name = name;
//        this.products = new HashMap<>();
//        this.inventoryManager = InventoryManager.getInstance();
//        this.replenishmentStrategy = new AutoMaticReplishment();
//    }
//
//    public void addProduct(Product product, int quantity) {
//        if (products.containsKey(product.getSku())) {
//            Product existingProduct = products.get(product.getSku());
//            existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
//        } else {
//            product.setQuantity(quantity);
//            products.put(product.getSku(), product);
//        }
//    }
//
//    public boolean removeProduct(String sku, int quantity) {
//        if (products.containsKey(sku)) {
//            Product existingProduct = products.get(sku);
//            if (existingProduct.getQuantity() >= quantity) {
//                existingProduct.setQuantity(existingProduct.getQuantity() - quantity);
//                if (existingProduct.getQuantity() <= 0) {
//                    products.remove(sku);
//                    System.out.println("Product " + existingProduct.getName()
//                            + " removed from inventory as quantity is now zero.");
//
//                    // Trigger replenishment when product is depleted
//                    triggerReplenishment();
//                    return true;
//                }
//            } else {
//                System.out.println("Insufficient quantity. Available: " + existingProduct.getQuantity());
//                return false;
//            }
//        } else {
//            System.out.println("Product not exist");
//            return false;
//        }
//        return true;
//    }
//
//    public void triggerReplenishment() {
//        System.out.println("Triggering replenishment for warehouse: " + name);
//        replenishmentStrategy.replenish();
//    }
//
//    public void switchReplenishmentStrategy(ReplenishmentStrategy newStrategy) {
//        System.out.println("Switching replenishment strategy for warehouse: " + name);
//        this.replenishmentStrategy = newStrategy;
//    }
//
//    public void addWarehouseToInventoryManager() {
//        inventoryManager.warehouses.add(this);
//        System.out.println("Warehouse " + name + " added to inventory manager");
//    }
//
//    public void checkInventoryStatus() {
//        System.out.println("=== Inventory Status for " + name + " ===");
//        System.out.println("Total products: " + products.size());
//        for (Product product : products.values()) {
//            System.out.println("- " + product.getName() + " (SKU: " + product.getSku() +
//                             ", Quantity: " + product.getQuantity() + ")");
//        }
//        System.out.println("================================");
//    }
//}
//
//// ==================== DEMO CLASS ====================
//
//public class WarehouseManagementSystem {
//    public static void main(String[] args) {
//        System.out.println("=== WAREHOUSE MANAGEMENT SYSTEM DEMO ===\n");
//
//        // Get inventory manager instance
//        InventoryManager inventoryManager = InventoryManager.getInstance();
//
//        // Create warehouses using inventory manager methods
//        Warehouse mainWarehouse = inventoryManager.createWarehouse(1, "New York", "Main Warehouse");
//        Warehouse secondaryWarehouse = inventoryManager.createWarehouse(2, "Los Angeles", "Secondary Warehouse", new BatchReplishment());
//
//        // List all warehouses
//        inventoryManager.listAllWarehouses();
//
//        // Create products using factory
//        Product laptop = inventoryManager.productFactory.createProduct("LAP001", "Laptop", ProductCategories.ELECTRONIC, 999.99, 10);
//        Product mouse = inventoryManager.productFactory.createProduct("MOU001", "Mouse", ProductCategories.ELECTRONIC, 25.99, 5);
//        Product keyboard = inventoryManager.productFactory.createProduct("KEY001", "Keyboard", ProductCategories.ELECTRONIC, 49.99, 8);
//
//        // Add products to warehouses
//        mainWarehouse.addProduct(laptop, 10);
//        mainWarehouse.addProduct(mouse, 5);
//        secondaryWarehouse.addProduct(keyboard, 8);
//
//        // Check inventory status
//        mainWarehouse.checkInventoryStatus();
//        secondaryWarehouse.checkInventoryStatus();
//
//        // Use replenishment strategies
//        System.out.println("\n--- Testing Replenishment Strategies ---");
//        System.out.println("Main Warehouse (Automatic):");
//        mainWarehouse.triggerReplenishment();
//
//        System.out.println("\nSecondary Warehouse (Batch):");
//        secondaryWarehouse.triggerReplenishment();
//
//        // Switch strategies at runtime
//        System.out.println("\n--- Switching Strategies ---");
//        mainWarehouse.switchReplenishmentStrategy(new BatchReplishment());
//        mainWarehouse.triggerReplenishment();
//
//        // Remove products and trigger auto-replenishment
//        System.out.println("\n--- Testing Auto-Replenishment on Depletion ---");
//        mainWarehouse.removeProduct("LAP001", 10); // This will trigger replenishment
//        secondaryWarehouse.removeProduct("KEY001", 8);  // This will trigger replenishment
//
//        // Final inventory check
//        mainWarehouse.checkInventoryStatus();
//        secondaryWarehouse.checkInventoryStatus();
//
//        // Test warehouse management methods
//        System.out.println("\n--- Testing Warehouse Management ---");
//        Warehouse foundWarehouse = inventoryManager.getWarehouseById(1);
//        if (foundWarehouse != null) {
//            System.out.println("Found warehouse: " + foundWarehouse.name);
//        }
//
//        // Remove a warehouse
//        inventoryManager.removeWarehouse(2);
//        inventoryManager.listAllWarehouses();
//
//        System.out.println("\n=== DEMO COMPLETED ===");
//    }
//}
