package lld.vendingmechine.service;

import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.Slot;

public class DispenseProductCommand implements Command{
    private InventoryService inventoryService;
    private int  slotId;
    private Product dispenseProduct;
    private int previousQuantity;
    private boolean executed=false;

    public DispenseProductCommand(InventoryService inventoryService, int slotId) {
        this.inventoryService = inventoryService;
        this.slotId = slotId;
        this.dispenseProduct = dispenseProduct;
    }

    @Override
    public void execute() {
        if(!executed){
            if(!inventoryService.isSlotAvailable(slotId)){
                throw new IllegalStateException("Slot not available");
            }
            
            // Use the new dispenseProduct method
            dispenseProduct = inventoryService.dispenseProduct(slotId);
            executed = true;
        }
    }

    @Override
    public void undo() {
        if (executed){
            // For undo, we need to restore the product to inventory
            // This is a simplified undo - in reality, you'd need to track the previous quantity
            Slot slot = inventoryService.getSlot(slotId);
            if (slot != null) {
                slot.setCurrentQuantity(slot.getCurrentQuantity() + 1);
                inventoryService.notifyDeepensProduct(slotId);
            }
            executed = false;
        }
    }

    @Override
    public String getCommandType() {
        return "DispenseProductCommand";
    }
    public Product getDispenseProduct() {
        return dispenseProduct;
    }
    public int getSlotId(){
        return slotId;
    }
}
