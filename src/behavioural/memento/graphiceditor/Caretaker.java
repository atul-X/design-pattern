package behavioural.memento.graphiceditor;

import java.util.Stack;

public class Caretaker {

	private final Stack<EditorMemento> history = new Stack<>();

	public void saveState(GraphicEditor graphicEditor) {
		// TODO: Save the current behavioural.state of the graphic editor by pushing its behavioural.memento onto the history stack.
		history.push(graphicEditor.save());
	}

	public void undo(GraphicEditor graphicEditor){
		// TODO: Restore the last saved behavioural.state of the graphic editor if history is not empty.
		if(!history.isEmpty()){
			history.pop();
			graphicEditor.restore(history.peek());
		}
	}
}
