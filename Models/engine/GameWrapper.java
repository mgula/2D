package engine;

import java.util.ArrayList;

import java.awt.Color;
import java.io.Serializable;

import map.AreaMap;
import map.Exit;
import models.*;
import enums.Direction;
import enums.GameState;
import enums.MapID;
import enums.RoomID;

/*This class serves as a "wrapper" that uses an engine object on a controllable object. This class
 *is responsible for making maps (essentially just collections of rooms) and calling the necessary engine
 *methods every tick.*/
public class GameWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private GameState lastState;
	
	private int groundLevel = 0;
	
	public static final int RoomDataArrayLength = 4;
	
	private AreaMap map1;
	private RoomID[] map1Rooms = {RoomID.SPAWN, RoomID.EAST1, RoomID.WEST1, RoomID.EAST2, RoomID.WEST2, RoomID.NORTHEAST1};
	
	private AreaMap currMap;
	private MapID currMapID = MapID.MAP1;
	
	public GameWrapper() {
		this.initCurrMap();
		this.initCurrentMapRooms();
	}
	
	public void initCurrentMapRooms() {
		for (RoomID r : this.currMap.getRoomIDs()) {
			
			int[] roomDims = new int[RoomDataArrayLength];
			ArrayList<Model> env = new ArrayList<Model>();
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
					env.add(new Autonomous(2000, -250, 50, 50, Direction.WEST, 900, 4, 100));
					env.add(new Platform(1700, -125, 50));
					env.add(new Platform(1400, -400, 1000));
					env.add(new Platform(2925, -500, 75));
					env.add(new RegenArea(2250, -50, 50, 50, 1));
					env.add(new DamageArea(2350, -50, 50, 50, 1));
					env.add(new EnemyA(2000, -600, 60, 60, Direction.EAST, 500, 4, 25));
					
					env.add(new Controllable(1200, -125, 20, 20, 5, 4, 150, 150, 20, 3, 30));
					env.add(new Controllable(1300, -125, 30, 30, 5, 4, 100, 200, 15, 1, 70));
					
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
					env.add(new Force(3300, -175, 70, 70, Direction.EAST, 1));
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
					
					env.add(new Controllable(650, -125, 20, 20, 5, 4, 150, 150, 30, 3, 50));
					
					env.add(new TextArea(100, -50, 100, 100, "here is some sample dialogue bub", false, 4, Color.CYAN, 100, -300));
					env.add(new TextArea(250, -50, 100, 100, "this here is test dialogue bub", true, 7, Color.GREEN, 250, -300));
					
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
	
	public void tick(GameEngine e, boolean rightPressed, boolean leftPressed, boolean spacePressed, boolean downPressed) {
		/*Check all edges for collisions*/
		e.checkAllEdges(e.getPlayer());
		
		/*Assert gravity on player*/
		e.assertGravity(e.getPlayer());
		
		/*Check moving surfaces*/
		e.respondToMovingSurfaces(e.getPlayer());
		
		/*Move all non-player entities*/
		e.moveAll(e.getPlayer());
		
		/*Check and evaluate area collisions*/
		e.checkAreaCollisions(e.getPlayer());
		
		/*Evaluate user input*/
		if (rightPressed) {
			e.moveRight(e.getPlayer());
			e.checkLeavingRoom(e.getPlayer(), Direction.EAST);
		}
		if (leftPressed) {
			e.moveLeft(e.getPlayer());
			e.checkLeavingRoom(e.getPlayer(), Direction.WEST);
		}
		e.checkLeavingSurface(e.getPlayer());
		if (spacePressed) {
			e.getPlayer().setJumping(true);
		}
		if (downPressed) {
			e.phaseThroughPlatformOrExit(e.getPlayer());
		}
		
		/*Evaluate jumping*/
		e.jumpCheck(e.getPlayer());
		
		/*Check for room changes*/
		if (e.getRoomChangeEvent()) {
			e.changeRoom(e.getPlayer());
		}
		
		/*Check if player died*/
		if (e.getPlayer().getCurrHealth() <= 0) {
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
	
	/*Getters*/
	public AreaMap getCurrMap() {
		return this.map1;
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public GameState getLastState() {
		return this.lastState;
	}
}
