package lld.resturantrating.restaurant.service;

import lld.resturantrating.restaurant.model.Restaurant;

import java.util.Comparator;

public interface RestaurantSortStrategy {
    Comparator<Restaurant> getComparator();
}
