package game1Models;

public class Platform extends Game1Model {

	public Platform(int x, int y, int w) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(0);
		this.setWidth(w);
	}
	
	@Override
	public String toString() {
		return this.getInfo();
	}
}
