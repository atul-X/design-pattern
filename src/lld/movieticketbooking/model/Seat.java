package lld.movieticketbooking.model;

public class Seat  {
    private String name;
    private String type;
    private double price;
    private boolean isBooked;    // needed to track booking state

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}
