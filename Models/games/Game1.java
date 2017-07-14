package games;

import java.util.ArrayList;

import enums.Direction;
import enums.GameState;
import minigame1Models.*;

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
	private Map currMap;
	private Map map1;
	private Map map2;
	private Map map3;
	private int groundLevel = 578;
	private int mapWidth;
	private int mapHeight;
	private ArrayList<Game1Model> environment;
	
	public void initPlayer() {
		this.player = new Player(this.playerStartingXloc, this.playerStartingYloc, this.playerHeight, this.playerWidth);
	}
	
	public void passEnvironmentAndMapToPlayer() {
		this.player.loadEnvironmentAndMap(this.environment, this.currMap);
	}
	
	public void initDebugLevel() {
		this.playerStartingXloc = 200;
		this.playerStartingYloc = 500;
		this.currMap = new Map(2000, 5000, this.groundLevel);
	}
	
	public void initMap1() {
		this.playerStartingXloc = 200;
		this.playerStartingYloc = 500;
		this.mapWidth = 2700;
		this.mapHeight = 1700;
		
		this.map1 = new Map(this.mapHeight, this.mapWidth, this.groundLevel);
		this.currMap = this.map1;
	}
	
	public void initMap2() {
		this.playerStartingXloc = 0;
		this.playerStartingYloc = this.groundLevel;
		this.mapWidth = 2500;
		this.mapHeight = 1600;
		this.map2 = new Map(this.mapHeight, this.mapWidth, this.groundLevel);
		this.currMap = this.map2;
	}
	
	public void initMap3() {
		this.playerStartingXloc = 0;
		this.playerStartingYloc = this.groundLevel;
		this.mapWidth = 2700;
		this.mapHeight = 1500;
		this.map3 = new Map(this.mapHeight, this.mapWidth, this.groundLevel);
		this.currMap = this.map3;
	}
	
	public void makeDebugStage() {
		this.environment = new ArrayList<Game1Model>();
		this.environment.add(new Sand(424, 377, 58, 71));
		this.environment.add(new Rock(43, 540, 22, 73));
		this.environment.add(new Sand(168, 428, 22, 73));
		this.environment.add(new Rock(781, 312, 101, 210));
		this.environment.add(new Sand(247, 336, 20, 34));
		this.environment.add(new Rock(366, 240, 50, 50));
		this.environment.add(new Sand(387, -104, 69, 134));
		this.environment.add(new Rock(24, -205, 4, 78));
		this.environment.add(new Sand(222, 50, 20, 31));
		this.environment.add(new Sand(652, 122, 1, 1));
		this.environment.add(new Rock(700, 60, 23, 234));
		this.environment.add(new Sand(444, -310, 95, 8));
		this.environment.add(new Sand(421, -410, 28, 39));
		this.environment.add(new Sand(400, -168, 55, 20));
		this.environment.add(new Interactable(1623, 540, 40, 40, Direction.WEST, 200, 5));
		this.environment.add(new Interactable(1809, 340, 33, 27, Direction.EAST, 300, 1));
		this.environment.add(new Interactable(1657, 240, 22, 22, Direction.EAST, 100, 4));
		this.environment.add(new Interactable(1848, 0, 100, 100, Direction.WEST, 200, 2));
		this.environment.add(new Current(2300, 560, 30, 500, Direction.WEST, 5));
		this.environment.add(new Current(2900, 560, 30, 500, Direction.EAST, 10));
	}
	
	public void makeTutStage1() {
		this.environment = null;
		this.environment = new ArrayList<Game1Model>();
	}

	public void makeStage1() {
		this.environment = new ArrayList<Game1Model>();
		this.environment.add(new Debris(450, 475));
		this.environment.add(new Debris(900, 475));
		this.environment.add(new Debris(1200, 400));
		this.environment.add(new Debris(2050, 205));
		this.environment.add(new Sand(1400, 350, 268, 2000));
		this.environment.add(new Sand(2500, 200, 150, 1000));
		this.environment.add(new RegenArea(1450, 300, 50, 50));
		this.environment.add(new EnemyB(725, 400, 50, 50, 100, 2));
		this.environment.add(new EnemyB(1078, 400, 50, 50, 100, 2));
		this.environment.add(new EnemyA(2300, 190, 50, 50, Direction.EAST, 100, 4));
		this.environment.add(new Marker(this.currMap.getWidth(), 0));
	}
	
	public void makeStage2() {
		this.environment = new ArrayList<Game1Model>();
		this.environment.add(new Debris(300, 475));
		this.environment.add(new Sand(500, 345, 223, 199));
		this.environment.add(new Sand(699, 205, 363, 150));
		this.environment.add(new Sand(2350, 100, 528, 900));
		this.environment.add(new Debris(1250, 205));
		this.environment.add(new Debris(1650, 200));
		this.environment.add(new Debris(2050, 195));
		this.environment.add(new RegenArea(750, 155, 50, 50));
		this.environment.add(new EnemyB(1400, 175, 50, 50, 150, 2));
		this.environment.add(new EnemyB(1910, 175, 50, 50, 150, 2));
		this.environment.add(new EnemyA(1660, 145, 50, 50, Direction.EAST, 200, 3));
		/*Instead of one long current, implement many small current strips - much less area to check each tick*/
		for (int i = 500; i < this.currMap.getWidth() - (this.playerWidth - 1)*3; i += (this.playerWidth - 1)) {
			this.environment.add(new Current(i, 568, 50, 1, Direction.WEST, 5));
		}
		/*Instead of drawing the current strips, create CurrentDrawable instances (rectangles) that will be drawn instead.*/
		for (int i = 500; i < 2350; i+=185) {
			this.environment.add(new CurrentDrawable(i, 568));
		}
		this.environment.add(new Marker(850, 0));
		this.environment.add(new Marker(this.currMap.getWidth(), -50));
	}
	
	public void makeStage3() {
		this.environment = new ArrayList<Game1Model>();
		this.environment.add(new Rock(500, 156, 462, 150));
		this.environment.add(new Rock(650, 126, 492, 299));
		this.environment.add(new Rock(949, 236, 382, 1152));
		this.environment.add(new Rock(2100, 126, 401, 301));
		this.environment.add(new Rock(2400, -400, 1018, 800));
		this.environment.add(new RegenArea(2150, 76, 50, 50));
		this.environment.add(new Interactable(425, 340, 50, 50, Direction.NORTH, 184, 4));
		this.environment.add(new Interactable(1500, 186, 50, 50, Direction.EAST, 500, 4));
		this.environment.add(new Interactable(2320, -176, 50, 50, Direction.NORTH, 197, 4));
		this.environment.add(new EnemyA(1500, 92, 50, 50, Direction.EAST, 400, 5));
		this.environment.add(new EnemyA(1500, 92, 50, 50, Direction.WEST, 400, 5));
		this.environment.add(new Marker(this.currMap.getWidth(), -540));
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
		for (Game1Model m : this.environment) {
			if (m instanceof minigame1Models.Enemy) {
				((minigame1Models.Enemy) m).move();
			} else if (m instanceof minigame1Models.Interactable) {
				((minigame1Models.Interactable) m).move(this.player);
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
		/*Check for win condition.*/
		if (this.player.getXloc() == this.currMap.getWidth()) {
			this.lastState = this.gameState;
			this.gameState = GameState.WIN;
			return;
		}
		/*Check for lose condition.*/
		if (this.player.getHealth() <= 0) {
			this.lastState = this.gameState;
			this.gameState = GameState.LOSE;
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
	
	public Map getMap() {
		return this.currMap;
	}
	
	public ArrayList<Game1Model> getEnvironment() {
		return this.environment;
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
