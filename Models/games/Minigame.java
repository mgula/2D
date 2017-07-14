package games;

import enums.GameState;

/**
 * Underlying minigame class that all minigames will implement.
 * 
 * @author marcusgula
 *
 */
public interface Minigame {
	public GameState getGameState();
	public void setGameState(GameState state);
	public GameState getLastState();
	public void setLastState(GameState state);
	public boolean getFirstTime();
	public void setFirstTime();
}
