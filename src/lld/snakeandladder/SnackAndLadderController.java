package lld.snakeandladder;

import creational.builder.House;

import java.util.*;

public class SnackAndLadderController {
	private final Board board;
	private final Queue<Player> players;
	private final Dice dice;
	private GameStatus gameStatus;
	private Player winner;

	public SnackAndLadderController(Board board, Queue<Player> players, Dice dice, GameStatus gameStatus) {
		this.board = board;
		this.players = players;
		this.dice = dice;
		this.gameStatus = gameStatus;
	}
	public void play(){
		if(players.size()<2){
			throw new IllegalStateException();
		}
		this.gameStatus=GameStatus.RUNNING;
		System.out.println("Running Game");
		while (gameStatus==GameStatus.RUNNING){
			Player currentPlayer=players.poll();

		}
	}
	private void takeUrTurn(Player player){
		int roll= dice.roll();
		int currentPosition=player.getPosition();
		int nextPosition=currentPosition+roll;
		if (nextPosition> board.size){
			System.out.printf(
					"Oops, %s needs to land exactly on %d. Turn skipped.%n",
					player.getName(), board.getSize()
			);
			return;
		}
		if(nextPosition== board.size){
			player.setPosition(nextPosition);
			this.winner=player;
			this.gameStatus=GameStatus.FINISHED;
			System.out.printf(
					"Hooray! %s reached the final square %d and won!%n",
					player.getName(), board.getSize()
			);
			return;
		}
		int finalPosition= board.getFinalPosition(nextPosition);
		if (finalPosition>nextPosition){
			System.out.println();
		}

	}
}
