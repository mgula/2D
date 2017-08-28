package game1Models;

import java.io.Serializable;

public abstract class Game1Model implements Serializable {
	private static final long serialVersionUID = 2452479368975808182L;
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
	
	//each child will use this method in their toString()
	public String getString() {
		return "Model name: " + this.getClass() + 
				"\nX Loc: " + this.xloc + 
				"\nY Loc: " + this.yloc + 
				"\nHeight: " + this.height +
				"\nWidth: " + this.width;
	}
	
	//each child will use this method in their toStringForAreaMap() that they will override.
	public String getStringForAreaMap() {
		return "\n\t\tModel name: " + this.getClass() + 
				"\n\t\tX Loc: " + this.xloc + 
				"\n\t\tY Loc: " + this.yloc + 
				"\n\t\tHeight: " + this.height +
				"\n\t\tWidth: " + this.width;
	}
	
	//method for each child to override that AreaMap will use in its toString()
	public String toStringForAreaMap(){
		return null;
	}
}
