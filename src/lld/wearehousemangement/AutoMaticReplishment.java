package lld.wearehousemangement;

import java.util.HashMap;
import java.util.Map;

public class AutoMaticReplishment implements ReplenishmentStrategy {
    // Simulated inventory thresholds for different product types
    private static final Map<String, Integer> REORDER_POINTS = new HashMap<>();
    private static final Map<String, Integer> SAFETY_STOCK = new HashMap<>();
    
    static {
        // Reorder points: minimum quantity before replenishment
        REORDER_POINTS.put("LAP001", 5);  // Laptops
        REORDER_POINTS.put("MOU001", 10); // Mice
        REORDER_POINTS.put("KEY001", 8);  // Keyboards
        
        // Safety stock: extra buffer stock
        SAFETY_STOCK.put("LAP001", 15);
        SAFETY_STOCK.put("MOU001", 25);
        SAFETY_STOCK.put("KEY001", 20);
    }
    
    @Override
    public void replenish() {
        System.out.println("🤖 Automatic Replenishment Activated:");
        System.out.println("   - Analyzing current inventory levels...");
        
        // Simulate checking each product's inventory level
        for (String sku : REORDER_POINTS.keySet()) {
            int reorderPoint = REORDER_POINTS.get(sku);
            int safetyStock = SAFETY_STOCK.get(sku);
            int optimalLevel = reorderPoint + safetyStock;
            
            System.out.println("   - Product " + sku + ": Reorder point = " + reorderPoint + ", Optimal level = " + optimalLevel);
            System.out.println("   - Generating automatic restock order for " + optimalLevel + " units");
        }
        
        System.out.println("   - Placing immediate orders with suppliers...");
        System.out.println("   - Priority shipping enabled for critical items");
        System.out.println("   - Estimated delivery: 2-3 business days");
        System.out.println("✅ Automatic replenishment completed - All items restocked to optimal levels!");
    }
}
