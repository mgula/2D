package models;

import java.util.ArrayList;

import enums.RoomID;

public class Room extends Game1Model {
	private RoomID ID;
	private ArrayList<Game1Model> environment;
	private ArrayList<Exit> roomLinks;
	
	public Room(RoomID n, int x, int y, int h, int w, ArrayList<Game1Model> e) {
		this.ID = n;
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.environment = e;
	}
	
	public Room(RoomID n, int x, int y, int h, int w, ArrayList<Game1Model> e, ArrayList<Exit> rl) {
		this.ID = n;
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.environment = e;
		this.roomLinks = rl;
	}
	
	public RoomID getID() {
		return this.ID;
	}
	
	public ArrayList<Game1Model> getEnvironment() {
		return this.environment;
	}
	
	public ArrayList<Exit> getRoomLinks() {
		return this.roomLinks;
	}
	
	@Override
	public String toString() {
		String info = "Room ID: " + this.ID +
				"\n" + this.getString();
		
		info += "\nEnvironment Info: \n";
		
		for (Game1Model m : this.environment) {
			info += m.toString() + "\n";
		}
		
		info += "Exit Info: \n";
		
		for (Exit e : this.roomLinks) {
			info += e.toString() + "\n";
		}
		return info;
	}
}
