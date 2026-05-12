package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.RiderRequest;

import java.util.List;

public interface RideRequestSubject {
    void registerDriver(DriverNotificationObserver observer);
    void deregisterDriver(DriverNotificationObserver observer);
    List<Driver> notifyDrivers(RiderRequest riderRequest);
}