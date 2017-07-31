package game1Models;

import java.util.ArrayList;

import enums.Direction;

public class Player extends Game1Model {

	private int xIncr = 6;
	private int yIncr = 5;
	private int currXSegment = 0;
	private int currYSegment = 0;
	private int floatingCounter = 0;
	private final int floatingThreshold = 10; // used to make the player float for a moment after jumping, as if underwater
	private boolean onSurfaceBottom = false; // boolean used for checking if the player's bottom edge is in contact with an object
	private boolean againstSurfaceTop = false; // boolean used for checking if the player's top edge is in contact with another object
	private boolean againstSurfaceRight = false; // etc
	private boolean againstSurfaceLeft = false;
	private Interactable inContactWith;
	private boolean onMovingSurfaceBottom = false; // boolean used for checking if the player's bottom edge is in contact with a moving object
	private boolean againstMovingSurfaceBottom = false; // similar to above variable, except used in the case where the player is falling and the moving object is rising
	private boolean againstMovingSurfaceTop = false; // boolean used for checking if the player's top edge is in contact with a moving object
	private boolean againstMovingSurfaceRight = false; // etc
	private boolean againstMovingSurfaceLeft = false;
	private boolean onPlatform = false;
	private boolean enemyCollision = false; // true if player is occupying same area as an enemy
	private boolean damageCollision = false;
	private boolean regenCollision = false; // true if player is occupying same area as a regen area
	private boolean currentCollision = false; // true if player is occupying same area as a current (Current.java)
	private int jumpingCounter = 0;
	private final int jumpDuration = 30;
	private int jumpCount = 0; // current number of times jumped (resets when you land on a surface)
	private final int maxJumps = 2; // maximum number of jumps allowed
	private final int maxHealth = 100;
	private int currHealth = 100;
	private final int damageCooldownThresh = 80;
	private int damageCooldown = this.damageCooldownThresh;
	private int healthIncrease;
	private int healthDecreaseEnemy;
	private int healthDecreaseDamArea;
	private boolean damageDealt = false;
	private Direction dirOfCurrent; // direction of current (Current.java)
	private int incrFromCurrent; // magnitude of push from currents (Current.java)
	
	public Player(int x, int y, int h, int w) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
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
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	/**
	 * Used by view to create the flashing effect after taking damage.
	 * 
	 * @return enemy collision boolean
	 */
	public boolean getEnemyCollision() {
		return this.enemyCollision;
	}
	
	//debug view display suite
	public boolean getOnSurfaceBottom() {
		return this.onSurfaceBottom;
	}
	
	public boolean getAgainstSurfaceTop() {
		return this.againstSurfaceTop;
	}
	
	public boolean getAgainstSurfaceRight() {
		return this.againstSurfaceRight;
	}
	
	public boolean getAgainstSurfaceLeft() {
		return this.againstSurfaceLeft;
	}
	
	public boolean getOnMovingSurfaceBottom() {
		return this.onMovingSurfaceBottom;
	}
	
	public boolean getAgainstMovingSurfaceBottom() {
		return this.againstMovingSurfaceBottom;
	}
	
	public boolean getAgainstMovingSurfaceTop() {
		return this.againstMovingSurfaceTop;
	}
	
	public boolean getAgainstMovingSurfaceRight() {
		return this.againstMovingSurfaceRight;
	}
	
	public boolean getAgainstMovingSurfaceLeft() {
		return this.againstMovingSurfaceLeft;
	}
	
	public boolean getOnPlatform() {
		return this.onPlatform;
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
	//end debug view display suite
	
	public void incrX() {
		if (!this.againstSurfaceRight && !this.againstMovingSurfaceRight) {
			this.setXLoc(this.getXLoc() + 1);
		}
		this.currXSegment++;
	}
	
	public void decrX() {
		if (!this.againstSurfaceLeft && !this.againstMovingSurfaceLeft) {
			this.setXLoc(this.getXLoc() - 1);
		}
		this.currXSegment++;
	}
	
	void incrY() {
		if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
			this.setYLoc(this.getYLoc() - 1);
		}
		this.currYSegment++;
	}
	
	public void decrY() {
		if (!this.onSurfaceBottom && !this.onMovingSurfaceBottom) {
			this.setYLoc(this.getYLoc() + 1);
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
	
	public void moveLeft(Room r, ArrayList<Game1Model> e) {
		while (this.currXSegment < this.xIncr) {
			this.checkLeftEdgeCollisions(r, e);
			this.decrX();
		}
		this.currXSegment = 0;
	}
	
	public void raiseY(Room r, ArrayList<Game1Model> e) {
		if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
			this.onSurfaceBottom = false;
			this.onMovingSurfaceBottom = false;
			this.onPlatform = false;
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
				this.jumpingCounter++;
				this.raiseY(r, e);
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
				if (m instanceof game1Models.Interactable) {
					if (this.checkBottomSurface(m)) {
						movingContact = true;
					}
				} else if (m instanceof SolidObject || m instanceof Platform) {
					if (this.checkBottomSurface(m)) {
						contact = true;
					}
				} 
			}
			/*If there were no environmental collisions, reset floating counter and update the appropriate boolean.*/
			if (!contact) {
				this.onPlatform = false;
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
		this.checkLeftEdgeCollisions(r, e);
		this.checkRightEdgeCollisions(r, e);
		this.checkBottomEdgeCollisions(r, e);
		this.checkTopEdgeCollisions(r, e);
		/*Respond to these collisions. These collisions only increment/decrement because Interactable uses 
		 *the same while loop scheme to move (move a pixel and then check for collisions).*/
		if (this.againstMovingSurfaceLeft && this.inContactWith.getDirection() == Direction.EAST) {
			this.setXLoc(this.getXLoc() + 1);
		}
		if (this.againstMovingSurfaceRight && this.inContactWith.getDirection() == Direction.WEST) {
			this.setXLoc(this.getXLoc() - 1);
		}	
		if (this.againstMovingSurfaceTop && this.inContactWith.getDirection() == Direction.SOUTH) {
			this.setYLoc(this.getYLoc() + 1);
		}
		if (this.againstMovingSurfaceBottom && this.inContactWith.getDirection() == Direction.NORTH) {
			this.setYLoc(this.getYLoc() - 1);
			this.againstMovingSurfaceBottom = false;
		}
		/*If resting on a moving surface, the crab will move in the same direction as the 
		 *moving surface. This code should never be executed by Interactable.*/
		if (this.onMovingSurfaceBottom) {
			if (!calledByInteractable) {
				switch (this.inContactWith.getDirection()) {
					case EAST:
						this.setXLoc(this.getXLoc() + this.inContactWith.getIncr());
						break;
						
					case WEST:
						this.setXLoc(this.getXLoc() - this.inContactWith.getIncr());
						break;
						
					case NORTH:
						this.setYLoc(this.getYLoc() - this.inContactWith.getIncr());
						break;
						
					case SOUTH:
						this.setYLoc(this.getYLoc() + this.inContactWith.getIncr());
						break;
						
					default:
						break;
				}
			}
		}
	}
	
	public void checkBottomEdgeCollisions(Room r, ArrayList<Game1Model> e) {
		/*If yloc is at ground level, reset jump count and jump counter, and and update the appropriate boolean.*/
		if (this.getYLoc() + this.getHeight() >= r.getYLoc()) {
			this.onSurfaceBottom = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject || m instanceof Platform) {
				if (this.checkBottomSurface(m)) {
					/*If there was an environmental bottom edge collision, reset jump count and 
					 *jump counter, and and update the appropriate boolean.*/
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
						if (m instanceof Platform) {
							this.onPlatform = true;
						}
					}
				}
			}
		}
		
		if ((this.onSurfaceBottom || this.onMovingSurfaceBottom) && this.jumpingCounter == 0) {
			this.jumpCount = 0;
		}
	}
	
	public void checkTopEdgeCollisions(Room r, ArrayList<Game1Model> e) {
		boolean newCollision = false;
		/*Check yloc to see if we've hit the ceiling (valid y locations will range from map.getGroundLevel() 
		 *to map.getGroundLevel() - map.getHeight(), since y = 0 is the top of the screen.*/
		if (this.getYLoc() <= r.getYLoc() - r.getHeight()) {
			this.againstSurfaceTop = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (this.getYLoc() <= y + h && this.getYLoc() >= y) {
					for (int i = m.getXLoc() - this.getWidth() + 1; i < x + w; i++) {
						if (this.getXLoc() == i) {
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
		if (this.getXLoc() + this.getWidth() >= r.getXLoc() + r.getWidth() && !r.hasRoomEast()) {
			this.againstSurfaceRight = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int h = m.getHeight();
				if (this.getXLoc() + this.getWidth() == x) {
					for (int i = y - this.getHeight() + 1; i < y + h; i++) {
						if (this.getYLoc() == i) {
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
	
	public void checkLeftEdgeCollisions(Room r, ArrayList<Game1Model> e) {
		boolean newCollision = false;
		/*Check xloc to see if we're at the left edge of the map.*/
		if (this.getXLoc() <= r.getXLoc() && !r.hasRoomWest()) {
			this.againstSurfaceLeft = true;
			newCollision = true;
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : e) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (this.getXLoc() == x + w) {
					for (int i = y - this.getHeight() + 1; i < y + h; i++) {
						if (this.getYLoc() == i) {
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
			if (m instanceof EventArea) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				/*Check every point of the hitbox against every point of the model.*/
				for (int i = x; i < x + w; i++) {
					for (int j = y; j < y + h; j++) {
						for (int k = this.getXLoc(); k < this.getXLoc() + this.getWidth(); k++) {
							for (int l = this.getYLoc(); l < this.getYLoc() + this.getHeight(); l++) {
								if (i == k && j == l) {
									/*Currently, if an enemy and regen area/current occupy the same area, the
									 *enemy takes precedence.*/
									if (m instanceof game1Models.Enemy) {
										this.enemyCollision = true;
										this.healthDecreaseEnemy = ((game1Models.Enemy)m).getDamage();
										return;
									} else if (m instanceof game1Models.DamageArea) {
										this.damageCollision = true;
										this.healthDecreaseDamArea = ((game1Models.DamageArea)m).getHealthDecr();
										return;
									} else if (m instanceof game1Models.RegenArea) {
										this.regenCollision = true;
										this.healthIncrease = ((game1Models.RegenArea)m).getHealthIncr();
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
		int x = m.getXLoc();
		int y = m.getYLoc();
		int w = m.getWidth();
		if (this.getYLoc() + this.getHeight() == y) {
			for (int i = x - this.getWidth() + 1; i < x + w; i++) {
				if (this.getXLoc() == i) {
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
				this.currHealth -= this.healthDecreaseEnemy;
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
		
		if (this.damageCollision) {
			if (this.currHealth >= 0) {
				this.currHealth -= this.healthDecreaseDamArea;
				this.damageCollision = false;
			} else {
				this.damageCollision = false;
			}
		}
		
		if (this.regenCollision) {
			/*Don't give the player more than the maximum health.*/
			if (this.currHealth < this.maxHealth) {
				if (this.currHealth + this.healthIncrease > this.maxHealth) {
					this.currHealth = this.maxHealth;
				} else {
					this.currHealth += this.healthIncrease;
				}
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
						this.setXLoc(this.getXLoc() + this.incrFromCurrent);
					}
					break;
					
				case WEST:
					this.checkLeftEdgeCollisions(r, e);
					if (!this.againstSurfaceLeft && !this.againstMovingSurfaceLeft) {
						this.setXLoc(this.getXLoc() - this.incrFromCurrent);
					}
					break;
					
				case NORTH:
					this.checkTopEdgeCollisions(r, e);
					if (!this.againstSurfaceTop && !this.againstMovingSurfaceTop) {
						this.setYLoc(this.getYLoc() - this.incrFromCurrent);
					}
					break;
					
				case SOUTH:
					this.checkBottomEdgeCollisions(r, e);
					if (!this.onSurfaceBottom && !this.onMovingSurfaceBottom) {
						this.setYLoc(this.getYLoc() + this.incrFromCurrent);
					}
					break;
				
				default:
					break;
			}
		}
	}
}
