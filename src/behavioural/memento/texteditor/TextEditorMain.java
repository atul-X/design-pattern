package behavioural.memento.texteditor;

public class TextEditorMain {
	public static void main(String[] args) {
		TextEditor textEditor=new TextEditor();
		CareTaker careTaker=new CareTaker();
		textEditor.write("abc");
		careTaker.saveState(textEditor);
		textEditor.write("xyz");
		careTaker.saveState(textEditor);
		//Problem -> Undo the last write;
		careTaker.undo(textEditor);
		System.out.println(textEditor.getContent());

	}


}
