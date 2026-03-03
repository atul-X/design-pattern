package lld.resturantrating.restaurant.service;

import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.rateing.service.RatingService;
import lld.resturantrating.restaurant.model.Restaurant;

import java.util.*;

public class RestaurantService {
    Map<Integer, Restaurant>  restaurantMap;
    RatingService ratingService;
    public RestaurantService() {
        this.restaurantMap = new HashMap<>();
        this.ratingService=new RatingService();
    }

    public Restaurant add(Restaurant restaurant){
        restaurantMap.put(restaurant.getId(),restaurant);
        return restaurant;
    }
    public List<Restaurant> listAll(){
        List<Restaurant> restaurants=new ArrayList<>();
        for (Map.Entry<Integer,Restaurant> restaurantEntry:restaurantMap.entrySet()){
            restaurants.add(restaurantEntry.getValue());
        }
        return restaurants;
    }

    public List<Restaurant> listByPinCode(String pincode){
        List<Restaurant> restaurants=new ArrayList<>();
        for (Map.Entry<Integer,Restaurant> restaurantEntry:restaurantMap.entrySet()){
            if (restaurantEntry.getValue().getServiceablePinCode().contains(pincode)){
                restaurants.add(restaurantEntry.getValue());
            }
        }
        return restaurants;
    }
    public List<Restaurant> listByPinCodeSortedOrderByRateing(String pincode){
        List<Restaurant> restaurants=new ArrayList<>();
        for (Map.Entry<Integer,Restaurant> restaurantEntry:restaurantMap.entrySet()){
            if (restaurantEntry.getValue().getServiceablePinCode().contains(pincode)){
                restaurants.add(restaurantEntry.getValue());
            }
        }
        Collections.sort(restaurants,(a,b)->{
            return a.getAvarageRating()-b.getAvarageRating();
        });
        return restaurants;
    }

    //overlap
    // store restaurent= serviceable by city based on punecode;

    //pune ->aundh-411007
    //pune ->aundh-411005
    //lat-lng -> 3km
}
