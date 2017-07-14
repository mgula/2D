package minigame1Models;

/**
 * Rocks don't move, and Crab's collision detection treats rocks as a surface that the player can
 * land on, but not move through. Rocks deal no damage.
 * 
 * @author marcusgula
 *
 */
public class Rock implements Minigame1Model {

	private final int xloc;
	private final int yloc;
	private int height;
	private int width;
	
	public Rock(int x, int y, int h, int w) {
		this.xloc = x;
		this.yloc = y;
		this.height = h;
		this.width = w;
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
