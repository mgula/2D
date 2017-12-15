package games;

import java.io.Serializable;
import java.util.ArrayList;

import enums.Direction;
import game1Models.*;

public class GameEngine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int defaultXIncr = 6;
	private final int defaultYIncr = 5;
	private final int defaultFloatingThreshold = 10;
	private final int defaultMaxJumps = 2;
	
	private int playerStartingXloc = 2000; 
	private int playerStartingYloc = -100;
	private int playerHeight = 40;
	private int playerWidth = 40;
	
	private boolean jumping = false;
	
	private int maxHealth = 100;
	
	private int floatingCounter = 0;
	
	private int floatingThreshold = this.defaultFloatingThreshold; // used to make the player float for a moment after jumping, as if underwater
	
	private int jumpingCounter = 0;
	private int jumpDuration = 30;
	private int jumpCount = 0; // current number of times jumped (resets when you land on a surface)
	
	private int damageCooldownThresh = 80;
	private int damageCooldown = this.damageCooldownThresh;
	private int healthIncrease;
	private int healthDecreaseEnemy;
	private int healthDecreaseDamArea;
	private boolean damageDealt = false;
	private Direction dirOfCurrent; // direction of current (Current.java)
	private int incrFromCurrent; // magnitude of push from currents (Current.java)
	
	private int maxJumps = this.defaultMaxJumps; // maximum number of jumps allowed
	
	private final int interactableWaitTime = 50;
	
	public Controllable getPlayer() {
		return new Controllable(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth, this.defaultXIncr, this.defaultYIncr, this.maxHealth);
	}
	
	public void restoreDefaultAttributes(Controllable c) {
		c.setXIncr(this.defaultXIncr);
		c.setYIncr(this.defaultYIncr);
		this.floatingThreshold = this.defaultFloatingThreshold;
		this.maxJumps = this.defaultMaxJumps;
	}
	
	public void respawn(Controllable c) {
		c.setXLoc(this.playerStartingXloc);
		c.setYLoc(this.playerStartingYloc);
		c.setHealth(this.maxHealth);
		
		this.jumping = false;
		this.jumpCount = 0;
		this.jumpingCounter = 0;
		this.floatingCounter = 0;
	}
	
	/*Getters*/
	public int getPlayerStartingXloc() {
		return this.playerStartingXloc;
	}
	
	public int getPlayerStartingYloc() {
		return this.playerStartingYloc;
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
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public int getFloatingCounter() {
		return this.floatingCounter;
	}
	
	public int getDamageCoolDown() {
		return this.damageCooldown;
	}
	
	public int getMaxJumps() {
		return this.maxJumps;
	}
	
	public int getFloatingThreshold() {
		return this.floatingThreshold;
	}
	
	/*Setters*/
	public void setJumping(boolean b) {
		this.jumping = b;
	}
	
	public void setMaxJumps(int n) {
		this.maxJumps = n;
	}
	
	public void setFloatingThreshold(int n) {
		this.floatingThreshold = n;
	}
	
	/*Movement/collision detection*/
	public void incrX(Controllable c) {
		if (!c.isAgainstSurfaceRight() && !c.isAgainstMovingSurfaceRight()) {
			c.setXLoc(c.getXLoc() + 1);
		}
		c.setCurrXSegment(c.getCurrXSegment() + 1);
	}
	
	public void decrX(Controllable c) {
		if (!c.isAgainstSurfaceLeft() && !c.isAgainstMovingSurfaceLeft()) {
			c.setXLoc(c.getXLoc() - 1);
		}
		c.setCurrXSegment(c.getCurrXSegment() + 1);
	}
	
	public void incrY(Controllable c) {
		if (!c.isAgainstSurfaceTop() && !c.isAgainstMovingSurfaceTop()) {
			c.setYLoc(c.getYLoc() - 1);
		}
		c.setCurrYSegment(c.getCurrYSegment() + 1);
	}
	
	public void decrY(Controllable c) {
		if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
			c.setYLoc(c.getYLoc() + 1);
		}
		c.setCurrYSegment(c.getCurrYSegment() + 1);
	}
	
	public void moveRight(Room r, ArrayList<Game1Model> e, Controllable c) {
		/*Implement the concept mentioned above: check for collisions after every pixel.*/
		while (c.getCurrXSegment() < c.getXIncr()) {
			this.checkRightEdgeCollisions(r, e, c);
			this.incrX(c);
		}
		c.setCurrXSegment(0);
	}
	
	public void moveLeft(Room r, ArrayList<Game1Model> e, Controllable c) {
		while (c.getCurrXSegment() < c.getXIncr()) {
			this.checkLeftEdgeCollisions(r, e, c);
			this.decrX(c);
		}
		c.setCurrXSegment(0);
	}
	
	public void raiseY(Room r, ArrayList<Game1Model> e, Controllable c) {
		if (!c.isAgainstSurfaceTop() && !c.isAgainstMovingSurfaceTop()) {
			c.setOnSurfaceBottom(false);
			c.setOnMovingSurfaceBottom(false);
			c.setOnPlatform(false);
			c.setAgainstMovingSurfaceBottom(false);
			this.floatingCounter = 0;
			while (c.getCurrYSegment() < c.getYIncr()) {
				this.checkTopEdgeCollisions(r, e, c);
				this.incrY(c);
			}
			c.setCurrYSegment(0);
		} else {
			this.jumpingCounter = this.jumpDuration;
		}
	}
	
	public boolean initiateJumpArc(Room r, ArrayList<Game1Model> e, Controllable c) {
		if (this.jumpCount < this.maxJumps) {
			if (this.jumpingCounter < this.jumpDuration) {
				this.jumpingCounter++;
				this.raiseY(r, e, c);
				return true;
			} else {
				this.jumpCount++;
				this.jumpingCounter = 0;
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void assertGravity(Room r, ArrayList<Game1Model> e, Controllable c) {
		if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
			if (this.floatingCounter < this.floatingThreshold) {
				this.floatingCounter++;
			} else {
				while (c.getCurrYSegment() < c.getYIncr()) {
					this.checkMovingSurfaces(r, e, true, c);
					this.decrY(c);
				}
				c.setCurrYSegment(0);
			}
		}
	}
	
	public void phaseThroughPlatformOrExit(Room r, ArrayList<Game1Model> e, Controllable c) {
		if (c.isOnPlatform()) {
			c.setYLoc(c.getYLoc() + 1);
			c.setOnPlatform(false);
			c.setOnSurfaceBottom(false);
			this.floatingCounter = this.floatingThreshold;
		} else {
			for (Exit ex : r.getRoomLinks()) {
				if (ex.getDirection() == Direction.SOUTH) {
					if (c.getYLoc() <= ex.getYLoc()) {
						if (c.getXLoc() >= ex.getXLoc() && c.getXLoc() + c.getWidth() <= ex.getXLoc() + ex.getWidth()) {
							c.setYLoc(c.getYLoc() + 1);
							c.setOnSurfaceBottom(false);
							this.floatingCounter = this.floatingThreshold;
						}
					}
				}
			}
		}
	}
	
	public void checkLeavingSurface(ArrayList<Game1Model> e, Controllable c) {
		if (c.isOnSurfaceBottom() || c.isOnMovingSurfaceBottom()) {
			boolean contact = false;
			boolean movingContact = false;
			/*Check against every model that acts as a surface.*/
			for (Game1Model m : e) {
				if (m instanceof game1Models.Interactable) {
					if (this.checkBottomSurface(m, c)) {
						movingContact = true;
					}
				} else if (m instanceof SolidObject || m instanceof Platform) {
					if (this.checkBottomSurface(m, c)) {
						contact = true;
					}
				} 
			}
			/*If there were no environmental collisions, reset floating counter and update the appropriate boolean.*/
			if (!contact) {
				c.setOnPlatform(false);
				this.floatingCounter = 0;
				c.setOnSurfaceBottom(false);
			}
			if (!movingContact) {
				this.floatingCounter = 0;
				c.setOnMovingSurfaceBottom(false);
				c.setAgainstMovingSurfaceBottom(false);
			}
		}
	}
	
	public void checkMovingSurfaces(Room r, ArrayList<Game1Model> e, boolean calledByInteractable, Controllable c) {
		/*Check all edges for collisions (particularly of the moving variety).*/
		this.checkLeftEdgeCollisions(r, e, c);
		this.checkRightEdgeCollisions(r, e, c);
		this.checkBottomEdgeCollisions(r, e, c);
		this.checkTopEdgeCollisions(r, e, c);
		/*Respond to these collisions. These collisions only increment/decrement because Interactable uses 
		 *the same while loop scheme to move (move a pixel and then check for collisions).*/
		if (c.isAgainstMovingSurfaceLeft() && c.getInContactWith().getDirection() == Direction.EAST) {
			c.setXLoc(c.getXLoc() + 1);
		}
		if (c.isAgainstMovingSurfaceRight() && c.getInContactWith().getDirection() == Direction.WEST) {
			c.setXLoc(c.getXLoc() - 1);
		}	
		if (c.isAgainstMovingSurfaceTop() && c.getInContactWith().getDirection() == Direction.SOUTH) {
			c.setYLoc(c.getYLoc() + 1);
		}
		if (c.isAgainstMovingSurfaceBottom() && c.getInContactWith().getDirection() == Direction.NORTH) {
			c.setYLoc(c.getYLoc() - 1);
			c.setAgainstMovingSurfaceBottom(false);
		}
		/*If resting on a moving surface, the crab will move in the same direction as the 
		 *moving surface. This code should never be executed by Interactable.*/
		if (c.isOnMovingSurfaceBottom()) {
			if (!calledByInteractable) {
				switch (c.getInContactWith().getDirection()) {
					case EAST:
						c.setXLoc(c.getXLoc() + c.getInContactWith().getIncr());
						break;
						
					case WEST:
						c.setXLoc(c.getXLoc() - c.getInContactWith().getIncr());
						break;
						
					case NORTH:
						c.setYLoc(c.getYLoc() - c.getInContactWith().getIncr());
						break;
						
					case SOUTH:
						c.setYLoc(c.getYLoc() + c.getInContactWith().getIncr());
						break;
						
					default:
						break;
				}
			}
		}
	}
	
	public void checkBottomEdgeCollisions(Room r, ArrayList<Game1Model> e, Controllable c) {
		/*If yloc is at ground level, reset jump count and jump counter, and and update the appropriate boolean.*/
		if (c.getYLoc() + c.getHeight() >= r.getYLoc()) {
			c.setOnSurfaceBottom(true);
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject || m instanceof Platform) {
				if (this.checkBottomSurface(m, c)) {
					/*If there was an environmental bottom edge collision, reset jump count and 
					 *jump counter, and and update the appropriate boolean.*/
					this.floatingCounter = 0;
					if (m instanceof game1Models.Interactable) {
						c.setInContactWith((Interactable) m);
						switch (((game1Models.Interactable) m).getDirection()) {
							case NORTH:
								c.setAgainstMovingSurfaceBottom(true);
								c.setOnMovingSurfaceBottom(false);
								break;
							default:
								c.setOnMovingSurfaceBottom(true);
								c.setAgainstMovingSurfaceBottom(false);
								break;
						}
					} else {
						c.setOnSurfaceBottom(true);
						if (m instanceof Platform) {
							c.setOnPlatform(true);
						}
					}
				}
			}
		}
		
		if ((c.isOnSurfaceBottom() || c.isOnMovingSurfaceBottom()) && this.jumpingCounter == 0) {
			this.jumpCount = 0;
		}
	}
	
	public void checkTopEdgeCollisions(Room r, ArrayList<Game1Model> e, Controllable c) {
		boolean newCollision = false;
		/*Check yloc to see if we've hit the ceiling (valid y locations will range from map.getGroundLevel() 
		 *to map.getGroundLevel() - map.getHeight(), since y = 0 is the top of the screen.*/
		if (c.getYLoc() <= r.getYLoc() - r.getHeight()) {
			c.setAgainstSurfaceTop(true);
			newCollision = true;
			for (Exit ex : r.getRoomLinks()) {
				if (c.getYLoc() <= ex.getYLoc()) {
					if (c.getXLoc() >= ex.getXLoc() && c.getXLoc() + c.getWidth() <= ex.getXLoc() + ex.getWidth()) {
						c.setAgainstSurfaceTop(false);
						newCollision = false;
					}
				}
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (c.getYLoc() <= y + h && c.getYLoc() >= y) {
					for (int i = m.getXLoc() - c.getWidth() + 1; i < x + w; i++) {
						if (c.getXLoc() == i) {
							newCollision = true;
							if (m instanceof game1Models.Interactable) {
								c.setInContactWith((Interactable) m);
								c.setAgainstMovingSurfaceTop(true);
							} else {
								c.setAgainstSurfaceTop(true);
							}
						}
					}
				}
			}
		}
		/*If there was no collision, update the appropriate boolean.*/
		if (!newCollision) {
			c.setAgainstSurfaceTop(false);
			c.setAgainstMovingSurfaceTop(false);
		}
	}
	
	public void checkRightEdgeCollisions(Room r, ArrayList<Game1Model> e, Controllable c) {
		boolean newCollision = false;
		/*Check xloc to see if we're at the right edge of the map.*/
		if (c.getXLoc() + c.getWidth() >= r.getXLoc() + r.getWidth()) {
			c.setAgainstSurfaceRight(true);
			newCollision = true;
			for (Exit ex : r.getRoomLinks()) {
				if (c.getXLoc() + c.getWidth() >= ex.getXLoc()) {
					if (c.getYLoc() >= ex.getYLoc() && c.getYLoc() <= ex.getYLoc() + ex.getHeight()) {
						c.setAgainstSurfaceRight(false);
						newCollision = false;
					}
				}
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int h = m.getHeight();
				if (c.getXLoc() + c.getWidth() == x) {
					for (int i = y - c.getHeight() + 1; i < y + h; i++) {
						if (c.getYLoc() == i) {
							newCollision = true;
							if (m instanceof game1Models.Interactable) {
								c.setInContactWith((Interactable) m);
								c.setAgainstMovingSurfaceRight(true);
							} else {
								c.setAgainstSurfaceRight(true);
							}
						}
					}
				}
			}
		}
		/*If there was no collision, update the appropriate boolean.*/
		if (!newCollision) {
			c.setAgainstSurfaceRight(false);
			c.setAgainstMovingSurfaceRight(false);
		}
	}
	
	public void checkLeftEdgeCollisions(Room r, ArrayList<Game1Model> e, Controllable c) {
		boolean newCollision = false;
		/*Check xloc to see if we're at the left edge of the map.*/
		if (c.getXLoc() <= r.getXLoc()) {
			c.setAgainstSurfaceLeft(true);
			newCollision = true;
			for (Exit ex : r.getRoomLinks()) {
				if (c.getXLoc() <= ex.getXLoc()) {
					if (c.getYLoc() >= ex.getYLoc() && c.getYLoc() <= ex.getYLoc() + ex.getHeight()) {
						c.setAgainstSurfaceLeft(false);
						newCollision = false;
					}
				}
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (c.getXLoc() == x + w) {
					for (int i = y - c.getHeight() + 1; i < y + h; i++) {
						if (c.getYLoc() == i) {
							newCollision = true;
							if (m instanceof game1Models.Interactable) {
								c.setInContactWith((Interactable) m);
								c.setAgainstMovingSurfaceLeft(true);
							} else {
								c.setAgainstSurfaceLeft(true);
							}
						}
					}
				}
			}
		}
		/*If there was no collision, update the appropriate boolean.*/
		if (!newCollision) {
			c.setAgainstSurfaceLeft(false);
			c.setAgainstMovingSurfaceLeft(false);
		}
	}
	
	public void checkAreaCollisions(ArrayList<Game1Model> e, Controllable c) {
		c.setCurrentCollision(false); // innocent until proven guilty policy
		for (Game1Model m : e) {
			if (m instanceof EventArea) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				/*Check every point of the hitbox against every point of the model.*/
				for (int i = x; i < x + w; i++) {
					for (int j = y; j < y + h; j++) {
						for (int k = c.getXLoc(); k < c.getXLoc() + c.getWidth(); k++) {
							for (int l = c.getYLoc(); l < c.getYLoc() + c.getHeight(); l++) {
								if (i == k && j == l) {
									/*Currently, if an enemy and regen area/current occupy the same area, the
									 *enemy takes precedence.*/
									if (m instanceof game1Models.Enemy) {
										c.setEnemyCollision(true);
										this.healthDecreaseEnemy = ((game1Models.Enemy)m).getDamage();
										return;
									} else if (m instanceof game1Models.DamageArea) {
										c.setDamageCollision(true);
										this.healthDecreaseDamArea = ((game1Models.DamageArea)m).getHealthDecr();
										return;
									} else if (m instanceof game1Models.RegenArea) {
										c.setRegenCollision(true);
										this.healthIncrease = ((game1Models.RegenArea)m).getHealthIncr();
										return;
									} else if (m instanceof game1Models.Current) {
										c.setCurrentCollision(true);
										this.dirOfCurrent = ((game1Models.Current)m).getFlowDirection();
										this.incrFromCurrent = ((game1Models.Current)m).getIncr();
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public boolean checkBottomSurface(Game1Model m, Controllable c) {
		int x = m.getXLoc();
		int y = m.getYLoc();
		int w = m.getWidth();
		if (c.getYLoc() + c.getHeight() == y) {
			for (int i = x - c.getWidth() + 1; i < x + w; i++) {
				if (c.getXLoc() == i) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void evaluateAreaCollisions(Room r, ArrayList<Game1Model> e, Controllable c) {
		if (c.getEnemyCollision()) {
			if (!this.damageDealt) {
				/*Deal the appropriate amount of damage, and only once.*/
				c.setHealth(c.getHealth() - this.healthDecreaseEnemy);
				this.damageDealt = true;
			}
			/*Give the player some time before being open to taking damage again.*/
			if (this.damageCooldown > 0) {
				this.damageCooldown--;
			} else {
				/*Update the appropriate booleans after the cooldown.*/
				this.damageCooldown = this.damageCooldownThresh;
				this.damageDealt = false;
				c.setEnemyCollision(false);
			}
		}
		
		if (c.getDamageCollision()) {
			if (c.getHealth() >= 0) {
				c.setHealth(c.getHealth() - this.healthDecreaseDamArea);
				c.setDamageCollision(false);
			} else {
				c.setDamageCollision(false);
			}
		}
		
		if (c.getRegenCollision()) {
			/*Don't give the player more than the maximum health.*/
			if (c.getHealth() < this.maxHealth) {
				if (c.getHealth() + this.healthIncrease > this.maxHealth) {
					c.setHealth(this.maxHealth);
				} else {
					c.setHealth(c.getHealth() + this.healthIncrease);
				}
				c.setRegenCollision(false);
			} else {
				c.setRegenCollision(false);
			}
		}
		
		/*If a current collision has been detected, move the player in the corresponding direction.*/
		if (c.getCurrentCollision()) {
			switch (this.dirOfCurrent) {
				case EAST:
					this.checkRightEdgeCollisions(r, e, c);
					if (!c.isAgainstSurfaceRight() && !c.isAgainstMovingSurfaceRight()) {
						c.setXLoc(c.getXLoc() + this.incrFromCurrent);
					}
					break;
					
				case WEST:
					this.checkLeftEdgeCollisions(r, e, c);
					if (!c.isAgainstSurfaceLeft() && !c.isAgainstMovingSurfaceLeft()) {
						c.setXLoc(c.getXLoc() - this.incrFromCurrent);
					}
					break;
					
				case NORTH:
					this.checkTopEdgeCollisions(r, e, c);
					if (!c.isAgainstSurfaceTop() && !c.isAgainstMovingSurfaceTop()) {
						c.setYLoc(c.getYLoc() - this.incrFromCurrent);
					}
					break;
					
				case SOUTH:
					this.checkBottomEdgeCollisions(r, e, c);
					if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
						c.setYLoc(c.getYLoc() + this.incrFromCurrent);
					}
					break;
				
				default:
					break;
			}
		}
	}
	
	public void moveInteractable(Room r, ArrayList<Game1Model> e, Interactable i, Controllable c) {
		switch (i.getDirection()) {
			case EAST:
				while (i.getCurrSegment() < i.getIncr()) {
					i.setXLoc(i.getXLoc() + 1);
					i.setCurrSegment(i.getCurrSegment() + 1);
					this.checkMovingSurfaces(r, e, true, c);
				}
				i.setCurrSegment(0);
				if (i.getXLoc() >= i.getMoveThreshR()) {
					i.setLastDirection(i.getDirection());
					i.setDirection(Direction.IDLE);
				}
				break;
				
			case WEST:
				while (i.getCurrSegment() < i.getIncr()) {
					i.setXLoc(i.getXLoc() - 1);
					i.setCurrSegment(i.getCurrSegment() + 1);
					this.checkMovingSurfaces(r, e, true, c);
				}
				i.setCurrSegment(0);
				if (i.getXLoc() <= i.getMoveThreshL()) {
					i.setLastDirection(i.getDirection());
					i.setDirection(Direction.IDLE);
				}
				break;
				
			case NORTH:
				while (i.getCurrSegment() < i.getIncr()) {
					i.setYLoc(i.getYLoc() - 1);
					i.setCurrSegment(i.getCurrSegment() + 1);
					this.checkMovingSurfaces(r, e, true, c);
				}
				i.setCurrSegment(0);
				if (i.getYLoc() <= i.getMoveThreshU()) {
					i.setLastDirection(i.getDirection());
					i.setDirection(Direction.IDLE);
				}
				break;
				
			case SOUTH:
				while (i.getCurrSegment() < i.getIncr()) {
					i.setYLoc(i.getYLoc() + 1);
					i.setCurrSegment(i.getCurrSegment() + 1);
					this.checkMovingSurfaces(r, e, true, c);
				}
				i.setCurrSegment(0);
				if (i.getYLoc() >= i.getMoveThreshD()) {
					i.setLastDirection(i.getDirection());
					i.setDirection(Direction.IDLE);
				}
				break;
				
			/*Interactables wait for a moment before switching directions.*/
			case IDLE:
				if (i.getWaitCounter() < this.interactableWaitTime) {
					i.setWaitCounter(i.getWaitCounter() + 1);
				} else {
					i.setWaitCounter(0);
					switch (i.getLastDirection()) {
						case NORTH:
							i.setDirection(Direction.SOUTH);
							break;
							
						case SOUTH:
							i.setDirection(Direction.NORTH);
							break;
							
						case EAST:
							i.setDirection(Direction.WEST);
							break;
							
						case WEST:
							i.setDirection(Direction.EAST);
							break;
							
						default:
							break;
					}
				}
				
			default:
				break;
		}
	}
}
