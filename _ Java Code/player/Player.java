package player;

import game.MainBoard;
import game.Move;

public abstract class Player {

	public final Move getMove(MainBoard currentBoard){
		Move move = getNextMove(currentBoard, false);
		while (!currentBoard.isMoveValid(move)){
			move = getNextMove(currentBoard, true);
		}
		return move;
	}
	
	public abstract String getName();
	
	public abstract void setName(String newName);
	
	public abstract char getSymbol();
	
	public abstract void setSymbol(char newSymbol);
	
	/**
	 * @param currentBoard - The full board for reference. 
	 * @param subBoard - The sub board that the player needs to move on.
	 * @param lastMoveWasInvalid - True if an invalid move use just returned.
	 * @return
	 */
	public abstract Move getNextMove(MainBoard currentBoard, boolean lastMoveWasInvalid);
	
	@Override
	public final String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		String name = getName();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		String name = getName();
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
	
	
	
}
