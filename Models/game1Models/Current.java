package game1Models;

import enums.Direction;

public class Current extends Game1Model {
	private Direction flowDir;
	private int incr;
	
	public Current(int x, int y, int h, int w, Direction d, int incr) {
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
		String info = this.getInfo() + 
				"\nFlow Direction: " + this.flowDir +
				"\nFlow Increase: " + this.incr;
		return info;
	}
}
