package game1Models;

import enums.Direction;

public class Interactable implements Game1Model {

	private int xloc;
	private int yloc;
	private int height;
	private int width;
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
		this.xloc = x;
		this.yloc = y;
		this.height = h;
		this.width = w;
		this.currDir = d;
		this.moveThreshL = x - moveVariance;
		this.moveThreshR = x + moveVariance;
		this.moveThreshU = y - moveVariance;
		this.moveThreshD = y + moveVariance;
		this.incr = incr;
	}
	
	@Override
	public int getXloc() {
		return this.xloc;
	}

	@Override
	public int getYloc() {
		return this.yloc;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}
	
	public Direction getDirection() {
		return this.currDir;
	}
	
	public int getIncr() {
		return this.incr;
	}
	
	public void move(Player c) {
		switch (this.currDir) {
			case EAST:
				while (this.currSegment < this.incr) {
					this.xloc++;
					this.currSegment++;
					c.checkMovingSurfaces(true);
				}
				this.currSegment = 0;
				if (this.xloc >= this.moveThreshR) {
					this.lastDir = this.currDir;
					this.currDir = Direction.IDLE;
				}
				break;
				
			case WEST:
				while (this.currSegment < this.incr) {
					this.xloc--;
					this.currSegment++;
					c.checkMovingSurfaces(true);
				}
				this.currSegment = 0;
				if (this.xloc <= this.moveThreshL) {
					this.lastDir = this.currDir;
					this.currDir = Direction.IDLE;
				}
				break;
				
			case NORTH:
				while (this.currSegment < this.incr) {
					this.yloc--;
					this.currSegment++;
					c.checkMovingSurfaces(true);
				}
				this.currSegment = 0;
				if (this.yloc <= this.moveThreshU) {
					this.lastDir = this.currDir;
					this.currDir = Direction.IDLE;
				}
				break;
				
			case SOUTH:
				while (this.currSegment < this.incr) {
					this.yloc++;
					this.currSegment++;
					c.checkMovingSurfaces(true);
				}
				this.currSegment = 0;
				if (this.yloc >= this.moveThreshD) {
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
}
