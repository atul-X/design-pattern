package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.Ride;
import lld.uberdesign.model.RideStatus;

import java.util.List;

public class RequestedState implements RideState {

    @Override
    public void requestRide(RideContext rideContext) {
        List<Driver> interestedDrivers = rideContext.getRideBookingManager()
                .notifyDrivers(rideContext.getRiderRequest());
        rideContext.setInterestedDrivers(interestedDrivers);
        System.out.println("Ride requested. " + interestedDrivers.size() + " driver(s) notified.");
    }

    @Override
    public void assignDriver(RideContext rideContext) {
        Driver driver = rideContext.getRideBookingManager()
                .matchDriver(rideContext.getRiderRequest(), rideContext.getInterestedDrivers());
        if (driver == null) {
            System.out.println("No driver available. Ride cancelled.");
            rideContext.setRideState(new CancelledState());
            return;
        }
        Ride ride = new Ride(rideContext.getRiderRequest(), driver);
        ride.setRideStatus(RideStatus.DRIVER_ASSIGNED);
        rideContext.setRide(ride);
        System.out.println("Driver " + driver.getName() + " assigned.");
        rideContext.setRideState(new DriverAssignedState());
    }

    @Override
    public void driverArriver(RideContext rideContext) {
        throw new IllegalStateException("Driver not assigned yet.");
    }

    @Override
    public void rideStarted(RideContext rideContext) {
        throw new IllegalStateException("Driver not assigned yet.");
    }

    @Override
    public void rideEnded(RideContext rideContext) {
        throw new IllegalStateException("Ride has not started yet.");
    }

    @Override
    public void rideCancel(RideContext rideContext) {
        System.out.println("Ride cancelled by rider.");
        rideContext.setRideState(new CancelledState());
    }
}