package models;

public class RegenArea extends EventArea {
	private static final long serialVersionUID = 1L;
	private int healthIncr;
	
	public RegenArea(int x, int y, int h, int w, int i) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.healthIncr = i;
	}
	
	public int getHealthIncr() {
		return this.healthIncr;
	}
	
	@Override
	public String toString() {
		return this.getString() + 
				"\n\t\t\tHealth increase: " + this.healthIncr;
	}
}
