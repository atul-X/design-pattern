package lld.movieticketbooking.model;

import java.util.ArrayList;
import java.util.List;

public class City {
    private int id;
    private String name;
    private List<Cinema> cinemas = new ArrayList<>();
    // removed List<Movie> — use ShowManagementService.getMoviesByCity(cityId) instead

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Cinema> getCinemas() { return cinemas; }
    public void setCinemas(List<Cinema> cinemas) { this.cinemas = cinemas; }

    // City manages its own cinema list — no external mutation
    public void addCinema(Cinema cinema) { this.cinemas.add(cinema); }
}
