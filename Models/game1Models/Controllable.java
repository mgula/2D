package game1Models;

public class Controllable extends Game1Model {
	private int xIncr;
	private int yIncr;
	
	private int currXSegment = 0;
	private int currYSegment = 0;
	
	private int currHealth;
	
	private Autonomous inContactWith;
	
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
	
	private boolean enemyCollision = false; // true if player is occupying same area as an enemy
	
	public Controllable(int x, int y, int h, int w, int xIncr, int yIncr, int health) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		
		this.xIncr = xIncr;
		this.yIncr = yIncr;
		this.currHealth = health;
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
	
	public int getHealth() {
		return this.currHealth;
	}
	
	public Autonomous getInContactWith() {
		return this.inContactWith;
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
	
	public boolean getEnemyCollision() {
		return this.enemyCollision;
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
	
	public void setHealth(int h) {
		this.currHealth = h;
	}
	
	public void setInContactWith(Autonomous a) {
		this.inContactWith = a;
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
	
	public void setEnemyCollision(boolean b) {
		this.enemyCollision = b;
	}
}
