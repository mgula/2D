package minigame1Models;

public class Map {

	private int height;
	private int width;
	private int groundLevel;
	
	public Map(int h, int w, int g) {
		this.height = h;
		this.width = w;
		this.groundLevel = g;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getGroundLevel() {
		return this.groundLevel;
	}
}
