package lld.vendingmechine.service;

import lld.vendingmechine.model.Coin;
import lld.vendingmechine.model.Product;
import lld.vendingmechine.model.Slot;
import lld.vendingmechine.model.VendingMachineState;

import java.util.List;

public class VendingMachineContext {
    private VendingMachineState currentState;
    private PaymentService paymentService;
    private InventoryService inventoryService;
    private ChangeService changeService;
    private CommandInvoker commandInvoker;

    public VendingMachineContext(VendingMachineState currentState, PaymentService paymentService, InventoryService inventoryService) {
        this.currentState = currentState;
        this.paymentService = paymentService;
        this.changeService=new ChangeService();
        this.inventoryService = inventoryService;
        this.commandInvoker = new CommandInvoker();
    }
    private int selectedSlotId;
    private Product selectedProduct;
    private double totalAmount;

    public VendingMachineState getCurrentState() {
        return currentState;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public int getSelectedSlotId() {
        return selectedSlotId;
    }

    public void setSelectedSlotId(int selectedSlotId) {
        this.selectedSlotId = selectedSlotId;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void  setState(VendingMachineState vendingMachineState){
        System.out.println("State transition: " +
                currentState.getStateName() + " -> " + vendingMachineState.getStateName());
        this.currentState=vendingMachineState;
    }
    
    public void  setCurrentState(VendingMachineState vendingMachineState){
        setState(vendingMachineState);
    }
    
    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }
    public VendingMachineState getVendingMachineState(){
        return currentState;
    }
    public void selectProduct(int slotId){
        currentState.selectProduct(this,slotId);
    }
    public void insertCoin(Coin coin){
        currentState.insertCoin(this,coin);
    }
    public void dispenseProduct(){
        currentState.dispenseProduct(this);
    }
    public void cancelTransaction(){
        currentState.cancelTransaction(this);
    }
    public void enterMaintenance(){
        currentState.enterMaintenance(this);
    }
    public void exitMaintenance(){
        currentState.exitMaintenance(this);
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public ChangeService getChangeService() {
        return changeService;
    }

    public void setChangeService(ChangeService changeService) {
        this.changeService = changeService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }
    public Product getProductFromSlot(int slotId) {
        return inventoryService.getProductFromSlot(slotId);
    }
    public void resetTransaction() {
        selectedSlotId = 0;
        selectedProduct = null;
        totalAmount = 0.0;
    }

}
