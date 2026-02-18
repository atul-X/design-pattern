package lld.wearehousemangement;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private static InventoryManager instance;
    public List<Warehouse> warehouses;
    public ProductFactory productFactory;
    public ReplenishmentStrategy   replenishmentStrategy;

    private InventoryManager() {
        warehouses = new ArrayList<>();
        productFactory = new ProductFactory();
        replenishmentStrategy=new AutoMaticReplishment();
    }
    
    public static synchronized InventoryManager getInstance(){
        if (instance == null){
            instance = new InventoryManager();
        }
        return instance;
    }
    
    public Warehouse createWarehouse(int id, String location, String name) {
        Warehouse warehouse = new Warehouse(id, location, name);
        warehouses.add(warehouse);
        System.out.println("Warehouse '" + name + "' created and added to inventory manager");
        return warehouse;
    }
    
    public Warehouse createWarehouse(int id, String location, String name, ReplenishmentStrategy strategy) {
        Warehouse warehouse = new Warehouse(id, location, name);
        warehouse.switchReplenishmentStrategy(strategy);
        warehouses.add(warehouse);
        System.out.println("Warehouse '" + name + "' created with custom strategy and added to inventory manager");
        return warehouse;
    }
    
    public void removeWarehouse(int warehouseId) {
        warehouses.removeIf(warehouse -> warehouse.id == warehouseId);
        System.out.println("Warehouse with ID " + warehouseId + " removed from inventory manager");
    }
    
    public Warehouse getWarehouseById(int warehouseId) {
        return warehouses.stream()
                .filter(warehouse -> warehouse.id == warehouseId)
                .findFirst()
                .orElse(null);
    }
    
    public void listAllWarehouses() {
        System.out.println("=== All Warehouses ===");
        for (Warehouse warehouse : warehouses) {
            System.out.println("ID: " + warehouse.id + ", Name: " + warehouse.name + 
                             ", Location: " + warehouse.location + 
                             ", Products: " + warehouse.products.size());
        }
        System.out.println("========================");
    }
}
