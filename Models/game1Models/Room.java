package game1Models;

import java.util.ArrayList;

public class Room {
	private String ID;
	private int xLoc; //x coord of the SOUTH WEST most corner of the room
	private int yLoc; //y coord of the SOUTH WEST most corner of the room
	private int height;
	private int width;
	private ArrayList<Game1Model> environment;
	private String roomWestID;
	private String roomEastID;
	private String roomNorthID;
	private String roomSouthID;
	
	public Room(String n, int x, int y, int h, int w, ArrayList<Game1Model> e) {
		this.ID = n;
		this.xLoc = x;
		this.yLoc = y;
		this.height = h;
		this.width = w;
		this.environment = e;
	}
	
	public Room(String n, int x, int y, int h, int w, ArrayList<Game1Model> e, String west, String east, String north, String south) {
		this.ID = n;
		this.xLoc = x;
		this.yLoc = y;
		this.height = h;
		this.width = w;
		this.environment = e;
		this.roomWestID = west;
		this.roomEastID = east;
		this.roomNorthID = north;
		this.roomSouthID = south;
	}
	
	public String getID() {
		return this.ID;
	}
	
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
	
	public ArrayList<Game1Model> getEnvironment() {
		return this.environment;
	}
	
	public String getRoomWest() {
		return this.roomWestID;
	}
	
	public String getRoomEast() {
		return this.roomEastID;
	}
	
	public String getRoomNorth() {
		return this.roomNorthID;
	}
	
	public String getRoomSouth() {
		return this.roomSouthID;
	}
	
	@Override
	public String toString() {
		String info = "Room ID: " + this.ID +
				"\nX Loc: " + this.xLoc + 
				"\nY Loc: " + this.yLoc + 
				"\nHeight: " + this.height +
				"\nWidth: " + this.width +
				"\nEnvironment Contents:\n";
		
		for (Game1Model m : this.environment) {
			info += "\t" + m.toString() + "\n";
		}
		
		info += "\nWest Room ID: " + this.roomWestID +
				"\nEast Room ID: " + this.roomEastID +
				"\nNorth Room ID: " + this.roomNorthID +
				"\nSouth Room ID: " + this.roomSouthID;
		return info;
	}
}
