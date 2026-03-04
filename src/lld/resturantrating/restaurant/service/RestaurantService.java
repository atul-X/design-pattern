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

    public Restaurant get(int id){
        return restaurantMap.get(id);
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
    public List<Restaurant> listByPinCodeSorted(String pincode, RestaurantSortStrategy strategy){
        return restaurantMap.values().stream()
                .filter(r -> r.getServiceablePinCode().contains(pincode))
                .sorted(strategy.getComparator())
                .collect(java.util.stream.Collectors.toList());
    }

}
