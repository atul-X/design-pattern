package lld.movieticketbooking.model;

import java.util.List;

public class Cinema {
    private int id;
    private String name;
    private int cityId;          // which city this cinema belongs to
    private List<Screen> screens;
    // removed List<Movie> — movies are accessed via Screen → Show → Movie

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }

    public List<Screen> getScreens() { return screens; }
    public void setScreens(List<Screen> screens) { this.screens = screens; }
}
