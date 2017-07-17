package game1Models;

import java.util.ArrayList;

public class Room {

	private String ID;
	private int height;
	private int width;
	private int groundLevel;
	private ArrayList<Game1Model> environment;
	private boolean hasRoomLeft;
	private boolean hasRoomRight;
	private boolean hasRoomUp;
	private boolean hasRoomDown;
	
	public Room(String n, int h, int w, int g, ArrayList<Game1Model> e) {
		this.ID = n;
		this.height = h;
		this.width = w;
		this.groundLevel = g;
		this.environment = e;
	}
	
	public Room(String n, int h, int w, int g, ArrayList<Game1Model> e, boolean l, boolean r, boolean u, boolean d) {
		this.ID = n;
		this.height = h;
		this.width = w;
		this.groundLevel = g;
		this.environment = e;
		this.hasRoomLeft = l;
		this.hasRoomRight = r;
		this.hasRoomUp = u;
		this.hasRoomDown = d;
	}
	
	public String getName() {
		return this.ID;
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
	
	public boolean hasRoomLeft() {
		return this.hasRoomLeft;
	}
	
	public boolean hasRoomRight() {
		return this.hasRoomRight;
	}
	
	public boolean hasRoomUp() {
		return this.hasRoomUp;
	}
	
	public boolean hasRoomDown() {
		return this.hasRoomDown;
	}
}
