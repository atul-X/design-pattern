package creational.factorymethod;

// 1. Product Interface
interface Transport {
    void deliver();
}

// 2. Concrete Products
class Truck implements Transport {
    @Override
    public void deliver() {
        System.out.println("Delivering by land in a truck.");
    }
}

class Ship implements Transport {
    @Override
    public void deliver() {
        System.out.println("Delivering by sea in a ship.");
    }
}

// 3. Creator (Abstract Class)
abstract class Logistics {
    // The factory method - subclasses will implement this
    public abstract Transport createTransport();

    /**
     * The creator's primary responsibility is not creating products.
     * It contains some core business logic that relies on Transport objects,
     * returned by the factory method. Subclasses can indirectly change that
     * business logic by overriding the factory method and returning a
     * different type of product from it.
     */
    public void planDelivery() {
        // Call the factory method to create a Transport object.
        Transport transport = createTransport();
        // Now, use the product.
        System.out.print("Planning delivery... ");
        transport.deliver();
    }
}

// 4. Concrete Creators
class RoadLogistics extends Logistics {
    @Override
    public Transport createTransport() {
        return new Truck();
    }
}

class SeaLogistics extends Logistics {
    @Override
    public Transport createTransport() {
        return new Ship();
    }
}

// 5. Demo class
public class LogisticsApp {
    public static void main(String[] args) {
        Logistics logistics;

        // Decide at runtime which factory to use based on some condition.
        String deliveryType = "road"; // This could come from config, user input, etc.

        if (deliveryType.equalsIgnoreCase("road")) {
            logistics = new RoadLogistics();
        } else if (deliveryType.equalsIgnoreCase("sea")) {
            logistics = new SeaLogistics();
        } else {
            throw new IllegalArgumentException("Unknown delivery type.");
        }

        // The client code works with an instance of a concrete creator,
        // but through its base interface. As long as the client keeps
        // working with the creator via the base interface, you can pass it
        // any creator's subclass.
        logistics.planDelivery();

        // Let's try the other one to show the flexibility
        System.out.println("\n--- Switching logistics type ---\n");
        logistics = new SeaLogistics();
        logistics.planDelivery();
    }
}
