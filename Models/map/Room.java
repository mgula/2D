package map;

import java.io.Serializable;
import java.util.ArrayList;

import models.Model;
import enums.RoomID;

public class Room implements Serializable {
	private static final long serialVersionUID = 1L;
	private int xloc;
	private int yloc;
	private int height;
	private int width;
	
	private RoomID ID;
	private ArrayList<Model> environment;
	private ArrayList<Exit> roomLinks;
	
	public static final int RoomDataArrayLength = 4;
	
	public Room(RoomID n, int x, int y, int h, int w, ArrayList<Model> e) {
		this.ID = n;
		this.xloc = x;
		this.yloc = y;
		this.height = h;
		this.width = w;
		this.environment = e;
	}
	
	public Room(RoomID n, int x, int y, int h, int w, ArrayList<Model> e, ArrayList<Exit> rl) {
		this.ID = n;
		this.xloc = x;
		this.yloc = y;
		this.height = h;
		this.width = w;
		this.environment = e;
		this.roomLinks = rl;
	}
	
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
	
	public RoomID getID() {
		return this.ID;
	}
	
	public ArrayList<Model> getEnvironment() {
		return this.environment;
	}
	
	public ArrayList<Exit> getRoomLinks() {
		return this.roomLinks;
	}
}
