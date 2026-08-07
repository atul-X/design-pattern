package lld.movieticketbooking.service;

import lld.movieticketbooking.model.Cinema;
import lld.movieticketbooking.model.City;
import lld.movieticketbooking.model.Movie;
import lld.movieticketbooking.model.Seat;
import lld.movieticketbooking.model.Show;

import lld.movieticketbooking.service.observer.BookingEventPublisher;
import lld.movieticketbooking.service.observer.EmailObserver;
import lld.movieticketbooking.service.observer.InventoryObserver;
import lld.movieticketbooking.service.observer.SMSObserver;

import java.util.List;

/**
 * Facade Pattern
 *
 * MovieBookingManager hides the complexity of:
 *   - MovieSystemManagement  (city / cinema / movie / show setup)
 *   - MovieTicketContext      (state machine: SelectShow → SelectSeat → Pay → Book)
 *   - PaymentStrategy         (UPI / Card / etc.)
 *   - BookingEventPublisher   (Observer: Email / SMS / Inventory)
 *
 * Client only talks to MovieBookingManager — one simple interface.
 */
public class MovieBookingManager {

    private final MovieSystemManagement systemManagement;
    private final BookingEventPublisher publisher;
    private MovieTicketContext currentContext;

    public MovieBookingManager() {
        CityManagementService cityService     = new CityManagementService();
        CinemaManagementService cinemaService = new CinemaManagementService();
        MovieSerivce movieService             = new MovieSerivce();
        ShowManagementService showService     = new ShowManagementService();

        this.systemManagement = new MovieSystemManagement(
            cityService, cinemaService, movieService, showService
        );

        // setup observer pipeline
        this.publisher = new BookingEventPublisher();
        publisher.register(new EmailObserver());
        publisher.register(new SMSObserver());
        publisher.register(new InventoryObserver());
    }

    // ─────────────────────────────────────────────
    // ADMIN: setup operations
    // ─────────────────────────────────────────────

    public City addCity(City city) {
        return systemManagement.addCity(city);
    }

    public Cinema addCinema(Cinema cinema) {
        return systemManagement.addCinema(cinema.getCityId(),cinema);
    }

    public Movie addMovie(Movie movie) {
        return systemManagement.addMovie(movie);
    }

    public Show addShow(Show show) {
        return systemManagement.addShow(show);
    }

    public Show attachMovieToShow(int showId, int movieId) {
        return systemManagement.attachMovieToShow(showId, movieId);
    }

    // ─────────────────────────────────────────────
    // QUERY operations
    // ─────────────────────────────────────────────

    public List<Movie> getMoviesByCity(int cityId) {
        List<Movie> movies = systemManagement.getMoviesByCity(cityId);
        System.out.println("Movies in city " + cityId + ": " + movies.size() + " found");
        return movies;
    }

    public List<Show> getShowsByMovie(int movieId) {
        List<Show> shows = systemManagement.getShowsByMovie(movieId);
        System.out.println("Shows for movie " + movieId + ": " + shows.size() + " found");
        return shows;
    }

    public List<Show> getShowsByCinema(int cinemaId) {
        return systemManagement.getShowsByCinema(cinemaId);
    }

    public List<Seat> getAvailableSeats(int showId) {
        Show show = systemManagement.getShowsByCity(0).stream()
            .filter(s -> s.getShowId() == showId)
            .findFirst()
            .orElse(null);

        // get show directly from showsByMovie fallback
        List<Show> allShows = systemManagement.getShowsByMovie(
            show != null ? show.getMovie().getMovieId() : -1
        );

        Show target = allShows.stream()
            .filter(s -> s.getShowId() == showId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Show not found: " + showId));

        List<Seat> available = new java.util.ArrayList<>();
        for (Seat seat : target.getSeats()) {
            if (!seat.isBooked()) available.add(seat);
        }
        System.out.println("Available seats for show " + showId + ": " + available.size());
        return available;
    }

    // ─────────────────────────────────────────────
    // BOOKING flow (Facade hides state machine)
    // ─────────────────────────────────────────────

    /** Step 1: start a new booking session */
    public void startBooking() {
        currentContext = new MovieTicketContext(publisher);
        System.out.println("New booking session started.");
    }

    /** Step 2: select show */
    public void selectShow(Show show) {
        ensureSession();
        currentContext.selectShow(show);
    }

    /** Step 3: select seats */
    public void selectSeats(List<Seat> seats) {
        ensureSession();
        currentContext.selectSeats(seats);
    }

    /** Step 4: pay */
    public void pay(PaymentStrategy paymentStrategy) {
        ensureSession();
        currentContext.pay(paymentStrategy);
    }

    /** Step 5: confirm booking — synchronized so concurrent bookings on same show are serialized */
    public synchronized void confirmBooking() {
        ensureSession();
        currentContext.book();
    }

    /** Cancel anytime */
    public void cancelBooking() {
        ensureSession();
        currentContext.cancel();
    }

    // ─────────────────────────────────────────────
    // CONVENIENCE: full booking in one call
    // ─────────────────────────────────────────────

    public void bookTicket(Show show, List<Seat> seats, PaymentStrategy paymentStrategy) {
        startBooking();
        selectShow(show);
        selectSeats(seats);
        pay(paymentStrategy);
        confirmBooking();
    }

    // ─────────────────────────────────────────────
    private void ensureSession() {
        if (currentContext == null) {
            throw new IllegalStateException("No active booking session. Call startBooking() first.");
        }
    }
}
