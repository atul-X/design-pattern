package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.RiderRequest;

import java.util.List;

public interface DriverMatchingStrategy {
    Driver match(RiderRequest riderRequest, List<Driver> interestedDrivers);
}
