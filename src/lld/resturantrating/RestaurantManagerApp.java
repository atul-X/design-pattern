package lld.resturantrating;

import lld.resturantrating.customer.service.UserService;
import lld.resturantrating.rateing.service.RatingService;
import lld.resturantrating.restaurant.service.RestaurantService;

public class RestaurantManagerApp {
	public static void main(String[] args) {
		RestaurantManager restaurantManager=RestaurantManager.getRestaurantManager(new RestaurantService(),new RatingService(),new UserService());
	}
}
