package creational.builder;

public class EcoHouseBuilder implements HouseBuilder {
    private House.Builder builder;

    public EcoHouseBuilder() {
        this.builder = new House.Builder();
    }

    @Override
    public void buildFoundation() {
        builder.foundation("Reinforced Bamboo-Concrete Hybrid");
    }

    @Override
    public void buildStructure() {
        builder.structure("Sustainably Sourced Timber Frame");
    }

    @Override
    public void buildRooms() {
        builder.rooms(3);
    }

    @Override
    public void buildRoof() {
        builder.roof("Green Roof with Solar Tiles");
    }

    @Override
    public void buildInterior() {
        builder.interior("Eco-Modern with Natural Finishes").garden(true).garage(false).swimmingPool(false);
    }

    @Override
    public House getHouse() {
        return builder.build();
    }
}
