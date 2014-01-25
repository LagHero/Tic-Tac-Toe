package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import player.BlockerAI;
import player.Player;
import player.TestAI;

public class ConsoleScreen {

	public static void main(String[] args) {

//		Player player1 = new TestAI();
//		player1.setName("Player 1");
//		player1.setSymbol('1');
		
//		Player player2 = new TestAI();
//		player2.setName("Player 2");
//		player2.setSymbol('2');
		
		Player player1 = new BlockerAI();
		player1.setName("Blocker 1");
		player1.setSymbol('1');
		
		Player player2 = new BlockerAI();
		player2.setName("Blocker 2");
		player2.setSymbol('2');
		
		List<Player> players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		
		
		game.System simpleGame = new game.System();
		
		char inputChar = 0;
		do{
			simpleGame.run(players, new ConsoleUpdateListener());
			System.out.println("Do you want to play again? [Y]: ");
			try {
				inputChar = (char) System.in.read(); //Waits until the user enters data...
				System.in.skip(1000);
			} catch (IOException e) {
				//Do nothing
			}
		}while(inputChar == 'y' || inputChar == 'Y' );

		System.out.println("Thanks for playing!");
		
	}

}
