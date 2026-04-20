package lld.movieticketbooking.service;

import lld.movieticketbooking.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieSerivce {
    Map<Integer, Movie> mapOfMovies = new HashMap<>();

    public Movie addMovie(Movie movie) {
        mapOfMovies.put(movie.getMovieId(), movie);
        return movie;
    }

    public Movie getMovie(int movieId) {
        return mapOfMovies.get(movieId);
    }

    public List<Movie> getListOfMovies() {
        return new ArrayList<>(mapOfMovies.values());
    }
}