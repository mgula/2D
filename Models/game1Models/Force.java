package game1Models;

import enums.Direction;

public class Force extends EventArea {
	private Direction flowDir;
	private int incr;
	
	public Force(int x, int y, int h, int w, Direction d, int incr) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.flowDir = d;
		this.incr = incr;
	}
	
	public int getIncr() {
		return this.incr;
	}
	
	public Direction getFlowDirection() {
		return this.flowDir;
	}
	
	public void setIncr(int s) {
		this.incr = s;
	}
	public void setFlowDirection(Direction d) {
		this.flowDir = d;
	}
	
	@Override
	public String toString() {
		return this.getString() + 
				"\nFlow Direction: " + this.flowDir +
				"\nFlow Increase: " + this.incr;
	}
	
	@Override
	public String toStringForAreaMap() {
		return this.getStringForAreaMap() + 
				"\n\t\tFlow Direction: " + this.flowDir +
				"\n\t\tFlow Increase: " + this.incr;
	}
}