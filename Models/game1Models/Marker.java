package game1Models;

public class Marker implements Game1Model {

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