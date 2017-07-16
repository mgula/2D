package game1Models;

public class Debris implements Game1Model {

	private final int xloc;
	private final int yloc;
	private final int height = 30;
	private final int width = 100;
	
	public Debris(int x, int y) {
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
