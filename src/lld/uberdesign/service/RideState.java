package lld.uberdesign.service;

public interface RideState {
    void requestRide(RideContext rideContext);
    void assignDriver(RideContext rideContext);
    void driverArriver(RideContext rideContext);
    void rideStarted(RideContext rideContext);
    void rideEnded(RideContext rideContext);
    void rideCancel(RideContext rideContext);
}
