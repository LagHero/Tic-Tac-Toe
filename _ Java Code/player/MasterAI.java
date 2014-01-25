package player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

import game.MainBoard;
import game.Move;
import game.MainBoard.SubBoard;
import game.StandardBoard;
import game.StandardBoard.Position;

public class MasterAI extends Player {
	
	private static int RECURSION_MAX = 3;
	
	private String name = "Master AI";
	private char symbol = 'M';

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
		Move bestMove = null;
		int bestMoveWinCount = 0;
		List<Move> posibleMoves = getPosibleMoves(currentBoard);
		for(Move currentMove : posibleMoves){
			int currentMoveWinCount = getWinCount(currentBoard, currentMove, 1);
			if(currentMoveWinCount >= bestMoveWinCount){
				bestMove = currentMove;
				bestMoveWinCount = currentMoveWinCount;
			}
		}
		return bestMove;
	}

	private List<Move> getPosibleMoves(MainBoard currentBoard) {
		List<Move> posibleMoves = new ArrayList<Move>();
		
		//Check each possible sub board for all possible moves (usually can only play on one sub board).
		List<StandardBoard> posibleSubBoards = getPosibleSubBoards(currentBoard);
		for(StandardBoard currentStandardBoard : posibleSubBoards){
			
			//Check each possible position.
			List<Position> posiblePositions = getPosiblePositions(currentStandardBoard);
			for(Position currentPosition : posiblePositions){
				
				//Add this position to the list of possible moves.
				posibleMoves.add(new Move(currentStandardBoard.getPosition(), currentPosition));
			}
		}
		
		return posibleMoves;
	}

	private List<Position> getPosiblePositions(StandardBoard currentStandardBoard) {
		List<Position> posiblePositions = new ArrayList<Position>();
		
		for(Position currentPosition : Position.values()){
			if(currentStandardBoard.getPositionOwner(currentPosition) == null){
				posiblePositions.add(currentPosition);
			}
		}
		
		return posiblePositions;
	}

	private List<StandardBoard> getPosibleSubBoards(MainBoard currentBoard) {
		List<StandardBoard> posibleSubBoards = new ArrayList<StandardBoard>();
		
		//Check if we have to move in a sub board (common case).
		SubBoard currentSubBoardPos = currentBoard.getCurrentSubBoard();
		if(currentSubBoardPos != null){
			posibleSubBoards.add(currentBoard.getSubBoard(currentSubBoardPos));
			return posibleSubBoards;
		}
		
		//Otherwise we can move anywhere, need to check every sub board.
		for(SubBoard currentSubBoardPosition : SubBoard.values()){
			StandardBoard currentSubBoard = currentBoard.getSubBoard(currentSubBoardPosition);
			
			//Check if we can play on this sub board.
			if(!currentSubBoard.isGameOver()){
				posibleSubBoards.add(currentSubBoard);
			}
		}
			
		return posibleSubBoards;
	}

	private int getWinCount(MainBoard currentBoard, Move currentMove, int recursionDepth) {
		currentBoard.getSubBoard(currentMove.getSubBoard()).set;
		
		MainBoard currentBoard2 = new MainBoard();
		
		
		if(currentBoard.isGameOver()){
			if(currentBoard.getWinner().equals(this)){
				return 1;
			}else{
				return 0;
			}
		}
		
		boolean wonCurrentBoard = isWinningMove(currentBoard, currentMove);
		
		if(currentMove.getSubBoard())
		
		
		return 0;
	}

	private boolean isWinningMove(MainBoard currentBoard, Move currentMove) {
		StandardBoard currentSubBoard = currentBoard.getSubBoard(currentMove.getSubBoard());
		//Loop though each possible winning combination.
		for(EnumSet<Position> possibleWinningCombination : StandardBoard.possibleWinningSubBoardPositionCombinations){
			//If this possible winning combination includes the move I want to make.
			if(possibleWinningCombination.contains(currentMove.getPosition())){
				boolean isWinningMove = true;
				//Check if the other two positions are mine (which means its a winning move).
				for(Position currentPosition : possibleWinningCombination){
					if(!currentPosition.equals(currentMove.getPosition())){
						//If this position is not mine, check the next possible winning combination.
						if(!currentSubBoard.getPositionOwner(currentPosition).equals(this)){
							isWinningMove = false;
							break;
						}
					}
				}
				//Check if this is a winning set.
				if(isWinningMove){
					return true;
				}
			}
		}
		return false;
	}

	private class TempMainBoard extends MainBoard{
		
		
	}
	
	private class TempStandardBoard extends StandardBoard{
		
	}
}
