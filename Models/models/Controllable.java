package models;

public class Controllable extends Model {
	private int xIncr;
	private int yIncr;
	
	private int currXSegment = 0;
	private int currYSegment = 0;
	
	private int currHealth;
	private int maxHealth;
	
	private Autonomous adjacentAutonmous = null;
	private Controllable adjacentControllable = null;
	private Exit adjacentExit = null;
	
	private boolean onSurfaceBottom = false; // boolean used for checking if the player's bottom edge is in contact with an object
	private boolean againstSurfaceTop = false; // boolean used for checking if the player's top edge is in contact with another object
	private boolean againstSurfaceRight = false; // etc
	private boolean againstSurfaceLeft = false;
	
	private boolean onMovingSurfaceBottom = false; // boolean used for checking if the player's bottom edge is in contact with a moving object
	private boolean againstMovingSurfaceBottom = false; // similar to above variable, except used in the case where the player is falling and the moving object is rising
	private boolean againstMovingSurfaceTop = false; // boolean used for checking if the player's top edge is in contact with a moving object
	private boolean againstMovingSurfaceRight = false; // etc
	private boolean againstMovingSurfaceLeft = false;
	
	private boolean onPlatform = false;
	
	public Controllable(int x, int y, int h, int w, int xIncr, int yIncr, int health, int maxHealth) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		
		this.xIncr = xIncr;
		this.yIncr = yIncr;
		this.currHealth = health;
		this.maxHealth = maxHealth;
	}
	
	public static Controllable makeCopy(Controllable c) {
		return new Controllable(c.getXLoc(), c.getYLoc(), c.getHeight(), c.getWidth(), c.getXIncr(), c.getYIncr(), c.getCurrHealth(), c.getMaxHealth());
	}
	
	/*Getters*/
	public int getXIncr() {
		return this.xIncr;
	}
	
	public int getYIncr() {
		return this.yIncr;
	}
	
	public int getCurrXSegment() {
		return this.currXSegment;
	}
	
	public int getCurrYSegment() {
		return this.currYSegment;
	}
	
	public int getCurrHealth() {
		return this.currHealth;
	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public Autonomous getAdjacentAutonomous() {
		return this.adjacentAutonmous;
	}
	
	public Controllable getAdjacentControllable() {
		return this.adjacentControllable;
	}
	
	public Exit getAdjacentExit() {
		return this.adjacentExit;
	}
	
	public boolean isOnSurfaceBottom() {
		return this.onSurfaceBottom;
	}
	
	public boolean isAgainstSurfaceTop() {
		return this.againstSurfaceTop;
	}
	
	public boolean isAgainstSurfaceRight() {
		return this.againstSurfaceRight;
	}
	
	public boolean isAgainstSurfaceLeft() {
		return this.againstSurfaceLeft;
	}
	
	public boolean isOnMovingSurfaceBottom() {
		return this.onMovingSurfaceBottom;
	}
	
	public boolean isAgainstMovingSurfaceBottom() {
		return this.againstMovingSurfaceBottom;
	}
	
	public boolean isAgainstMovingSurfaceTop() {
		return this.againstMovingSurfaceTop;
	}
	
	public boolean isAgainstMovingSurfaceRight() {
		return this.againstMovingSurfaceRight;
	}
	
	public boolean isAgainstMovingSurfaceLeft() {
		return this.againstMovingSurfaceLeft;
	}
	
	public boolean isOnPlatform() {
		return this.onPlatform;
	}
	
	/*Setters*/
	public void setXIncr(int x) {
		this.xIncr = x;
	}
	
	public void setYIncr(int y) {
		this.yIncr = y;
	}
	
	public void setCurrXSegment(int x) {
		this.currXSegment = x;
	}
	
	public void setCurrYSegment(int y) {
		this.currYSegment = y;
	}
	
	public void setCurrHealth(int h) {
		this.currHealth = h;
	}
	
	public void setMaxHealth(int h) {
		this.maxHealth = h;
	}
	
	public void setAdjacentAutonomous(Autonomous a) {
		this.adjacentAutonmous = a;
	}
	
	public void setNewBody(Controllable c) {
		this.adjacentControllable = c;
	}
	
	public void setAdjacentExit(Exit e) {
		this.adjacentExit = e;
	}
	
	public void setOnSurfaceBottom(boolean b) {
		this.onSurfaceBottom = b;
	}
	
	public void setAgainstSurfaceTop(boolean b) {
		this.againstSurfaceTop = b;
	}
	
	public void setAgainstSurfaceRight(boolean b) {
		this.againstSurfaceRight = b;
	}
	
	public void setAgainstSurfaceLeft(boolean b) {
		this.againstSurfaceLeft = b;
	}
	
	public void setOnMovingSurfaceBottom(boolean b) {
		this.onMovingSurfaceBottom = b;
	}
	
	public void setAgainstMovingSurfaceBottom(boolean b) {
		this.againstMovingSurfaceBottom = b;
	}
	
	public void setAgainstMovingSurfaceTop(boolean b) {
		this.againstMovingSurfaceTop = b;
	}
	
	public void setAgainstMovingSurfaceRight(boolean b) {
		this.againstMovingSurfaceRight = b;
	}
	
	public void setAgainstMovingSurfaceLeft(boolean b) {
		this.againstMovingSurfaceLeft = b;
	}
	
	public void setOnPlatform(boolean b) {
		this.onPlatform = b;
	}
}
