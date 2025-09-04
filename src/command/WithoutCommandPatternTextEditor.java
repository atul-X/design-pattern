package command;
class TextEditor{
	public void boldText(){
		System.out.println("Text has been bolded");
	}
	public void italicizeText(){
		System.out.println("Text has been italicized");
	}
	public void underlineText(){
		System.out.println("Text has been underlined");
	}
}
class BoldButton{
	private TextEditor editor;

	public BoldButton(TextEditor editor) {
		this.editor = editor;
	}

	public void click(){
		editor.boldText();;
	}
}
class italicButton{
	private TextEditor editor;

	public italicButton(TextEditor editor) {
		this.editor = editor;
	}

	public void click(){
		editor.italicizeText();;
	}
}
class underlineButton{
	private TextEditor editor;

	public underlineButton(TextEditor editor) {
		this.editor = editor;
	}

	public void click(){
		editor.underlineText();;
	}
}

public class WithoutCommandPatternTextEditor {
	public static void main(String[] args) {
		TextEditor textEditor=new TextEditor();

		BoldButton button=new BoldButton(textEditor);
		button.click();
	}
}
