package lld.resturantrating.rateing.service;

import lld.resturantrating.rateing.model.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingService
{
    Map<Integer, List<Rating>> ratingMap;
    
    public RatingService() {
        this.ratingMap = new HashMap<>();
    }

    public Rating addRating(Rating rating){
        List<Rating> ratingList = ratingMap.getOrDefault(rating.getRestaurantId(), new ArrayList<>());
        ratingList.add(rating);
        ratingMap.put(rating.getRestaurantId(), ratingList);
        return rating;
    }
    
    public List<Rating> getRatingsByRestaurant(int restaurantId) {
        return ratingMap.getOrDefault(restaurantId, new ArrayList<>());
    }
}
