package state.solution;

public class Car implements TransportationMode {

	@Override
	public int calcETA() {
		System.out.println("Calculating ETA CAR");

		return 0;
	}

	@Override
	public String getDirection() {
		return "Directions for CAR";
	}
}
