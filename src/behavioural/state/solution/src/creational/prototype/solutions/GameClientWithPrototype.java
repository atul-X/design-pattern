package creational.prototype.solutions;

public class GameClientWithPrototype {
	public static void main(String[] args) {
		// Create initial game pieces
		GamePiece redPiece = new GamePiece("Red", 1);
		GamePiece bluePiece = new GamePiece("Blue", 2);

		// Create game board and add pieces
		GameBoard gameBoard = new GameBoard();
		gameBoard.addPiece(redPiece);
		gameBoard.addPiece(bluePiece);

		// Show initial state
		gameBoard.showBoardState();

		GameBoard copiedBoard = gameBoard.clone();
		copiedBoard.showBoardState();
		redPiece.setPosition(3);
		gameBoard.showBoardState();
		copiedBoard.showBoardState();
	}
}
