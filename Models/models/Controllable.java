package models;

import map.Exit;

public class Controllable extends Model {
	private static final long serialVersionUID = 1L;
	private int xIncr;
	private int yIncr;
	
	private int currXSegment = 0;
	private int currYSegment = 0;
	
	private int currHealth;
	private int maxHealth;
	
	private Autonomous adjacentAutonmous = null;
	private Controllable adjacentControllable = null;
	private Exit adjacentExit = null;
	private TextArea activatedTextArea = null;
	
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
	
	private int floatingCounter = 0;
	private int floatingThreshold; // used to make the player float for a moment after jumping
	
	private int maxJumps; // maximum number of jumps allowed
	
	private boolean jumping = false;
	
	private int jumpingCounter = 0;
	private int jumpDuration;
	private int jumpCount = 0; // current number of times jumped (resets when you land on a surface)
	
	public Controllable(int x, int y, int h, int w, int xIncr, int yIncr, int health, int maxHealth, int floatThresh, int maxJumps, int jumpDuration) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		
		this.xIncr = xIncr;
		this.yIncr = yIncr;
		this.currHealth = health;
		this.maxHealth = maxHealth;
		
		this.floatingThreshold = floatThresh;
		this.maxJumps = maxJumps;
		this.jumpDuration = jumpDuration;
	}
	
	/*Make a square controllable*/
	public Controllable(int x, int y, int d, int xIncr, int yIncr, int health, int maxHealth, int floatThresh, int maxJumps, int jumpDuration) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(d);
		this.setWidth(d);
		
		this.xIncr = xIncr;
		this.yIncr = yIncr;
		this.currHealth = health;
		this.maxHealth = maxHealth;
		
		this.floatingThreshold = floatThresh;
		this.maxJumps = maxJumps;
		this.jumpDuration = jumpDuration;
	}
	
	public static Controllable makeCopy(Controllable c) {
		return new Controllable(c.getXLoc(), c.getYLoc(), c.getHeight(), c.getWidth(), c.getXIncr(), c.getYIncr(), c.getCurrHealth(), c.getMaxHealth(), c.getFloatingThreshold(), c.getMaxJumps(), c.getJumpDuration());
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
	
	public TextArea getActivatedTextArea() {
		return this.activatedTextArea;
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
	
	public int getFloatingCounter() {
		return this.floatingCounter;
	}
	
	public int getFloatingThreshold() {
		return this.floatingThreshold;
	}
	
	public int getMaxJumps() {
		return this.maxJumps;
	}
	
	public boolean getJumping() {
		return this.jumping;
	}
	
	public int getJumpCounter() {
		return this.jumpingCounter;
	}
	
	public int getJumpDuration() {
		return this.jumpDuration;
	}
	
	public int getJumpNumber() {
		return this.jumpCount;
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
	
	public void setActivatedTextArea(TextArea t) {
		this.activatedTextArea = t;
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
	
	public void setFloatingCounter(int f) {
		this.floatingCounter = f;
	}
	
	public void setFloatingThreshold(int t) {
		this.floatingThreshold = t;
	}

	public void setMaxJumps(int n) {
		this.maxJumps = n;
	}
	
	public void setJumping(boolean b) {
		this.jumping = b;
	}
	
	public void setJumpCounter(int n) {
		this.jumpingCounter = n;
	}
	
	public void setJumpDuration(int n) {
		this.jumpDuration = n;
	}
	
	public void setJumpNumber(int n) {
		this.jumpCount = n;
	}
}
