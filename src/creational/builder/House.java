package creational.builder;

public class House {
    private final String foundation;
    private final String structure;
    private final int rooms;
    private final boolean hasGarage;
    private final boolean hasGarden;
    private final boolean hasSwimmingPool;
    private final String roofType;
    private final String interiorStyle;

    private House(Builder builder) {
        this.foundation = builder.foundation;
        this.structure = builder.structure;
        this.rooms = builder.rooms;
        this.hasGarage = builder.hasGarage;
        this.hasGarden = builder.hasGarden;
        this.hasSwimmingPool = builder.hasSwimmingPool;
        this.roofType = builder.roofType;
        this.interiorStyle = builder.interiorStyle;
    }

    public String getSummary() {
        return new StringBuilder()
                .append("House[foundation=").append(foundation)
                .append(", structure=").append(structure)
                .append(", rooms=").append(rooms)
                .append(", garage=").append(hasGarage)
                .append(", garden=").append(hasGarden)
                .append(", pool=").append(hasSwimmingPool)
                .append(", roof=").append(roofType)
                .append(", interior=").append(interiorStyle)
                .append("]")
                .toString();
    }

    @Override
    public String toString() {
        return getSummary();
    }

    // Optional: Fluent builder nested in product (demonstrates builder style too)
    public static class Builder {
        private String foundation = "Concrete";
        private String structure = "Brick";
        private int rooms = 2;
        private boolean hasGarage = false;
        private boolean hasGarden = false;
        private boolean hasSwimmingPool = false;
        private String roofType = "Gable";
        private String interiorStyle = "Standard";

        public Builder foundation(String foundation) { this.foundation = foundation; return this; }
        public Builder structure(String structure) { this.structure = structure; return this; }
        public Builder rooms(int rooms) { this.rooms = rooms; return this; }
        public Builder garage(boolean hasGarage) { this.hasGarage = hasGarage; return this; }
        public Builder garden(boolean hasGarden) { this.hasGarden = hasGarden; return this; }
        public Builder swimmingPool(boolean hasSwimmingPool) { this.hasSwimmingPool = hasSwimmingPool; return this; }
        public Builder roof(String roofType) { this.roofType = roofType; return this; }
        public Builder interior(String interiorStyle) { this.interiorStyle = interiorStyle; return this; }

        public House build() { return new House(this); }
    }
}
