package lld.wearehousemangement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchReplishment implements ReplenishmentStrategy {
    // Simulated supplier grouping and batch optimization
    private static final Map<String, List<String>> SUPPLIER_GROUPS = new HashMap<>();
    private static final Map<String, Double> BULK_DISCOUNTS = new HashMap<>();
    
    static {
        // Group products by supplier
        List<String> techSupplier = new ArrayList<>();
        techSupplier.add("LAP001");
        techSupplier.add("MOU001");
        techSupplier.add("KEY001");
        SUPPLIER_GROUPS.put("TechCorp", techSupplier);
        
        // Bulk discount rates
        BULK_DISCOUNTS.put("TechCorp", 0.15); // 15% discount for bulk orders
        BULK_DISCOUNTS.put("OfficeSupply", 0.12); // 12% discount
    }
    
    @Override
    public void replenish() {
        System.out.println("📦 Batch Replenishment Process Started:");
        System.out.println("   - Collecting all low-stock items across warehouse...");
        
        double totalSavings = 0.0;
        int totalItems = 0;
        
        // Process each supplier group
        for (Map.Entry<String, List<String>> entry : SUPPLIER_GROUPS.entrySet()) {
            String supplier = entry.getKey();
            List<String> products = entry.getValue();
            double discount = BULK_DISCOUNTS.getOrDefault(supplier, 0.0);
            
            System.out.println("   - Processing batch order for " + supplier + ":");
            
            for (String product : products) {
                int batchQuantity = getBatchQuantity(product);
                double unitCost = getUnitCost(product);
                double savings = unitCost * batchQuantity * discount;
                
                System.out.println("     • " + product + ": " + batchQuantity + " units @ $" + 
                                 String.format("%.2f", unitCost) + " each");
                System.out.println("     • Bulk discount: " + (int)(discount * 100) + "% (Savings: $" + 
                                 String.format("%.2f", savings) + ")");
                
                totalItems += batchQuantity;
                totalSavings += savings;
            }
            
            System.out.println("   - Consolidated order placed with " + supplier);
        }
        
        System.out.println("   - Optimizing shipping for " + totalItems + " total items");
        System.out.println("   - Scheduling weekly batch delivery");
        System.out.println("   - Estimated delivery: 5-7 business days");
        System.out.println("💰 Total cost savings through bulk ordering: $" + String.format("%.2f", totalSavings));
        System.out.println("📊 Batch replenishment completed - Cost efficiency achieved!");
    }
    
    private int getBatchQuantity(String sku) {
        // Return optimized batch quantities for cost efficiency
        switch (sku) {
            case "LAP001": return 50;  // Laptops - bulk order
            case "MOU001": return 100; // Mice - high volume
            case "KEY001": return 80;  // Keyboards - medium volume
            default: return 25;
        }
    }
    
    private double getUnitCost(String sku) {
        // Simulated unit costs
        switch (sku) {
            case "LAP001": return 850.0;
            case "MOU001": return 20.0;
            case "KEY001": return 35.0;
            default: return 50.0;
        }
    }
}
