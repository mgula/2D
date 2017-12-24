package engine;

import java.io.Serializable;
import java.util.ArrayList;

import models.*;
import enums.Direction;
import enums.RoomID;

public class GameEngine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int defaultXIncr = 4;
	private final int defaultYIncr = 3;
	private final int defaultFloatingThreshold = 20;
	private final int defaultMaxJumps = 2;
	
	private int playerStartingXloc = 2000; 
	private int playerStartingYloc = -100;
	private int playerHeight = 40;
	private int playerWidth = 40;
	
	private boolean jumping = false;
	
	private int defaultHealth = 100;
	
	private int floatingCounter = 0;
	private int floatingThreshold = this.defaultFloatingThreshold; // used to make the player float for a moment after jumping, as if underwater
	
	private Controllable player; //we'll use this reference to the player in various methods to see if the controllable in question is the player
	
	private ArrayList<Model> currEnvironment = new ArrayList<Model>();
	
	private AreaMap currMap;
	
	private boolean enemyCollision = false;
	
	private Room currRoom;
	private RoomID currRoomID = RoomID.SPAWN;
	private RoomID destinationRoomID;
	
	private boolean roomChangeEvent = false;
	private Direction directionOfRoomChangeEvent;
	
	private int jumpingCounter = 0;
	private int jumpDuration = 50;
	private int jumpCount = 0; // current number of times jumped (resets when you land on a surface)
	
	private int damageCooldownThresh = 135;
	private int damageCooldown = this.damageCooldownThresh;
	private int healthIncrease = 0;
	private int healthDecreaseEnemy = 0;
	private int healthDecreaseDamArea = 0;
	private boolean damageDealt = false;
	
	private int maxJumps = this.defaultMaxJumps; // maximum number of jumps allowed
	
	private final int interactableWaitTime = 100;
	
	public void init() {
		this.player = new Controllable(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth, this.defaultXIncr, this.defaultYIncr, this.defaultHealth, this.defaultHealth);
	}

	private boolean isPlayer(Controllable c) {
		return c == this.player;
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
		c.setCurrHealth(c.getMaxHealth());
		
		this.jumping = false;
		this.jumpCount = 0;
		this.jumpingCounter = 0;
		this.floatingCounter = 0;
		
		this.currRoomID = RoomID.SPAWN;
		this.makeCurrRoom();
	}
	
	/*Getters*/
	public Controllable getPlayer() {
		return this.player;
	}
	
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
	
	public ArrayList<Model> getEnvironment() {
		return this.currEnvironment;
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
	
	public boolean getEnemyCollision() {
		return this.enemyCollision;
	}
	
	public boolean getRoomChangeEvent() {
		return this.roomChangeEvent;
	}
	
	/*Setters*/
	public void setPlayer(Controllable c) {
		this.player = c;
	}
	
	public void setJumping(boolean b) {
		this.jumping = b;
	}
	
	public void setMaxJumps(int n) {
		this.maxJumps = n;
	}
	
	public void setFloatingThreshold(int n) {
		this.floatingThreshold = n;
	}
	
	public void setEnvironment(ArrayList<Model> e) {
		this.currEnvironment = e;
	}
	
	public void setMap(AreaMap m) {
		this.currMap = m;
	}
	
	public void setRoom(Room r) {
		this.currRoom = r;
	}
	
	public void setRoomChangeEvent(boolean b) {
		this.roomChangeEvent = b;
	}
	
	
	/*Methods that move a controllable object*/
	public void moveRight(Controllable c) {
		while (c.getCurrXSegment() < c.getXIncr()) {
			this.checkRightEdgeCollisions(c);
			if (!c.isAgainstSurfaceRight() && !c.isAgainstMovingSurfaceRight()) {
				c.setXLoc(c.getXLoc() + 1);
			} else {
				break;
			}
			c.setCurrXSegment(c.getCurrXSegment() + 1);
		}
		c.setCurrXSegment(0);
	}
	
	public void moveLeft(Controllable c) {
		while (c.getCurrXSegment() < c.getXIncr()) {
			this.checkLeftEdgeCollisions(c);
			if (!c.isAgainstSurfaceLeft() && !c.isAgainstMovingSurfaceLeft()) {
				c.setXLoc(c.getXLoc() - 1);
			} else {
				break;
			}
			c.setCurrXSegment(c.getCurrXSegment() + 1);
		}
		c.setCurrXSegment(0);
	}
	
	public void raiseY(Controllable c) {
		if (!c.isAgainstSurfaceTop() && !c.isAgainstMovingSurfaceTop()) {
			c.setOnSurfaceBottom(false);
			c.setOnMovingSurfaceBottom(false);
			c.setNewBody(null);
			c.setOnPlatform(false);
			c.setAgainstMovingSurfaceBottom(false);
			this.floatingCounter = 0;
			while (c.getCurrYSegment() < c.getYIncr()) {
				this.checkTopEdgeCollisions(c);
				if (!c.isAgainstSurfaceTop() && !c.isAgainstMovingSurfaceTop()) {
					c.setYLoc(c.getYLoc() - 1);
				} else {
					break;
				}
				c.setCurrYSegment(c.getCurrYSegment() + 1);
			}
			c.setCurrYSegment(0);
		} else {
			this.jumpingCounter = this.jumpDuration;
		}
	}
	
	public boolean executeJump(Controllable c) {
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
		boolean player = this.isPlayer(c);
		if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
			if (this.floatingCounter < this.floatingThreshold && player) {
				this.floatingCounter++;
			} else {
				while (c.getCurrYSegment() < c.getYIncr()) {
					this.respondToMovingSurfaces(c);
					this.checkBottomEdgeCollisions(c);
					if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
						c.setYLoc(c.getYLoc() + 1);
					} else {
						break;
					}
					c.setCurrYSegment(c.getCurrYSegment() + 1);
				}
				c.setCurrYSegment(0);
			}
		}
	}
	
	public void phaseThroughPlatformOrExit(Controllable c) {
		if (c.isOnPlatform()) {
			c.setOnPlatform(false);
			c.setOnSurfaceBottom(false);
			this.floatingCounter = this.floatingThreshold;
			if (!c.isOnSurfaceBottom() && !c.isOnMovingSurfaceBottom()) {
				c.setYLoc(c.getYLoc() + 1);
			}
			c.setCurrYSegment(0);
		} else {
			for (Exit ex : this.currRoom.getRoomLinks()) {
				if (ex.getDirection() == Direction.SOUTH) {
					if (c.getYLoc() <= ex.getYLoc()) {
						if (c.getXLoc() >= ex.getXLoc() && c.getXLoc() + c.getWidth() <= ex.getXLoc() + ex.getWidth()) {
							c.setOnSurfaceBottom(false);
							this.floatingCounter = this.floatingThreshold;
							
							this.destinationRoomID = ex.getNextRoom();
							this.roomChangeEvent = true;
							this.directionOfRoomChangeEvent = Direction.SOUTH;
							break;
						}
					}
				}
			}
		}
	}
	
	public void movePlayerWithAutonomous(Controllable c) {
		/*If resting on a moving surface, the player will move in the same direction as the 
		 *moving surface.*/
		if (c.isOnMovingSurfaceBottom()) {
			switch (c.getAdjacentAutonomous().getDirection()) {
				case EAST:
					c.setXLoc(c.getXLoc() + c.getAdjacentAutonomous().getIncr());
					break;
						
				case WEST:
					c.setXLoc(c.getXLoc() - c.getAdjacentAutonomous().getIncr());
					break;
						
				case NORTH:
					c.setYLoc(c.getYLoc() - c.getAdjacentAutonomous().getIncr());
					break;
						
				case SOUTH:
					c.setYLoc(c.getYLoc() + c.getAdjacentAutonomous().getIncr());
					break;
						
				default:
					break;
			}
		}
	}
	
	public void respondToMovingSurfaces(Controllable c) {
		if (c.isAgainstMovingSurfaceLeft() && c.getAdjacentAutonomous().getDirection() == Direction.EAST) {
			c.setXLoc(c.getXLoc() + 1);
		}
		if (c.isAgainstMovingSurfaceRight() && c.getAdjacentAutonomous().getDirection() == Direction.WEST) {
			c.setXLoc(c.getXLoc() - 1);
		}	
		if (c.isAgainstMovingSurfaceTop() && c.getAdjacentAutonomous().getDirection() == Direction.SOUTH) {
			c.setYLoc(c.getYLoc() + 1);
		}
		if (c.isAgainstMovingSurfaceBottom() && c.getAdjacentAutonomous().getDirection() == Direction.NORTH) {
			c.setYLoc(c.getYLoc() - 1);
			c.setAgainstMovingSurfaceBottom(false);
		}
	}
	
	/*Methods for checking collisions with other models*/
	public void checkLeavingSurface(Controllable c) {
		boolean player = this.isPlayer(c);
		
		if (c.isOnSurfaceBottom() || c.isOnMovingSurfaceBottom()) {
			boolean contact = false;
			boolean movingContact = false;
			/*Check against every model that acts as a surface.*/
			for (Model m : this.currEnvironment) {
				if (m instanceof models.Autonomous) {
					if (this.checkBottomSurface(m, c)) {
						movingContact = true;
					}
				} else if (m instanceof SolidObject || m instanceof Platform || m instanceof Controllable) {
					if (m instanceof Controllable) {
						if (this.isPlayer((models.Controllable) m) && this.isPlayer(c)) {
							continue; //skip the case where c is the player and m is the player
						}
					}
					if (this.checkBottomSurface(m, c) || c.getYLoc() + c.getHeight() + 1 > this.currRoom.getYLoc()) {
						contact = true;
					}
				} 
			}
			/*If there were no environmental collisions, reset floating counter and update the appropriate boolean.*/
			if (!contact) {
				c.setOnPlatform(false);
				if (player) {
					this.floatingCounter = 0;
				}
				c.setOnSurfaceBottom(false);
			}
			if (!movingContact) {
				if (player) {
					this.floatingCounter = 0;
				}
				c.setOnMovingSurfaceBottom(false);
				c.setAgainstMovingSurfaceBottom(false);
			}
		}
	}
	
	public void checkBottomEdgeCollisions(Controllable c) {
		boolean player = this.isPlayer(c);
		
		/*Check to see if we're on the bottom of the map.*/
		if (c.getYLoc() + c.getHeight() + 1 > this.currRoom.getYLoc()) {
			c.setOnSurfaceBottom(true);
		}
		/*Check against every model that acts as a surface.*/
		for (Model m : this.currEnvironment) {
			if (m instanceof SolidObject || m instanceof Platform || m instanceof Controllable) {
				if (this.checkBottomSurface(m, c)) {
					if (player) {
						this.floatingCounter = 0;
					}
					if (m instanceof models.Autonomous) {
						c.setAdjacentAutonomous((Autonomous) m);
						switch (((models.Autonomous) m).getDirection()) {
							case NORTH:
								c.setAgainstMovingSurfaceBottom(true);
								c.setOnMovingSurfaceBottom(false);
								break;
							default:
								c.setOnMovingSurfaceBottom(true);
								c.setAgainstMovingSurfaceBottom(false);
								break;
						}
					} else if (m instanceof Controllable) {
						if (this.isPlayer((models.Controllable) m) && this.isPlayer(c)) {
							continue;
						}
						c.setOnSurfaceBottom(true);
						c.setNewBody((models.Controllable) m);
					} else {
						c.setOnSurfaceBottom(true);
						if (m instanceof Platform) {
							c.setOnPlatform(true);
						}
					}
				}
			}
		}
		
		if ((c.isOnSurfaceBottom() || c.isOnMovingSurfaceBottom()) && this.jumpingCounter == 0 && player) {
			this.jumpCount = 0;
		}
	}
	
	public void checkTopEdgeCollisions(Controllable c) {
		int px = c.getXLoc();
		int py = c.getYLoc();
		int pw = c.getWidth();
		
		boolean newCollision = false;
		
		/*Check yloc to see if we've hit the ceiling (valid y locations will range from map.getGroundLevel() 
		 *to map.getGroundLevel() - map.getHeight(), since y = 0 is the top of the screen.*/
		if (py - 1 < this.currRoom.getYLoc() - this.currRoom.getHeight()) {
			c.setAgainstSurfaceTop(true);
			newCollision = true;
		}
		/*Check exits to see if we're leaving the room.*/
		loop:
		for (Exit e : this.currRoom.getRoomLinks()) {
			int x = e.getXLoc();
			int y = e.getYLoc();
			int w = e.getWidth();
			switch (e.getDirection()) {
				case NORTH:
					if (px >= x && px + pw <= x + w && py == y) {
						c.setAdjacentExit(e);
						break loop;
					} else {
						c.setAdjacentExit(null);
					}
					break;
					
				default:
					break;
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Model m : this.currEnvironment) {
			if (m instanceof SolidObject || m instanceof Controllable) {
				if (m instanceof Controllable) {
					if (this.isPlayer((models.Controllable) m) && this.isPlayer(c)) {
						continue;
					}
				}
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (py <= y + h && py >= y) {
					for (int i = x - pw + 1; i < x + w; i++) {
						if (c.getXLoc() == i) {
							newCollision = true;
							if (m instanceof models.Autonomous) {
								c.setAdjacentAutonomous((Autonomous) m);
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
		int px = c.getXLoc();
		int py = c.getYLoc();
		int pw = c.getWidth();
		
		boolean newCollision = false;
		
		/*Check xloc to see if we're at the right edge of the map.*/
		if (px + pw + 1 > this.currRoom.getXLoc() + this.currRoom.getWidth()) {
			c.setAgainstSurfaceRight(true);
			newCollision = true;
		}
		/*Check exits to see if we're leaving the room.*/
		loop:
		for (Exit e : this.currRoom.getRoomLinks()) {
			int x = e.getXLoc();
			int y = e.getYLoc();
			int h = e.getHeight();
			switch (e.getDirection()) {
				case EAST:
					if (py <= y && py > y - h && px + pw == x) {
						c.setAdjacentExit(e);
						break loop;
					} else {
						c.setAdjacentExit(null);
					}
					break;
					
				default:
					break;
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Model m : this.currEnvironment) {
			if (m instanceof SolidObject || m instanceof Controllable) {
				if (m instanceof Controllable) {
					if (this.isPlayer((models.Controllable) m) && this.isPlayer(c)) {
						continue;
					}
				}
				int x = m.getXLoc();
				int y = m.getYLoc();
				int h = m.getHeight();
				if (px + pw == x) {
					for (int i = y - c.getHeight() + 1; i < y + h; i++) {
						if (py == i) {
							newCollision = true;
							if (m instanceof models.Autonomous) {
								c.setAdjacentAutonomous((Autonomous) m);
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
		int px = c.getXLoc();
		int py = c.getYLoc();
		
		boolean newCollision = false;
		
		/*Check xloc to see if we're at the left edge of the map.*/
		if (px - 1 < this.currRoom.getXLoc()) {
			c.setAgainstSurfaceLeft(true);
			newCollision = true;
		}
		/*Check exits to see if we're leaving the room.*/
		loop:
		for (Exit e : this.currRoom.getRoomLinks()) {
			int x = e.getXLoc();
			int y = e.getYLoc();
			int h = e.getHeight();
			switch (e.getDirection()) {
				case WEST:
					if (py <= y && py > y - h && px == x) {
						c.setAdjacentExit(e);
						break loop;
					} else {
						c.setAdjacentExit(null);
					}
					break;
				
				default:
					break;
			}
		}
		/*Check against every model that acts as a surface.*/
		for (Model m : this.currEnvironment) {
			if (m instanceof SolidObject || m instanceof Controllable) {
				if (m instanceof Controllable) {
					if (this.isPlayer((models.Controllable) m) && this.isPlayer(c)) {
						continue;
					}
				}
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				if (px == x + w) {
					for (int i = y - c.getHeight() + 1; i < y + h; i++) {
						if (py == i) {
							newCollision = true;
							if (m instanceof models.Autonomous) {
								c.setAdjacentAutonomous((Autonomous) m);
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
	
	public void checkLeavingRoom(Controllable c, Direction d) {
		Exit e = c.getAdjacentExit();
		if (e != null) {
			if (e.getDirection() == d) {
				this.roomChangeEvent = true;
				this.destinationRoomID = e.getNextRoom();
				this.directionOfRoomChangeEvent = e.getDirection();
			}
		}
	}
	
	public void checkAllEdges(Controllable c) {
		this.checkBottomEdgeCollisions(c);
		this.checkTopEdgeCollisions(c);
		this.checkRightEdgeCollisions(c);
		this.checkLeftEdgeCollisions(c);
	}
	
	public boolean checkBottomSurface(Model m, Controllable c) {
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
		for (Model m : this.currEnvironment) {
			if (m instanceof EventArea) {
				int x = m.getXLoc();
				int y = m.getYLoc();
				int w = m.getWidth();
				int h = m.getHeight();
				/*Check every point of the hitbox against every point of the model.*/
				loop:
				for (int i = x; i < x + w; i++) {
					for (int j = y; j < y + h; j++) {
						for (int k = c.getXLoc(); k < c.getXLoc() + c.getWidth(); k++) {
							for (int l = c.getYLoc(); l < c.getYLoc() + c.getHeight(); l++) {
								if (i == k && j == l) {
									/*Currently, if an enemy and regen area/current occupy the same area, the
									 *enemy takes precedence.*/
									if (m instanceof models.Enemy) {
										this.enemyCollision = true;
										this.healthDecreaseEnemy = ((models.Enemy)m).getDamage();
										break loop;
									} else if (m instanceof models.DamageArea) {
										damageCollision = true;
										this.healthDecreaseDamArea = ((models.DamageArea)m).getHealthDecr();
										break loop;
									} else if (m instanceof models.RegenArea) {
										regenCollision = true;
										this.healthIncrease = ((models.RegenArea)m).getHealthIncr();
										break loop;
									} else if (m instanceof models.Force) {
										forceCollision = true;
										f = (models.Force) m;
										break loop;
									}
								}
							}
						}
					}
				}
			}
		}
		
		/*Evaluate area collisions*/
		if (this.enemyCollision) {
			if (!this.damageDealt) {
				/*Deal the appropriate amount of damage, and only once.*/
				c.setCurrHealth(c.getCurrHealth() - this.healthDecreaseEnemy);
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
		if (damageCollision) {
			if (c.getCurrHealth() >= 0) {
				c.setCurrHealth(c.getCurrHealth() - this.healthDecreaseDamArea);
			}
		}
		if (regenCollision) {
			/*Don't give the player more than the maximum health.*/
			if (c.getCurrHealth() < c.getMaxHealth()) {
				if (c.getCurrHealth() + this.healthIncrease > c.getMaxHealth()) {
					c.setCurrHealth(c.getMaxHealth());
				} else {
					c.setCurrHealth(c.getCurrHealth() + this.healthIncrease);
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
		for (Model m : this.currEnvironment) {
			if (m instanceof models.Enemy) {
				((models.Enemy) m).move();
			} else if (m instanceof models.Autonomous) {
				this.moveAutonomous((models.Autonomous) m, c);
			} else if (m instanceof models.Controllable) {
				Controllable ce = (models.Controllable) m;
				if (this.isPlayer(ce) && this.isPlayer(c)) {
					continue;
				}
				this.checkBottomEdgeCollisions(ce);
				this.assertGravity(ce);
				this.checkLeavingSurface(ce);
			}
		}
	}
	
	public void moveAutonomous(Autonomous a, Controllable c) {
		switch (a.getDirection()) {
			case EAST:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setXLoc(a.getXLoc() + 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkAllEdges(c);
					this.respondToMovingSurfaces(c);
				}
				a.setCurrSegment(0);
				this.movePlayerWithAutonomous(c);
				if (a.getXLoc() >= a.getMoveThreshR()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			case WEST:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setXLoc(a.getXLoc() - 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkAllEdges(c);
					this.respondToMovingSurfaces(c);
				}
				a.setCurrSegment(0);
				this.movePlayerWithAutonomous(c);
				if (a.getXLoc() <= a.getMoveThreshL()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			case NORTH:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setYLoc(a.getYLoc() - 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkAllEdges(c);
					this.respondToMovingSurfaces(c);
				}
				a.setCurrSegment(0);
				this.movePlayerWithAutonomous(c);
				if (a.getYLoc() <= a.getMoveThreshU()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			case SOUTH:
				while (a.getCurrSegment() < a.getIncr()) {
					a.setYLoc(a.getYLoc() + 1);
					a.setCurrSegment(a.getCurrSegment() + 1);
					this.checkAllEdges(c);
					this.respondToMovingSurfaces(c);
				}
				a.setCurrSegment(0);
				this.movePlayerWithAutonomous(c);
				if (a.getYLoc() >= a.getMoveThreshD()) {
					a.setLastDirection(a.getDirection());
					a.setDirection(Direction.IDLE);
				}
				break;
				
			/*Autonomous objects wait for a moment before switching directions.*/
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
		
		while (!this.currEnvironment.isEmpty()) {
			this.currEnvironment.remove(0);
		}
		this.currEnvironment.addAll(this.currRoom.getEnvironment());
		
		/*Add the player to the environment*/
		this.currEnvironment.add(this.player);
	}
	
	public void changeRoom(Controllable c) {
		/*Make the current room*/
		this.currRoomID = this.destinationRoomID;
		this.makeCurrRoom();
		
		/*Offset the player from the doorway*/
		switch (this.directionOfRoomChangeEvent) {
			case NORTH:
				c.setYLoc(this.currRoom.getYLoc() - c.getHeight());
				c.setCurrYSegment(0);
				
				c.setOnSurfaceBottom(true);
				
				this.floatingCounter = 0;
				
				this.jumpCount = 0;
				this.jumpingCounter = 0;
				this.jumping = false;
				break;
				
			case SOUTH:
				c.setYLoc(c.getYLoc() + c.getHeight());
				c.setCurrYSegment(0);
				break;
				
			case EAST:
				c.setXLoc(c.getXLoc() + c.getWidth());
				c.setCurrXSegment(0);
				break;
				
			case WEST:
				c.setXLoc(c.getXLoc() - c.getWidth());
				c.setCurrXSegment(0);
				break;
				
			default:
				break;
		}
	}
	
	/*Change bodies*/
	public void changeBody() {
		Controllable newBody = this.player.getAdjacentControllable();
		if (newBody != null) {
			/*Remove old model from current environment and room environment*/
			this.currEnvironment.remove(newBody);
			this.currMap.accessRoomEnvs(this.currRoomID).remove(newBody);
			
			/*Make a copy of the player controllable in order to set all variables back to starting state*/
			Controllable oldCopy = Controllable.makeCopy(this.player); 
			
			/*Remove the player from the environment*/
			this.currEnvironment.remove(this.player);
			
			/*Add copy to the current environment and room environment*/
			this.currEnvironment.add(oldCopy);
			this.currMap.accessRoomEnvs(this.currRoomID).add(oldCopy);
			
			/*Make a copy of the body we are switching to*/
			Controllable newCopy = Controllable.makeCopy(newBody);
			
			/*Set player to new body and add to current environment*/
			this.player = newCopy;
			this.currEnvironment.add(newCopy);
		}
	}
}
