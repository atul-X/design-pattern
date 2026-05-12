package lld.uberdesign.service;

public class EndedState implements RideState {

    @Override
    public void requestRide(RideContext rideContext) {
        throw new IllegalStateException("Ride already ended.");
    }

    @Override
    public void assignDriver(RideContext rideContext) {
        throw new IllegalStateException("Ride already ended.");
    }

    @Override
    public void driverArriver(RideContext rideContext) {
        throw new IllegalStateException("Ride already ended.");
    }

    @Override
    public void rideStarted(RideContext rideContext) {
        throw new IllegalStateException("Ride already ended.");
    }

    @Override
    public void rideEnded(RideContext rideContext) {
        throw new IllegalStateException("Ride already ended.");
    }

    @Override
    public void rideCancel(RideContext rideContext) {
        throw new IllegalStateException("Ride already ended.");
    }
}