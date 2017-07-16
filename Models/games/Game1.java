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
	private Room baseRoom1;
	private ArrayList<Game1Model> currEnvironment;
	
	public Game1() {
		ArrayList<Game1Model> baseRoom1Env = new ArrayList<Game1Model>();
		baseRoom1Env.add(new Rock(500, 500, 70, 70));
		baseRoom1Env.add(new Rock(1500, 500, 70, 70));
		baseRoom1Env.add(new Interactable(1100, 400, 50, 50, Direction.WEST, 1000, 5));
		
		this.baseRoom1 = new Room(2000, 2000, 578, baseRoom1Env);
	}
	
	public void initRooms() {
		this.currRoom = this.baseRoom1;
		
		
		
		this.currEnvironment = this.currRoom.getEnvironment();
	}
	
	public void initPlayer() {
		this.player = new Player(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth);
	}
	
	public void passEnvironmentAndMapToPlayer() {
		this.player.loadEnvironmentAndMap(this.currEnvironment, this.currRoom);
	}
	
	public void moveRight() {
		this.player.checkRightEdgeCollisions();
		this.player.moveRight();
		this.player.checkLeavingSurface();
	}
	
	public void moveLeft() {
		this.player.checkLeftEdgeCollisions();
		this.player.moveLeft();
		this.player.checkLeavingSurface();
	}
	
	public void assertGravity() {
		this.player.checkBottomEdgeCollisions();
		this.player.assertGravity();
	}
	
	public void checkMovingSurfaces() {
		this.player.checkMovingSurfaces(false);
	}
	
	public void moveAll() {
		for (Game1Model m : this.currEnvironment) {
			if (m instanceof game1Models.Enemy) {
				((game1Models.Enemy) m).move();
			} else if (m instanceof game1Models.Interactable) {
				((game1Models.Interactable) m).move(this.player);
			}
		}
	}
	
	public void evaluateJumping() {
		if (this.jumping) {
			this.player.checkTopEdgeCollisions();
			if (this.player.initiateJumpArc()) {
				this.jumping = false;
			}
		}
	}
	
	public void checkAreaCollisions() {
		this.player.checkAreaCollisions();
		this.player.evaluateAreaCollisions();
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
	
	public Room getMap() {
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
