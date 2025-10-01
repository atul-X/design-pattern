package creational.builder;

public interface HouseBuilder {
    void buildFoundation();
    void buildStructure();
    void buildRooms();
    void buildRoof();
    void buildInterior();

    House getHouse();
}
