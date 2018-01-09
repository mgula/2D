package models;

public class Rock extends SolidObject {
	private static final long serialVersionUID = 1L;

	public Rock(int x, int y, int h, int w) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
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
