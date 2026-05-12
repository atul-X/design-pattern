package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.RiderRequest;

import java.util.List;

public class RideBookingManager {
    private RiderService riderService;
    private RideContext rideContext;

    public RideBookingManager(DriverMatchingStrategy driverMatchingStrategy, RideRequestSubject driverNotificationService) {
        this.riderService = new RiderService(driverMatchingStrategy, driverNotificationService);
    }

    public void setRideContext(RideContext rideContext) {
        this.rideContext = rideContext;
    }

    List<Driver> notifyDrivers(RiderRequest riderRequest) {
        return riderService.notifyDrivers(riderRequest);
    }

    Driver matchDriver(RiderRequest riderRequest, List<Driver> interestedDrivers) {
        return riderService.matchDriver(riderRequest, interestedDrivers);
    }

    // --- state transition methods (called by external code) ---

    public void requestRide() {
        rideContext.getRideState().requestRide(rideContext);
    }

    public void assignDriver() {
        rideContext.getRideState().assignDriver(rideContext);
    }

    public void driverArrived() {
        rideContext.getRideState().driverArriver(rideContext);
    }

    public void startRide() {
        rideContext.getRideState().rideStarted(rideContext);
    }

    public void endRide() {
        rideContext.getRideState().rideEnded(rideContext);
    }

    public void cancelRide() {
        rideContext.getRideState().rideCancel(rideContext);
    }
}
