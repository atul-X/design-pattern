package creational.abstractfactorypattern.problem;
class WindowButton{
	public void render() {
		System.out.println("Rendering a button in Windows.");
	}
}
class WindowScrollBar{
	public void render() {
		System.out.println("Rendering a scrollbar in Windows.");
	}
}
class MacButton {
	public void render() {
		System.out.println("Rendering a button in Mac.");
	}
}
class MacScrollBar {
	public void render() {
		System.out.println("Rendering a scrollbar in Mac.");
	}
}
public class Application {

	public static void main(String[] args) {
		// For Windows
		WindowButton winButton = new WindowButton();
		WindowScrollBar winScrollBar = new WindowScrollBar();
		winButton.render();
		winScrollBar.render();

		// For Mac
		MacButton macButton = new MacButton();
		MacScrollBar macScrollBar = new MacScrollBar();
		macButton.render();
		macScrollBar.render();
	}
}
