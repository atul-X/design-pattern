package lld.vendingmechine.model;

public class Product {
    private int skuId;
    private String name;
    private String category;
    private double price;
    private int slotId;

    public Product(int id,String name, double price) {
        this.skuId=id;
        this.name = name;
        this.price = price;
        this.slotId = id; // Set slotId
    }

    public int getSkuId() {
        return skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
}
