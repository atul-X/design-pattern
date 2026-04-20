package lld.movieticketbooking.model;

import java.util.List;

public class Show {
    private int showId;
    private Movie movie;         // which movie is playing
    private int screenId;        // which screen it's playing on
    private int cinemaId;        // which cinema hall — kept for fast cinema-based queries
    private int cityId;          // kept for fast city-based queries
    private String date;
    private String time;
    private List<Seat> seats;    // seats available for THIS show

    public int getShowId() { return showId; }
    public void setShowId(int showId) { this.showId = showId; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public int getScreenId() { return screenId; }
    public void setScreenId(int screenId) { this.screenId = screenId; }

    public int getCinemaId() { return cinemaId; }
    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }

    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
}
