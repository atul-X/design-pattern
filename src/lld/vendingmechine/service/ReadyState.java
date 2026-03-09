package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.VendingMachineState;

public class ReadyState implements VendingMachineState {

    @Override
    public void selectProduct(VendingMachineContext context, int slotId) {
        if (context.getInventoryService().isSlotAvailable(slotId)){
            context.setSelectedSlotId(slotId);
            Product product = context.getInventoryService().getProductFromSlot(slotId);
            context.setSelectedProduct(product);
            context.setState(new ItemSelectState());
            System.out.println("Product selected: " + product.getName() + " - $" + product.getPrice());
        } else {
            System.out.println("Product not available in slot " + slotId);
        }
    }

    @Override
    public void insertCoin(VendingMachineContext context, Coin coin) {
        System.out.println("Please select a product first");

    }

    @Override
    public void dispenseProduct(VendingMachineContext context) {
        System.out.println("Please select a product and pay first");

    }

    @Override
    public void cancelTransaction(VendingMachineContext context) {
        System.out.println("No transaction to cancel");

    }

    @Override
    public void enterMaintenance(VendingMachineContext context) {
        context.setState(new MaintenanceState());
        System.out.println("Entering maintenance mode");
    }

    @Override
    public void exitMaintenance(VendingMachineContext context) {
        System.out.println("Not in maintenance mode");

    }

    @Override
    public String getStateName() {
        return "READY";
    }
}
