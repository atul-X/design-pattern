package behavioural.state.solution;

public class Cycling implements TransportationMode{
	@Override
	public int calcETA() {
		System.out.println("Calculating ETA CYCLING");
		return 0;
	}

	@Override
	public String getDirection() {
		return "Directions for Cycling ";
	}
}
