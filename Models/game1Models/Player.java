package game1Models;

import java.util.ArrayList;

import enums.Direction;

public class Player implements Game1Model {

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
	
	public boolean getOnASurface() {
		return this.onSurfaceBottom || this.onMovingSurfaceBottom;
	}
	
	/*public void loadEnvironmentAndMap(ArrayList<Game1Model> e, Room m) {
		this.environment = e;
		this.currRoom = m;
	}*/
	
	public void incrX() {
		if (!this.againstSurfaceRight && !this.againstMovingSurfaceRight) {
			this.xloc++;
		}
		this.currXSegment++;
	}
	
	public void decrX() {
		if (!this.againstSurfaceLeft && !this.againstMovingSurfaceLeft) {
			this.xloc--;
		}
		this.currXSegment++;
	}
	
	void incrY() {
		if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
			this.yloc--;
		}
		this.currYSegment++;
	}
	
	public void decrY() {
		if (!this.onSurfaceBottom && !this.onMovingSurfaceBottom) {
			this.yloc++;
		}
		this.currYSegment++;
	}
	
	public void moveRight(Room r, ArrayList<Game1Model> e) {
		/*Implement the concept mentioned above: check for collisions after every pixel.*/
		while (this.currXSegment < this.xIncr) {
			this.checkRightEdgeCollisions(r, e);
			this.incrX();
		}
		this.currXSegment = 0;
	}
	
	public void moveLeft(ArrayList<Game1Model> e) {
		while (this.currXSegment < this.xIncr) {
			this.checkLeftEdgeCollisions(e);
			this.decrX();
		}
		this.currXSegment = 0;
	}
	
	public void raiseY(Room r, ArrayList<Game1Model> e) {
		if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
			this.onSurfaceBottom = false;
			this.onMovingSurfaceBottom = false;
			this.againstMovingSurfaceBottom = false;
			this.floatingCounter = 0;
			while (this.currYSegment < this.yIncr) {
				this.checkTopEdgeCollisions(r, e);
				this.incrY();
			}
			this.currYSegment = 0;
		} else {
			this.jumpingCounter = this.jumpDuration;
		}
	}
	
	public boolean initiateJumpArc(Room r, ArrayList<Game1Model> e) {
		if (this.jumpCount < this.maxJumps) {
			if (this.jumpingCounter < this.jumpDuration) {
				this.raiseY(r, e);
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
	
	public void assertGravity(Room r, ArrayList<Game1Model> e) {
		if (!this.onSurfaceBottom && !this.onMovingSurfaceBottom) {
			if (this.floatingCounter < this.floatingThreshold) {
				this.floatingCounter++;
			} else {
				while (this.currYSegment < this.yIncr) {
					this.checkMovingSurfaces(r, e, true);
					this.decrY();
				}
				this.currYSegment = 0;
			}
		}
	}
	
	public void checkLeavingSurface(ArrayList<Game1Model> e) {
		if (this.onSurfaceBottom || this.onMovingSurfaceBottom) {
			boolean contact = false;
			boolean movingContact = false;
			/*Check against every model that acts as a surface.*/
			for (Game1Model m : e) {
				if (m instanceof game1Models.Rock || m instanceof game1Models.Debris || m instanceof game1Models.Sand) {
					if (this.checkBottomSurface(m)) {
						contact = true;
					}
				} else if (m instanceof game1Models.Interactable) {
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
	
	public void checkMovingSurfaces(Room r, ArrayList<Game1Model> e, boolean calledByInteractable) {
		/*Check all edges for collisions (particularly of the moving variety).*/
		this.checkLeftEdgeCollisions(e);
		this.checkRightEdgeCollisions(r, e);
		this.checkBottomEdgeCollisions(r, e);
		this.checkTopEdgeCollisions(r, e);
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
	
	public void checkBottomEdgeCollisions(Room r, ArrayList<Game1Model> e) {
		/*If yloc is at ground level, reset jump count and jump counter, and and update the appropriate boolean.*/
		if (this.yloc >= r.getGroundLevel()) {
			this.jumpingCounter = 0;
			this.jumpCount = 0;
			this.onSurfaceBottom = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof game1Models.Rock || m instanceof game1Models.Debris || m instanceof game1Models.Interactable || m instanceof game1Models.Sand) {
				if (this.checkBottomSurface(m)) {
					/*If there was an environmental bottom edge collision, reset jump count and 
					 *jump counter, and and update the appropriate boolean.*/
					this.jumpingCounter = 0;
					this.jumpCount = 0;
					this.floatingCounter = 0;
					if (m instanceof game1Models.Interactable) {
						this.inContactWith = (Interactable) m;
						switch (((game1Models.Interactable) m).getDirection()) {
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
	
	public void checkTopEdgeCollisions(Room r, ArrayList<Game1Model> e) {
		boolean newCollision = false;
		/*Check yloc to see if we've hit the ceiling (valid y locations will range from map.getGroundLevel() 
		 *to map.getGroundLevel() - map.getHeight(), since y = 0 is the top of the screen.*/
		if (this.yloc <= r.getGroundLevel() - r.getHeight()) {
			this.againstSurfaceTop = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof game1Models.Rock || m instanceof game1Models.Debris || m instanceof game1Models.Interactable || m instanceof game1Models.Sand) {
				int x = m.getXloc();
				int y = m.getYloc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (this.yloc <= y + h && this.yloc >= y) {
					for (int i = x - this.width + 1; i < x + w; i++) {
						if (this.xloc == i) {
							newCollision = true;
							if (m instanceof game1Models.Interactable) {
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
	
	public void checkRightEdgeCollisions(Room r, ArrayList<Game1Model> e) {
		boolean newCollision = false;
		/*Check xloc to see if we're at the right edge of the map.*/
		if (this.xloc >= r.getWidth()) {
			this.againstSurfaceRight = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof game1Models.Rock || m instanceof game1Models.Debris || m instanceof game1Models.Interactable || m instanceof game1Models.Sand) {
				int x = m.getXloc();
				int y = m.getYloc();
				int h = m.getHeight();
				if (this.xloc + this.width == x) {
					for (int i = y - this.height + 1; i < y + h; i++) {
						if (this.yloc == i) {
							newCollision = true;
							if (m instanceof game1Models.Interactable) {
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
	
	public void checkLeftEdgeCollisions(ArrayList<Game1Model> e) {
		boolean newCollision = false;
		/*Check xloc to see if we're at the left edge of the map.*/
		if (this.xloc <= 0) {
			this.againstSurfaceLeft = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof game1Models.Rock || m instanceof game1Models.Debris || m instanceof game1Models.Interactable || m instanceof game1Models.Sand) {
				int x = m.getXloc();
				int y = m.getYloc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (this.xloc == x + w) {
					for (int i = y - this.height + 1; i < y + h; i++) {
						if (this.yloc == i) {
							newCollision = true;
							if (m instanceof game1Models.Interactable) {
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
	
	public void checkAreaCollisions(ArrayList<Game1Model> e) {
		this.currentCollision = false; // innocent until proven guilty policy
		for (Game1Model m : e) {
			if (m instanceof game1Models.Enemy || m instanceof game1Models.RegenArea || m instanceof game1Models.Current) {
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
									if (m instanceof game1Models.Enemy) {
										this.enemyCollision = true;
										this.healthDecrease = ((game1Models.Enemy)m).getDamage();
										return;
									} else if (m instanceof game1Models.RegenArea) {
										this.regenCollision = true;
										return;
									} else if (m instanceof game1Models.Current) {
										this.currentCollision = true;
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
	
	public void evaluateAreaCollisions(Room r, ArrayList<Game1Model> e) {
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
					this.checkRightEdgeCollisions(r, e);
					if (!this.againstSurfaceRight && !this.againstMovingSurfaceRight) {
						this.xloc += this.incrFromCurrent;
					}
					break;
					
				case WEST:
					this.checkLeftEdgeCollisions(e);
					if (!this.againstSurfaceLeft && !this.againstMovingSurfaceLeft) {
						this.xloc -= this.incrFromCurrent;
					}
					break;
					
				case NORTH:
					this.checkTopEdgeCollisions(r, e);
					if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
						this.yloc -= this.incrFromCurrent;
					}
					break;
					
				case SOUTH:
					this.checkBottomEdgeCollisions(r, e);
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
