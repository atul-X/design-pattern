package lld.wearehousemangement;

public abstract class Product {
    String sku;
    String name;
    double price;
    ProductCategories productCategories;
    int quantity;


    public Product(String name, double price, ProductCategories productCategories, int quantity, String sku) {
        this.name = name;
        this.price = price;
        this.productCategories = productCategories;
        this.quantity = quantity;
        this.sku = sku;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public double getPrice() {
        return price;
    }


    public void setPrice(double price) {
        this.price = price;
    }


    public ProductCategories getProductCategories() {
        return productCategories;
    }


    public void setProductCategories(ProductCategories productCategories) {
        this.productCategories = productCategories;
    }


    public int getQuantity() {
        return quantity;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getSku() {
        return sku;
    }


    public void setSku(String sku) {
        this.sku = sku;
    }
}
