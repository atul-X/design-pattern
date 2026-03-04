package lld.resturantrating.restaurant.service;

import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.restaurant.model.Restaurant;

import java.util.List;

public class BayesianRatingStrategy implements RestaturantRatingStategry {

    private static final double MIN_RATINGS = 3.0;
    private static final double AVERAGE_RATING = 3.5;
    private final RestaurantService restaurantService;

    public BayesianRatingStrategy(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    
    @Override
    public void updateRating(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return;
        }
        double bayesianRating = calculateBayesianRating(ratings);
        int restaurantId = ratings.get(0).getRestaurantId();
        Restaurant restaurant = restaurantService.get(restaurantId);
        restaurant.setAvarageRating((int)bayesianRating);
    }
    
    private double calculateBayesianRating(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return AVERAGE_RATING;
        }
        double average = ratings.stream()
                .mapToDouble(Rating::getRatingNumber)
                .average()
                .orElse(AVERAGE_RATING);
        double ratingCount = ratings.size();
        return (MIN_RATINGS * AVERAGE_RATING + ratingCount * average) / (MIN_RATINGS + ratingCount);
    }
}
