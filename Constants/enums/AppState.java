package enums;

public enum AppState {
	START,
	SELECT,
	GAME1,
	END,
	LOAD,
	INBETWEEN1,
	SETTINGS,
	SATISFIED; // only intended state can have this value; current state will never have this value
	
	private AppState(){};
}
