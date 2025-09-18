package creational.abstractfactorypattern.solution;


interface  Button{
	void render();
}

interface Scrollbar {
	void scroll();
}


class WindowButton implements Button{
	public void render() {
		System.out.println("Rendering a button in Windows.");
	}
}
class WindowScrollBar implements Scrollbar{
	public void scroll() {
		System.out.println("Rendering a scrollbar in Windows.");
	}
}
class MacButton implements Button{
	public void render() {
		System.out.println("Rendering a button in Mac.");
	}
}
interface  UiFactory{
	Button createButton();
	Scrollbar createScrollBar();
}
class MacUiFactory implements UiFactory{
	public Button createButton() {
		return new MacButton();
	}
	public Scrollbar createScrollBar() {
		return new MacScrollBar();
	}
}
class WindowUiFactory implements UiFactory{
	public Button createButton() {
		return new WindowButton();
	}
	public Scrollbar createScrollBar() {
		return new WindowScrollBar();
	}
}

class MacScrollBar implements Scrollbar{
	public void scroll() {
		System.out.println("Rendering a scrollbar in Mac.");
	}
}



public class Application {
	private Button button;
	private Scrollbar scrollBar;
	public Application(UiFactory factory) {
		button = factory.createButton();
		scrollBar = factory.createScrollBar();
	}
	public void render() {
		button.render();
	}
	public void scroll() {
		scrollBar.scroll();
	}
	public static void main(String[] args) {
		// For Windows
		WindowUiFactory windowUiFactory=new WindowUiFactory();
		Application windowsApp = new Application(windowUiFactory);
		windowsApp.render();
		windowsApp.scroll();

	}
}
