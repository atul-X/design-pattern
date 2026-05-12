package lld.uberdesign.model;

public class RiderRequest {
    private Destination destination;
    private PickUpPoint pickUpPoint;
    private String riderType;

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public PickUpPoint getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(PickUpPoint pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }

    public String getRiderType() {
        return riderType;
    }

    public void setRiderType(String riderType) {
        this.riderType = riderType;
    }
}
