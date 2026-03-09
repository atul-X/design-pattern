package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.VendingMachineState;

import java.util.Map;

public class DispenseState implements VendingMachineState {
    @Override
    public void selectProduct(VendingMachineContext context, int slotId) {
        System.out.println("Cannot select product during dispensing");

    }

    @Override
    public void insertCoin(VendingMachineContext context, Coin coin) {
        System.out.println("Cannot insert coin during dispensing");

    }

    @Override
    public void dispenseProduct(VendingMachineContext context) {
        try {
            // Create and execute dispense command through CommandInvoker
            DispenseProductCommand dispenseCommand = new DispenseProductCommand(
                context.getInventoryService(), context.getSelectedSlotId());
            
            context.getCommandInvoker().executeCommand(dispenseCommand);
            
            Product dispensedProduct = dispenseCommand.getDispenseProduct();
            
            // Fix method name
            double amountPaid = context.getPaymentService().getTotalInserted();
            double changeAmount = amountPaid - context.getSelectedProduct().getPrice();
            
            if (changeAmount > 0) {
                Map<Coin, Integer> change = context.getChangeService().calculateChange(changeAmount);
                System.out.println("Change returned: $" + String.format("%.2f", changeAmount));
            }

            System.out.println("Product dispensed: " + dispensedProduct.getName());

            // Reset and return to ready state
            context.resetTransaction();
            context.setState(new ReadyState());
            
        } catch (Exception e) {
            System.out.println("Dispensing failed: " + e.getMessage());
            // Refund payment
            context.getPaymentService().clearInsertedCoins();
            context.resetTransaction();
            context.setState(new ReadyState());
        }
    }

    @Override
    public void cancelTransaction(VendingMachineContext context) {
        System.out.println("Cannot cancel during dispensing");
    }

    @Override
    public void enterMaintenance(VendingMachineContext context) {
        System.out.println("Cannot enter maintenance during dispensing");
    }

    @Override
    public void exitMaintenance(VendingMachineContext context) {
        System.out.println("Not in maintenance mode");
    }

    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
