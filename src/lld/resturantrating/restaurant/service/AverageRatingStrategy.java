package lld.resturantrating.restaurant.service;

import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.restaurant.model.Restaurant;

import java.util.List;

public class AverageRatingStrategy implements RestaturantRatingStategry {
    RestaurantService restaurantService;

    public AverageRatingStrategy(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    @Override
    public void updateRating(List<Rating> ratings) {
        double avgRating = calculateAverageRating(ratings);
        if (!ratings.isEmpty()) {
            int restaurantId = ratings.get(0).getRestaurantId();
			Restaurant restaurant=restaurantService.get(restaurantId);
            restaurant.setAvarageRating((int) avgRating);
        }
    }
    
    private double calculateAverageRating(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream()
                .mapToDouble(Rating::getRatingNumber)
                .average()
                .orElse(0.0);
    }
}
