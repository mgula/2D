package game1Models;

import enums.Direction;

/*X loc must be within moveThreshL and moveThreshR!*/
public class EnemyA extends Enemy {
	private final int xIncr;
	private final int damage;
	private int moveThreshL;
	private int moveThreshR;
	private Direction currDir;

	public EnemyA(int x, int y, int h, int w, Direction d, int moveVariance, int xIncr, int dam) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.currDir = d;
		this.moveThreshL = x - moveVariance;
		this.moveThreshR = x + moveVariance;
		this.xIncr = xIncr;
		this.damage = dam;
	}
	
	public Direction getCurrDir() {
		return this.currDir;
	}
	
	@Override
	public void move() {
		switch (this.currDir) {
			case EAST:
				this.setXLoc(this.getXLoc() + this.xIncr);
				if (this.getXLoc() >= this.moveThreshR) {
					this.currDir = Direction.WEST;
				}
				break;
				
			case WEST:
				this.setXLoc(this.getXLoc() - this.xIncr);
				if (this.getXLoc() <= this.moveThreshL) {
					this.currDir = Direction.EAST;
				}
				break;
				
			default:
				break;
		}
	}
	
	@Override
	public int getDamage() {
		return this.damage;
	}
	
	@Override
	public String toString() {
		return this.getString() + 
				"\nX Increase: " + this.xIncr +
				"\nDamage: " + this.damage +
				"\nCurrent Direction: " + this.currDir;
	}
	
	@Override
	public String toStringForAreaMap() {
		return this.getStringForAreaMap() + 
				"\n\t\tX Increase: " + this.xIncr +
				"\n\t\tDamage: " + this.damage +
				"\n\t\tCurrent Direction: " + this.currDir;
	}
}
