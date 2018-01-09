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
	
	//each child will use this method in their toString()
	public String getString() {
		return "Model name: " + this.getClass() + 
				"\nX Loc: " + this.xLoc + 
				"\nY Loc: " + this.yLoc + 
				"\nHeight: " + this.height +
				"\nWidth: " + this.width;
	}
	
	//each child will use this method in their toStringForAreaMap() that they will override.
	public String getStringForAreaMap() {
		return "\n\t\tModel name: " + this.getClass() + 
				"\n\t\tX Loc: " + this.xLoc + 
				"\n\t\tY Loc: " + this.yLoc + 
				"\n\t\tHeight: " + this.height +
				"\n\t\tWidth: " + this.width;
	}
	
	//method for each child to override that AreaMap will use in its toString()
	public String toStringForAreaMap(){
		return null;
	}
}
