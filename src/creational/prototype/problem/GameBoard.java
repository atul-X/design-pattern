package creational.prototype.problem;

import creational.prototype.solutions.GamePiece;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
	private List<GamePiece> pieces=new ArrayList<>();
	public void addPiece(GamePiece piece){
		pieces.add(piece);
	}
	public List<GamePiece> getPieces(){
		return pieces;
	}
	public void showBoardState(){
		System.out.println("Current Game Board State:");
		for(GamePiece piece:pieces){
			System.out.println(piece);
		}
	}
	
	@Override
	public GameBoard clone() {
		GameBoard cloned = new GameBoard();
		for(GamePiece piece : pieces) {
			cloned.addPiece(piece.clone());
		}
		return cloned;
	}
}
