package models;

public class ForceDrawable extends Game1Model {
	private final int drawHeight = 50;
	private final int drawWidth = 185;
	
	public ForceDrawable(int x, int y) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(this.drawHeight);
		this.setWidth(this.drawWidth);
	}
	
	@Override
	public int getHeight() {
		return this.drawHeight;
	}
	
	@Override
	public int getWidth() {
		return this.drawWidth;
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