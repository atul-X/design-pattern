package lld.resturantrating;

import lld.resturantrating.customer.model.Customer;
import lld.resturantrating.customer.model.Gender;
import lld.resturantrating.customer.service.UserService;
import lld.resturantrating.rateing.model.Rating;
import lld.resturantrating.rateing.service.RatingService;
import lld.resturantrating.restaurant.model.Restaurant;
import lld.resturantrating.restaurant.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;

public class RestaurantManger {

    public static void main(String[] args) {
        UserService userService=new UserService();
        Customer customer=new Customer();
        customer.setId(1);
        customer.setName("Pralove");
        customer.setGender(Gender.M);
        customer.setPinCode("560035");
        customer.setMobileNo("123");

        userService.add(customer);
        customer.setId(2);
        Customer customer1=new Customer();
        customer1.setName("Nitesh");
        customer1.setGender(Gender.M);
        customer1.setPinCode("560088");
        customer1.setMobileNo("124");
        userService.add(customer);

        Customer customer2=new Customer();
        customer.setId(3);
        customer2.setName("Vatsal");
        customer2.setGender(Gender.M);
        customer2.setPinCode("560088");
        customer2.setMobileNo("125");
        userService.add(customer);
        //Restaurant
        RestaurantService restaurantService=new RestaurantService();
        Restaurant restaurant=new Restaurant();
        restaurant.setId(1);
        restaurant.setName("Food Court-1”");
        List<String> arryList=new ArrayList<>();
        arryList.add("560088");
        arryList.add("560035");
        restaurant.setFood("NI Thali");
        restaurant.setPrice("100");
        restaurant.setServiceablePinCode(arryList);
        Restaurant restaurantCreated1=restaurantService.add(restaurant);
        Restaurant restaurant1=new Restaurant();
        restaurant1.setId(2);
        restaurant1.setName("Food Court-2”");
        List<String> arryList1=new ArrayList<>();
        arryList1.add("560088");
        restaurant1.setFood("Burger");
        restaurant1.setPrice("120");
        restaurant1.setServiceablePinCode(arryList1);
        Restaurant restaurantCreated2=restaurantService.add(restaurant1);
        Restaurant restaurant2=new Restaurant();
        restaurant2.setId(3);
        restaurant2.setName("Food Court-3”");
        List<String> arryList2=new ArrayList<>();
        arryList2.add("560035");
        restaurant2.setFood("SI Thali");
        restaurant2.setPrice("150");
        restaurant2.setServiceablePinCode(arryList2);
        Restaurant restaurantCreated3=restaurantService.add(restaurant2);

        List<Restaurant> restaurantList1=restaurantService.listByPinCode(customer1.getPinCode());
        System.out.println("====Restaurant start=====");
        for (Restaurant rest:restaurantList1){
            System.out.println(rest.toString());
        }
        System.out.println("====Restaurant end=====");

        RatingService ratingService=new RatingService();
        ratingService.registerOberserv(restaurantCreated1);
        ratingService.registerOberserv(restaurantCreated2);
        Rating rating=new Rating();
        rating.setId(1);
        rating.setRestaurantId(restaurant1.getId());
        rating.setCustomerId(customer.getId());
        rating.setRatingNumber(3);
        rating.setDescriptions("Good Food");
        ratingService.addRating(rating);
        Rating rating1=new Rating();
        rating1.setId(2);
        rating1.setRestaurantId(restaurant.getId());
        rating1.setCustomerId(customer1.getId());
        rating1.setRatingNumber(5);
        rating1.setDescriptions("Good Food");
        ratingService.addRating(rating1);

        List<Restaurant> restaurantList2=restaurantService.listByPinCodeSortedOrderByRateing(customer.getPinCode());
        System.out.println("====Restaurant start ratings=====");
        for (Restaurant rest:restaurantList2){
            System.out.println(rest.toString());
        }
        System.out.println("====Restaurant end ratings=====");

        System.out.println("====avaerge   ratings=====");
        System.out.println(restaurantCreated1.getAvarageRating());
    }
}
