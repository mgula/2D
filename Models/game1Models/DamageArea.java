package game1Models;

public class DamageArea extends EventArea {
	
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
		return this.getInfo();
	}
}
