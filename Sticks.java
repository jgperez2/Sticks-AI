//
// Title:            Sticks
// Files:            Config.java, TestSticks.java
// Semester:         Fall 2016
//
// Author:           Jacob Perez
// Email:            Jgperez2@wisc.edu
// CS Login:         jperez
// Lecturer's Name:  Gary Dahl
// Lab Section:      334
///////////////////////////////////////////////////////////////////////////////

import java.util.Arrays;
import java.util.Scanner;

/**
 * This class creates a game of pick up sticks. The player sets the number of 
 * sticks they would like to start with. Students can then choose to either play
 * 2 player with another human player, play against a basic (random choosing)
 * computer, or play against a computer with AI. 
 *
 * @author Jacob Perez
 */

public class Sticks {
	
	/**
	 * This is the main method for the sticks game, where all methods are called
	 * from a menu per the user's instruction. 
	 * 
	 * @param	Config.java must be present
	 * 
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String player1Name = ""; // Stores the name of player 1.
		int menu = 0;	// Stores a numeric value for the menu choice.
		int startSticks = 0; // Stores the number of sticks to start with. 
		
		boolean nad = true;  // Boolean object used to exit loops.
		int i = 0;	// Counting number for loops.
		String blank = ""; // Used to store empty lines of text left over after
						   // scnr.next() or scnr.nextInt() method are called.
		
		System.out.println("Welcome to the Game of Sticks!\n==================="
				+ "===========\n");
		
		System.out.print("What is your name? ");
		player1Name = input.next();			// Trims extra whitespace off of the
		player1Name = player1Name.trim();   // player's entered name.
		blank = input.nextLine();
		System.out.println("Hello " + player1Name + ".");
		
		// Retrieves a value for starting number of sticks, from the user, between
		// the given bounds MIN_STICKS and MAX_STICKS found in Config.java.
		startSticks = promptUserForNumber(input, "How many sticks are there on"
				+ " the table initially (" + Config.MIN_STICKS + "-" + 
				Config.MAX_STICKS + ")? ", Config.MIN_STICKS, Config.MAX_STICKS);
		
		System.out.println("\nWould you like to:\n 1) Play against a friend\n 2) "
				+ "Play against computer (basic)\n 3) Play against computer "
				+ "with AI");
		
		// Has the user choose a number between 1 and 3 for the menu choice.
		menu = promptUserForNumber(input, "Which do you choose (1,2,3)? ", 1, 3);

		do{
			switch (menu){  // This switch method is used to easily navigate
							// through menu choices.
				case 1:
					String player2Name = "";
					System.out.print("\nWhat is your friend's name? ");
					player2Name = input.next();
					player2Name = player2Name.trim();
					System.out.println("Hello " + player2Name + ".\n");
					playAgainstFriend(input, startSticks, player1Name, player2Name);
					break;
					
				case 2:	// Creates a game with the computer without a strategy 
					    // table.
					System.out.println();
					playAgainstComputer(input, startSticks, player1Name, null);
					break;
					
				case 3:	// Creates a game with a computer opponent with AI 
					
					System.out.println();
					
					// Retrieves a number of games for the computer to train in
					// before playing the human player.
					int numGames = promptUserForNumber(input, "How many games "
							+ "should the AI learn from (" + Config.MIN_GAMES +
							" to " + Config.MAX_GAMES + ")? ", Config.MIN_GAMES,
							Config.MAX_GAMES);
					
					// Retrieves the Strategy table of the computer oponent with
					// the most wins after training.
					int [][] strategyTable = trainAi(startSticks, numGames);
					playAgainstComputer(input, startSticks, player1Name, 
							strategyTable);
					
					// Stores the Strategy table of the comuter as a string.
					String stTable = strategyTableToString(strategyTable);
					nad = true;
					do {	
						System.out.print("Would you like to see the strategy table"
								+ " (Y/N)? ");
						String table = input.next();
						char tbl = Character.toUpperCase(table.charAt(0));
						if (tbl == 'Y') {
							System.out.print(stTable);
							nad = false;
						}
						else if (tbl =='N') {
							nad = false;
						}
						else {
							System.out.println("Please enter Y or N.");
						}
					} while(nad);
					break;
				default:
					break;
			}
			nad = false;
		} while (nad);
		
		System.out.println("\n=========================================\nThank "
				+ "you for playing the Game of Sticks!");
		input.close();
	}

	/**
	 * This method encapsulates the code for prompting the user for a number and
	 * verifying the number is within the expected bounds.
	 * 
	 * @param input
	 *            The instance of the Scanner reading System.in.
	 * @param prompt
	 *            The prompt to the user requesting a number within a specific
	 *            range.
	 * @param min
	 *            The minimum acceptable number.
	 * @param max
	 *            The maximum acceptable number.
	 * @return The number entered by the user between and including min and max.
	 */
	static int promptUserForNumber(Scanner input, String prompt, 
			int min, int max) {
		int number = 0;	// Stores the number inputed by the user
		boolean nad = true;
		String blank = "";
		do{
			System.out.print(prompt);
			
			if (input.hasNextInt()) {	   // Checks to make sure that the user
				number = input.nextInt();  // entered a number.
				if (number >= min && number <= max){ // Checks to make sure the
					blank = input.nextLine();        // number is between the max
					nad = false;					 // and the min.
					
				} 
				else{
					System.out.println("Please enter a number between " + min +" and " + max + ".");
					blank = input.nextLine();
				}
			}
			else{
				String invalid = input.nextLine();  // Stores what the user inputs
													// if it is not a number.
				System.out.println("Error: expected a number between " + min +
						" and " + max + " but found: " + invalid);
			}
		} while(nad);
		
		return number;
	}
	
	/**
	 * This method has one person play the Game of Sticks against another
	 * person.
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1Name
	 *            The name of one player.
	 * @param player2Name
	 *            The name of the other player.
	 * 
	 *            As a courtesy, player2 is considered the friend and gets to
	 *            pick up sticks first.
	 * 
	 */
	static void playAgainstFriend(Scanner input, int startSticks, 
			String player1Name, String player2Name) {
		boolean nad = true;
		int sticksLeft = startSticks;  // Stores the number of sticks left. 
		int sticksTaken = 0;	// Stores the number of sticks taken in one turn.
		int max = Config.MAX_ACTION;
		int min = Config.MIN_ACTION;
		int i = 0;	// Sets which player is currently playing.
		int neg = 1;	// Number used to change the current player.
		String[] playerList = new String[2];  // Creates an array to  store the
		playerList[1] = player1Name;		  // two players' names.
		playerList[0] = player2Name;
		do{
			
			if (sticksLeft >= max){
				System.out.println("There are " + sticksLeft + " sticks on the "
						+ "board.");
				sticksTaken = promptUserForNumber(input, playerList[i] + ": How"
						+ " many sticks do you "
						+ "take (" + min + "-" + max + ")? ", min, max);
				sticksLeft = sticksLeft - sticksTaken;
				i += neg;  // Changes between 0 and 1.
				neg = neg * -1;	// Changes between -1 and 1.
			}
			else if (sticksLeft > 1){  // If sticksLeft is no longer longer greater 
				max = sticksLeft;      // the max action, then the sticks remaining
			}						   // becomes the new max.	
			else{
				max = sticksLeft;
				System.out.println("There is " + sticksLeft + " stick on the "
						+ "board.");
				sticksTaken = promptUserForNumber(input, playerList[i] + ": How"
						+ " many sticks do you "
						+ "take (" + min + "-" + max + ")? ", min, max);
				sticksLeft = sticksLeft - sticksTaken;
				i += neg;
				neg = neg * -1;
			}
				
				
		} while (sticksLeft > 0); // Runs the loop until there are no sticks left.
		System.out.println(playerList[i] + " wins. " + playerList[i + neg] + 
				" loses.");
		return;
	}	
		
	
	/**
	 * Make a choice about the number of sticks to pick up when given the number
	 * of sticks remaining.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining in the game.
	 * @return The number of sticks to pick up, or 0 if sticksRemaining is <= 0.
	 */
	static int basicChooseAction(int sticksRemaining) {
		if (sticksRemaining <= 0) {
			return 0;
		}
		else if (sticksRemaining < Config.MAX_ACTION) {	// Returns the minimum
			return Config.MIN_ACTION;					// if sticks left is less
		}												// than the max action.
		else {
			// Returns a random number between and including the min and max action.
			return Config.RNG.nextInt((Config.MAX_ACTION - Config.MIN_ACTION) 
					+1) + Config.MIN_ACTION;
		}  
	}
	
	/**
	 * This method has a person play against a computer.
	 * Call the promptUserForNumber method to obtain user input.  
	 * Call the aiChooseAction method with the actionRanking row 
	 * for the number of sticks remaining. 
	 * 
	 * If the strategyTable is null, then this method calls the 
	 * basicChooseAction method to make the decision about how 
	 * many sticks to pick up. If the strategyTable parameter
	 * is not null, this method makes the decision about how many sticks to 
	 * pick up by calling the aiChooseAction method. 
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param playerName
	 *            The name of one player.
	 * @param strategyTable
	 *            An array of action rankings. One action ranking for each stick
	 *            that the game begins with.
	 * 
	 */
	static void playAgainstComputer(Scanner input, int startSticks, 
			String playerName, int[][] strategyTable) {
		boolean nad = true;
		int sticksLeft = startSticks;  // Stores the number of sticks left. 
		int sticksTaken = 0;	// Stores the number of sticks taken in one turn.
		int max = Config.MAX_ACTION;
		int min = Config.MIN_ACTION;
		int i = 0;	// Sets which player is currently playing.
		int neg = 1;	// Number used to change the current player.
		String[] playerList = new String[2];// Creates an array to  store the
		playerList[0] = playerName;		    // players' names.
		playerList[1] = "Computer";
		do{
			
			if (sticksLeft >= max){
				System.out.println("There are " + sticksLeft + " sticks on the "
						+ "board.");
				if (i == 0) {
					sticksTaken = promptUserForNumber(input, playerList[i] + ": "
						+ "How many sticks do you "
						+ "take (" + min + "-" + max + ")? ", min, max);
				}
				else{
					if (strategyTable == null){	// If menu option #2 is chosen
												// the strategyTable will be null
												// and thus the computer will always
												// use the basicChoose Action method.
						sticksTaken = basicChooseAction(sticksLeft);
					}
					else {	// If menu option #3 is chosen then the AI will be
							// trained and a strategy table will be present. 
						sticksTaken = aiChooseAction(sticksLeft,
								strategyTable[sticksLeft-1]);
					}
					
					if (sticksTaken != 1){
						System.out.println(playerList[i] + " selects " + sticksTaken
								+ " sticks.");
					}
					else {
						System.out.println(playerList[i] + " selects " + sticksTaken
								+ " stick.");
					}
				}
				sticksLeft = sticksLeft - sticksTaken;
				i += neg;
				neg = neg * -1;
			}
			else if (sticksLeft > 1){  // If sticksLeft is no longer longer greater
				max = sticksLeft;      // the max action, then the sticks remaining
			}						   // becomes the new max.	
			else{
				max = sticksLeft;
				System.out.println("There is " + sticksLeft + " stick on the "
						+ "board.");
				if (i == 0) {
					sticksTaken = promptUserForNumber(input, playerList[i] + 
							": How many sticks do you take (" + min + "-" + 
							max + ")? ", min, max);
				}
				else{
					if (strategyTable == null){
						sticksTaken = basicChooseAction(sticksLeft);
					}
					else {
						sticksTaken = aiChooseAction(sticksLeft, 
								strategyTable[sticksLeft-1]);
					}
					System.out.println(playerList[i] + " selects " + sticksTaken
							+ " stick.");
				}
				sticksLeft = sticksLeft - sticksTaken;
				i += neg;
				neg = neg * -1;
			}
				
				
		} while (sticksLeft > 0);
		
		System.out.println(playerList[i] + " wins. " + playerList[i + neg] + 
				" loses.");
		return;
	}
	
	/**
	 * This method chooses the number of sticks to pick up based on the
	 * sticksRemaining and actionRanking parameters.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining to be picked up.
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @return The number of sticks to pick up. 0 is returned for the following
	 *         conditions: actionRanking is null, actionRanking has a length of
	 *         0, or sticksRemaining is <= 0.
	 * 
	 */
	static int aiChooseAction(int sticksRemaining, int[] actionRanking) {
		int sum = 0;	// Stores the sum of the action rankings.
		if (actionRanking == null) {
			return 0;
		}
		for (int i = 0; i < actionRanking.length; ++i) {
			sum += actionRanking[i];  
		}
		int num = Config.RNG.nextInt(sum)+1;  // Finds a random number between
											  // 1 and the sum.
		sum = 0;	// Stores the upper bound of the current action Ranking.
		if (sticksRemaining >= Config.MAX_ACTION) {
			for (int i = 0; i < actionRanking.length; ++i) {
				sum += actionRanking[i]; 	// Adds action rankings one at a
				if (num <= sum) {			// time and checks if the num is 
											// less than it. 
					return Config.MIN_ACTION + i;	// Once the num is within the
													// bounds the index, the action
													// which the index refers to 
				}									// will be returned.
			}
			return 0;
		}
		else if (sticksRemaining > 0) {
			return Config.MIN_ACTION;	// Will take 1 stick if there is only 1 left.
		}
		else {
			return 0;
		}
	}
	

	/**
	 * This method initializes each element of the array to 1. If actionRanking
	 * is null then method simply returns.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 */
	static void initializeActionRanking(int []actionRanking) {
		if (actionRanking == null) {
			return;
		}
		for (int i = 0; i < actionRanking.length; ++i) {	// Creates an array
			actionRanking[i] = 1;							// the length of the
															// number of actions
															// possible, and 
															// initializes them
															// all to 1.
		}
		return;
	}
	
	/**
	 * This method returns a string with the number of sticks left and the
	 * ranking for each action as follows.
	 * 
	 * @param sticksLeft
	 *            The number of sticks left.
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 * @return A string formatted as described.
	 */
	static String actionRankingToString(int sticksLeft, int[]actionRanking) {
		if (actionRanking == null) {
			return null;
		}
		String actRnkString = ""; 	// Stores the action ranking string.
		actRnkString = sticksLeft + "\t";	// First adds the # of sticks left.
		actRnkString = actRnkString.concat(actionRanking[0] + ""); // Adds each 
		for (int i = 1; i < actionRanking.length; ++i) {		   // action ranking
																   // to the string.
			actRnkString = actRnkString.concat( "," + actionRanking[i]);
		}
		actRnkString = actRnkString.concat("\n");	// Adds a new line at the end
													// of the String.
		return actRnkString;
	}


	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was lost, the actionRanking for the action is decremented by 1, but not
	 * allowing the value to go below 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnLoss(int []actionRanking, int action) {
		int i = action - Config.MIN_ACTION;
		if (actionRanking[i] > 1) {		// Decrements the action ranking of the
			--actionRanking[i];			// action performed in a losing game.
		}
		else {
			actionRanking[i] = 1;	// Ensures no action ranking can go below 1.
		}
	}
	
	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was won, the actionRanking for the action is incremented by 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnWin(int []actionRanking, int action) {
		int i = action - Config.MIN_ACTION;
		if (actionRanking[i] > 0) {// Increments the action ranking of the
			++actionRanking[i];    // action performed in a wining game.
		}
		else {
			actionRanking[i] = 1;	// Ensures no action ranking can go below 1.
		}
	}
	
	/**
	 * Allocates and initializes a 2 dimensional array. The number of rows
	 * corresponds to the number of startSticks. Each row is an actionRanking
	 * with an element for each possible action. The possible actions range from
	 * Config.MIN_ACTION to Config.MAX_ACTION. Each actionRanking is initialized
	 * with the initializeActionRanking method.
	 * 
	 * @param startSticks
	 *            The number of sticks the game is starting with.
	 * @return The two dimensional strategyTable, properly initialized.
	 */
	static int[][] createAndInitializeStrategyTable(int startSticks) {
		// Creates an array the length of the # of starting sticks and the width
		// of the number of actions possible.
		int[][] strategyTable = new int[startSticks][Config.MAX_ACTION - 
		                                             Config.MIN_ACTION + 1];
		
		for (int j = 0; j < strategyTable.length; ++j){	// Initializes each index of 
			initializeActionRanking(strategyTable[j]);	// a row to 1's using the
														// initializeActionRanking
														// method.
		}
		return strategyTable;
	}	
		
	/**
	 * This formats the whole strategyTable as a string utilizing the
	 * actionRankingToString method. 
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @return A string containing the properly formatted strategy table.
	 */
	static String strategyTableToString(int[][] strategyTable) {
		// Creates a String to hold the Strategy table with a title and index.
		String stTable = "\nStrategy Table\nSticks Rankings\n";
		for (int j = strategyTable.length-1; j >= 0; --j){	// Uses the 
															// actionRankingToString
															// method to add each
															// row of action rankings
															// to the String.
			stTable += actionRankingToString(j+1 ,strategyTable[j]);
		}
		return stTable;
	}	
	
	
	/**
	 * This updates the strategy table since a game was won.
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * 
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnWin(int[][] strategyTable, int[] 
			actionHistory) {
		for (int i = 0; i < actionHistory.length; ++i) {
			if (actionHistory[i] != 0) {	// Checks the actionHistory to see 
											// where moves were made.
				// Increments the index of the action within the strategy table
				// using the updateActionRankingOnWin method.
				updateActionRankingOnWin(strategyTable[i], actionHistory[i]);
			}
		}
		for (int i = 0; i < actionHistory.length; ++i) {
			actionHistory[i] = 0; 
		}
	}
	
	/**
	 * This updates the strategy table for a loss.
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnLoss(int[][] strategyTable, int[] 
			actionHistory) {
		for (int i = 0; i < actionHistory.length; ++i) {
			if (actionHistory[i] != 0) {	// Checks the actionHistory to see 
											// where moves were made.
				// Decrements the index of the action within the strategy table
				// using the updateActionRankingOnLoss method.
				updateActionRankingOnLoss(strategyTable[i], actionHistory[i]);
			}
		}
		for (int i = 0; i < actionHistory.length; ++i) {
			actionHistory[i] = 0; 
		}
	}	

	/**
	 * This method simulates a game between two players using their
	 * corresponding strategyTables. Use the aiChooseAction method
	 * to choose an action for each player. Record each player's 
	 * actions in their corresponding history array. 
	 * This method doesn't print out any of the actions being taken. 
	 * 
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1StrategyTable
	 *            An array of actionRankings.
	 * @param player1ActionHistory
	 *            An array for recording the actions that occur.
	 * @param player2StrategyTable
	 *            An array of actionRankings.
	 * @param player2ActionHistory
	 *            An array for recording the actions that occur.
	 * @return 1 or 2 indicating which player won the game.
	 */
	static int playAiVsAi(int startSticks, int[][] player1StrategyTable, 
			int[] player1ActionHistory, int[][] player2StrategyTable, 
			int[] player2ActionHistory) {
		boolean nad = true;
		int sticksLeft = startSticks;
		int sticksTaken = 0;
		int max = Config.MAX_ACTION;
		int min = Config.MIN_ACTION;
		int i = 0;
		int neg = 1;
		int[] playerList = new int[2];
		playerList[0] = 1;
		playerList[1] = 2;
		do{		// Runs a game between 2 AI computers with their own 
				// strategy table.
			
			if (sticksLeft >= max){
				if (i == 0) {
					if (player1StrategyTable == null){
						sticksTaken = basicChooseAction(sticksLeft);
						player1ActionHistory[sticksLeft-1] = sticksTaken;
					}
					else {
						sticksTaken = aiChooseAction(sticksLeft, 
								player1StrategyTable[sticksLeft-1]);
						player1ActionHistory[sticksLeft-1] = sticksTaken;
					}
				}
				else{
					if (player2StrategyTable == null){
						sticksTaken = basicChooseAction(sticksLeft);
						player2ActionHistory[sticksLeft-1] = sticksTaken;
					}
					else {
						sticksTaken = aiChooseAction(sticksLeft, 
								player2StrategyTable[sticksLeft-1]);
						player2ActionHistory[sticksLeft-1] = sticksTaken;
					}
				}
				sticksLeft = sticksLeft - sticksTaken;
				i += neg;
				neg = neg * -1;
			}
			else{
				max = sticksLeft;
			}	
		} while (sticksLeft > 0);
		return playerList[i];	// Returns the player number of the winner.
	}

	/**
	 * This method has the computer play against itself many times. Each time 
	 * it plays it records the history of its actions and uses those actions 
	 * to improve its strategy.
	 * 
	 * @param startSticks
	 *            The number of sticks to start with.
	 * @param numberOfGamesToPlay
	 *            The number of games to play and learn from.
	 * @return A strategyTable that can be used to make action choices when
	 *         playing a person. Returns null if startSticks is less than
	 *         Config.MIN_STICKS or greater than Config.MAX_STICKS. Also returns
	 *         null if numberOfGamesToPlay is less than 1.
	 */
	static int[][] trainAi(int startSticks, int numberOfGamesToPlay) {
		if (startSticks < Config.MIN_STICKS || startSticks > Config.MAX_STICKS){
			return null;
		}
		// Creates a Strategy table for Player 1.
		int[][] player1T = createAndInitializeStrategyTable(startSticks);
		int[] p1AH = new int[startSticks]; // Creates an action history for player 1.
		// Creates a Strategy table for Player 2.
		int[][] player2T = createAndInitializeStrategyTable(startSticks);
		int[] p2AH = new int[startSticks]; // Creates an action history for player 2.
		int winner = 0;	// Stores the winner of the current game.
		
		int ovrlWin = 0; // Stores a number of the overall winner with Player 1 
						 // wins adding 1 to the total and player 2 wins subtracting
						 // 1.
		
		for (int i = 0; i < numberOfGamesToPlay; ++i) {
			winner = playAiVsAi(startSticks, player1T, p1AH, player2T, p2AH);
			if (winner == 1) {
				updateStrategyTableOnWin(player1T, p1AH);
				updateStrategyTableOnLoss(player2T, p2AH);
				++ovrlWin;
			}
			else {
				updateStrategyTableOnWin(player2T, p2AH);
				updateStrategyTableOnLoss(player1T, p1AH);
				--ovrlWin;
			}
		}
		if(ovrlWin < 0){
			return player2T;
		}
		else {					// If player 1 won more times, or if they tied with
			return player1T;	// an equal number of wins, player 1's Strategy 
								// table will be returned
		}
	}

}
