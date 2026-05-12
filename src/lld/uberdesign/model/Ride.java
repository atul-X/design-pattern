package lld.uberdesign.model;

public class Ride {
    private int id;
    private Destination destination;
    private PickUpPoint pickUpPoint;
    private String riderType;
    private int rideDistance;
    private int rideDuration;
    private Driver driver;
    private RideStatus rideStatus;
    private Payment payment;
    private Fare fare;

    public Ride(RiderRequest request, Driver driver) {
        this.destination = request.getDestination();
        this.pickUpPoint = request.getPickUpPoint();
        this.riderType   = request.getRiderType();
        this.driver      = driver;
        this.rideStatus  = RideStatus.REQUESTED;
    }

    public RideStatus getRideStatus() { return rideStatus; }
    public void setRideStatus(RideStatus rideStatus) { this.rideStatus = rideStatus; }
    public Driver getDriver() { return driver; }
    public int getId() { return id; }
}
