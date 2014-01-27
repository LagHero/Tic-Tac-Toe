package game;


import game.MainBoard.StateChangeListener;
import game.MainBoard.SubBoard;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import javax.management.Notification;

import player.Player;

public class StandardBoard{

	private final MainBoard parentBoard;
	private final SubBoard position; 
	
	private Player winner;
	private List<Player> losers;
	private Set<Position> winningSet;
	private boolean isGameOver;
	private BoardPosition[] boardPositions;
	private final StateChangeListener stateChangeListener;
	private final AtomicLong sequenceNumber = new AtomicLong(0);

	public static final HashSet<EnumSet<Position>> possibleWinningSubBoardPositionCombinations = new HashSet<EnumSet<Position>>();	{
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.NORTH_WEST, Position.NORTH, Position.NORTH_EAST));
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.WEST, Position.CENTRAL, Position.EAST));
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.SOUTH_WEST, Position.SOUTH, Position.SOUTH_EAST));
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.NORTH_WEST, Position.WEST, Position.SOUTH_WEST));
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.NORTH, Position.CENTRAL, Position.SOUTH));
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.NORTH_EAST, Position.EAST, Position.SOUTH_EAST));
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.NORTH_WEST, Position.CENTRAL, Position.SOUTH_EAST));
		possibleWinningSubBoardPositionCombinations.add(EnumSet.of(Position.SOUTH_WEST, Position.CENTRAL, Position.NORTH_EAST));
	}
	
	public StandardBoard(MainBoard parentBoard, SubBoard position, StateChangeListener stateChangeListener){
		this.parentBoard = parentBoard;
		this.position = position;
		this.stateChangeListener = stateChangeListener;

		winner = null;
		losers = new ArrayList<Player>();
		winningSet = null;
		isGameOver = false;

		//  1 | 2 | 3
		// ---+---+---
		//  4 | 5 | 6
		// ---+---+---
		//  7 | 8 | 9
		boardPositions = new BoardPosition[]{
				new BoardPosition(this, Position.NORTH_WEST), // 1
				new BoardPosition(this, Position.NORTH), // 2
				new BoardPosition(this, Position.NORTH_EAST), // 3
				new BoardPosition(this, Position.WEST), // 4
				new BoardPosition(this, Position.CENTRAL), // 5
				new BoardPosition(this, Position.EAST), // 6
				new BoardPosition(this, Position.SOUTH_WEST), // 7
				new BoardPosition(this, Position.SOUTH), // 8
				new BoardPosition(this, Position.SOUTH_EAST) // 9
		};
	}
	
	public MainBoard getParentBoard() {
		return parentBoard;
	}

	public SubBoard getPosition() {
		return position;
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
	public Set<Position> getWinningSubBoards(){
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
	 * Get the owner for a given position. 
	 * @param position - The position to lookup. 
	 * @return The player that owns the given position, or null if undecided.
	 */
	public Player getPositionOwner(Position position){

		//  1 | 2 | 3
		// ---+---+---
		//  4 | 5 | 6
		// ---+---+---
		//  7 | 8 | 9
		//Get the board position, and set the owner
		switch (position){
		case CENTRAL:
			return boardPositions[4].getOwner();
		case EAST:
			return boardPositions[5].getOwner();
		case NORTH:
			return boardPositions[1].getOwner();
		case NORTH_EAST:
			return boardPositions[2].getOwner();
		case NORTH_WEST:
			return boardPositions[0].getOwner();
		case SOUTH:
			return boardPositions[7].getOwner();
		case SOUTH_EAST:
			return boardPositions[8].getOwner();
		case SOUTH_WEST:
			return boardPositions[6].getOwner();
		case WEST:
			return boardPositions[3].getOwner();
		default:
			throw new IllegalStateException("Must pass in a valid position.");
		}
	}
	
	/**
	 * Sets the given position's owner to the given player.
	 * @param position - The position to set. 
	 * @param owner - The given player.
	 * @throws IllegalStateException If the owner has already been set.
	 */
	void setPositionOwner(Player owner, Move move){
		if(move == null){
			java.lang.System.err.println(owner.toString() + " gives up!");
			//Player gives up.
			isGameOver = true;
			losers.add(owner);
			
			stateChangeListener.handleNotification(new Notification(StateChangeListener.PLAYER_QUIT, this, sequenceNumber.incrementAndGet()), owner);
			
			return;
		}

//		java.lang.System.err.println(owner.toString() + " moves to: " + move.toString());

		//  1 | 2 | 3
		// ---+---+---
		//  4 | 5 | 6
		// ---+---+---
		//  7 | 8 | 9
		//Get the board position, and set the owner
		SubBoard newSubBoard = null;
		switch (move.getPosition()){
		case CENTRAL:
			boardPositions[4].setOwner(owner);
			newSubBoard = SubBoard.CENTRAL;
			break;
		case EAST:
			boardPositions[5].setOwner(owner);
			newSubBoard = SubBoard.EAST;
			break;
		case NORTH:
			boardPositions[1].setOwner(owner);
			newSubBoard = SubBoard.NORTH;
			break;
		case NORTH_EAST:
			boardPositions[2].setOwner(owner);
			newSubBoard = SubBoard.NORTH_EAST;
			break;
		case NORTH_WEST:
			boardPositions[0].setOwner(owner);
			newSubBoard = SubBoard.NORTH_WEST;
			break;
		case SOUTH:
			boardPositions[7].setOwner(owner);
			newSubBoard = SubBoard.SOUTH;
			break;
		case SOUTH_EAST:
			boardPositions[8].setOwner(owner);
			newSubBoard = SubBoard.SOUTH_EAST;
			break;
		case SOUTH_WEST:
			boardPositions[6].setOwner(owner);
			newSubBoard = SubBoard.SOUTH_WEST;
			break;
		case WEST:
			boardPositions[3].setOwner(owner);
			newSubBoard = SubBoard.WEST;
			break;
		default:
			throw new IllegalStateException("Must pass in a valid position.");
		}
//		java.lang.System.err.println("    Next sub board: " + newSubBoard.toString());
		
		//See if there is a winner now or if the game is a draw.
		calculatePossibleWinner();

		stateChangeListener.handleNotification(new Notification(StateChangeListener.NEW_SUB_BOARD, this, sequenceNumber.incrementAndGet()), newSubBoard);
	}

	/**
	 * Checks if the given move/position is able to be set. 
	 * @param move - The position to check. 
	 * @return True if the given position has no owner.
	 */
	public boolean isMoveValid(Position move) {
		if(move == null){
			return true; //Player can't make a move and wants to quit.
		}else if(isGameOver()){
			return false; //This board in closed.
		}
		return getPositionOwner(move) == null;
	}

	private void calculatePossibleWinner() {
		boolean isBoardFull = true;
		Iterator<EnumSet<Position>> iter = possibleWinningSubBoardPositionCombinations.iterator();
		//Loop through each of the winning combinations.
		while(iter.hasNext()){
			boolean isWinningSet = true;
			EnumSet<Position> possibleWinningSet = iter.next();
			
			//See if the positions of this set have the same owner. 
			Iterator<Position> iter2 = possibleWinningSet.iterator();
			
			//NOTE: Assuming that there are 3 positions.
			Player position1Owner = getPositionOwner(iter2.next());
			Player position2Owner = getPositionOwner(iter2.next());
			Player position3Owner = getPositionOwner(iter2.next());
			
			if(position1Owner == null){
				//First position is open, not a winning set.
				isWinningSet = false;
				isBoardFull = false;
			}else{
				if(position2Owner == null){
					//Second position is open, not a winning set.
					isWinningSet = false;
					isBoardFull = false;
				}else{
					if(!position1Owner.equals(position2Owner)){
						//First 2 positions have different owners, not a winning set. 
						isWinningSet = false;
					}else{
						if(position3Owner == null){
							//Third position is open, not a winning set.
							isWinningSet = false;
							isBoardFull = false;
						}else{
							if(position1Owner.equals(position3Owner)){
								//All positions have the same owner, winning set and not a cat game.
								isWinningSet = true; //Already set.
							}else{
								//Last position has a different owner, not a winning set. 
								isWinningSet = false;
							}
						}
					}
				}
			}
			
			if(isWinningSet){
//				java.lang.System.err.println("        ...this set is a winner! - " + position1Owner.toString());
				winner = position1Owner;
				winningSet = possibleWinningSet;
				isGameOver = true;
				
				stateChangeListener.handleNotification(new Notification(StateChangeListener.SUB_BOARD_WINNER, this, sequenceNumber.incrementAndGet()), null);
				
				return;
			}
		}
		
		if(isBoardFull){
			isGameOver = true;
			
			stateChangeListener.handleNotification(new Notification(StateChangeListener.SUB_BOARD_TIE, this, sequenceNumber.incrementAndGet()), null);
			
			return;
		}
		
//		java.lang.System.err.println("    this board is not won but its still possible.");
		
	}
	
	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder("[");
		toString.append(String.format("[%s, %s, %s]", getPositionOwner(Position.NORTH_WEST) == null ? ' ' : getPositionOwner(Position.NORTH_WEST).getSymbol(),
				getPositionOwner(Position.NORTH) == null ? ' ' : getPositionOwner(Position.NORTH).getSymbol(),
						getPositionOwner(Position.NORTH_EAST) == null ? ' ' : getPositionOwner(Position.NORTH_EAST).getSymbol()));
		toString.append(", ");
		toString.append(String.format("[%s, %s, %s]", getPositionOwner(Position.WEST) == null ? ' ' : getPositionOwner(Position.WEST).getSymbol(),
				getPositionOwner(Position.CENTRAL) == null ? ' ' : getPositionOwner(Position.CENTRAL).getSymbol(),
						getPositionOwner(Position.EAST) == null ? ' ' : getPositionOwner(Position.EAST).getSymbol()));
		toString.append(", ");
		toString.append(String.format("[%s, %s, %s]", getPositionOwner(Position.SOUTH_WEST) == null ? ' ' : getPositionOwner(Position.SOUTH_WEST).getSymbol(),
				getPositionOwner(Position.SOUTH) == null ? ' ' : getPositionOwner(Position.SOUTH).getSymbol(),
						getPositionOwner(Position.SOUTH_EAST) == null ? ' ' : getPositionOwner(Position.SOUTH_EAST).getSymbol()));
		toString.append("]");
		return toString.toString();
		
	}
	
	public static HashSet<EnumSet<Position>> getSetsWithPosition(Position position){
		HashSet<EnumSet<Position>> positionSets = new HashSet<EnumSet<Position>>();
		
		for(EnumSet<Position> currentSet : possibleWinningSubBoardPositionCombinations){
			if(currentSet.contains(position)){
				positionSets.add(currentSet);
			}
		}
		
		return positionSets;
	}

	/**
	 * The position reference on a standard board (sub board).
	 * <pre>
	 *   NW | N | NE
	 *  ----+---+----
	 *    W | C | E
	 *  ----+---+----
	 *   SW | S | SE
	 *  </pre>
	 */
	public enum Position{
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
