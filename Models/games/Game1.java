package games;

import java.util.ArrayList;
import java.util.HashSet;

import enums.Direction;
import enums.GameState;
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
	private String[] map1RoomIDs = {"base", "east", "west"};
	
	private AreaMap currMap;
	private Room currRoom;
	private String currRoomID = "base";
	private String lastRoomID = "base";
	private ArrayList<Game1Model> currEnvironment;
	
	public Game1() {
		this.player = new Player(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth);
		
		this.map1 = new AreaMap(4000, 2000, this.map1RoomIDs, "base", this.map1RoomIDs.length);
		
		for (int i = 0; i < this.map1.getRoomIDs().length; i++) {
			int[] roomDims = new int[this.RoomDataArrayLength];
			ArrayList<Game1Model> env = new ArrayList<Game1Model>();
			String[] roomLinks = new String[this.RoomLinksArrayLength];
			switch (this.map1.getRoomIDs()[i]) {
				case "base":
					roomDims[0] = 1000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 2000;
					roomDims[3] = 2000;
					
					env.add(new Rock(1500, -100, 70, 70));
					env.add(new Rock(1600, -200, 30, 70));
					env.add(new Rock(2500, -100, 70, 70));
					env.add(new Interactable(2000, -250, 50, 50, Direction.WEST, 1000, 10));
					
					roomLinks[0] = "west";
					roomLinks[1] = "east";
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				case "east":
					roomDims[0] = 3000;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(3500, -100, 70, 70));
					env.add(new Rock(3250, -250, 50, 50));
					
					roomLinks[0] = "base";
					roomLinks[1] = null;
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				case "west":
					roomDims[0] = 0;
					roomDims[1] = this.groundLevel;
					roomDims[2] = 500;
					roomDims[3] = 1000;
					
					env.add(new Rock(500, -100, 70, 70));
					env.add(new Rock(750, -250, 50, 50));
					
					roomLinks[0] = null;
					roomLinks[1] = "base";
					roomLinks[2] = null;
					roomLinks[3] = null;
					break;
					
				default:
					break;
			}
			this.map1.addRoomData(this.map1.getRoomIDs()[i], roomDims);
			this.map1.addRoomEnv(this.map1.getRoomIDs()[i], env);
			this.map1.addRoomLinks(this.map1.getRoomIDs()[i], roomLinks);
		}
	}
	
	public void makeCurrRoom() {
		this.currRoom = new Room(this.currRoomID, this.map1.accessRoomData(this.currRoomID)[0], this.map1.accessRoomData(this.currRoomID)[1], this.map1.accessRoomData(this.currRoomID)[2], this.map1.accessRoomData(this.currRoomID)[3], this.map1.accessRoomEnvs(this.currRoomID), this.map1.accessRoomLinks(this.currRoomID)[0], this.map1.accessRoomLinks(this.currRoomID)[1], this.map1.accessRoomLinks(this.currRoomID)[2], this.map1.accessRoomLinks(this.currRoomID)[3]);
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void makeBaseRoom() {
		this.currMap = this.map1;
		this.currRoomID = this.map1.getStartRoomID();
		this.makeCurrRoom();
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
		this.player.checkRightEdgeCollisions(this.map1.getWidth(), this.currEnvironment);
		this.player.moveRight(this.map1.getWidth(), this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		
		this.checkRoomBoundaries();
	}
	
	public void moveLeft() {
		this.player.checkLeftEdgeCollisions(0, this.currEnvironment);
		this.player.moveLeft(0, this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		
		this.checkRoomBoundaries();
	}
	
	public void assertGravity() {
		this.player.checkBottomEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.assertGravity(this.map1.getWidth(), 0, this.currRoom, this.currEnvironment);
	}
	
	public void checkMovingSurfaces() {
		this.player.checkMovingSurfaces(this.map1.getWidth(), 0, this.currRoom, this.currEnvironment, false);
	}
	
	public void moveAll() {
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof game1Models.Enemy) {
				((game1Models.Enemy) m).move();
			} else if (m instanceof game1Models.Interactable) {
				((game1Models.Interactable) m).move(this.map1.getWidth(), 0, this.currRoom, this.currEnvironment, this.player);
			}
		}
	}
	
	public void evaluateJumping() {
		if (this.jumping) {
			this.player.checkTopEdgeCollisions(this.currRoom, this.currEnvironment);
			if (this.player.initiateJumpArc(this.currRoom, this.currEnvironment)) {
				this.jumping = false;
			}
		}
	}
	
	public void checkAreaCollisions() {
		this.player.checkAreaCollisions(this.currEnvironment);
		this.player.evaluateAreaCollisions(this.map1.getWidth(), 0, this.currRoom, this.currEnvironment);
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
	
	public String getCurrRoomID() {
		return this.currRoomID;
	}
	
	public String getLastRoomID() {
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
