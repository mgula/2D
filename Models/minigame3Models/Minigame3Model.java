package minigame3Models;

/**
 * Minigame 3 Models Interface
 * All models in minigame 3 implement this
 * @author kylew
 */
public interface Minigame3Model {

	public int getXloc();
	public int getYloc();
	public int getDimensions();
	public int getImageNumber();
	public void resetCoords();
}
