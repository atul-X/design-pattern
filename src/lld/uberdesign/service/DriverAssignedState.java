package lld.uberdesign.service;

import lld.uberdesign.model.RideStatus;

public class DriverAssignedState implements RideState {

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
        rideContext.getRide().setRideStatus(RideStatus.DRIVER_ARRIVED);
        System.out.println("Driver has arrived at pickup point.");
        rideContext.setRideState(new DriverArrivedAtPickupState());
    }

    @Override
    public void rideStarted(RideContext rideContext) {
        throw new IllegalStateException("Driver has not arrived yet.");
    }

    @Override
    public void rideEnded(RideContext rideContext) {
        throw new IllegalStateException("Ride has not started yet.");
    }

    @Override
    public void rideCancel(RideContext rideContext) {
        rideContext.getRide().setRideStatus(RideStatus.CANCELLED);
        System.out.println("Ride cancelled after driver assigned.");
        rideContext.setRideState(new CancelledState());
    }
}