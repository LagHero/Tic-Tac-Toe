package player;

import game.MainBoard;
import game.MainBoard.SubBoard;
import game.Move;
import game.StandardBoard;
import game.StandardBoard.Position;


/**
 * This is just a test AI that loops through the list of positions and moves to the first open one. 
 * @author Aaron Gehri
 */
public class TestAI extends Player{

	private String name = "Test AI";
	private char symbol = '?';
		
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String newName) {
		name = newName;
	}

	@Override
	public void setSymbol(char newSymbol) {
		symbol = newSymbol;
	}

	@Override
	public char getSymbol() {
		return symbol;
	}
	
	@Override
	public Move getNextMove(MainBoard currentBoard, boolean lastMoveWasInvalid) {
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
		for(Position position : StandardBoard.Position.values()){
			Move currentMove = new Move(currentSubBoard, position);
			if(currentBoard.isMoveValid(currentMove)){
				return currentMove;
			}
		}
		return null;
		
	}


}
