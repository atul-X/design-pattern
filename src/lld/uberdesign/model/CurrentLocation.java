package lld.uberdesign.model;

public class CurrentLocation {
    private int lat;
    private int lng;

    public CurrentLocation(int lat, int lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public int getLat() { return lat; }
    public int getLng() { return lng; }
}
