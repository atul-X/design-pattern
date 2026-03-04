package lld.resturantrating.restaurant.model;


import java.util.List;

public class Restaurant  {
    private int id;
    private String name;
    private List<String> serviceablePinCode;
    private int avarageRating;
    private String food;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getServiceablePinCode() {
        return serviceablePinCode;
    }

    public void setServiceablePinCode(List<String> serviceablePinCode) {
        this.serviceablePinCode = serviceablePinCode;
    }

    public int getAvarageRating() {
        return avarageRating;
    }

    public void setAvarageRating(int avarageRating) {
        this.avarageRating = avarageRating;
    }

    @Override
    public String toString() {
        return "Restaurant{" + "id=" + id + ", name='" + name + '\'' + ", serviceablePinCode=" + serviceablePinCode + ", avarageRating=" + avarageRating + ", food='" + food + '\'' + ", price='" + price + '\'' + '}';
    }
}
