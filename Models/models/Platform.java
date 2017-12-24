package models;

public class Platform extends Model {

	public Platform(int x, int y, int w) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(0);
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
