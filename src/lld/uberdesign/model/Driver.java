package lld.uberdesign.model;

import lld.uberdesign.service.DriverNotificationObserver;
import lld.uberdesign.model.RiderRequest;

public class Driver extends User implements DriverNotificationObserver {
    private CurrentLocation currentLocation;
    private boolean available = true;
    private double rating;
    private int totalRides;

    @Override
    public boolean onRideRequested(RiderRequest riderRequest) {
        if (!available) return false;
        System.out.println("Driver " + getName() + " accepted ride request");
        return true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public CurrentLocation getCurrentLocation() { return currentLocation; }
    public double getRating() { return rating; }
    public int getTotalRides() { return totalRides; }
}
