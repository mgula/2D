package game1Models;

import java.util.ArrayList;

public class Room {

	private String name;
	private int height;
	private int width;
	private int groundLevel;
	private ArrayList<Game1Model> environment;
	
	public Room(String n, int h, int w, int g, ArrayList<Game1Model> e) {
		this.name = n;
		this.height = h;
		this.width = w;
		this.groundLevel = g;
		this.environment = e;
	}
	
	public String getName() {
		return this.name;
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
	
	public ArrayList<Game1Model> getEnvironment() {
		return this.environment;
	}
}
