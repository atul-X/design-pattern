package lld.wearehousemangement;

public class RetailsProducts extends Product{
    String color;
    int size;


    public RetailsProducts(String name, double price, ProductCategories productCategories, int quantity, String sku) {
        super(name, price, productCategories, quantity, sku);
    }


    public String getColor() {
        return color;
    }


    public void setColor(String color) {
        this.color = color;
    }


    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }
}
