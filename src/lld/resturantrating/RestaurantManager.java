package lld.resturantrating;

import lld.resturantrating.customer.model.Customer;
import lld.resturantrating.customer.service.UserService;
import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.rateing.service.RatingService;
import lld.resturantrating.restaurant.model.Restaurant;
import lld.resturantrating.restaurant.service.*;

import java.util.List;

public class RestaurantManager {
	private static RestaurantManager instance;
	private final RestaurantService restaurantService;
	private final RatingService ratingService;
	private final UserService userService;

	// Constructor Injection - Dependencies injected from outside
	private RestaurantManager(
			RestaurantService restaurantService,
			RatingService ratingService,
			UserService userService) {
		this.restaurantService = restaurantService;
		this.ratingService = ratingService;
		this.userService = userService;
	}

	public static synchronized RestaurantManager getRestaurantManager(RestaurantService restaurantService,
	                                                     RatingService ratingService,
	                                                     UserService userService){
		if(instance!=null){
			return instance;
		}else {
			instance=new RestaurantManager(restaurantService,ratingService,userService);
			return instance;
		}
	}
	public Restaurant addRestaurant(Restaurant restaurant){
		return restaurantService.add(restaurant);
	}
	public List<Restaurant> restaurantList(){
		return restaurantService.listAll();
	}
	public Customer addCustomer(Customer customer){
		return userService.add(customer);
	}
	public List<Restaurant> listRestorantByPincode(String pincode){
		return restaurantService.listByPinCode(pincode);
	}
	public List<Restaurant> listRestorantByPincodeSorted(String pincode,RestaurantSortStrategy strategy){
		return restaurantService.listByPinCodeSorted(pincode, strategy);
	}

	public List<Restaurant> listRestorantByPincodeSortedByRating(String pincode){
		return listRestorantByPincodeSorted(pincode,new SortByRatingStrategy());
	}

	public List<Restaurant> listRestorantByPincodeSortedByPrice(String pinCode){
		return listRestorantByPincodeSorted(pinCode,new SortByPriceStrategy());
	}
	public Rating addRating(Rating rating){
		return ratingService.addRating(rating);
	}

	// Methods for testing and dependency access
	public RestaurantService getRestaurantService() {
		return restaurantService;
	}

	public RatingService getRatingService() {
		return ratingService;
	}

	public UserService getUserService() {
		return userService;
	}
}
