package lld.resturantrating.restaurant.service;

import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.restaurant.model.Restaurant;

import java.util.List;

public class WeightedRatingStrategy implements RestaturantRatingStategry {

    private final RestaurantService restaurantService;

    public WeightedRatingStrategy(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @Override
    public void updateRating(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return;
        }
        double weightedRating = calculateWeightedRating(ratings);
        int restaurantId = ratings.get(0).getRestaurantId();
        Restaurant restaurant = restaurantService.get(restaurantId);
        restaurant.setAvarageRating((int)weightedRating);
    }
    
    private double calculateWeightedRating(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double weightedSum = 0;
        double totalWeight = 0;
        for (int i = 0; i < ratings.size(); i++) {
            double weight = i + 1;
            weightedSum += ratings.get(i).getRatingNumber() * weight;
            totalWeight += weight;
        }
        return weightedSum / totalWeight;
    }
}
