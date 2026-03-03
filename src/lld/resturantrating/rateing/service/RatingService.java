package lld.resturantrating.rateing.service;

import lld.resturantrating.rateing.model.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingService
{
    Map<Integer, Rating> ratingMap;
    List<RateingObservers> listRateingObservers;
    public RatingService() {
        this.ratingMap = new HashMap<>();
        this.listRateingObservers=new ArrayList<>();
    }

    public Rating addRating(Rating rating){
        ratingMap.put(rating.getRestaurantId(),rating);
        List<Rating> ratingList=fetchRateing(rating.getRestaurantId());
        updateRateings(ratingList);
        return rating;
    }
    public List<Rating> fetchRateing(int restatuantId){
        List<Rating> ratings=new ArrayList<>();
        for (Map.Entry<Integer,Rating> ratingEntry:ratingMap.entrySet()){
            if (ratingEntry.getValue().getRestaurantId()==restatuantId){
                ratings.add(ratingEntry.getValue());
            }
        }
        return ratings;
    }

    public List<Rating> fetchRateingByCustomerId(int customerId){
        List<Rating> ratings=new ArrayList<>();
        for (Map.Entry<Integer,Rating> ratingEntry:ratingMap.entrySet()){
            if (ratingEntry.getValue().getCustomerId()==customerId){
                ratings.add(ratingEntry.getValue());
            }
        }
        return ratings;
    }

    public void  registerOberserv(RateingObservers rateingObservers){
        listRateingObservers.add(rateingObservers);
    }

    public void updateRateings(List<Rating> listRating){
        for (RateingObservers rateingObservers:listRateingObservers){
            rateingObservers.update(listRating);
        }
    }

}
