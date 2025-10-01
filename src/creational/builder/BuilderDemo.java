package creational.builder;

public class BuilderDemo {
    public static void main(String[] args) {
        // Build an Eco House
        HouseBuilder ecoBuilder = new EcoHouseBuilder();
        ConstructionEngineer engineerEco = new ConstructionEngineer(ecoBuilder);
        House ecoHouse = engineerEco.constructHouse();
        System.out.println("EcoHouse: " + ecoHouse);

        // Build a Luxury House
        HouseBuilder luxuryBuilder = new LuxuryHouseBuilder();
        ConstructionEngineer engineerLuxury = new ConstructionEngineer(luxuryBuilder);
        House luxuryHouse = engineerLuxury.constructHouse();
        System.out.println("LuxuryHouse: " + luxuryHouse);

        // Alternative: Direct fluent builder without Director (also common with Builder pattern)
        House customHouse = new House.Builder()
                .foundation("Concrete Slab")
                .structure("Brick and Timber Mix")
                .rooms(4)
                .garage(true)
                .garden(true)
                .roof("Hip Roof")
                .interior("Minimal Scandinavian")
                .build();
        System.out.println("CustomHouse (Fluent Builder): " + customHouse);
    }
}
