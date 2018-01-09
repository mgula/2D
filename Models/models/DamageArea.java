package models;

public class DamageArea extends EventArea {
	private static final long serialVersionUID = 1L;
	private int healthDecr;
	
	public DamageArea(int x, int y, int h, int w, int d) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.healthDecr = d;
	}
	
	public int getHealthDecr() {
		return this.healthDecr;
	}
	
	@Override
	public String toString() {
		return this.getString() + 
				"\n\t\t\tHealth decrease: " + this.healthDecr;
	}
}
