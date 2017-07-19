package game1Models;

public class Debris extends Game1Model {
	private final int debrisHeight = 30;
	private final int debrisWidth = 100;
	
	public Debris(int x, int y) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(this.debrisHeight);
		this.setWidth(this.debrisWidth);
	}
	
	@Override
	public int getHeight() {
		return this.debrisHeight;
	}
	
	@Override
	public int getWidth() {
		return this.debrisWidth;
	}
	
	@Override
	public String toString() {
		return this.getInfo();
	}
}
