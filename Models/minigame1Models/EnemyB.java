package minigame1Models;

import enums.Direction;

public class EnemyB extends Enemy {
	private int xloc; // must be within moveThreshU and moveThreshD
	private int yloc;
	private int width;
	private int height;
	private final int yIncr;
	private final int damage = 1;
	private int moveThreshU;
	private int moveThreshD;
	private Direction currDir = Direction.NORTH;

	public EnemyB(int x, int y, int h, int w, int moveVariance, int yIncr) {
		this.xloc = x;
		this.yloc = y;
		this.height = h;
		this.width = w;
		this.moveThreshU = y - moveVariance;
		this.moveThreshD = y + moveVariance;
		this.yIncr = yIncr;
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
	
	@Override
	public void move() {
		switch (this.currDir) {
			case NORTH:
				this.yloc -= yIncr;
				if (this.yloc <= this.moveThreshU) {
					this.currDir = Direction.SOUTH;
				}
				break;
				
			case SOUTH:
				this.yloc += yIncr;
				if (this.yloc >= this.moveThreshD) {
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
}
