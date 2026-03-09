package lld.vendingmechine.service;

import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.Slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private Map<Integer, Slot> slotMap;
    private List<InventoryObserver> observerList;

    public InventoryService() {
        this.slotMap = new HashMap<>();
        this.observerList = new ArrayList<>();
    }
    public boolean isSlotAvailable(int slotId) {
        Slot slot = slotMap.get(slotId);
        return slot != null && slot.isOperational() && slot.getCurrentQuantity() > 0;
    }
    public Product getProductFromSlot(int slotId) {
        Slot slot = slotMap.get(slotId);
        return slot != null ? slot.getProduct() : null;
    }
    public Slot addSlot(Slot slot) {
        this.slotMap.put(slot.getId(), slot);
        return slot;
    }
    public Slot removeSlot(int slotId) {
        return this.slotMap.remove(slotId);
    }
    public Slot getSlot(int slotId) {
        return this.slotMap.get(slotId);
    }
    public void notifyDeepensProduct(int slotId){
        for (InventoryObserver observer:observerList){
            observer.onProductDispensed(slotId);
        }
    }
    public List<Slot> getAllSlots() {
        return new ArrayList<>(slotMap.values());
    }
    public void notifySlotOutOfStock(int slotId){
        for (InventoryObserver observer:observerList){
            observer.onSlotOutOfStock(slotId);
        }
    }

    
    public Product dispenseProduct(int slotId) {
        Slot slot = slotMap.get(slotId);
        if (slot == null || slot.getCurrentQuantity() <= 0) {
            throw new IllegalStateException("Slot not available or empty");
        }
        
        Product product = slot.getProduct();
        slot.setCurrentQuantity(slot.getCurrentQuantity() - 1);
        
        notifyDeepensProduct(slotId);
        
        if (slot.getCurrentQuantity() == 0) {
            notifySlotOutOfStock(slotId);
        }
        
        return product;
    }
}
