package behavioural.state.problem;

public class WithOutStatePattern {
	public static void main(String[] args) {
		DirectionService directionService=new DirectionService(TransportationMode.CYCLING);
		directionService.setMode(TransportationMode.CYCLING);
		System.out.println(directionService.getDirection());
		directionService.getETA();
	}
}
