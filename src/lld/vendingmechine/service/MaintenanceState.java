package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.VendingMachineState;

public class MaintenanceState implements VendingMachineState {
    @Override
    public void selectProduct(VendingMachineContext context, int slotId) {

    }

    @Override
    public void insertCoin(VendingMachineContext context, Coin coin) {

    }

    @Override
    public void dispenseProduct(VendingMachineContext context) {

    }

    @Override
    public void cancelTransaction(VendingMachineContext context) {

    }

    @Override
    public void enterMaintenance(VendingMachineContext context) {

    }

    @Override
    public void exitMaintenance(VendingMachineContext context) {

    }

    @Override
    public String getStateName() {
        return "";
    }
}
