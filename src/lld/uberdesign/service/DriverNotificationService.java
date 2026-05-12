package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.RiderRequest;

import java.util.ArrayList;
import java.util.List;

public class DriverNotificationService implements RideRequestSubject {

    private final List<Driver> availableDrivers = new ArrayList<>();

    @Override
    public void registerDriver(DriverNotificationObserver observer) {
        availableDrivers.add((Driver) observer);
    }

    @Override
    public void deregisterDriver(DriverNotificationObserver observer) {
        availableDrivers.remove(observer);
    }

    // notifies all drivers, returns only those who accepted
    @Override
    public List<Driver> notifyDrivers(RiderRequest riderRequest) {
        List<Driver> interestedDrivers = new ArrayList<>();
        for (Driver driver : availableDrivers) {
            if (driver.onRideRequested(riderRequest)) {
                interestedDrivers.add(driver);
            }
        }
        return interestedDrivers;
    }
}