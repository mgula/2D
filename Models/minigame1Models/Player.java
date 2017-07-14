package minigame1Models;

import java.util.ArrayList;

import enums.Direction;

public class Player implements Game1Model {

	private ArrayList<Game1Model> environment;
	private Map map;
	private int xloc;
	private int yloc;
	private final int width;
	private final int height;
	private int xIncr = 6;
	private int yIncr = 5;
	private int currXSegment = 0;
	private int currYSegment = 0;
	private int floatingCounter = 0;
	private final int floatingThreshold = 10; // used to make the player float for a moment after jumping, as if underwater
	private boolean onSurfaceBottom = false; // boolean used for checking if the crab's bottom edge is in contact with an object
	private boolean againstSurfaceTop = false; // boolean used for checking if the crab's top edge is in contact with another object
	private boolean againstSurfaceRight = false; // etc
	private boolean againstSurfaceLeft = false;
	private Interactable inContactWith;
	private boolean onMovingSurfaceBottom = false; // boolean used for checking if the crab's bottom edge is in contact with a moving object
	private boolean againstMovingSurfaceBottom = false; // similar to above variable, except used in the case where the crab is falling and the moving object is rising
	private boolean againstMovingSurfaceTop = false; // boolean used for checking if the crab's top edge is in contact with a moving object
	private boolean againstMovingSurfaceRight = false; // etc
	private boolean againstMovingSurfaceLeft = false;
	private boolean enemyCollision = false; // true if crab is occupying same area as an enemy
	private boolean regenCollision = false; // true if crab is occupying same area as a regen area
	private boolean currentCollision = false; // true if crab is occupying same area as a current (Current.java)
	private int jumpingCounter = 0;
	private final int jumpDuration = 30;
	private int jumpCount = 0; // current number of times jumped (resets when you land on a surface)
	private final int maxJumps = 1; // maximum number of jumps allowed
	private final int maxHealth = 3;
	private int currHealth = 3;
	private final int damageCooldownThresh = 80;
	private int damageCooldown = this.damageCooldownThresh;
	private int healthDecrease;
	private boolean damageDealt = false;
	private Direction dirOfCurrent; // direction of current (Current.java)
	private int incrFromCurrent; // magnitude of push from currents (Current.java)
	
	/**
	 * Create a new crab instance with the specified parameters.
	 * @param x x loc
	 * @param y y loc
	 * @param h crab height
	 * @param w crab width
	 */
	public Player(int x, int y, int h, int w) {
		this.xloc = x;
		this.yloc = y;
		this.height = h;
		this.width = w;
	}
	
	/*Getters*/
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
	
	/**
	 * Used only by view to achieve the flashing effect after taking damage.
	 * 
	 * @return current damgeCooldown
	 */
	public int getDamageCoolDown() {
		return this.damageCooldown;
	}
	
	/**
	 * Used by view to draw correct number of hearts on screen, and by minigame1 to check if 
	 * out of health.
	 * 
	 * @return current health
	 */
	public int getHealth() {
		return this.currHealth;
	}
	
	/**
	 * Used by view to create the flashing effect after taking damage.
	 * 
	 * @return enemy collision boolean
	 */
	public boolean getEnemyCollision() {
		return this.enemyCollision;
	}
	
	/**
	 * View needs to know if the crab is resting on a surface in order to draw the correct images.
	 * 
	 * @return on bottom surface boolean
	 */
	public boolean getOnASurface() {
		return this.onSurfaceBottom || this.onMovingSurfaceBottom;
	}
	
	/**
	 * Load the given environment and map.
	 * 
	 * @param e the given array list of Minigame1Models
	 * @param m the given map
	 */
	public void loadEnvironmentAndMap(ArrayList<Game1Model> e, Map m) {
		this.environment = e;
		this.map = m;
	}
	
	/**
	 * Instead of moving by xIncr all at once, the idea here is to move by a single pixel,
	 * and then check for collisions.
	 */
	public void incrX() {
		if (!this.againstSurfaceRight && !this.againstMovingSurfaceRight) {
			this.xloc++;
		}
		this.currXSegment++;
	}
	
	/**
	 * Instead of moving by xIncr all at once, the idea here is to move by a single pixel,
	 * and then check for collisions.
	 */
	public void decrX() {
		if (!this.againstSurfaceLeft && !this.againstMovingSurfaceLeft) {
			this.xloc--;
		}
		this.currXSegment++;
	}
	
	/**
	 * Instead of moving by yIncr all at once, the idea here is to move by a single pixel,
	 * and then check for collisions.
	 */
	public void incrY() {
		if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
			this.yloc--;
		}
		this.currYSegment++;
	}
	
	/**
	 * Instead of moving by yIncr all at once, the idea here is to move by a single pixel,
	 * and then check for collisions.
	 */
	public void decrY() {
		if (!this.onSurfaceBottom && !this.onMovingSurfaceBottom) {
			this.yloc++;
		}
		this.currYSegment++;
	}
	
	/**
	 * Move right, if right edge is not against a surface.
	 */
	public void moveRight() {
		/*Implement the concept mentioned above: check for collisions after every pixel.*/
		while (this.currXSegment < this.xIncr) {
			this.checkRightEdgeCollisions();
			this.incrX();
		}
		this.currXSegment = 0;
	}
	
	/**
	 * Move left, if left edge is not against a surface.
	 */
	public void moveLeft() {
		while (this.currXSegment < this.xIncr) {
			this.checkLeftEdgeCollisions();
			this.decrX();
		}
		this.currXSegment = 0;
	}
	
	/**
	 * Increment yloc if there are no top edge collisions. Also, reset the floating counter when yloc is decreased. 
	 * If the top edge is against a surface, end the current jump by setting the jump counter to the jump duration.
	 */
	public void raiseY() {
		if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
			this.onSurfaceBottom = false;
			this.onMovingSurfaceBottom = false;
			this.againstMovingSurfaceBottom = false;
			this.floatingCounter = 0;
			while (this.currYSegment < this.yIncr) {
				this.checkTopEdgeCollisions();
				this.incrY();
			}
			this.currYSegment = 0;
		} else {
			this.jumpingCounter = this.jumpDuration;
		}
	}
	
	/**
	 * If the player hasn't already jumped more than the maximum number jumps, call raiseY() and increase the 
	 * jumping counter. When the counter reaches jumpDuration, increment jump count and reset the counter.
	 * 
	 * @return false if still jumping, true if jump completed
	 */
	public boolean initiateJumpArc() {
		if (this.jumpCount < this.maxJumps) {
			if (this.jumpingCounter < this.jumpDuration) {
				this.raiseY();
				this.jumpingCounter++;
				return false;
			} else {
				this.jumpCount++;
				this.jumpingCounter = 0;
				return true;
			}
		} else {
			return true;
		}
	}
	
	/**
	 * If the bottom edge isn't on a surface, and if the floating counter is less than the threshold, decrement yloc.
	 */
	public void assertGravity() {
		if (!this.onSurfaceBottom && !this.onMovingSurfaceBottom) {
			if (this.floatingCounter < this.floatingThreshold) {
				this.floatingCounter++;
			} else {
				while (this.currYSegment < this.yIncr) {
					this.checkMovingSurfaces(true);
					this.decrY();
				}
				this.currYSegment = 0;
			}
		}
	}
	
	/**
	 * Check to see if bottom edge has left a surface.
	 */
	public void checkLeavingSurface() {
		if (this.onSurfaceBottom || this.onMovingSurfaceBottom) {
			boolean contact = false;
			boolean movingContact = false;
			/*Check against every model that acts as a surface.*/
			for (Game1Model m : this.environment) {
				if (m instanceof minigame1Models.Rock || m instanceof minigame1Models.Debris || m instanceof minigame1Models.Sand) {
					if (this.checkBottomSurface(m)) {
						contact = true;
					}
				} else if (m instanceof minigame1Models.Interactable) {
					if (this.checkBottomSurface(m)) {
						movingContact = true;
					}
				}
			}
			/*If there were no environmental collisions, reset floating counter and update the appropriate boolean.*/
			if (!contact) {
				this.floatingCounter = 0;
				this.onSurfaceBottom = false;
			}
			if (!movingContact) {
				this.floatingCounter = 0;
				this.onMovingSurfaceBottom = false;
				this.againstMovingSurfaceBottom = false;
			}
		}
	}
	
	/**
	 * The key difference between regular surfaces and moving surfaces is that moving surfaces can
	 * push the crab in a certain direction. This difference is key because this means the crab may
	 * now be moving even if there's no player input - in other words, crab movement can occur in 
	 * multiple regions of the tick(). We use the boolean calledByInteractable essentially to indicate 
	 * where in tick() this method is being called from.
	 * 
	 * @param calledByInteractable whether or not the method was called by Interactable.java
	 */
	public void checkMovingSurfaces(boolean calledByInteractable) {
		/*Check all edges for collisions (particularly of the moving variety).*/
		this.checkLeftEdgeCollisions();
		this.checkRightEdgeCollisions();
		this.checkBottomEdgeCollisions();
		this.checkTopEdgeCollisions();
		/*Respond to these collisions. These collisions only increment/decrement because Interactable uses 
		 *the same while loop scheme to move (move a pixel and then check for collisions).*/
		if (this.againstMovingSurfaceLeft && this.inContactWith.getDirection() == Direction.EAST) {
			this.xloc++;
		}
		if (this.againstMovingSurfaceRight && this.inContactWith.getDirection() == Direction.WEST) {
			this.xloc--;
		}	
		if (this.againstMovingSurfaceTop && this.inContactWith.getDirection() == Direction.SOUTH) {
			this.yloc++;
		}
		if (this.againstMovingSurfaceBottom && this.inContactWith.getDirection() == Direction.NORTH) {
			this.yloc--;
			this.againstMovingSurfaceBottom = false;
		}
		/*If resting on a moving surface, the crab will move in the same direction as the 
		 *moving surface. This code should never be executed by Interactable.*/
		if (this.onMovingSurfaceBottom) {
			if (!calledByInteractable) {
				switch (this.inContactWith.getDirection()) {
					case EAST:
						this.xloc += this.inContactWith.getIncr();
						break;
						
					case WEST:
						this.xloc -= this.inContactWith.getIncr();
						break;
						
					case NORTH:
						this.yloc -= this.inContactWith.getIncr();
						break;
						
					case SOUTH:
						this.yloc += this.inContactWith.getIncr();
						break;
						
					default:
						break;
				}
			}
		}
	}
	
	/**
	 * Check bottom edge for collisions.
	 */
	public void checkBottomEdgeCollisions() {
		/*If yloc is at ground level, reset jump count and jump counter, and and update the appropriate boolean.*/
		if (this.yloc >= this.map.getGroundLevel()) {
			this.jumpingCounter = 0;
			this.jumpCount = 0;
			this.onSurfaceBottom = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.environment) {
			if (m instanceof minigame1Models.Rock || m instanceof minigame1Models.Debris || m instanceof minigame1Models.Interactable || m instanceof minigame1Models.Sand) {
				if (this.checkBottomSurface(m)) {
					/*If there was an environmental bottom edge collision, reset jump count and 
					 *jump counter, and and update the appropriate boolean.*/
					this.jumpingCounter = 0;
					this.jumpCount = 0;
					this.floatingCounter = 0;
					if (m instanceof minigame1Models.Interactable) {
						this.inContactWith = (Interactable) m;
						switch (((minigame1Models.Interactable) m).getDirection()) {
							case NORTH:
								this.againstMovingSurfaceBottom = true;
								this.onMovingSurfaceBottom = false;
								break;
							default:
								this.onMovingSurfaceBottom = true;
								this.againstMovingSurfaceBottom = false;
								break;
						}
					} else {
						this.onSurfaceBottom = true;
					}
					return;
				}
			}
		}
	}
	
	/**
	 * Check top edge for collisions.
	 */
	public void checkTopEdgeCollisions() {
		boolean newCollision = false;
		/*Check yloc to see if we've hit the ceiling (valid y locations will range from map.getGroundLevel() 
		 *to map.getGroundLevel() - map.getHeight(), since y = 0 is the top of the screen.*/
		if (this.yloc <= this.map.getGroundLevel() - this.map.getHeight()) {
			this.againstSurfaceTop = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.environment) {
			if (m instanceof minigame1Models.Rock || m instanceof minigame1Models.Debris || m instanceof minigame1Models.Interactable || m instanceof minigame1Models.Sand) {
				int x = m.getXloc();
				int y = m.getYloc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (this.yloc <= y + h && this.yloc >= y) {
					for (int i = x - this.width + 1; i < x + w; i++) {
						if (this.xloc == i) {
							newCollision = true;
							if (m instanceof minigame1Models.Interactable) {
								this.inContactWith = (Interactable) m;
								this.againstMovingSurfaceTop = true;
							} else {
								this.againstSurfaceTop = true;
							}
						}
					}
				}
			}
		}
		/*If there was no collision, update the appropriate boolean.*/
		if (!newCollision) {
			this.againstSurfaceTop = false;
			this.againstMovingSurfaceTop = false;
		}
	}
	
	/**
	 * Check right edge for collisions.
	 */
	public void checkRightEdgeCollisions() {
		boolean newCollision = false;
		/*Check xloc to see if we're at the right edge of the map.*/
		if (this.xloc >= this.map.getWidth()) {
			this.againstSurfaceRight = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.environment) {
			if (m instanceof minigame1Models.Rock || m instanceof minigame1Models.Debris || m instanceof minigame1Models.Interactable || m instanceof minigame1Models.Sand) {
				int x = m.getXloc();
				int y = m.getYloc();
				int h = m.getHeight();
				if (this.xloc + this.width == x) {
					for (int i = y - this.height + 1; i < y + h; i++) {
						if (this.yloc == i) {
							newCollision = true;
							if (m instanceof minigame1Models.Interactable) {
								this.inContactWith = (Interactable) m;
								this.againstMovingSurfaceRight = true;
							} else {
								this.againstSurfaceRight = true;
							}
						}
					}
				}
			}
		}
		/*If there was no collision, update the appropriate boolean.*/
		if (!newCollision) {
			this.againstSurfaceRight = false;
			this.againstMovingSurfaceRight = false;
		}
	}
	
	/**
	 * Check left edge for collisions.
	 */
	public void checkLeftEdgeCollisions() {
		boolean newCollision = false;
		/*Check xloc to see if we're at the left edge of the map.*/
		if (this.xloc <= 0) {
			this.againstSurfaceLeft = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.environment) {
			if (m instanceof minigame1Models.Rock || m instanceof minigame1Models.Debris || m instanceof minigame1Models.Interactable || m instanceof minigame1Models.Sand) {
				int x = m.getXloc();
				int y = m.getYloc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (this.xloc == x + w) {
					for (int i = y - this.height + 1; i < y + h; i++) {
						if (this.yloc == i) {
							newCollision = true;
							if (m instanceof minigame1Models.Interactable) {
								this.inContactWith = (Interactable) m;
								this.againstMovingSurfaceLeft = true;
							} else {
								this.againstSurfaceLeft = true;
							}
						}
					}
				}
			}
		}
		/*If there was no collision, update the appropriate boolean.*/
		if (!newCollision) {
			this.againstSurfaceLeft = false;
			this.againstMovingSurfaceLeft = false;
		}
	}
	
	/**
	 * Check for collisions with objects that don't act as surfaces: health regen areas and enemies.
	 */
	public void checkAreaCollisions() {
		this.currentCollision = false; // innocent until proven guilty policy
		for (Game1Model m : this.environment) {
			if (m instanceof minigame1Models.Enemy || m instanceof minigame1Models.RegenArea || m instanceof minigame1Models.Current) {
				int x = m.getXloc();
				int y = m.getYloc();
				int w = m.getWidth();
				int h = m.getHeight();
				/*Check every point of the hitbox against every point of the model.*/
				for (int i = x; i < x + w; i++) {
					for (int j = y; j < y + h; j++) {
						for (int k = this.xloc; k < this.xloc + this.width; k++) {
							for (int l = this.yloc; l < this.yloc + this.height; l++) {
								if (i == k && j == l) {
									/*Currently, if an enemy and regen area/current occupy the same area, the
									 *enemy takes precedence.*/
									if (m instanceof minigame1Models.Enemy) {
										this.enemyCollision = true;
										this.healthDecrease = ((minigame1Models.Enemy)m).getDamage();
										return;
									} else if (m instanceof minigame1Models.RegenArea) {
										this.regenCollision = true;
										return;
									} else if (m instanceof minigame1Models.Current) {
										this.currentCollision = true;
										this.dirOfCurrent = ((minigame1Models.Current)m).getFlowDirection();
										this.incrFromCurrent = ((minigame1Models.Current)m).getIncr();
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
	
	/**
	 * Return true if a bottom edge collision with the given Minigame1Model
	 *  is detected, and false otherwise.
	 * 
	 * @param m the given Minigame1Model
	 * @return true if collision, false otherwise
	 */
	public boolean checkBottomSurface(Game1Model m) {
		int x = m.getXloc();
		int y = m.getYloc();
		int w = m.getWidth();
		if (this.yloc + this.height == y) {
			for (int i = x - this.width + 1; i < x + w; i++) {
				if (this.xloc == i) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Look at enemy and regen collision flags and update player health accordingly.
	 */
	public void evaluateAreaCollisions() {
		if (this.enemyCollision) {
			if (!this.damageDealt) {
				/*Deal the appropriate amount of damage, and only once.*/
				this.currHealth -= this.healthDecrease;
				this.damageDealt = true;
			}
			/*Give the player some time before being open to taking damage again.*/
			if (this.damageCooldown > 0) {
				this.damageCooldown--;
			} else {
				/*Update the appropriate booleans after the cooldown.*/
				this.damageCooldown = this.damageCooldownThresh;
				this.damageDealt = false;
				this.enemyCollision = false;
			}
		}
		if (this.regenCollision) {
			/*Don't give the player more than the maximum health.*/
			if (this.currHealth < this.maxHealth) {
				/*There is no health cooldown; restore health immediately.*/
				this.currHealth++;
				this.regenCollision = false;
			} else {
				this.regenCollision = false;
			}
		}
		/*If a current collision has been detected, move the player in the corresponding direction.*/
		if (this.currentCollision) {
			switch (this.dirOfCurrent) {
				case EAST:
					this.checkRightEdgeCollisions();
					if (!this.againstSurfaceRight && !this.againstMovingSurfaceRight) {
						this.xloc += this.incrFromCurrent;
					}
					break;
					
				case WEST:
					this.checkLeftEdgeCollisions();
					if (!this.againstSurfaceLeft && !this.againstMovingSurfaceLeft) {
						this.xloc -= this.incrFromCurrent;
					}
					break;
					
				case NORTH:
					this.checkTopEdgeCollisions();
					if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
						this.yloc -= this.incrFromCurrent;
					}
					break;
					
				case SOUTH:
					this.checkBottomEdgeCollisions();
					if (!this.onSurfaceBottom && !this.onMovingSurfaceBottom) {
						this.yloc += this.incrFromCurrent;
					}
					break;
				
				default:
					break;
			}
		}
	}
}
