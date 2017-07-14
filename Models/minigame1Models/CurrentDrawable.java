package minigame1Models;

public class CurrentDrawable implements Game1Model {

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