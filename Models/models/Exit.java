package models;

import enums.Direction;
import enums.RoomID;

public class Exit extends Model {

	private RoomID thisRoom;
	private RoomID nextRoom;
	
	private Direction dir; //location of exit relative to room
	
	public Exit(RoomID curr, RoomID next, Direction d, int x, int y, int dimension) {
		this.thisRoom = curr;
		this.nextRoom = next;
		this.dir = d;
		this.setXLoc(x);
		this.setYLoc(y);
		
		this.setDimensions(dimension);
	}
	
	public void setDimensions(int dimension) {
		switch (this.dir) {
			case NORTH:
				this.setHeight(0);
				this.setWidth(dimension);
				break;
			case SOUTH:
				this.setHeight(0);
				this.setWidth(dimension);
				break;
			case EAST:
				this.setHeight(dimension);
				this.setWidth(0);
				break;
			case WEST:
				this.setHeight(dimension);
				this.setWidth(0);
				break;
				
			default:
				break;
		}
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
	
	@Override
	public String toStringForAreaMap() {
		return this.getStringForAreaMap() + 
				"\n\t\tExists in Room ID: " + this.thisRoom +
				"\n\t\tLeads to Room ID: " + this.nextRoom +
				"\n\t\tDirection: " + this.dir;
	}
}
