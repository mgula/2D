package game1Models;

import java.util.ArrayList;

import enums.Direction;

public class Interactable extends SolidObject {
	private int incr;
	private int currSegment;
	private int waitCounter = 0;
	private final int waitTime = 50;
	private int moveThreshL;
	private int moveThreshR;
	private int moveThreshU;
	private int moveThreshD;
	private Direction currDir;
	private Direction lastDir;
	
	public Interactable(int x, int y, int h, int w, Direction d, int moveVariance, int incr) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.currDir = d;
		this.moveThreshL = x - moveVariance;
		this.moveThreshR = x + moveVariance;
		this.moveThreshU = y - moveVariance;
		this.moveThreshD = y + moveVariance;
		this.incr = incr;
	}
	
	public Direction getDirection() {
		return this.currDir;
	}
	
	public int getIncr() {
		return this.incr;
	}
	
	public void move(Room r, ArrayList<Game1Model> e, Player p) {
		switch (this.currDir) {
			case EAST:
				while (this.currSegment < this.incr) {
					this.setXLoc(this.getXLoc() + 1);
					this.currSegment++;
					p.checkMovingSurfaces(r, e, true);
				}
				this.currSegment = 0;
				if (this.getXLoc() >= this.moveThreshR) {
					this.lastDir = this.currDir;
					this.currDir = Direction.IDLE;
				}
				break;
				
			case WEST:
				while (this.currSegment < this.incr) {
					this.setXLoc(this.getXLoc() - 1);
					this.currSegment++;
					p.checkMovingSurfaces(r, e, true);
				}
				this.currSegment = 0;
				if (this.getXLoc() <= this.moveThreshL) {
					this.lastDir = this.currDir;
					this.currDir = Direction.IDLE;
				}
				break;
				
			case NORTH:
				while (this.currSegment < this.incr) {
					this.setYLoc(this.getYLoc() - 1);
					this.currSegment++;
					p.checkMovingSurfaces(r, e, true);
				}
				this.currSegment = 0;
				if (this.getYLoc() <= this.moveThreshU) {
					this.lastDir = this.currDir;
					this.currDir = Direction.IDLE;
				}
				break;
				
			case SOUTH:
				while (this.currSegment < this.incr) {
					this.setYLoc(this.getYLoc() + 1);
					this.currSegment++;
					p.checkMovingSurfaces(r, e, true);
				}
				this.currSegment = 0;
				if (this.getYLoc() >= this.moveThreshD) {
					this.lastDir = this.currDir;
					this.currDir = Direction.IDLE;
				}
				break;
				
			/*Interactables wait for a moment before switching directions.*/
			case IDLE:
				if (this.waitCounter < this.waitTime) {
					this.waitCounter++;
				} else {
					this.waitCounter = 0;
					switch (this.lastDir) {
						case NORTH:
							this.currDir = Direction.SOUTH;
							break;
							
						case SOUTH:
							this.currDir = Direction.NORTH;
							break;
							
						case EAST:
							this.currDir = Direction.WEST;
							break;
							
						case WEST:
							this.currDir = Direction.EAST;
							break;
							
						default:
							break;
					}
				}
				
			default:
				break;
		}
	}
	
	@Override
	public String toString() {
		return this.getString() + 
				"\nIncrease: " + this.incr +
				"\nCurrent Direction: " + this.currDir +
				"\nLast Direction: " + this.lastDir;
	}
	
	@Override
	public String toStringForAreaMap() {
		return this.getStringForAreaMap() + 
				"\n\t\tIncrease: " + this.incr +
				"\n\t\tCurrent Direction: " + this.currDir +
				"\n\t\tLast Direction: " + this.lastDir;
	}
}
