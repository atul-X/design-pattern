package state.solution;

public class Train implements TransportationMode{
	@Override
	public int calcETA() {
		System.out.println("Calculating ETA TRAIn");
		return 0;
	}

	@Override
	public String getDirection() {
		return "Directions for train ";
	}
}
