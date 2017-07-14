package enums;

public enum AppState {
	START,
	SELECT,
	GAME1,
	GAME2,
	GAME3,
	END,
	LOAD,
	TUTORIAL1,
	INBETWEEN1,
	INBETWEEN2, // state for displaying game 2 objectives
	INBETWEEN3, // state for displaying game 3 objectives
	SETTINGS,
	SATISFIED; // only intended state can have this value; current state will never have this value
	
	private AppState(){};
}
