package lld.snakeandladder;

public class Ladder extends BoardEntitiy {
	public Ladder(int start, int end) {
		super(start, end);
		if (start<=end){
			throw new IllegalStateException("Ladder  head must be at a  higher position than its tail");
		}
	}
}
