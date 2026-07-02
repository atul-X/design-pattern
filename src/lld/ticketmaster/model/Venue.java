package lld.ticketmaster.model;

public class Venue {
    private final String venueId;
    private final String name;
    private final String city;
    private final int totalCapacity;

    public Venue(String venueId, String name, String city, int totalCapacity) {
        this.venueId = venueId;
        this.name = name;
        this.city = city;
        this.totalCapacity = totalCapacity;
    }

    public String getVenueId()     { return venueId; }
    public String getName()        { return name; }
    public String getCity()        { return city; }
    public int getTotalCapacity()  { return totalCapacity; }
}