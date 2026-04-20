package lld.movieticketbooking;

import lld.movieticketbooking.model.*;
import lld.movieticketbooking.service.*;

import java.util.Arrays;
import java.util.List;

public class MovieBookingDemo {

    public static void main(String[] args) {

        // single entry point — Facade
        MovieBookingManager manager = new MovieBookingManager();

        // ── ADMIN SETUP ──────────────────────────────
        City mumbai = new City();
        mumbai.setId(1);
        mumbai.setName("Mumbai");
        manager.addCity(mumbai);

        Cinema pvr = new Cinema();
        pvr.setId(1);
        pvr.setName("PVR Andheri");
        pvr.setCityId(1);
        manager.addCinema(pvr);

        Movie inception = new Movie();
        inception.setMovieId(1);
        inception.setName("Inception");
        inception.setDuration("2h 28m");
        inception.setPrice(250.0);
        manager.addMovie(inception);

        Movie interstellar = new Movie();
        interstellar.setMovieId(2);
        interstellar.setName("Interstellar");
        interstellar.setDuration("2h 49m");
        interstellar.setPrice(300.0);
        manager.addMovie(interstellar);

        // seats for show 1
        Seat s1 = new Seat(); s1.setName("A1"); s1.setType("GOLD");   s1.setPrice(300.0);
        Seat s2 = new Seat(); s2.setName("A2"); s2.setType("GOLD");   s2.setPrice(300.0);
        Seat s3 = new Seat(); s3.setName("B1"); s3.setType("SILVER"); s3.setPrice(200.0);

        Show show1 = new Show();
        show1.setShowId(1);
        show1.setCinemaId(1);
        show1.setCityId(1);
        show1.setDate("2025-06-01");
        show1.setTime("18:00");
        show1.setSeats(Arrays.asList(s1, s2, s3));
        manager.addShow(show1);
        manager.attachMovieToShow(1, 1);  // attach Inception to show1

        Show show2 = new Show();
        show2.setShowId(2);
        show2.setCinemaId(1);
        show2.setCityId(1);
        show2.setDate("2025-06-01");
        show2.setTime("21:00");
        Seat s4 = new Seat(); s4.setName("C1"); s4.setType("SILVER"); s4.setPrice(200.0);
        show2.setSeats(Arrays.asList(s4));
        manager.addShow(show2);
        manager.attachMovieToShow(2, 2);  // attach Interstellar to show2

        System.out.println("\n── QUERY ────────────────────────────────────");
        List<Movie> movies = manager.getMoviesByCity(1);
        movies.forEach(m -> System.out.println("  " + m.getName()));

        List<Show> shows = manager.getShowsByMovie(1);
        shows.forEach(s -> System.out.println("  Show " + s.getShowId() + " at " + s.getTime()));

        System.out.println("\n── BOOKING: step by step ────────────────────");
        manager.startBooking();
        manager.selectShow(show1);
        manager.selectSeats(Arrays.asList(s1, s2));
        manager.pay(new UpiPayment("alice@upi"));
        manager.confirmBooking();

        System.out.println("\n── BOOKING: one-call convenience ────────────");
        manager.bookTicket(show2, Arrays.asList(s4), new CardPayment("4111111111111234"));

        System.out.println("\n── CANCEL BOOKING ───────────────────────────");
        manager.startBooking();
        manager.selectShow(show1);
        manager.selectSeats(Arrays.asList(s3));
        manager.cancelBooking();

        System.out.println("\n── INVALID TRANSITION TEST ──────────────────");
        try {
            manager.confirmBooking(); // no active session after cancel
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
