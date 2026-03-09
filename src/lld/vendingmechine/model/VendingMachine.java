package lld.vendingmechine.model;

import java.util.ArrayList;
import java.util.List;

public class VendingMachine {
    private int machineId;
    private String location;
    private VendingMachineState vendingMachineState;
    private List<Product> products;
//    private List<Transaction> transactionHistory;
    private List<Level> levels;
    private double currentBalance;
    private Product selectedProduct;

    public VendingMachine() {
        this.products = new ArrayList<>();
        this.levels = new ArrayList<>();
        this.products=new ArrayList<>();
        this.currentBalance=0;
        this.selectedProduct=null;
    }
}
