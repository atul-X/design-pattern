package lld.movieticketbooking.service;

import lld.movieticketbooking.model.Cinema;
import lld.movieticketbooking.model.City;
import lld.movieticketbooking.model.Movie;
import lld.movieticketbooking.model.Show;

import java.util.List;

public class MovieSystemManagement {
    private final CityManagementService cityManagementService;
    private final CinemaManagementService cinemaManagementService;
    private final MovieSerivce movieService;
    private final ShowManagementService showManagementService;

    public MovieSystemManagement(CityManagementService cityManagementService,
                                  CinemaManagementService cinemaManagementService,
                                  MovieSerivce movieService,
                                  ShowManagementService showManagementService) {
        this.cityManagementService = cityManagementService;
        this.cinemaManagementService = cinemaManagementService;
        this.movieService = movieService;
        this.showManagementService = showManagementService;
    }

    // --- City operations ---
    public City addCity(City city) {

        return cityManagementService.addCity(city);
    }

    // --- Cinema operations ---
    public Cinema addCinema(Cinema cinema) {
        return cinemaManagementService.addCinema(cinema);
    }

    // --- Movie operations ---
    public Movie addMovie(Movie movie) {
        return movieService.addMovie(movie);
    }

    // --- Show operations ---
    public Show addShow(Show show) {
        // validate cinema exists before indexing the show
        if (!cinemaManagementService.exists(show.getCinemaId())) {
            throw new IllegalArgumentException("Cinema not found: " + show.getCinemaId()
                    + ". Register the cinema first via addCinema().");
        }
        // validate city exists too
        if (cityManagementService.getCity(show.getCityId()) == null) {
            throw new IllegalArgumentException("City not found: " + show.getCityId());
        }
        return showManagementService.addShow(show);
    }

    // attach a movie to an already-created show
    // works even after show is indexed — showsByCity holds object reference,
    // so setting movie on the Show object is immediately visible in all queries
    public Show attachMovieToShow(int showId, int movieId) {
        Show show = showManagementService.getShow(showId);
        if (show == null) throw new IllegalArgumentException("Show not found: " + showId);

        Movie movie = movieService.getMovie(movieId);
        if (movie == null) throw new IllegalArgumentException("Movie not found: " + movieId);

        show.setMovie(movie);
        return show;
    }

    // --- Core queries ---

    // get all movies playing in a city (derived from shows — no stale List<Movie> in City)
    public List<Movie> getMoviesByCity(int cityId) {
        return showManagementService.getMoviesByCity(cityId);
    }

    // get all shows running in a city
    public List<Show> getShowsByCity(int cityId) {
        return showManagementService.getShowsByCity(cityId);
    }

    // get all shows for a specific movie
    public List<Show> getShowsByMovie(int movieId) {
        return showManagementService.getShowsByMovie(movieId);
    }

    // get all shows in a specific cinema hall
    public List<Show> getShowsByCinema(int cinemaId) {
        return showManagementService.getShowsByCinema(cinemaId);
    }

    // get all movies playing in a specific cinema hall
    public List<Movie> getMoviesByCinema(int cinemaId) {
        return showManagementService.getMoviesByCinema(cinemaId);
    }
}