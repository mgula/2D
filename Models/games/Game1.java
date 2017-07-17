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
	private int playerStartingXloc = 200; 
	private int playerStartingYloc = 500;
	private int playerHeight = 40;
	private int playerWidth = 50;
	private boolean jumping = false;
	private boolean firstTime = true;
	private Room currRoom;
	private String currRoomID = "base";
	private String[] roomIDs = {"base", "east", "west"};
	private ArrayList<Game1Model> currEnvironment;
	
	public void makeCurrRoom() {
		ArrayList<Game1Model> env = new ArrayList<Game1Model>();
		switch (this.currRoomID) {
			case "base":
				env.add(new Rock(500, 500, 70, 70));
				env.add(new Rock(500, 1500, 70, 70));
				env.add(new Rock(500, 500, 70, 70));
				
				env.add(new Interactable(800, 400, 50, 50, Direction.WEST, 1000, 5));
				this.currRoom = new Room(this.currRoomID, 2000, 2000, 578, env);
				break;
				
			case "east":
				env.add(new Rock(300, 500, 70, 70));
				this.currRoom = new Room(this.currRoomID, 1000, 1000, 578, env);
				break;
				
			case "west":
				env.add(new Rock(400, 500, 70, 70));
				this.currRoom = new Room(this.currRoomID, 1000, 1000, 578, env);
				break;
				
			default:
				break;
		}
	}
	
	public void initRooms() {
		this.makeCurrRoom();
		
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void initPlayer() {
		this.player = new Player(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth);
	}
	
	public void advanceToRoom(Direction d) {
		switch (d) {
			case EAST:
				this.currRoomID = "east";
				this.makeCurrRoom();
				break;
				
			case WEST:
				this.currRoomID = "west";
				this.makeCurrRoom();
				break;
				
			default:
				break;
		}
	}
	
	public void moveRight() {
		this.player.checkRightEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.moveRight(this.currRoom, this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		/*Check if entering new room*/
		if (this.player.getXloc() > this.currRoom.getWidth()) {
			this.advanceToRoom(Direction.EAST);
		}
	}
	
	public void moveLeft() {
		this.player.checkLeftEdgeCollisions(this.currEnvironment);
		this.player.moveLeft(this.currEnvironment);
		this.player.checkLeavingSurface(this.currEnvironment);
		/*Check if entering new room*/
		if (this.player.getXloc() <= 0) {
			this.advanceToRoom(Direction.WEST);
		}
	}
	
	public void assertGravity() {
		this.player.checkBottomEdgeCollisions(this.currRoom, this.currEnvironment);
		this.player.assertGravity(this.currRoom, this.currEnvironment);
	}
	
	public void checkMovingSurfaces() {
		this.player.checkMovingSurfaces(this.currRoom, this.currEnvironment, false);
	}
	
	public void moveAll() {
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof game1Models.Enemy) {
				((game1Models.Enemy) m).move();
			} else if (m instanceof game1Models.Interactable) {
				((game1Models.Interactable) m).move(this.currRoom, this.currEnvironment, this.player);
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
		this.player.evaluateAreaCollisions(this.currRoom, this.currEnvironment);
	}
	
	public void gameStateCheck() {
		if (this.player.getHealth() <= 0) {
			this.lastState = this.gameState;
			this.gameState = GameState.DEATH;
		}
	}
	
	/*Setters*/
	public void setFirstTime() {
		this.firstTime = false;
	}
	
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
	public boolean getFirstTime() {
		return this.firstTime;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Room getCurrRoom() {
		return this.currRoom;
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
