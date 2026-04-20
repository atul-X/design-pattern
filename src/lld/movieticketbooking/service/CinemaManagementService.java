package lld.movieticketbooking.service;

import lld.movieticketbooking.model.Cinema;

import java.util.HashMap;
import java.util.Map;

public class CinemaManagementService {
    private final Map<Integer, Cinema> cinemaMap = new HashMap<>();

    public Cinema addCinema(Cinema cinema) {
        cinemaMap.put(cinema.getId(), cinema);
        return cinema;
    }

    public Cinema getCinema(int cinemaId) {
        return cinemaMap.get(cinemaId);
    }

    public boolean exists(int cinemaId) {
        return cinemaMap.containsKey(cinemaId);
    }
}