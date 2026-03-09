package lld.vendingmechine.model;

import lld.vendingmechine.service.VendingMachineContext;

public interface VendingMachineState {
    void selectProduct(VendingMachineContext context, int slotId);
    void insertCoin(VendingMachineContext context, Coin coin);
    void dispenseProduct(VendingMachineContext context);
    void cancelTransaction(VendingMachineContext context);
    void enterMaintenance(VendingMachineContext context);
    void exitMaintenance(VendingMachineContext context);
    String getStateName();
}
