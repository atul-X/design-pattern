package lld.wearehousemangement;

import java.util.HashMap;
import java.util.Map;

public class Warehouse {
    int id;
    String name;
    String location;
    Map<String,Product> products;
    InventoryManager inventoryManager;
    ReplenishmentStrategy replenishmentStrategy;



    public Warehouse(int id, String location, String name) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.products = new HashMap<>();
        this.inventoryManager = InventoryManager.getInstance();
        this.replenishmentStrategy = new AutoMaticReplishment();
    }
    
    public void addProduct(Product product,int quantity){
        if (products.containsKey(product.getSku())){
            Product existingProduct=products.get(product.getSku());
            existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
        }else {
            product.setQuantity(quantity);
            products.put(product.getSku(),product);
        }
    }
    
    public boolean removeProduct(String sku,int quantity){
        if (products.containsKey(sku)){
            Product existingProduct=products.get(sku);
            if (existingProduct.getQuantity() >= quantity){
                existingProduct.setQuantity(existingProduct.getQuantity() - quantity);
                if (existingProduct.getQuantity() <= 0){
                    products.remove(sku);
                    System.out.println("Product " + existingProduct.getName()
                            + " removed from inventory as quantity is now zero.");
                    
                    // Trigger replenishment when product is depleted
                    triggerReplenishment();
                    return true;
                }
            } else {
                System.out.println("Insufficient quantity. Available: " + existingProduct.getQuantity());
                return false;
            }
        }else {
            System.out.println("Product not exist");
            return false;
        }
        return true;
    }
    
    public void triggerReplenishment() {
        System.out.println("Triggering replenishment for warehouse: " + name);
        replenishmentStrategy.replenish();
    }
    
    public void switchReplenishmentStrategy(ReplenishmentStrategy newStrategy) {
        System.out.println("Switching replenishment strategy for warehouse: " + name);
        this.replenishmentStrategy = newStrategy;
    }
    
    public void addWarehouseToInventoryManager() {
        inventoryManager.warehouses.add(this);
        System.out.println("Warehouse " + name + " added to inventory manager");
    }
    
    public void checkInventoryStatus() {
        System.out.println("=== Inventory Status for " + name + " ===");
        System.out.println("Total products: " + products.size());
        for (Product product : products.values()) {
            System.out.println("- " + product.getName() + " (SKU: " + product.getSku() + 
                             ", Quantity: " + product.getQuantity() + ")");
        }
        System.out.println("================================");
    }
}
