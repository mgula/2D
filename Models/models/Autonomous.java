package models;

import enums.Direction;

public class Autonomous extends SolidObject {
	private int incr;
	private int currSegment;
	private int waitCounter = 0;
	private int moveThreshL;
	private int moveThreshR;
	private int moveThreshU;
	private int moveThreshD;
	private Direction currDir;
	private Direction lastDir;
	
	public Autonomous(int x, int y, int h, int w, Direction d, int moveVariance, int incr) {
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
	
	/*Getters*/
	public int getIncr() {
		return this.incr;
	}
	
	public int getCurrSegment() {
		return this.currSegment;
	}
	
	public int getWaitCounter() {
		return this.waitCounter;
	}
	
	public int getMoveThreshL() {
		return this.moveThreshL;
	}
	
	public int getMoveThreshR() {
		return this.moveThreshR;
	}
	
	public int getMoveThreshU() {
		return this.moveThreshU;
	}
	
	public int getMoveThreshD() {
		return this.moveThreshD;
	}
	
	public Direction getDirection() {
		return this.currDir;
	}
	
	public Direction getLastDirection() {
		return this.lastDir;
	}
	
	/*Setters*/
	public void setCurrSegment(int i) {
		this.currSegment = i;
	}
	
	public void setWaitCounter(int i) {
		this.waitCounter = i;
	}
	
	public void setDirection(Direction d) {
		this.currDir = d;
	}
	
	public void setLastDirection(Direction d) {
		this.lastDir = d;
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
