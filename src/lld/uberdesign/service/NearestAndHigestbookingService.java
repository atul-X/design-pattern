package lld.uberdesign.service;

import lld.uberdesign.model.Driver;
import lld.uberdesign.model.PickUpPoint;
import lld.uberdesign.model.RiderRequest;

import java.util.Comparator;
import java.util.List;

public class NearestAndHigestbookingService implements DriverMatchingStrategy {

    private static final int TOP_NEAREST_COUNT = 3;

    @Override
    public Driver match(RiderRequest riderRequest, List<Driver> interestedDrivers) {
        if (interestedDrivers.isEmpty()) return null;

        PickUpPoint pickUp = riderRequest.getPickUpPoint();

        // step 1: take top 3 nearest drivers
        // step 2: among those, pick the one with highest total rides
        return interestedDrivers.stream()
                .sorted(Comparator.comparingDouble((Driver d) -> distanceTo(d, pickUp)))
                .limit(TOP_NEAREST_COUNT)
                .max(Comparator.comparingInt(Driver::getTotalRides))
                .orElse(null);
    }

    private double distanceTo(Driver driver, PickUpPoint pickUp) {
        int dLat = driver.getCurrentLocation().getLat() - pickUp.getLat();
        int dLng = driver.getCurrentLocation().getLng() - pickUp.getLng();
        return Math.sqrt(dLat * dLat + dLng * dLng);
    }
}
