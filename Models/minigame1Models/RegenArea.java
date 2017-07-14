package minigame1Models;

/**
 * Regeneration areas restore health to the player. Crab's collision detection doesn't treat regeneration areas
 * as surfaces - the player can move through (and cannot land on) a regeneration area.
 * 
 * @author marcusgula
 *
 */
public class RegenArea implements Minigame1Model {
	
	private final int xloc;
	private final int yloc;
	private int height;
	private int width;
	
	public RegenArea(int x, int y, int h, int w) {
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
