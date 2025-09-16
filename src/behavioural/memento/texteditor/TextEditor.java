package behavioural.memento.texteditor;

import java.util.Stack;

public class TextEditor {
	private String content;
	static Stack<TextEditor> textEditors=new Stack<>();

	public void write(String content){
		this.content=content;
	}
	public EditorMemento save(){
		return  new EditorMemento(content);
	}
	public void restore(EditorMemento editorMemento){
		content = editorMemento.getContent();
	}

	public String getContent(){
		return content;
	}
}
