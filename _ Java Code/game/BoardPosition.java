package game;

import game.StandardBoard.Position;
import player.Player;

class BoardPosition {
	
	private final StandardBoard parentBoard;
	private final Position position; 
	private Player owner;
	

	BoardPosition(StandardBoard parentBoard, Position position){
		this.parentBoard = parentBoard;
		this.position = position;
		owner = null;
	}

	public StandardBoard getParentBoard() {
		return parentBoard;
	}
	
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Sets this position's owner to the given player.
	 * @param owner - The given player.
	 * @throws IllegalStateException If the owner has already been set.
	 */
	void setOwner(Player owner){
		if(this.owner == null){
			this.owner = owner;
		}else{
			throw new IllegalStateException("The position owner can only be set once.");
		}
	}
	
	/**
	 * @return the current owner of this position (null if undecided).
	 */
	public Player getOwner(){
		return owner;
	}
	
	@Override
	public String toString() {
		return String.format("Position: %s, Owner: %s", position, owner);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((parentBoard == null) ? 0 : parentBoard.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null || getClass() != obj.getClass()){
			return false;
		}
		
		BoardPosition other = (BoardPosition) obj;
		if (owner == null) {
			if (other.owner != null){
				return false;
			}
		} else if (!owner.equals(other.owner)){
			return false;
		}
		
		if (parentBoard == null) {
			if (other.parentBoard != null){
				return false;
			}
		} else if (!parentBoard.equals(other.parentBoard)){
			return false;
		}
		
		if (position != other.position){
			return false;
		}
		return true;
	}

	
	
}
