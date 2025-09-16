package behavioural.state.solution;

public class Walking implements TransportationMode{
	@Override
	public int calcETA() {
		System.out.println("Calculating ETA WALKING");
		return 0;
	}

	@Override
	public String getDirection() {
		return "Direction for WALKING";
	}
}
