package lld.resturantrating.restaurant.service;

import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.restaurant.model.Restaurant;

import java.util.List;

public interface RestaturantRatingStategry {
	public void updateRating(List<Rating> rating);
}
