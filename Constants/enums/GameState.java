package enums;

public enum GameState {
	UNINITIALIZED, // starting state for all games
	LOAD,
	DEBUG,
	PLAY, // this state is for only game 3 - also mini game 3's only state 
	LOSE, // possible to lose in games 1 and 2
	ROUNDOVER, 
	WIN, // possible to win in games 1 and 2
	STAGE1, // game 1 state
	STAGE2, // game 1 state
	STAGE3, // game 1 state 
	ROUND1, // game 2 state
	ROUND2, // game 2 state
	ROUND3, // game 2 state
	ROUND4, // game 2 state
	ROUND5, // game 2 states
	PAUSE; // all 3 games can be paused
	
	private GameState(){};
}
