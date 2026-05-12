package lld.uberdesign.service;

import lld.uberdesign.model.RiderRequest;

public interface DriverNotificationObserver {
    boolean onRideRequested(RiderRequest riderRequest);
}