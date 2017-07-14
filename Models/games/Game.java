package games;

import enums.GameState;

public interface Game {
	public GameState getGameState();
	public void setGameState(GameState state);
	public GameState getLastState();
	public void setLastState(GameState state);
	public boolean getFirstTime();
	public void setFirstTime();
}
