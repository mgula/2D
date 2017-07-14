package minigame1Models;

/**
 * Markers are used exclusively to indicate to the view that an image should be drawn at this
 * x and y coordinate. Crabs cannot interact with a marker in any way.
 * 
 * @author marcusgula
 *
 */
public class Marker implements Minigame1Model {

	private int xloc;
	private int yloc;
	
	public Marker(int x, int y) {
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
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}

}