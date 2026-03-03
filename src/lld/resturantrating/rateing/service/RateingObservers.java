package lld.resturantrating.rateing.service;

import lld.resturantrating.rateing.model.Rating;

import java.util.List;

public interface RateingObservers {
    void update(List<Rating> rating);
}
