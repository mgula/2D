package game1Models;

import enums.Direction;

/*Y loc must be within moveThreshU and moveThreshD!*/
public class EnemyB extends Enemy {
	private final int yIncr;
	private final int damage = 1;
	private int moveThreshU;
	private int moveThreshD;
	private Direction currDir = Direction.NORTH;

	public EnemyB(int x, int y, int h, int w, int moveVariance, int yIncr) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.moveThreshU = y - moveVariance;
		this.moveThreshD = y + moveVariance;
		this.yIncr = yIncr;
	}
	
	@Override
	public void move() {
		switch (this.currDir) {
			case NORTH:
				this.setYLoc(this.getYLoc() - this.yIncr);
				if (this.getYLoc() <= this.moveThreshU) {
					this.currDir = Direction.SOUTH;
				}
				break;
				
			case SOUTH:
				this.setYLoc(this.getYLoc() + this.yIncr);
				if (this.getYLoc() >= this.moveThreshD) {
					this.currDir = Direction.NORTH;
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
		String info = this.getInfo() + 
				"\nY Increase: " + this.yIncr +
				"\nDamage: " + this.damage +
				"\nCurrent Direction: " + this.currDir;
		return info;
	}
}
