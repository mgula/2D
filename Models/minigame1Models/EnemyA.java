package minigame1Models;

import enums.Direction;

/**
 * Enemies move and deal damage to the player. Crab's collision detection doesn't treat enemies
 * as surfaces - the player can move through (and cannot land on) an enemy. These enemies move horizontally 
 * (from side to side).
 * 
 * @author marcusgula
 *
 */
public class EnemyA extends Enemy {
	private int xloc; // must be within moveThreshL and moveThreshR
	private int yloc;
	private int width;
	private int height;
	private final int xIncr;
	private final int damage = 1;
	private int moveThreshL;
	private int moveThreshR;
	private Direction currDir;

	public EnemyA(int x, int y, int h, int w, Direction d, int moveVariance, int xIncr) {
		this.xloc = x;
		this.yloc = y;
		this.height = h;
		this.width = w;
		this.currDir = d;
		this.moveThreshL = x - moveVariance;
		this.moveThreshR = x + moveVariance;
		this.xIncr = xIncr;
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
	
	public Direction getCurrDir() {
		return this.currDir;
	}
	
	@Override
	public void move() {
		switch (this.currDir) {
			case EAST:
				this.xloc += xIncr;
				if (this.xloc >= this.moveThreshR) {
					this.currDir = Direction.WEST;
				}
				break;
				
			case WEST:
				this.xloc -= xIncr;
				if (this.xloc <= this.moveThreshL) {
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
}
