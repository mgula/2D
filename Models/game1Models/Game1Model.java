package game1Models;

public abstract class Game1Model {
	
	private int xloc;
	private int yloc;
	private int height;
	private int width;

	public int getXLoc() {
		return this.xloc;
	}
	
	public int getYLoc() {
		return this.yloc;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setXLoc(int x) {
		this.xloc = x;
	}
	
	public void setYLoc(int y) {
		this.yloc = y;
	}
	
	public void setHeight(int h) {
		this.height = h;
	}
	
	public void setWidth(int w) {
		this.width = w;
	}
	
	public String getInfo() {
		return "Model name: " + this.getClass() + 
				"\nX Loc: " + this.xloc + 
				"\nY Loc: " + this.yloc + 
				"\nHeight: " + this.height +
				"\nWidth: " + this.width;
	}
}
