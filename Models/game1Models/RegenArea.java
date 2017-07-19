package game1Models;

public class RegenArea extends Game1Model {
	
	public RegenArea(int x, int y, int h, int w) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
	}
	
	@Override
	public String toString() {
		return this.getInfo();
	}
}
