package player;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;

import game.MainBoard;
import game.Move;
import game.StandardBoard;
import game.MainBoard.SubBoard;
import game.StandardBoard.Position;

public class BlockerAI extends Player {

	String name = "Simple Blocker";
	char symbol = '#';
	
	public BlockerAI() {
		super();
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public char getSymbol() {
		return symbol;
	}

	@Override
	public void setSymbol(char newSymbol) {
		symbol = newSymbol;
	}
	
	@Override
	public Move getNextMove(MainBoard currentBoard, boolean lastMoveWasInvalid) {
		Move currentMove = new Move(SubBoard.CENTRAL, Position.CENTRAL);
		if(currentBoard.isMoveValid(currentMove)){
			return currentMove;
		}

		SubBoard currentSubBoard = currentBoard.getCurrentSubBoard();
		if(currentSubBoard == null){
			//Can move in any sub board, pick first one. 
			for(SubBoard subBoard : SubBoard.values()){
				if(!currentBoard.getSubBoard(subBoard).isGameOver()){
					currentSubBoard = subBoard;
					break;
				}
			}
		}
		
		HashSet<EnumSet<Position>> winningCombinations = StandardBoard.possibleWinningSubBoardPositionCombinations;
		EnumSet<Position> highestRankedSet = null;
		int highestRank = 0;
		for(EnumSet<Position> possibleWinningCombination : winningCombinations){
			Iterator<Position> iter = possibleWinningCombination.iterator();
			int rank = 0;
			while (iter.hasNext()){
				Position position = iter.next();
				Player owner = currentBoard.getSubBoard(currentSubBoard).getPositionOwner(position);
				if(owner == null){
					rank += 0;
				}else if(owner.equals(this)){
					rank += 3;
				}else if(!owner.equals(this)){
 					rank += 2;
				}
			}
			if(rank == 5){
				rank = -1;
			}else if(rank > 6){
				rank = -2;
			}
			if(rank >= highestRank){
				highestRank = rank;
				highestRankedSet = possibleWinningCombination;
			}
		}
		if(highestRankedSet != null){
			for(Position position : highestRankedSet){
				currentMove = new Move(currentSubBoard, position);
				if(currentBoard.isMoveValid(currentMove)){
					return currentMove;
				}
			}
		}else{
			for(Position position : Position.values()){
				currentMove = new Move(currentSubBoard, position);
				if(currentBoard.isMoveValid(currentMove)){
					return currentMove;
				}
			}
		}
		
		return null; 
	}

}
