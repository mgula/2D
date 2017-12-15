package games;

import java.util.ArrayList;
import java.io.Serializable;

import enums.Direction;
import enums.GameState;
import enums.MapID;
import enums.RoomID;
import game1Models.*;

public class Game1 implements Game, Serializable {
	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private GameState lastState;
	
	private int groundLevel = 0;
	
	private GameEngine engine = new GameEngine();
	
	private Controllable player;
	
	private boolean roomChangeEvent = false;
	
	private final int RoomDataArrayLength = 4;
	
	private RoomID destinationRoomID;
	private int changeRoomOffsetX;
	private int changeRoomOffsetY;
	
	private AreaMap map1;
	private RoomID[] map1Rooms = {RoomID.SPAWN, RoomID.EAST1, RoomID.WEST1, RoomID.EAST2, RoomID.WEST2, RoomID.NORTHEAST1};
	
	private AreaMap currMap;
	private MapID currMapID = MapID.MAP1;
	private Room currRoom;
	private RoomID currRoomID = RoomID.SPAWN;
	private RoomID lastRoomID = RoomID.SPAWN;
	private ArrayList<Game1Model> currEnvironment;
	
	public Game1() {
		this.initCurrMap();
		
		this.player = this.engine.getPlayer();
		
		this.initCurrentMapRooms();
		
		this.makeCurrRoom();
		
		/*Update the engine with the current room and environment*/
		this.engine.setEnvironment(this.currEnvironment);
		this.engine.setRoom(this.currRoom);
	}
	
	public void initCurrentMapRooms() {
		for (RoomID r : this.currMap.getRoomIDs()) {
			
			int[] roomDims = new int[this.RoomDataArrayLength];
			ArrayList<Game1Model> env = new ArrayList<Game1Model>();
			ArrayList<Exit> roomLinks = new ArrayList<Exit>();
			
			switch (r) {
				case SPAWN:
					roomDims[0] = 1000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 2000;
					roomDims[3] = 2000;
					
					env.add(new Rock(1500, -125, 70, 70));
					env.add(new Rock(1600, -200, 30, 70));
					env.add(new Rock(2500, -125, 70, 70));
					env.add(new Rock(1500, -550, 70, 70));
					env.add(new Rock(2500, -550, 70, 70));
					env.add(new Autonomous(2000, -250, 50, 50, Direction.WEST, 1000, 10));
					env.add(new Platform(1700, -125, 50));
					env.add(new Platform(1400, -400, 1000));
					env.add(new Platform(2925, -500, 75));
					env.add(new RegenArea(2250, -50, 50, 50, 1));
					env.add(new DamageArea(2350, -50, 50, 50, 1));
					env.add(new EnemyA(2000, -600, 60, 60, Direction.EAST, 500, 5, 25));
					
					roomLinks.add(new Exit(RoomID.SPAWN, RoomID.WEST1, Direction.WEST, 1000, this.groundLevel, 50));
					roomLinks.add(new Exit(RoomID.SPAWN, RoomID.EAST1, Direction.EAST, 1000 + 2000, this.groundLevel, 50));
					roomLinks.add(new Exit(RoomID.SPAWN, RoomID.NORTHEAST1, Direction.EAST, 1000 + 2000, -500, 50));
					break;
					
				case EAST1:
					roomDims[0] = 3000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(3500, -125, 70, 70));
					env.add(new Rock(3250, -250, 50, 50));
					
					
					roomLinks.add(new Exit(RoomID.EAST1, RoomID.SPAWN, Direction.WEST, 3000, this.groundLevel, 50));
					roomLinks.add(new Exit(RoomID.EAST1, RoomID.EAST2, Direction.EAST, 3000 + 1000, this.groundLevel, 50));
					roomLinks.add(new Exit(RoomID.EAST1, RoomID.NORTHEAST1, Direction.NORTH, 3250, -500, 50));
					break;
					
				case WEST1:
					roomDims[0] = 0;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(500, -125, 70, 70));
					env.add(new Rock(750, -250, 50, 50));
					
					roomLinks.add(new Exit(RoomID.WEST1, RoomID.SPAWN, Direction.EAST, 1000, this.groundLevel, 50));
					roomLinks.add(new Exit(RoomID.WEST1, RoomID.WEST2, Direction.WEST, 0, this.groundLevel, 50));
					break;
					
				case EAST2:
					roomDims[0] = 4000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(4500, -125, 70, 70));
					env.add(new Rock(4750, -250, 50, 50));
					
					roomLinks.add(new Exit(RoomID.EAST2, RoomID.EAST1, Direction.WEST, 4000, this.groundLevel, 50));
					break;
					
				case WEST2:
					roomDims[0] = -1000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(-500, -125, 70, 70));
					env.add(new Rock(-750, -250, 50, 50));
					
					roomLinks.add(new Exit(RoomID.WEST2, RoomID.WEST1, Direction.EAST, 0, this.groundLevel, 50));
					break;
					
				case NORTHEAST1:
					roomDims[0] = 3000;
					roomDims[1] = -500;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(3200, -750, 70, 70));
					
					roomLinks.add(new Exit(RoomID.NORTHEAST1, RoomID.SPAWN, Direction.WEST, 3000, -500, 50));
					roomLinks.add(new Exit(RoomID.NORTHEAST1, RoomID.EAST1, Direction.SOUTH, 3250, -500, 50));
					
					break;
				default:
					break;
			}
			this.currMap.addRoomData(r, roomDims);
			this.currMap.addRoomEnv(r, env);
			this.currMap.addRoomLinks(r, roomLinks);
		}
	}
	
	public void respawn() {
		this.currRoomID = RoomID.SPAWN;
		this.makeCurrRoom();
		
		/*Update the engine with the current room and environment*/
		this.engine.setEnvironment(this.currEnvironment);
		this.engine.setRoom(this.currRoom);
		
		this.engine.respawn(this.player);
	}
	
	public void initCurrMap() {
		switch (this.currMapID) {
			case MAP1:
				this.map1 = new AreaMap(MapID.MAP1, this.map1Rooms);
				this.currMap = this.map1;
				break;
			default:
				break;
		}
	}
	
	public void makeCurrRoom() {
		this.currRoom = new Room(this.currRoomID, this.currMap.accessRoomData(this.currRoomID)[0], this.currMap.accessRoomData(this.currRoomID)[1], this.currMap.accessRoomData(this.currRoomID)[2], this.currMap.accessRoomData(this.currRoomID)[3], this.currMap.accessRoomEnvs(this.currRoomID), this.currMap.accessRoomLinks(this.currRoomID));
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void changeRoom() {
		/*Make the current room*/
		this.currRoomID = this.destinationRoomID;
		this.makeCurrRoom();
		
		/*Update the engine with the current room and environment*/
		this.engine.setEnvironment(this.currEnvironment);
		this.engine.setRoom(this.currRoom);
		
		/*Offset the player from the doorway*/
		this.player.setXLoc(this.player.getXLoc() + this.changeRoomOffsetX);
		this.player.setYLoc(this.player.getYLoc() + this.changeRoomOffsetY);
	}
	
	public void checkRoomBoundaries() {
		int px = this.player.getXLoc();
		int py = this.player.getYLoc();
		
		for (Exit e : this.currMap.accessRoomLinks(this.currRoomID)) {
			int x = e.getXLoc();
			int y = e.getYLoc();
			int h = e.getHeight();
			int w = e.getWidth();
			switch (e.getDirection()) {
				case NORTH:
					if (px >= x && px + this.player.getWidth() <= x + w) {
						if (py < y) {
							this.roomChangeEvent = true;
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = 0;
							this.changeRoomOffsetY = -1 * (this.player.getHeight() + 1); //push rest of player through door (to avoid erroneously triggering more room change events)
						}
					}
					break;
					
				case SOUTH:
					if (px >= x && px + this.player.getWidth() <= x + w) {
						if (py + this.player.getHeight() > y) {
							this.roomChangeEvent = true;
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = 0;
							this.changeRoomOffsetY = this.player.getHeight() + 1;
						}
					}
					break;
					
				case EAST:
					if (py <= y && py > y - h) {
						if (px + this.player.getWidth() >= x) {
							this.roomChangeEvent = true;
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = this.player.getWidth() + 1;
							this.changeRoomOffsetY = 0;
						}
					}
					break;
					
				case WEST:
					if (py <= y && py > y - h) {
						if (px <= x) {
							this.roomChangeEvent = true;
							this.destinationRoomID = e.getNextRoom();
							this.changeRoomOffsetX = -1 * (this.player.getWidth() + 1);
							this.changeRoomOffsetY = 0;
						}
					}
					break;
				
				default:
					break;
			}
		}
	}
	
	public void tick(boolean rightPressed, boolean leftPressed, boolean spacePressed, boolean downPressed) {
		/*Assert gravity*/
		this.engine.checkBottomEdgeCollisions(this.player);
		this.engine.assertGravity(this.player);
		
		/*Check moving surfaces*/
		this.engine.checkMovingSurfaces(false, player);
		
		/*Move all non-player entities*/
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof game1Models.Enemy) {
				((game1Models.Enemy) m).move();
			} else if (m instanceof game1Models.Autonomous) {
				this.engine.moveAutonomous((game1Models.Autonomous) m, this.player);
			}
		}
		
		/*Check and evaluate area collisions*/
		this.engine.checkAreaCollisions(this.player);
		this.engine.evaluateAreaCollisions(this.player);
		
		/*Evaluate user input*/
		if (rightPressed) {
			this.engine.checkRightEdgeCollisions(this.player);
			this.engine.moveRight(this.player);
		}
		if (leftPressed) {
			this.engine.checkLeftEdgeCollisions(this.player);
			this.engine.moveLeft(this.player);
		}
		this.engine.checkLeavingSurface(this.player);
		
		if (spacePressed) {
			this.engine.setJumping(true);
		}
		if (downPressed) {
			this.engine.phaseThroughPlatformOrExit(this.player);
			this.engine.checkMovingSurfaces(false, player);
			this.engine.checkBottomEdgeCollisions(this.player);
		}
		
		/*Evaluate jumping*/
		if (this.engine.getJumping()) {
			this.engine.checkTopEdgeCollisions(this.player);
			if (!this.engine.initiateJumpArc(this.player)) {
				this.engine.setJumping(false);
			}
		}
		
		/*Check for room changes*/
		this.checkRoomBoundaries();
		
		/*Change room if room changed*/
		if (this.roomChangeEvent) {
			this.changeRoom(); // Main sets roomChangeEvent back to false
		}
		
		/*Check if player died*/
		this.gameStateCheck();
	}
	
	public void gameStateCheck() {
		if (this.player.getHealth() <= 0) {
			this.lastState = this.gameState;
			this.gameState = GameState.DEATH;
		}
	}
	
	/*Setters*/
	public void setGameState(GameState state) {
		this.gameState = state;
	}
	public void setLastState(GameState state) {
		this.lastState = state;
	}
	
	public void setRoomChangeEvent(boolean b) {
		this.roomChangeEvent = b;
	}
	
	/*Getters*/
	public boolean getRoomChangeEvent() {
		return this.roomChangeEvent;
	}
	
	public GameEngine getEngine() {
		return this.engine;
	}
	
	public Controllable getPlayer() {
		return this.player;
	}
	
	public Room getCurrRoom() {
		return this.currRoom;
	}
	
	public AreaMap getCurrMap() {
		return this.map1;
	}
	
	public RoomID getCurrRoomID() {
		return this.currRoomID;
	}
	
	public RoomID getLastRoomID() {
		return this.lastRoomID;
	}
	
	public ArrayList<Game1Model> getEnvironment() {
		return this.currEnvironment;
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public GameState getLastState() {
		return this.lastState;
	}
}
