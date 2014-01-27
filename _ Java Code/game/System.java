package game;

import game.MainBoard.SubBoard;

import java.util.ArrayList;
import java.util.List;

import player.Player;
import ui.UIListener;


public class System {

	UIListener listener;
	boolean canceled;
	private List<Player> players;

	public System(){
	}
	
	public void run(List<Player> currentPlayers, UIListener listener){
		this.listener = listener;
		
		players = new ArrayList<Player>();
		if(currentPlayers.size() < 2){
			throw new IllegalArgumentException("You have to have more than 1 player.");
		}
		players.addAll(currentPlayers);
		
		canceled = false;
		
		listener.newGame(players);
		MainBoard mainBoard = new MainBoard(players);
		Player currentPlayer = getFirstPlayer();

		//Loop while there is no winner and we aren't canceled. 
		while (!mainBoard.isGameOver() && !canceled){
			Move move = currentPlayer.getMove(mainBoard);
			
			//Check if the move is valid, if not ask the user again. 
			while(!validateMove(mainBoard, move)){

				//Ask the player for their next move given the current board.
				move = currentPlayer.getMove(mainBoard);
			}
			
			//Check if this user wants to quit.
			if(move == null){
				StandardBoard currentSubBoard = mainBoard.getSubBoard(SubBoard.CENTRAL);
				currentSubBoard.setPositionOwner(currentPlayer, move);
				break;
			}

			//Apply the move to the board.
			SubBoard currentSubBoardPosition = move.getSubBoard();
			StandardBoard currentSubBoard = mainBoard.getSubBoard(currentSubBoardPosition);
			currentSubBoard.setPositionOwner(currentPlayer, move);
			
			//Notify UI
			listener.playerMoved(currentPlayer, move, mainBoard);
			
			//Get the next player.
			currentPlayer = getNextPlayer(currentPlayer);
			
		}
				
		//Notify UI of which play won.
		listener.GameOver(mainBoard);

	}

	private boolean validateMove(MainBoard mainBoard, Move move) {
		return mainBoard.isMoveValid(move);
	}
	
	private Player getFirstPlayer() {
		return players.get(0);
	}

	private Player getNextPlayer(Player currentPlayer) {
		//TODO: this will need to be adjusted if there are more then 2 players.
		for(Player player : players){
			if(!player.equals(currentPlayer)){
				return player;
			}
		}
		return null;
	}
}
