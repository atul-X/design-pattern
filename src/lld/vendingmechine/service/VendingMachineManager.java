package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.Slot;

public class VendingMachineManager {
    private VendingMachineContext context;
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private ChangeService changeService;

    public VendingMachineManager(){
        this.paymentService=new PaymentService(new CoinPaymentStrategy());
        this.inventoryService=new InventoryService();
        this.changeService=new ChangeService();
        this.context = new VendingMachineContext(new ReadyState(), paymentService, inventoryService);
    }

    // Public methods - delegate to context
    public void selectProduct(int slotId) {
        context.selectProduct(slotId);
    }

    public void insertCoin(Coin coin) {
        context.insertCoin(coin);
    }

    public void dispenseProduct() {
        context.dispenseProduct();
    }

    public void cancelTransaction() {
        context.cancelTransaction();
    }

    public void enterMaintenance() {
        context.enterMaintenance();
    }

    public void exitMaintenance() {
        context.exitMaintenance();
    }

    // Status methods
    public String getCurrentState() {
        return context.getCurrentState().getStateName();
    }

    public double getInsertedAmount() {
        return context.getPaymentService().getTotalInserted();
    }

    public Product getSelectedProduct() {
        return context.getSelectedProduct();
    }

    public VendingMachineContext getContext() {
        return context;
    }
    public void addProductToSlot(Slot slot){
        inventoryService.addSlot(slot);
    }
    public void removeProductFromSlot(int slotId){
        inventoryService.removeSlot(slotId);
    }
    public void displayAvailableProducts() {
        System.out.println("=== Available Products ===");
        for (Slot slot : context.getInventoryService().getAllSlots()) {
            System.out.println("Slot " + slot.getId() + ": " +
                    slot.getProduct().getName() + " - $" +
                    slot.getProduct().getPrice() + " (" +
                    slot.getCurrentQuantity() + " available)");
        }
    }

    public void displayMachineStatus() {
        System.out.println("=== Machine Status ===");
        System.out.println("Current State: " + getCurrentState());
        System.out.println("Selected Product: " +
                (getSelectedProduct() != null ? getSelectedProduct().getName() : "None"));
        System.out.println("Inserted Amount: $" + String.format("%.2f", getInsertedAmount()));
    }
}
