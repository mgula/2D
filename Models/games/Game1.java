package games;

import java.util.ArrayList;
import java.util.HashSet;

import enums.Direction;
import enums.GameState;
import enums.MapID;
import enums.RoomID;
import game1Models.*;

public class Game1 implements Game {
	private GameState gameState = GameState.UNINITIALIZED;
	private GameState lastState = GameState.UNINITIALIZED;
	private Player player;
	private int playerStartingXloc = 2000; 
	private int playerStartingYloc = -100;
	private int playerHeight = 40;
	private int playerWidth = 40;
	private int groundLevel = 0;
	private boolean jumping = false;
	private boolean roomChangeEvent = false;
	
	private final int RoomDataArrayLength = 4;
	private final int RoomLinksArrayLength = 4;
	
	private AreaMap map1;
	private RoomID[] map1Rooms = {RoomID.SPAWN, RoomID.EAST1, RoomID.WEST1, RoomID.EAST2, RoomID.WEST2};
	
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
			RoomID[] roomLinks = new RoomID[this.RoomLinksArrayLength];
			
			switch (r) {
				case SPAWN:
					roomDims[0] = 1000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 2000;
					roomDims[3] = 2000;
					
					env.add(new Rock(1500, -125, 70, 70));
					env.add(new Rock(1600, -200, 30, 70));
					env.add(new Rock(2500, -125, 70, 70));
					env.add(new Interactable(2000, -250, 50, 50, Direction.WEST, 1000, 10));
					env.add(new Platform(1700, -125, 50));
					env.add(new RegenArea(2250, -50, 50, 50, 1));
					env.add(new DamageArea(2350, -50, 50, 50, 1));
					
					roomLinks[0] = RoomID.WEST1;
					roomLinks[1] = RoomID.EAST1;
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				case EAST1:
					roomDims[0] = 3000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(3500, -125, 70, 70));
					env.add(new Rock(3250, -250, 50, 50));
					
					roomLinks[0] = RoomID.SPAWN;
					roomLinks[1] = RoomID.EAST2;
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				case WEST1:
					roomDims[0] = 0;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(500, -125, 70, 70));
					env.add(new Rock(750, -250, 50, 50));
					
					roomLinks[0] = RoomID.WEST2;
					roomLinks[1] = RoomID.SPAWN;
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				case EAST2:
					roomDims[0] = 4000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(4500, -125, 70, 70));
					env.add(new Rock(4750, -250, 50, 50));
					
					roomLinks[0] = RoomID.EAST1;
					roomLinks[1] = null;
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				case WEST2:
					roomDims[0] = -1000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(-500, -125, 70, 70));
					env.add(new Rock(-750, -250, 50, 50));
					
					roomLinks[0] = null;
					roomLinks[1] = RoomID.WEST1;
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				default:
					break;
			}
			this.currMap.addRoomData(r, roomDims);
			this.currMap.addRoomEnv(r, env);
			this.currMap.addRoomLinks(r, roomLinks);
		}
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
		this.currRoom = new Room(this.currRoomID, this.currMap.accessRoomData(this.currRoomID)[0], this.currMap.accessRoomData(this.currRoomID)[1], this.currMap.accessRoomData(this.currRoomID)[2], this.currMap.accessRoomData(this.currRoomID)[3], this.currMap.accessRoomEnvs(this.currRoomID), this.currMap.accessRoomLinks(this.currRoomID)[0], this.currMap.accessRoomLinks(this.currRoomID)[1], this.currMap.accessRoomLinks(this.currRoomID)[2], this.currMap.accessRoomLinks(this.currRoomID)[3]);
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void changeRoom(Direction d) {
		this.lastRoomID = this.currRoomID;
		switch (d) {
			case EAST:
				this.currRoomID = this.currRoom.getRoomEast();
				break;
				
			case WEST:
				this.currRoomID = this.currRoom.getRoomWest();
				break;
				
			default:
				break;
		}
		this.makeCurrRoom();
		this.roomChangeEvent = false;
	}
	
	public void checkRoomBoundaries() {
		if (this.player.getXLoc() < this.currRoom.getXLoc()) {
			if (this.map1.accessRoomLinks(this.currRoomID)[0] != null) {
				this.roomChangeEvent = true;
			}
		} else if (this.player.getXLoc() > (this.currRoom.getXLoc() + this.currRoom.getWidth())) {
			if (this.map1.accessRoomLinks(this.currRoomID)[1] != null) {
				this.roomChangeEvent = true;
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
		
		this.checkRoomBoundaries();
	}
	
	public void evaluateJumping() {
		if (this.jumping) {
			this.player.checkTopEdgeCollisions(this.currRoom, this.currEnvironment);
			if (!this.player.initiateJumpArc(this.currRoom, this.currEnvironment)) {
				this.jumping = false;
			}
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
