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
	private Player player;
	private int playerStartingXloc = 2000; 
	private int playerStartingYloc = -100;
	private int playerHeight = 40;
	private int playerWidth = 40;
	private int groundLevel = 0;
	private boolean jumping = false;
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
		
		this.player = new Player(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth);
		
		this.initCurrentMapRooms();
		
		this.makeCurrRoom();
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
					env.add(new Interactable(2000, -250, 50, 50, Direction.WEST, 1000, 10));
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
		this.player.respawn(this.playerStartingXloc, this.playerStartingYloc);
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
		this.currRoomID = this.destinationRoomID;
		this.makeCurrRoom();
		
		this.player.setXLoc(this.player.getXLoc() + this.changeRoomOffsetX);
		this.player.setYLoc(this.player.getYLoc() + this.changeRoomOffsetY);
		
		this.roomChangeEvent = false;
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
	
	public void moveRight() {
		this.player.checkRightEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.moveRight(this.currRoom, this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		
		this.checkRoomBoundaries();
	}
	
	public void moveLeft() {
		this.player.checkLeftEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.moveLeft(this.currRoom, this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		
		this.checkRoomBoundaries();
	}
	
	public void checkMovingSurfaces() {
		this.player.checkMovingSurfaces(this.currRoom, this.currEnvironment, false);
	}
	
	public void assertGravity() {
		this.player.checkBottomEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.assertGravity(this.currRoom, this.currEnvironment);
	}
	
	public void phaseThroughPlatformOrExit() {
		this.player.phaseThroughPlatformOrExit(this.currRoom, this.currEnvironment);
		this.checkMovingSurfaces();
		this.player.checkBottomEdgeCollisions(this.currRoom, this.currEnvironment);
		
		this.checkRoomBoundaries();
	}
	
	public void moveAll() {
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof game1Models.Enemy) {
				((game1Models.Enemy) m).move();
			} else if (m instanceof game1Models.Interactable) {
				((game1Models.Interactable) m).move(this.currRoom, this.currEnvironment, this.player);
			}
		}
		
		/*Check player, an interactable may have moved them*/
		this.player.checkRightEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.checkLeftEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.checkBottomEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.checkTopEdgeCollisions(this.currRoom, this.currEnvironment);
		
		this.player.checkLeavingSurface(this.currEnvironment);
	}
	
	public void evaluateJumping() {
		if (this.jumping) {
			this.player.checkTopEdgeCollisions(this.currRoom, this.currEnvironment);
			if (!this.player.initiateJumpArc(this.currRoom, this.currEnvironment)) {
				this.jumping = false;
			}
			this.checkRoomBoundaries();
		}
	}
	
	public void checkAreaCollisions() {
		this.player.checkAreaCollisions(this.currEnvironment);
		this.player.evaluateAreaCollisions(this.currRoom, this.currEnvironment);
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
	
	public void setJumping(boolean b) {
		this.jumping = b;
	}
	
	public void setRoomChangeEvent(boolean b) {
		this.roomChangeEvent = b;
	}
	
	/*Getters*/
	public boolean getRoomChangeEvent() {
		return this.roomChangeEvent;
	}
	
	public Player getPlayer() {
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
	
	public int getPlayerStartingXloc() {
		return this.playerStartingXloc;
	}
	
	public int getPlayerStartingYloc() {
		return this.playerStartingYloc;
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public GameState getLastState() {
		return this.lastState;
	}
}
