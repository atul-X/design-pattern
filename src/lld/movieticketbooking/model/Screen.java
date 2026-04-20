package lld.movieticketbooking.model;

import java.util.List;

public class Screen {
    private int id;
    private String name;
    private int cinemaId;        // which cinema this screen belongs to
    private List<Show> shows;    // shows scheduled on this screen
    // removed List<Seat> — seats belong to each individual Show, not Screen

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCinemaId() { return cinemaId; }
    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }

    public List<Show> getShows() { return shows; }
    public void setShows(List<Show> shows) { this.shows = shows; }
}
