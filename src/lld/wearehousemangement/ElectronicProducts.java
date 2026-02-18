package lld.wearehousemangement;

public class ElectronicProducts extends Product{
    int warrantyPeriod;
    String brand;


    public ElectronicProducts(String name, double price, ProductCategories productCategories, int quantity, String sku, String brand, int warrantyPeriod) {
        super(name, price, productCategories, quantity, sku);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }


    public String getBrand() {
        return brand;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }


    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }


    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
}
