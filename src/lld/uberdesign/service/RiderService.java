package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.RiderRequest;

import java.util.List;

public class RiderService  {
    private DriverMatchingStrategy driverMatchingStrategy;
    private RideRequestSubject driverNotificationService;

    public RiderService(DriverMatchingStrategy driverMatchingStrategy, RideRequestSubject driverNotificationService) {
        this.driverMatchingStrategy = driverMatchingStrategy;
        this.driverNotificationService = driverNotificationService;
    }

    List<Driver> notifyDrivers(RiderRequest riderRequest) {
        return driverNotificationService.notifyDrivers(riderRequest);
    }

    Driver matchDriver(RiderRequest riderRequest, List<Driver> interestedDrivers) {
        return driverMatchingStrategy.match(riderRequest, interestedDrivers);
    }

}
