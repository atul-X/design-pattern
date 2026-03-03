package lld.snakeandladder;

public class Snake extends BoardEntitiy {
	public Snake(int start, int end) {
		super(start, end);
		if (start<=end){
			throw new IllegalStateException("Snake head must be at a  higher position than its tail");
		}
	}
}
