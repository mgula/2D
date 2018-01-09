package map;

import java.io.Serializable;
import enums.Direction;
import enums.RoomID;

public class Exit implements Serializable {
	private static final long serialVersionUID = 1L;
	private int xLoc;
	private int yLoc;
	private int height;
	private int width;

	private RoomID thisRoom;
	private RoomID nextRoom;
	
	private Direction dir; //location of exit relative to room
	
	public Exit(RoomID curr, RoomID next, Direction d, int x, int y, int dimension) {
		this.thisRoom = curr;
		this.nextRoom = next;
		this.dir = d;
		this.xLoc = x;
		this.yLoc = y;
		
		this.setDimensions(dimension);
	}
	
	public void setDimensions(int dimension) {
		switch (this.dir) {
			case NORTH:
				this.height = 0;
				this.width = dimension;
				break;
			case SOUTH:
				this.height = 0;
				this.width = dimension;
				break;
			case EAST:
				this.height = dimension;
				this.width = 0;
				break;
			case WEST:
				this.height = dimension;
				this.width = 0;
				break;
				
			default:
				break;
		}
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
	
	public RoomID getThisRoom() {
		return this.thisRoom;
	}
	
	public RoomID getNextRoom() {
		return this.nextRoom;
	}
	
	public Direction getDirection() {
		return this.dir;
	}
	
	@Override 
	public String toString() {
		return this.getString() + 
				"\nExists in Room ID: " + this.thisRoom +
				"\nLeads to Room ID: " + this.nextRoom +
				"\nDirection: " + this.dir;
	}
	
	public String toStringForAreaMap() {
		return this.getStringForAreaMap() + 
				"\n\t\tExists in Room ID: " + this.thisRoom +
				"\n\t\tLeads to Room ID: " + this.nextRoom +
				"\n\t\tDirection: " + this.dir;
	}
	
	public String getStringForAreaMap() {
		return "\n\t\tModel name: " + this.getClass() + 
				"\n\t\tX Loc: " + this.xLoc + 
				"\n\t\tY Loc: " + this.yLoc + 
				"\n\t\tHeight: " + this.height +
				"\n\t\tWidth: " + this.width;
	}
	
	public String getString() {
		return "Model name: " + this.getClass() + 
				"\nX Loc: " + this.xLoc + 
				"\nY Loc: " + this.yLoc + 
				"\nHeight: " + this.height +
				"\nWidth: " + this.width;
	}
}
