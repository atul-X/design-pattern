package memento.texteditor;

import java.util.Stack;

public class CareTaker {
	Stack<EditorMemento> history=new Stack<>();
	public void saveState(TextEditor textEditor){
		history.push(textEditor.save());
	}
	public void  undo(TextEditor editor){
		if(!history.empty()){
			history.pop();
			editor.restore(history.peek());
		}
	}
}
