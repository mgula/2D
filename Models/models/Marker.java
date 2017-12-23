package models;

public class Marker extends Game1Model {

	public Marker(int x, int y) {
		this.setXLoc(x);
		this.setYLoc(y);
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}
	
	@Override
	public String toString() {
		return this.getString();
	}
	
	@Override
	public String toStringForAreaMap() {
		return this.getStringForAreaMap();
	}
}