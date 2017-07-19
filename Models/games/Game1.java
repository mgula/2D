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
	
	private AreaMap map1;
	private String[] map1RoomIDs = {"base", "east", "west"};
	private int[][] roomData;
	
	private Room currRoom;
	private String currRoomID = "base";
	private String lastRoomID = "base";
	private ArrayList<Game1Model> currEnvironment;
	
	public Game1() {
		this.player = new Player(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth);
		
		this.map1 = new AreaMap(4000, 2000, this.map1RoomIDs, "base", this.map1RoomIDs.length);
		this.makeCurrRoom();
	}
	
	public void makeCurrRoom() {
		ArrayList<Game1Model> env = new ArrayList<Game1Model>();
		int roomHeight = 0;
		int roomWidth = 0;
		int roomX = 0;
		int roomY = this.groundLevel;
		switch (this.currRoomID) {
			case "base":
				env.add(new Rock(1500, -100, 70, 70));
				env.add(new Rock(1600, -200, 30, 70));
				env.add(new Rock(2500, -100, 70, 70));
				
				env.add(new Interactable(2000, -250, 50, 50, Direction.WEST, 1000, 10));
				
				roomX = 1000;
				roomHeight = 2000;
				roomWidth = 2000;
				break;
				
			case "east":
				env.add(new Rock(3500, -100, 70, 70));
				
				roomX = 3000;
				roomHeight = 2000;
				roomWidth = 1000;
				break;
				
			case "west":
				env.add(new Rock(500, -100, 70, 70));
				
				roomX = 0;
				roomHeight = 2000;
				roomWidth = 1000;
				break;
				
			default:
				break;
		}
		this.currRoom = new Room(this.currRoomID, roomX, roomY, roomHeight, roomWidth, env);
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void makeBaseRoom() {
		this.currRoomID = "base";
		this.makeCurrRoom();
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void changeRoom(Direction d) {
		this.lastRoomID = this.currRoomID;
		switch (d) {
			case EAST:
				switch (this.currRoomID) {
					case "west":
						this.currRoomID = "base";
						break;
						
					case "base":
						this.currRoomID = "east";
						break;
						
					default:
						break;
				}
				break;
				
			case WEST:
				switch (this.currRoomID) {
					case "east":
						this.currRoomID = "base";
						break;
					
					case "base":
						this.currRoomID = "west";
						break;
					
					default:
						break;
				}
				break;
				
			default:
				break;
		}
		this.makeCurrRoom();
		this.roomChangeEvent = false;
	}
	
	public void checkRoomBoundaries(int curr, int last) {
		if (curr == last) {
			return;
		} else {
			if (last <= 1000 && curr > 1000) {
				this.roomChangeEvent = true;
			} else if (last > 1000 && last <= 3000) {
				if (curr < 1000 || curr > 3000) {
					this.roomChangeEvent = true;
				}
			} else if (last > 3000 && curr <= 3000) {
				this.roomChangeEvent = true;
			}
		}
	}
	
	public void moveRight() {
		int lastX = this.player.getXloc();
		
		this.player.checkRightEdgeCollisions(this.map1.getWidth(), this.currEnvironment);
		this.player.moveRight(this.map1.getWidth(), this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		
		this.checkRoomBoundaries(this.player.getXloc(), lastX);
	}
	
	public void moveLeft() {
		int lastX = this.player.getXloc();
		
		this.player.checkLeftEdgeCollisions(0, this.currEnvironment);
		this.player.moveLeft(0, this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		
		this.checkRoomBoundaries(this.player.getXloc(), lastX);
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
