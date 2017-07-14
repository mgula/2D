package minigame1Models;

/**
 * Currents are implemented as small slices that are spaced out so that, in a current area,
 * the player will detect a collision with at least one of the slices. These slices are
 * difficult to draw, so this class will be placed over the slices and drawn by view.
 * 
 * @author marcusgula
 *
 */
public class CurrentDrawable implements Minigame1Model {

	private final int xloc;
	private final int yloc;
	private int height = 50;
	private int width = 185;
	
	public CurrentDrawable(int x, int y) {
		this.xloc = x;
		this.yloc = y;
	}
	
	@Override
	public int getXloc() {
		return this.xloc;
	}

	@Override
	public int getYloc() {
		return this.yloc;
	}
	
	@Override
	public int getHeight() {
		return this.height;
	}
	
	@Override
	public int getWidth() {
		return this.width;
	}
}