package game1Models;

import java.util.ArrayList;

public class Room {

	private int height;
	private int width;
	private int groundLevel;
	private ArrayList<Game1Model> environment;
	private Room roomNorth = null;
	private Room roomSouth = null;
	private Room roomEast = null;
	private Room roomWest = null;
	
	public Room(int h, int w, int g, ArrayList<Game1Model> e) {
		this.height = h;
		this.width = w;
		this.groundLevel = g;
		this.environment = e;
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
	
	public void setNorthRoom(Room r) {
		this.roomNorth = r;
	}
	
	public void setSouthRoom(Room r) {
		this.roomSouth = r;
	}
	
	public void setEastRoom(Room r) {
		this.roomEast = r;
	}
	
	public void setWestRoom(Room r) {
		this.roomWest = r;
	}
}
