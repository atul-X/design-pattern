package lld.uberdesign.service;

import lld.uberdesign.model.RideStatus;

public class DriverArrivedAtPickupState implements RideState {

    @Override
    public void requestRide(RideContext rideContext) {
        throw new IllegalStateException("Ride already requested.");
    }

    @Override
    public void assignDriver(RideContext rideContext) {
        throw new IllegalStateException("Driver already assigned.");
    }

    @Override
    public void driverArriver(RideContext rideContext) {
        throw new IllegalStateException("Driver already at pickup.");
    }

    @Override
    public void rideStarted(RideContext rideContext) {
        rideContext.getRide().setRideStatus(RideStatus.RIDE_STARTED);
        System.out.println("Ride has started.");
        rideContext.setRideState(new StartedState());
    }

    @Override
    public void rideEnded(RideContext rideContext) {
        throw new IllegalStateException("Ride has not started yet.");
    }

    @Override
    public void rideCancel(RideContext rideContext) {
        rideContext.getRide().setRideStatus(RideStatus.CANCELLED);
        System.out.println("Ride cancelled after driver arrived.");
        rideContext.setRideState(new CancelledState());
    }
}