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
		return "\t\tExit exists in room ID: " + this.thisRoom +
				"\n\t\t\tLeads to room ID: " + this.nextRoom +
				"\n\t\t\tDirection: " + this.dir +
				"\n\t\t\tX loc: " + this.xLoc + 
				"\n\t\t\tY loc: " + this.yLoc + 
				"\n\t\t\tHeight: " + this.height +
				"\n\t\t\tWidth: " + this.width;
	}
}
