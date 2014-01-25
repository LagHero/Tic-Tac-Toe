package game;

import game.MainBoard.SubBoard;
import game.StandardBoard.Position;

public class Move {

	private final SubBoard subBoard;
	private final Position position;

	public Move(SubBoard subBoard, Position position) {
		super();
		this.subBoard = subBoard;
		this.position = position;
	}

	public SubBoard getSubBoard() {
		return subBoard;
	}

	public Position getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result
				+ ((subBoard == null) ? 0 : subBoard.hashCode());
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
		Move other = (Move) obj;
		if (position != other.position)
			return false;
		if (subBoard != other.subBoard)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Move [subBoard=" + subBoard + ", position=" + position + "]";
	}
}
