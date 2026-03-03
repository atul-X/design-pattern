package lld.snakeandladder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
	int size;
	Map<Integer,Integer> snakesAndLadders;

	public Board(int size, List<BoardEntitiy> entities) {
		this.size = size;
		this.snakesAndLadders=new HashMap<>();
		for (BoardEntitiy entitiy:entities){
			snakesAndLadders.put(entitiy.start,entitiy.end);
		}
	}

	public int getSize() {
		return size;
	}
	public int getFinalPosition(int position){
		return snakesAndLadders.getOrDefault(position,position);
	}
}
