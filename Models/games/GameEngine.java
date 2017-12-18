package games;

import java.io.Serializable;
import java.util.ArrayList;

import enums.Direction;
import enums.RoomID;
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
	
	private ArrayList<Game1Model> currEnvironment;
	
	private AreaMap currMap;
	
	private Room currRoom;
	private RoomID currRoomID = RoomID.SPAWN;
	private RoomID destinationRoomID;
	private int changeRoomOffsetX;
	private int changeRoomOffsetY;
	
	private int jumpingCounter = 0;
	private int jumpDuration = 30;
	private int jumpCount = 0; // current number of times jumped (resets when you land on a surface)
	
	private int damageCooldownThresh = 80;
	private int damageCooldown = this.damageCooldownThresh;
	private int healthIncrease;
	private int healthDecreaseEnemy;
	private int healthDecreaseDamArea;
	private boolean damageDealt = false;
	
	private int maxJumps = this.defaultMaxJumps; // maximum number of jumps allowed
	
	private final int interactableWaitTime = 50;
	
	public Controllable makePlayer() {
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
		
		this.currRoomID = RoomID.SPAWN;
		this.makeCurrRoom();
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
	
	public Room getCurrRoom() {
		return this.currRoom;
	}
	
	public ArrayList<Game1Model> getEnvironment() {
		return this.currEnvironment;
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
	
	public boolean getDamaged() {
		return this.damageCooldown != this.damageCooldownThresh;
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
	
	public void setEnvironment(ArrayList<Game1Model> e) {
		this.currEnvironment = e;
	}
	
	public void setMap(AreaMap m) {
		this.currMap = m;
	}
	
	public void setRoom(Room r) {
		this.currRoom = r;
	}
	
	/*Methods that move a controllable object*/
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
	
	public void moveRight(Controllable c) {
		while (c.getCurrXSegment() < c.getXIncr()) {
			this.checkRightEdgeCollisions(c);
			this.incrX(c);
		}
		c.setCurrXSegment(0);
	}
	
	public void moveLeft(Controllable c) {
		while (c.getCurrXSegment() < c.getXIncr()) {
			this.checkLeftEdgeCollisions(c);
			this.decrX(c);
		}
		c.setCurrXSegment(0);
	}
	
	public void raiseY(Controllable c) {
		if (!c.isAgainstSurfaceTop() && !c.isAgainstMovingSurfaceTop()) {
			c.setOnSurfaceBottom(false);
			c.setOnMovingSurfaceBottom(false);
			c.setOnPlatform(false);
			c.setAgainstMovingSurfaceBottom(false);
			this.floatingCounter = 0;
			while (c.getCurrYSegment() < c.getYIncr()) {
				this.checkTopEdgeCollisions(c);
				this.incrY(c);
			}
			c.setCurrYSegment(0);
		} else {
			this.jumpingCounter = this.jumpDuration;
		}
	}
	
	public boolean initiateJumpArc(Controllable c) {
		if (this.jumpCount < this.maxJumps) {
			if (this.jumpingCounter < this.jumpDuration) {
				this.jumpingCounter++;
				this.raiseY(c);
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
	
	public void assertGravity(Controllable c) {
		if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
			if (this.floatingCounter < this.floatingThreshold) {
				this.floatingCounter++;
			} else {
				while (c.getCurrYSegment() < c.getYIncr()) {
					this.checkMovingSurfaces(true, c);
					this.decrY(c);
				}
				c.setCurrYSegment(0);
			}
		}
	}
	
	public void phaseThroughPlatformOrExit(Controllable c) {
		if (c.isOnPlatform()) {
			c.setYLoc(c.getYLoc() + 1);
			c.setOnPlatform(false);
			c.setOnSurfaceBottom(false);
			this.floatingCounter = this.floatingThreshold;
		} else {
			for (Exit ex : this.currRoom.getRoomLinks()) {
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
	
	/*Methods for checking collisions with other models*/
	public void checkLeavingSurface(Controllable c) {
		if (c.isOnSurfaceBottom() || c.isOnMovingSurfaceBottom()) {
			boolean contact = false;
			boolean movingContact = false;
			/*Check against every model that acts as a surface.*/
			for (Game1Model m : this.currEnvironment) {
				if (m instanceof game1Models.Autonomous) {
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
	
	public void checkMovingSurfaces(boolean calledWithinEngine, Controllable c) {
		/*Check all edges for collisions (particularly of the moving variety).*/
		this.checkLeftEdgeCollisions(c);
		this.checkRightEdgeCollisions(c);
		this.checkBottomEdgeCollisions(c);
		this.checkTopEdgeCollisions(c);
		/*Respond to these collisions. These collisions only increment/decrement because Autonomous uses 
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
		/*If resting on a moving surface, the player will move in the same direction as the 
		 *moving surface. This code should never be executed by Autonomous.*/
		if (c.isOnMovingSurfaceBottom()) {
			if (!calledWithinEngine) {
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
	
	public void checkBottomEdgeCollisions(Controllable c) {
		/*If yloc is at ground level, reset jump count and jump counter, and and update the appropriate boolean.*/
		if (c.getYLoc() + c.getHeight() >= this.currRoom.getYLoc()) {
			c.setOnSurfaceBottom(true);
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof SolidObject || m instanceof Platform) {
				if (this.checkBottomSurface(m, c)) {
					/*If there was an environmental bottom edge collision, reset jump count and 
					 *jump counter, and and update the appropriate boolean.*/
					this.floatingCounter = 0;
					if (m instanceof game1Models.Autonomous) {
						c.setInContactWith((Autonomous) m);
						switch (((game1Models.Autonomous) m).getDirection()) {
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
	
	public void checkTopEdgeCollisions(Controllable c) {
		boolean newCollision = false;
		/*Check yloc to see if we've hit the ceiling (valid y locations will range from map.getGroundLevel() 
		 *to map.getGroundLevel() - map.getHeight(), since y = 0 is the top of the screen.*/
		if (c.getYLoc() <= this.currRoom.getYLoc() - this.currRoom.getHeight()) {
			c.setAgainstSurfaceTop(true);
			newCollision = true;
			for (Exit ex : this.currRoom.getRoomLinks()) {
				if (c.getYLoc() <= ex.getYLoc()) {
					if (c.getXLoc() >= ex.getXLoc() && c.getXLoc() + c.getWidth() <= ex.getXLoc() + ex.getWidth()) {
						c.setAgainstSurfaceTop(false);
						newCollision = false;
					}
				}
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (c.getYLoc() <= y + h && c.getYLoc() >= y) {
					for (int i = m.getXLoc() - c.getWidth() + 1; i < x + w; i++) {
						if (c.getXLoc() == i) {
							newCollision = true;
							if (m instanceof game1Models.Autonomous) {
								c.setInContactWith((Autonomous) m);
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
	
	public void checkRightEdgeCollisions(Controllable c) {
		boolean newCollision = false;
		/*Check xloc to see if we're at the right edge of the map.*/
		if (c.getXLoc() + c.getWidth() >= this.currRoom.getXLoc() + this.currRoom.getWidth()) {
			c.setAgainstSurfaceRight(true);
			newCollision = true;
			for (Exit ex : this.currRoom.getRoomLinks()) {
				if (c.getXLoc() + c.getWidth() >= ex.getXLoc()) {
					if (c.getYLoc() >= ex.getYLoc() && c.getYLoc() <= ex.getYLoc() + ex.getHeight()) {
						c.setAgainstSurfaceRight(false);
						newCollision = false;
					}
				}
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int h = m.getHeight();
				if (c.getXLoc() + c.getWidth() == x) {
					for (int i = y - c.getHeight() + 1; i < y + h; i++) {
						if (c.getYLoc() == i) {
							newCollision = true;
							if (m instanceof game1Models.Autonomous) {
								c.setInContactWith((Autonomous) m);
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
	
	public void checkLeftEdgeCollisions(Controllable c) {
		boolean newCollision = false;
		/*Check xloc to see if we're at the left edge of the map.*/
		if (c.getXLoc() <= this.currRoom.getXLoc()) {
			c.setAgainstSurfaceLeft(true);
			newCollision = true;
			for (Exit ex : this.currRoom.getRoomLinks()) {
				if (c.getXLoc() <= ex.getXLoc()) {
					if (c.getYLoc() >= ex.getYLoc() && c.getYLoc() <= ex.getYLoc() + ex.getHeight()) {
						c.setAgainstSurfaceLeft(false);
						newCollision = false;
					}
				}
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof SolidObject) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (c.getXLoc() == x + w) {
					for (int i = y - c.getHeight() + 1; i < y + h; i++) {
						if (c.getYLoc() == i) {
							newCollision = true;
							if (m instanceof game1Models.Autonomous) {
								c.setInContactWith((Autonomous) m);
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
	
	/*Methods for checking collisions with event areas*/
	public void checkAreaCollisions(Controllable c) {
		boolean damageCollision = false;
		boolean regenCollision = false;
		boolean forceCollision = false;
		
		Force f = null;
		
		/*Check for event area collisions*/
		for (Game1Model m : this.currEnvironment) {
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
										break;
									} else if (m instanceof game1Models.DamageArea) {
										damageCollision = true;
										this.healthDecreaseDamArea = ((game1Models.DamageArea)m).getHealthDecr();
										break;
									} else if (m instanceof game1Models.RegenArea) {
										regenCollision = true;
										this.healthIncrease = ((game1Models.RegenArea)m).getHealthIncr();
										break;
									} else if (m instanceof game1Models.Force) {
										forceCollision = true;
										f = (game1Models.Force) m;
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		/*Evaluate area collisions*/
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
		if (damageCollision) {
			if (c.getHealth() >= 0) {
				c.setHealth(c.getHealth() - this.healthDecreaseDamArea);
			}
		}
		if (regenCollision) {
			/*Don't give the player more than the maximum health.*/
			if (c.getHealth() < this.maxHealth) {
				if (c.getHealth() + this.healthIncrease > this.maxHealth) {
					c.setHealth(this.maxHealth);
				} else {
					c.setHealth(c.getHealth() + this.healthIncrease);
				}
			}
		}
		if (forceCollision) {
			int incr = f.getIncr();
			switch (f.getFlowDirection()) {
				case EAST:
					this.checkRightEdgeCollisions(c);
					if (!c.isAgainstSurfaceRight() && !c.isAgainstMovingSurfaceRight()) {
						c.setXLoc(c.getXLoc() + incr);
					}
					break;
					
				case WEST:
					this.checkLeftEdgeCollisions(c);
					if (!c.isAgainstSurfaceLeft() && !c.isAgainstMovingSurfaceLeft()) {
						c.setXLoc(c.getXLoc() - incr);
					}
					break;
					
				case NORTH:
					this.checkTopEdgeCollisions(c);
					if (!c.isAgainstSurfaceTop() && !c.isAgainstMovingSurfaceTop()) {
						c.setYLoc(c.getYLoc() - incr);
					}
					break;
					
				case SOUTH:
					this.checkBottomEdgeCollisions(c);
					if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
						c.setYLoc(c.getYLoc() + incr);
					}
					break;
				
				default:
					break;
			}
		}
	}
	
	/*Move things that move (enemies, autonomous)*/
	public void moveAll(Controllable c) {
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof game1Models.Enemy) {
				((game1Models.Enemy) m).move();
			} else if (m instanceof game1Models.Autonomous) {
				this.moveAutonomous((game1Models.Autonomous) m, c);
			}
		}
	}
	
	public void moveAutonomous(Autonomous a, Controllable c) {
		switch (a.getDirection()) {
			case EAST:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setXLoc(a.getXLoc() + 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkMovingSurfaces(true, c);
				}
				a.setCurrSegment(0);
				if (a.getXLoc() >= a.getMoveThreshR()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			case WEST:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setXLoc(a.getXLoc() - 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkMovingSurfaces(true, c);
				}
				a.setCurrSegment(0);
				if (a.getXLoc() <= a.getMoveThreshL()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			case NORTH:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setYLoc(a.getYLoc() - 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkMovingSurfaces(true, c);
				}
				a.setCurrSegment(0);
				if (a.getYLoc() <= a.getMoveThreshU()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			case SOUTH:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setYLoc(a.getYLoc() + 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkMovingSurfaces(true, c);
				}
				a.setCurrSegment(0);
				if (a.getYLoc() >= a.getMoveThreshD()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			/*Interactables wait for a moment before switching directions.*/
			case IDLE:
				if (a.getWaitCounter() < this.interactableWaitTime) {
					a.setWaitCounter(a.getWaitCounter() + 1);
				} else {
					a.setWaitCounter(0);
					switch (a.getLastDirection()) {
						case NORTH:
							a.setDirection(Direction.SOUTH);
							break;
							
						case SOUTH:
							a.setDirection(Direction.NORTH);
							break;
							
						case EAST:
							a.setDirection(Direction.WEST);
							break;
							
						case WEST:
							a.setDirection(Direction.EAST);
							break;
							
						default:
							break;
					}
				}
				
			default:
				break;
		}
	}
	
	/*Methods for rooms*/
	public void makeCurrRoom() {
		this.currRoom = new Room(this.currRoomID, this.currMap.accessRoomData(this.currRoomID)[0], this.currMap.accessRoomData(this.currRoomID)[1], this.currMap.accessRoomData(this.currRoomID)[2], this.currMap.accessRoomData(this.currRoomID)[3], this.currMap.accessRoomEnvs(this.currRoomID), this.currMap.accessRoomLinks(this.currRoomID));
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void changeRoom(Controllable c) {
		/*Make the current room*/
		this.currRoomID = this.destinationRoomID;
		this.makeCurrRoom();
		
		/*Offset the player from the doorway*/
		c.setXLoc(c.getXLoc() + this.changeRoomOffsetX);
		c.setYLoc(c.getYLoc() + this.changeRoomOffsetY);
	}
	
	public boolean checkRoomBoundaries(Controllable c) {
		int px = c.getXLoc();
		int py = c.getYLoc();
		
		for (Exit e : this.currMap.accessRoomLinks(this.currRoomID)) {
			int x = e.getXLoc();
			int y = e.getYLoc();
			int h = e.getHeight();
			int w = e.getWidth();
			switch (e.getDirection()) {
				case NORTH:
					if (px >= x && px + c.getWidth() <= x + w) {
						if (py < y) {
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = 0;
							this.changeRoomOffsetY = -1 * (c.getHeight() + 1); //push rest of player through door (to avoid erroneously triggering more room change events)
							return true;
						}
					}
					break;
					
				case SOUTH:
					if (px >= x && px + c.getWidth() <= x + w) {
						if (py + c.getHeight() > y) {
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = 0;
							this.changeRoomOffsetY = c.getHeight() + 1;
							return true;
						}
					}
					break;
					
				case EAST:
					if (py <= y && py > y - h) {
						if (px + c.getWidth() >= x) {
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = c.getWidth() + 1;
							this.changeRoomOffsetY = 0;
							return true;
						}
					}
					break;
					
				case WEST:
					if (py <= y && py > y - h) {
						if (px <= x) {
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = -1 * (c.getWidth() + 1);
							this.changeRoomOffsetY = 0;
							return true;
						}
					}
					break;
				
				default:
					break;
			}
		}
		return false;
	}
	
}
