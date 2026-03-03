package lld.resturantrating.restaurant.model;

import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.rateing.service.RateingObservers;

import java.util.List;

public class Restaurant implements RateingObservers {
    private int id;
    private String name;
    private List<String> serviceablePinCode;
    private int avarageRating;
    private String food;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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
    public void update(List<Rating> rating) {
        int sum=0;
        for (Rating rate:rating){
            sum+=rate.ratingNumber;
        }
        int avg=sum/rating.size();
        setAvarageRating(avg);
    }

    @Override
    public String toString() {
        return "Restaurant{" + "id=" + id + ", name='" + name + '\'' + ", serviceablePinCode=" + serviceablePinCode + ", avarageRating=" + avarageRating + ", food='" + food + '\'' + ", price='" + price + '\'' + '}';
    }
}
