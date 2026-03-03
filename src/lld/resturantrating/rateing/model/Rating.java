package lld.resturantrating.rateing.model;

public class Rating  {
    private int id;
    public int customerId;
    public int ratingNumber;
    public String descriptions;
    public int restaurantId;

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(int ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public String toString() {
        return "Rating{" + "id=" + id + ", customerId='" + customerId + '\'' + ", ratingNumber=" + ratingNumber + ", descriptions='" + descriptions + '\'' + ", restaurantId=" + restaurantId + '}';
    }
}
