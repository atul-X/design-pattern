package observer.weather;


import java.util.ArrayList;
import java.util.List;

interface  Subject{
	void attach(Observer obs);
	void detach(Observer obs);
	void notifyObservers();
}
interface Observer{
	void update(float temperature);
}
class Weather implements Subject {
	private float temperature;
	private List<Observer> observerList;

	public Weather() {
		observerList=new ArrayList<>();
	}

	@Override
	public void attach(Observer obs) {
		observerList.add(obs);
	}

	@Override
	public void detach(Observer obs) {
		observerList.remove(obs);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer:observerList){
			observer.update(temperature);
		}
	}
	public void setTemperature(float temperature){
		this.temperature=temperature;
		notifyObservers();
	}
}
class DisplayDevices implements Observer{
	private String name;

	public DisplayDevices(String name) {
		this.name = name;
	}

	@Override
	public void update(float temperature) {
		System.out.println("Temperature on "+name+" device "+temperature);
	}
}
class MobileDevice implements Observer{
	private String name;

	public MobileDevice(String name) {
		this.name = name;
	}

	/**
	 * @param temperature
	 *            the new temperature value
	 */
	@Override
	public void update(float temperature) {
		System.out.println("Temperature on "+name+" device "+temperature);
	}
}
public class ObserverPatternExample {
	public static void main(String[] args) {
		Weather weather=new Weather();
		DisplayDevices device=new DisplayDevices("samsung lcd");
		MobileDevice mobileDevice=new MobileDevice("iphone ");
		weather.attach(device);
		weather.attach(mobileDevice);
		weather.setTemperature(12);
		weather.setTemperature(13);
	}
}
