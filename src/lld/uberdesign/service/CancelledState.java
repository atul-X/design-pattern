package lld.uberdesign.service;

public class CancelledState implements RideState {

    @Override
    public void requestRide(RideContext rideContext) {
        throw new IllegalStateException("Ride is cancelled.");
    }

    @Override
    public void assignDriver(RideContext rideContext) {
        throw new IllegalStateException("Ride is cancelled.");
    }

    @Override
    public void driverArriver(RideContext rideContext) {
        throw new IllegalStateException("Ride is cancelled.");
    }

    @Override
    public void rideStarted(RideContext rideContext) {
        throw new IllegalStateException("Ride is cancelled.");
    }

    @Override
    public void rideEnded(RideContext rideContext) {
        throw new IllegalStateException("Ride is cancelled.");
    }

    @Override
    public void rideCancel(RideContext rideContext) {
        throw new IllegalStateException("Ride already cancelled.");
    }
}