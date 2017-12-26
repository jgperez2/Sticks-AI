//This file is a part of the game of Sticks project.
//This class contains constants that are used within Sticks.java
//and TestSticks.java. These constants should be used rather
//than typing numbers into your Sticks program.
//
//You may change the numbers in this file for your testing purposes.

import java.util.Random;

public class Config {
	//The minimum and maximum number of sticks that the game
	//will play with.  The maximum will always be greater than
	//the minimum.
	public static final int MIN_STICKS = 10;  //min number of sticks
	public static final int MAX_STICKS = 200; //max number of sticks
	
	//The minimum and maximum number of sticks that a player
	//can pick up during a turn.
	//The minimum will always be greater than 0
	//The maximum will always be greater than the minimum.
	public static final int MIN_ACTION = 1;   //min sticks to pick up
	public static final int MAX_ACTION = 3;   //max sticks to pick up
	
	//The minimum and maximum number of games in which to train the AI.
	//The maximum will always be greater than the minimum.
	public static final int MIN_GAMES = 1;  //min number of games to train AI
	public static final int MAX_GAMES = 1000000; //max games to train AI
	
	//A random number generator that is expected to be used.
	public static final int SEED = 432;
	public static final Random RNG = new Random(SEED);  
	
}
