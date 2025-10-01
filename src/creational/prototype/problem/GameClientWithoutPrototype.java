package creational.prototype.problem;

public class GameClientWithoutPrototype {
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

		GameBoard copiedBoard = new GameBoard();
		for (GamePiece piece : gameBoard.getPieces()) {
			copiedBoard.addPiece(new GamePiece(piece.getColor(),piece.getPosition()));
		}
		copiedBoard.showBoardState();

	}
}
