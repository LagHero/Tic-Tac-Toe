package game;


import game.StandardBoard.Position;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.Notification;
import javax.management.NotificationListener;

import player.Player;

public class MainBoard {

	private Player winner;
	private List<Player> players;
	private Set<SubBoard> winningSet;
	private boolean isGameOver;
	private SubBoard currentSubBoard;
	private StandardBoard[] subBoards;

	public static final HashSet<EnumSet<SubBoard>> possibleWinningSubBoardCombinations = new HashSet<EnumSet<SubBoard>>();{
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.NORTH_WEST, SubBoard.NORTH, SubBoard.NORTH_EAST));
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.WEST, SubBoard.CENTRAL, SubBoard.EAST));
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.SOUTH_WEST, SubBoard.SOUTH, SubBoard.SOUTH_EAST));
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.NORTH_WEST, SubBoard.WEST, SubBoard.SOUTH_WEST));
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.NORTH, SubBoard.CENTRAL, SubBoard.SOUTH));
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.NORTH_EAST, SubBoard.EAST, SubBoard.SOUTH_EAST));
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.NORTH_WEST, SubBoard.CENTRAL, SubBoard.SOUTH_EAST));
		possibleWinningSubBoardCombinations.add(EnumSet.of(SubBoard.SOUTH_WEST, SubBoard.CENTRAL, SubBoard.NORTH_EAST));
	}
	
	
	public MainBoard(List<Player> players){
		super();
		this.players = players;
		winner = null;
		winningSet = null;
		isGameOver = false;
		currentSubBoard = null;
		
		//  1 | 2 | 3
		// ---+---+---
		//  4 | 5 | 6
		// ---+---+---
		//  7 | 8 | 9
		subBoards = new StandardBoard[]{
				new StandardBoard(this, SubBoard.NORTH_WEST, new StateChangeListener()), // 1
				new StandardBoard(this, SubBoard.NORTH, new StateChangeListener()), // 2
				new StandardBoard(this, SubBoard.NORTH_EAST, new StateChangeListener()), // 3
				new StandardBoard(this, SubBoard.WEST, new StateChangeListener()), // 4
				new StandardBoard(this, SubBoard.CENTRAL, new StateChangeListener()), // 5
				new StandardBoard(this, SubBoard.EAST, new StateChangeListener()), // 6
				new StandardBoard(this, SubBoard.SOUTH_WEST, new StateChangeListener()), // 7
				new StandardBoard(this, SubBoard.SOUTH, new StateChangeListener()), // 8
				new StandardBoard(this, SubBoard.SOUTH_EAST, new StateChangeListener()) // 9
		};
	}
	
	public class StateChangeListener implements NotificationListener {

		public static final String SUB_BOARD_WINNER = "SUB_BOARD_WINNER";
		public static final String SUB_BOARD_TIE = "SUB_BOARD_TIE";
		public static final String PLAYER_QUIT = "PLAYER_QUIT";
		public static final String NEW_SUB_BOARD = "NEW_SUB_BOARD";
		
		@Override
		public void handleNotification(Notification notification, Object handback) {
			if(notification.getType().equals(NEW_SUB_BOARD)){
				calculatePossibleWinner();
				SubBoard newSubBoard = (SubBoard) handback;
				if(getSubBoard(newSubBoard).isGameOver()){
					currentSubBoard = null;
				}else{
					currentSubBoard = newSubBoard;
				}
			}else if(notification.getType().equals(SUB_BOARD_WINNER)){
				calculatePossibleWinner();
			}else if(notification.getType().equals(SUB_BOARD_TIE)){
				calculatePossibleWinner();
			}else if(notification.getType().equals(PLAYER_QUIT)){
				Player loser = (Player) handback;
				for(Player player : players){
					if(!player.equals(loser)){
						winner = player;
					}
				}
			}
		}
	}

	/**
	 * @return the current winner of this board (null if undecided).
	 */
	public Player getWinner(){
		return winner;
	}
	
	/**
	 * @return the current winner set of positions (null if undecided).
	 */
	public Set<SubBoard> getWinningSubBoards(){
		return winningSet;
	}
		
	/**
	 * Checks if the game is over.
	 * @return True if there is a winner of if its a cat game (tie), otherwise false.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * @return The current sub board that should be played on, null if the user can move anywhere. 
	 */
	public SubBoard getCurrentSubBoard() {
		return currentSubBoard;
	}

	/**
	 * 
	 * @param position 
	 * @return The StandardBoard represented by this sub board position, or null if the user can make a move on any sub board.
	 */
	public StandardBoard getSubBoard(SubBoard position){

		if(position == null){
			throw new IllegalStateException("Must pass in a valid position.");
		}
		
		//  1 | 2 | 3
		// ---+---+---
		//  4 | 5 | 6
		// ---+---+---
		//  7 | 8 | 9
		switch (position){
		case CENTRAL:
			return subBoards[4];
		case EAST:
			return subBoards[5];
		case NORTH:
			return subBoards[1];
		case NORTH_EAST:
			return subBoards[2];
		case NORTH_WEST:
			return subBoards[0];
		case SOUTH:
			return subBoards[7];
		case SOUTH_EAST:
			return subBoards[8];
		case SOUTH_WEST:
			return subBoards[6];
		case WEST:
			return subBoards[3];
		default:
			throw new IllegalStateException("Must pass in a valid position.");
		}
	}
	
	/**
	 * Checks if the given move/position is able to be set. 
	 * @param move - The position to check. 
	 * @return True if the given position has no owner.
	 */
	public boolean isMoveValid(Move move) {
		if(move == null){
			return true; //Player can't make a move and wants to quit.
		}
		
		Position movePosition = move.getPosition();
		SubBoard subBoardPosition = move.getSubBoard();
		if(movePosition == null || subBoardPosition == null){
			return false;
		}
		
		StandardBoard moveSubBoard = getSubBoard(subBoardPosition);
		//Trying to move into a closed board, nice try.
		if(moveSubBoard.isGameOver()){
			return false;
		}
		
		//Check if this move is on the correct sub board.
		if(moveSubBoard != null && (currentSubBoard == null || currentSubBoard.equals(subBoardPosition))){
			
			//Check if the position is valid on the sub board.
			if(moveSubBoard.isMoveValid(movePosition)){
				return true;
			}
		}
		
		return false;
	}

	private void calculatePossibleWinner() {
		boolean isCatGame = false;
				
		Iterator<EnumSet<SubBoard>> iter = possibleWinningSubBoardCombinations.iterator();
		//Loop through each of the winning combinations.
		while(iter.hasNext()){
			boolean isWinningSet = false;
			EnumSet<SubBoard> possibleWinningSet = iter.next();
//			java.lang.System.err.println("    Checking set: " + possibleWinningSet.toString());
			
			//Loop through each of the positions of this set to see if they have the same owner. 
			Iterator<SubBoard> iter2 = possibleWinningSet.iterator();

			//NOTE: Assuming that there are 3 sub boards.
			StandardBoard subBoard1 = getSubBoard(iter2.next());
			StandardBoard subBoard2 = getSubBoard(iter2.next());
			StandardBoard subBoard3 = getSubBoard(iter2.next());
			
			Player subBoard1Owner = subBoard1.getWinner();
			Player subBoard2Owner = subBoard2.getWinner();
			Player subBoard3Owner = subBoard3.getWinner();
//			java.lang.System.err.println(String.format("        Players: %s, %s, %s", subBoard1Owner, subBoard2Owner, subBoard3Owner));
			
			boolean subBoard1GameOver = subBoard1.isGameOver();
			boolean subBoard2GameOver = subBoard2.isGameOver();
			boolean subBoard3GameOver = subBoard3.isGameOver();
//			java.lang.System.err.println(String.format("        Game Over: %s, %s, %s", subBoard1GameOver, subBoard2GameOver, subBoard3GameOver));
			
			if(subBoard1GameOver){
				if(subBoard2GameOver){
					if(subBoard3GameOver){
						//All 3 games are over...
						isWinningSet = isWinningSet(subBoard1Owner, subBoard2Owner, subBoard3Owner);
						isCatGame = isCatGame || isCatSet(subBoard1Owner, subBoard2Owner, subBoard3Owner);
					}else{
						//Games 1 & 2 are over...
						isCatGame = isCatGame || isCatSet(subBoard1Owner, subBoard2Owner);
					}
				}else{
					if(subBoard3GameOver){
						//Games 1 & 3 are over...
						isCatGame = isCatGame || isCatSet(subBoard1Owner, subBoard3Owner);
					}else{
						//Game 1 is over...
						isCatGame = false;
					}
				}
			}else{
				if(subBoard2GameOver){
					if(subBoard3GameOver){
						//Games 2 & 3 are over...
						isCatGame = isCatGame || isCatSet(subBoard2Owner, subBoard3Owner);
					}else{
						//Game 2 is over...
						isCatGame = false;
					}
				}else{
					if(subBoard3GameOver){
						//Game 3 is over...
						isCatGame = false;
					}else{
						//No games are over...
						isCatGame = false;
					}
				}
			}
//			java.lang.System.err.println(String.format("        Is Winning Set: %s", isWinningSet));
			if(isWinningSet){
				winner = subBoard1Owner;
				winningSet = possibleWinningSet;
				isGameOver = true;
				return;
			}
		}
//		java.lang.System.err.println(String.format("    Is Cat Game: %s", isCatGame));
		if(isCatGame){
			isGameOver = true;
		}	
	}
	
	private boolean isCatSet(Player... subBoardOwners) {
		Player setWinner = null;
		for(Player owner : subBoardOwners){
			if(owner == null){
				//One of the owners of a board is null (cat board), this set is cat.
				return true;
			}
			if(setWinner == null){
				//First board, set the winner for the set.
				setWinner = owner;
			}else{
				//Check if the winner of this board matches the winners of previous boards.
				if(owner != setWinner){
					//New winner, not the same winner in all boards, this set is cat. 
					return true;
				}
			}
		}
		return false;
	}

	private boolean isWinningSet(Player subBoard1Owner, Player subBoard2Owner, Player subBoard3Owner) {
		//Check if any of the owners are null (cat board).
		if(subBoard1Owner == null || subBoard2Owner == null || subBoard3Owner == null){
			return false;
		}
		//Check if all the owners are the same.
		if(subBoard1Owner.equals(subBoard2Owner) && subBoard2Owner.equals(subBoard3Owner) && subBoard3Owner.equals(subBoard1Owner)){
			return true;
		}
		//Otherwise, there are different owners (cat set of boards).
		return false;
	}

	/**
	 * The sub board reference on the main board (sub board).
	 * <pre>
	 *   NW | N | NE
	 *  ----+---+----
	 *    W | C | E
	 *  ----+---+----
	 *   SW | S | SE
	 *  </pre>
	 */
	public enum SubBoard{
		NORTH_WEST,
		NORTH,
		NORTH_EAST,
		WEST,
		CENTRAL,
		EAST,
		SOUTH_WEST,
		SOUTH,
		SOUTH_EAST;
	}
	
}
