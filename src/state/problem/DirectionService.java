package state.problem;

enum TransportationMode{
	WALKING,CYCLING,CAR,TRAIN
}
public class DirectionService {
	private TransportationMode mode;

	public DirectionService(TransportationMode mode) {
		this.mode = mode;
	}

	public void setMode(TransportationMode mode) {
		this.mode = mode;
	}
	public int getETA(){
		switch (mode){
			case WALKING :
				System.out.println("Calc ETA for walking 10");
				return 10;
			case CYCLING:
				System.out.println("Calc ETA for CYCLING 5");
				return 5;
			case CAR:
				System.out.println("Calc ETA for car 2");
				return 2;
			case TRAIN:
				System.out.println("Calc ETA for train 3");
			default:
				throw new IllegalArgumentException("UnKnown MOde");
		}
	}
	public String getDirection(){
		switch (mode){
			case WALKING :
				return "Directions for walking : head north for 500 meters.";
			case CYCLING:
				return "Directions for cycling : head north for 500 meters.";

			case CAR:
				return "Directions for driving : head north for 500 meters.";
			case TRAIN:
				return "Directions for train : head north for 500 meters.";
			default:
				throw new IllegalArgumentException("UnKnown MOde");
		}
	}
}
