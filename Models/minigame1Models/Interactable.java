package minigame1Models;

import enums.Direction;

/**
 * Instances of this class move. Crab's collision detection treats interactables as surfaces that the player can
 * land on, but not move through. Interactables deal no damage.
 * 
 * @author marcusgula
 *
 */
public class Interactable implements Minigame1Model {

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
	
	/**
	 * Create an Interactable instance with the specified parameters.
	 * 
	 * @param x the x coordinate of the instance
	 * @param y the y coordinate of the instance
	 * @param h the height of the instance
	 * @param w the width of the instance
	 * @param d the direction the instance will initially move in
	 * @param moveVariance specifies by how much the instance will move from their initial
	 * x and y coordinates
	 * @param incr how far the instance will move each tick
	 * @return a new Interactable instance
	 */
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
	
	/**
	 * This method provides the instances movement patterns. Instances will move either
	 * left and right, or up and down. They will pause before changing direction (from
	 * up to down, or from left to right). 
	 * <p>
	 * It is possible that interactable instances will push the crab in a certain direction.
	 * To enable this behavior, this method takes a crab as a parameter that it will use to
	 * call checkMovingSurfaces(), which will move the crab.
	 * 
	 * @param c the crab to be moved
	 */
	public void move(Crab c) {
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
