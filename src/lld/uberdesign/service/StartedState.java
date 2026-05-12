package lld.uberdesign.service;

import lld.uberdesign.model.RideStatus;

public class StartedState implements RideState {

    @Override
    public void requestRide(RideContext rideContext) {
        throw new IllegalStateException("Ride already in progress.");
    }

    @Override
    public void assignDriver(RideContext rideContext) {
        throw new IllegalStateException("Ride already in progress.");
    }

    @Override
    public void driverArriver(RideContext rideContext) {
        throw new IllegalStateException("Ride already in progress.");
    }

    @Override
    public void rideStarted(RideContext rideContext) {
        throw new IllegalStateException("Ride already started.");
    }

    @Override
    public void rideEnded(RideContext rideContext) {
        rideContext.getRide().setRideStatus(RideStatus.ENDED);
        System.out.println("Ride ended. Please rate your driver.");
        rideContext.setRideState(new EndedState());
    }

    @Override
    public void rideCancel(RideContext rideContext) {
        throw new IllegalStateException("Cannot cancel a ride in progress.");
    }
}