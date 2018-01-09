package models;

import java.io.Serializable;

public abstract class Model implements Serializable {
	private static final long serialVersionUID = 1L;
	private int xLoc;
	private int yLoc;
	private int height;
	private int width;

	public int getXLoc() {
		return this.xLoc;
	}
	
	public int getYLoc() {
		return this.yLoc;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setXLoc(int x) {
		this.xLoc = x;
	}
	
	public void setYLoc(int y) {
		this.yLoc = y;
	}
	
	public void setHeight(int h) {
		this.height = h;
	}
	
	public void setWidth(int w) {
		this.width = w;
	}
	
	/*Not necessarily a toString() - each subclass will use this in their actual toString(). Each
	 *toString() will be formatted to print in a way that makes sense for AreaMap's toString().*/
	public String getString() {
		return "\t\tModel name: " + this.getClass() + 
				"\n\t\t\tX loc: " + this.xLoc + 
				"\n\t\t\tY loc: " + this.yLoc + 
				"\n\t\t\tHeight: " + this.height +
				"\n\t\t\tWidth: " + this.width;
	}
}
