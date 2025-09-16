package behavioural.state.solution;

public class WithStatePatten {
	public static void main(String[] args) {
		DirectionService directionService=new DirectionService(new Cycling());
		directionService.getETA();
		System.out.println(directionService.getDirection());
	}
}
