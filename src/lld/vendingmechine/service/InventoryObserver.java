package lld.vendingmechine.service;

public interface InventoryObserver {
    void onProductDispensed(int slotId);
    void onSlotOutOfStock(int slotId);
}
