package enums;

public enum GameState {
	UNINITIALIZED, // starting state for all games
	LOAD,
	DEBUG,
	DEATH,
	PLAY,
	PAUSE; // all 3 games can be paused
	
	private GameState(){};
}
