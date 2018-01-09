package models;

public class Platform extends Model {
	private static final long serialVersionUID = 1L;

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
}
