package lld.vendingmechine.model;

public enum Coin {
    PENNY(0.01, "1¢"),
    NICKEL(0.05, "5¢"),
    DIME(0.10, "10¢"),
    QUARTER(0.25, "25¢"),
    DOLLAR(1.00, "$1");

    private double value;
    private String displayName;

    Coin(double value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public double getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }
}
