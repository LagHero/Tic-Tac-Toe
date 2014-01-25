package ui;

import java.util.List;

import game.MainBoard;
import game.Move;
import player.Player;

public interface UIListener {

	/**
	 * Notifies the listening object that a player has moved.  Note: The move can be null, which would mean that this play either quits or can't play. 
	 * @param player - The player that made the move.
	 * @param move - The position of this move.
	 * @param board - The game board.
	 */
	public void playerMoved(Player player, Move move, MainBoard board);
	
	/**
	 * Notifies the listening object that the game has ended.
	 * @param board - The game board.
	 */
	public void GameOver(MainBoard board);

	/**
	 * Notifies the listening object that a game has started with the give players.
	 * @param players - The current players playing in this new game. 
	 */
	public void newGame(List<Player> players);
	
	
}
