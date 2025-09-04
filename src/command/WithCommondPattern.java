package command;
interface Command{
	void execute();
}
class BoldCommand implements Command{
	private TextEditorII textEditorII;

	public BoldCommand(TextEditorII textEditorII) {
		this.textEditorII = textEditorII;
	}

	@Override
	public void execute() {
		textEditorII.boldText();

	}
}
class italicCommand implements Command{
	private TextEditorII textEditorII;

	public italicCommand(TextEditorII textEditorII) {
		this.textEditorII = textEditorII;
	}
	@Override
	public void execute() {
		textEditorII.italicizeText();
	}
}
class UnderlineCommand implements Command{
	private TextEditorII textEditorII;

	public UnderlineCommand(TextEditorII textEditorII) {
		this.textEditorII = textEditorII;
	}
	@Override
	public void execute() {
		textEditorII.underlineText();
	}
}
class ChangeColorCommand implements Command{
	private TextEditorII textEditorII;

	public ChangeColorCommand(TextEditorII textEditorII) {
		this.textEditorII = textEditorII;
	}
	@Override
	public void execute() {
		textEditorII.changeColor();
	}
}

class TextEditorII{
	public void boldText(){
		System.out.println("Text has been bolded");
	}
	public void italicizeText(){
		System.out.println("Text has been italicized");
	}
	public void underlineText(){
		System.out.println("Text has been underlined");
	}
	public void changeColor(){
		System.out.println("Text color has been changed");
	}
}
class Button{
	private Command command;

	public void setCommand(Command command) {
		this.command = command;
	}
	public void click(){
		command.execute();
	}
}

public class WithCommondPattern {
	public static void main(String[] args) {
		TextEditorII textEditorII=new TextEditorII();
		Button button=new Button();
		button.setCommand(new BoldCommand(textEditorII));
		button.setCommand(new ChangeColorCommand(textEditorII));
		button.click();
	}
}
