package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.Ride;
import lld.uberdesign.model.RiderRequest;

import java.util.List;

public class RideContext {
    RideState rideState;
    RiderRequest riderRequest;
    Ride ride;
    List<Driver> interestedDrivers;

    public RiderRequest getRiderRequest() {
        return riderRequest;
    }

    public RideState getRideState() {
        return rideState;
    }

    RideBookingManager rideBookingManager;

    private RideRequestSubject driverNotificationService;

    public RideContext(RiderRequest riderRequest) {
        this.riderRequest = riderRequest;
        this.rideState = new RequestedState();
        this.driverNotificationService = new DriverNotificationService();
        this.rideBookingManager = new RideBookingManager(new NearestAndHigestbookingService(), driverNotificationService);
        this.rideBookingManager.setRideContext(this);
    }

    public RideRequestSubject getDriverNotificationService() {
        return driverNotificationService;
    }

    public void setRideState(RideState rideState) {
        this.rideState = rideState;
    }

    public RideBookingManager getRideBookingManager() {
        return rideBookingManager;
    }

    public Ride getRide() { return ride; }
    public void setRide(Ride ride) { this.ride = ride; }

    public List<Driver> getInterestedDrivers() { return interestedDrivers; }
    public void setInterestedDrivers(List<Driver> interestedDrivers) { this.interestedDrivers = interestedDrivers; }
}
