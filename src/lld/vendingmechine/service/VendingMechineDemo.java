package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.Slot;

public class VendingMechineDemo {
    public static void main(String[] args) {
        // ✅ Only create manager - everything else is handled internally
        VendingMachineManager manager = new VendingMachineManager();
        setupMachine(manager);
        // ✅ Display initial status
        System.out.println("=== Vending Machine Demo ===");
        manager.displayAvailableProducts();
        manager.displayMachineStatus();

        // ✅ Complete purchase flow
        System.out.println("\n=== Purchase Flow ===");

        // 1. Select product
        System.out.println("1. Selecting product...");
        manager.selectProduct(1);
        manager.displayMachineStatus();

        // 2. Insert coins
        System.out.println("\n2. Inserting coins...");
        manager.insertCoin(Coin.DOLLAR);
        manager.insertCoin(Coin.QUARTER);
        manager.insertCoin(Coin.QUARTER);
        manager.displayMachineStatus();

        // 3. Dispense product
        System.out.println("\n3. Dispensing product...");
        manager.dispenseProduct();
        manager.displayMachineStatus();

        // 4. Test maintenance mode
        System.out.println("\n=== Maintenance Mode Test ===");
        manager.enterMaintenance();
        manager.displayMachineStatus();

        // Try operations in maintenance
        System.out.println("\nTrying operations in maintenance mode...");
        manager.selectProduct(1);
        manager.insertCoin(Coin.DOLLAR);

        // Exit maintenance
        System.out.println("\nExiting maintenance mode...");
        manager.exitMaintenance();
        manager.displayMachineStatus();
    }
    private static void setupMachine(VendingMachineManager manager) {
        // ✅ Create Slot 1 - Coke
        Slot slot1 = new Slot();
        slot1.setId(1);
        slot1.setProduct(new Product(1,"Coke", 1.50));
        slot1.setCapacity(10);
        slot1.setCurrentQuantity(5);
        slot1.setOperational(true);

        // ✅ Create Slot 2 - Pepsi
        Slot slot2 = new Slot();
        slot2.setId(2);
        slot2.setProduct(new Product(2,"Pepsi", 1.50));
        slot2.setCapacity(10);
        slot2.setCurrentQuantity(3);
        slot2.setOperational(true);

        // ✅ Create Slot 3 - Water
        Slot slot3 = new Slot();
        slot3.setId(3);
        slot3.setProduct(new Product(3,"Water", 1.00));
        slot3.setCapacity(10);
        slot3.setCurrentQuantity(8);
        slot3.setOperational(true);

        // ✅ Create Slot 4 - Chips
        Slot slot4 = new Slot();
        slot4.setId(4);
        slot4.setProduct(new Product(4,"Chips", 1.25));
        slot4.setCapacity(10);
        slot4.setCurrentQuantity(2);
        slot4.setOperational(true);

        // ✅ Add all slots to manager
        manager.addProductToSlot(slot1);
        manager.addProductToSlot(slot2);
        manager.addProductToSlot(slot3);
        manager.addProductToSlot(slot4);
    }
}

