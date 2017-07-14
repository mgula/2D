package minigame1Models;

/**
 * 
 * Sea debri doesn't move, and Crab's collision detection treats sea debri as a surface that the player can
 * land on, but not move through. Sea debri deals no damage.
 * 
 * @author marcusgula
 *
 */
public class SeaDebris implements Minigame1Model {

	private final int xloc;
	private final int yloc;
	private final int height = 30;
	private final int width = 100;
	
	public SeaDebris(int x, int y) {
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
