package game1Models;

public class Map {

	private int upperXBound;
	private int lowerXBound;
	private int upperYBound;
	private int lowerYBound;
	
	public Map(int upperX, int lowerX, int upperY, int lowerY) {
		this.upperXBound = upperX;
		this.lowerXBound = lowerX;
		this.upperYBound = upperY;
		this.lowerYBound = lowerY;
	}
	
	public int getUpperXBound() {
		return this.upperXBound;
	}
	
	public int getLowerXBound() {
		return this.lowerXBound;
	}
	
	public int getUpperYBound() {
		return this.upperYBound;
	}
	
	public int getLowerYBound() {
		return this.lowerYBound;
	}
}
