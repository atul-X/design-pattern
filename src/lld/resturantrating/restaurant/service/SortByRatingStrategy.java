package lld.resturantrating.restaurant.service;

import lld.resturantrating.restaurant.model.Restaurant;

import java.util.Comparator;

public class SortByRatingStrategy implements RestaurantSortStrategy {
    @Override
    public Comparator<Restaurant> getComparator() {
        return Comparator.comparingInt(Restaurant::getAvarageRating);
    }
}
