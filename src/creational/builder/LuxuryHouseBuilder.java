package creational.builder;

public class LuxuryHouseBuilder implements HouseBuilder {
    private House.Builder builder;

    public LuxuryHouseBuilder() {
        this.builder = new House.Builder();
    }

    @Override
    public void buildFoundation() {
        builder.foundation("Deep Pile Foundation with Reinforced Concrete");
    }

    @Override
    public void buildStructure() {
        builder.structure("Steel and Glass Modern Structure");
    }

    @Override
    public void buildRooms() {
        builder.rooms(6);
    }

    @Override
    public void buildRoof() {
        builder.roof("Flat Roof with Skylights");
    }

    @Override
    public void buildInterior() {
        builder.interior("Luxury Contemporary").garage(true).garden(true).swimmingPool(true);
    }

    @Override
    public House getHouse() {
        return builder.build();
    }
}
