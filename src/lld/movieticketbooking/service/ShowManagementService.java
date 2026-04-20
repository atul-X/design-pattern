package lld.movieticketbooking.service;

import lld.movieticketbooking.model.Movie;
import lld.movieticketbooking.model.Show;

import java.util.*;
import java.util.stream.Collectors;

public class ShowManagementService {
    // primary store: showId → Show
    private final Map<Integer, Show> showMap = new HashMap<>();

    // index for fast city-based lookup: cityId → List<Show>
    private final Map<Integer, List<Show>> showsByCity = new HashMap<>();

    // index for fast cinema-based lookup: cinemaId → List<Show>
    private final Map<Integer, List<Show>> showsByCinema = new HashMap<>();

    public Show addShow(Show show) {
        showMap.put(show.getShowId(), show);

        showsByCity
            .computeIfAbsent(show.getCityId(), k -> new ArrayList<>())
            .add(show);

        showsByCinema
            .computeIfAbsent(show.getCinemaId(), k -> new ArrayList<>())
            .add(show);

        return show;
    }

    public Show getShow(int showId) {
        return showMap.get(showId);
    }

    public List<Show> getAllShows() {
        return new ArrayList<>(showMap.values());
    }

    // returns all shows running in a city
    public List<Show> getShowsByCity(int cityId) {
        return showsByCity.getOrDefault(cityId, Collections.emptyList());
    }

    // returns unique movies playing in a city — this is the core query
    public List<Movie> getMoviesByCity(int cityId) {
        return getShowsByCity(cityId).stream()
                .map(Show::getMovie)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    // returns all shows in a specific cinema hall
    public List<Show> getShowsByCinema(int cinemaId) {
        return showsByCinema.getOrDefault(cinemaId, Collections.emptyList());
    }

    // returns unique movies playing in a cinema hall
    public List<Movie> getMoviesByCinema(int cinemaId) {
        return getShowsByCinema(cinemaId).stream()
                .map(Show::getMovie)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    // returns all shows for a specific movie
    public List<Show> getShowsByMovie(int movieId) {
        return showMap.values().stream()
                .filter(s -> s.getMovie() != null && s.getMovie().getMovieId() == movieId)
                .collect(Collectors.toList());
    }
}