package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.VendingMachineState;

import java.util.List;

public class ItemSelectState implements VendingMachineState {
    @Override
    public void selectProduct(VendingMachineContext context, int slotId) {
        if (context.getInventoryService().isSlotAvailable(slotId)) {
            Product product = context.getInventoryService().getProductFromSlot(slotId);
            context.setSelectedSlotId(slotId);
            context.setSelectedProduct(product);

            System.out.println("Selection changed to: " + product.getName() + " - $" + product.getPrice());
        } else {
            System.out.println("Product not available in slot " + slotId);
        }
    }

    @Override
    public void insertCoin(VendingMachineContext context, Coin coin) {
        boolean inserted = context.getPaymentService().insertCoin(coin);
        if (inserted) {
            System.out.println("Inserted: " + coin.getDisplayName() +
                    " (Total: $" + String.format("%.2f", context.getPaymentService().getTotalInserted()) + ")");
            
            // Debug output
            Product selectedProduct = context.getSelectedProduct();
            System.out.println("Selected product: " + (selectedProduct != null ? selectedProduct.getName() : "null"));

            // Check if payment is complete
            if (selectedProduct != null && 
                context.getPaymentService().getTotalInserted() >= selectedProduct.getPrice()) {
                context.setState(new PaymentPendingState());
            }
        } else {
            System.out.println("Invalid coin: " + coin.getDisplayName());
        }
    }

    @Override
    public void dispenseProduct(VendingMachineContext context) {
        System.out.println("Please complete payment first");

    }

    @Override
    public void cancelTransaction(VendingMachineContext context) {
        List<Coin> insertedCoins = context.getPaymentService().getInsertedCoins();
        if (insertedCoins != null) {
            for (Coin coin : insertedCoins) {
                System.out.println("Refunding: " + coin.getDisplayName());
            }
        }

        // ✅ Use the service method you just added
        context.getPaymentService().clearInsertedCoins();

        // Reset transaction
        context.resetTransaction();
        context.setState(new ReadyState());

        System.out.println("Transaction cancelled");
    }

    @Override
    public void enterMaintenance(VendingMachineContext context) {
        cancelTransaction(context);
        context.setState(new MaintenanceState());
        System.out.println("Entering maintenance mode");
    }

    @Override
    public void exitMaintenance(VendingMachineContext context) {
        System.out.println("Not in maintenance mode");

    }

    @Override
    public String getStateName() {
        return "ITEM_SELECTED";
    }
}
