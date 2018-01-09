package models;

public abstract class Enemy extends EventArea {
	private static final long serialVersionUID = 1L;
	public void move(){};
	public int getDamage(){return 0;};
}
