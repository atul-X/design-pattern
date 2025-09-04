//package observer.weather;
//class DisplayDevice{
//	public void showtemp(float temp){
//		System.out.println("current temp:"+temp+"C");
//	}
//}
//class  WeatherStation{
//	private float temperature;
//	private DisplayDevice displayDevice;
//
//	public WeatherStation(DisplayDevice displayDevice) {
//		this.displayDevice=displayDevice;
//	}
//	public void setTemperature(float temperature){
//		this.temperature=temperature;
//		notifyDevices();
//	}
//	public void notifyDevices(){
//		displayDevice.showtemp(temperature);
//	}
//}
//public class WithOutObserverPattern {
//	public static void main(String[] args) {
//		WeatherStation weatherStation=new WeatherStation(new DisplayDevice());
//		weatherStation.setTemperature(23);
//		weatherStation.setTemperature(21);
//	}
//}
