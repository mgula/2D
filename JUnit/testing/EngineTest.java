package testing;

import java.util.ArrayList;

import engine.*;
import enums.RoomID;
import models.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class EngineTest {
	GameEngine engine;
	GameWrapper game;
	
	Controllable player;
	
	ArrayList<Model> testingEnvironment;
	Room testingRoom;
	RoomID testingRoomID = RoomID.SPAWN;
	
	AreaMap debugMap;
	
	public void init() {
		this.engine = new GameEngine();
		this.game = new GameWrapper();
	}
	
	public void setEngine(Controllable c, Room r, ArrayList<Model> e) {
		this.engine.setPlayer(c);
		this.engine.setRoom(r);
		this.engine.setEnvironment(e);
	}
	
	/*These tests checks basic collisions - only the player and
	 *non moving objects*/
	@Test
	public void basicCollisionTest1() { //rock objects and platform objects
		/*Initialize the engine and wrapper*/
		this.init();
		
		/*Make a small test environment with rocks and a platform*/
		this.testingEnvironment = new ArrayList<Model>();
		this.testingEnvironment.add(new Rock(200, -50, 50, 100));
		this.testingEnvironment.add(new Rock(300, -150, 50, 100));
		this.testingEnvironment.add(new Platform(400, -100, 200));
		
		/*Make a room that just acts as a box - no environment (we'll set that ourselves) or room links*/
		this.testingRoom = new Room(this.testingRoomID, 0, 0, 1000, 1000, new ArrayList<Model>(), new ArrayList<Exit>());
		
		/*Initialize the player*/
		this.player = new Controllable(50, -50, 30, 30, 4, 4, 100, 100);
		
		/*Pass the player, room and environment to the engine*/
		this.setEngine(this.player, this.testingRoom, this.testingEnvironment);
		
		/*Start ticking*/
		this.game.tick(this.engine, false, false, false, false);
		
		/*We start in the air so all variables should be false*/
		this.assertAllCollisionVariables(this.player, false, false, false, false, false, false, false, false, false, false);
		
		/*Calculate number of ticks needed to push player to the bottom of the room*/
		int fallFrames = Math.abs(this.player.getYLoc() + this.player.getHeight() + this.testingRoom.getYLoc());
		fallFrames /= this.player.getYIncr();
		fallFrames += this.engine.getFloatingThreshold();
		
		/*Execute*/
		for (int i = 0; i < fallFrames; i++) {
			this.game.tick(this.engine, false, false, false, false);
			//this.assertAllCollisionVariables(this.player, false, false, false, false, false, false, false, false, false, false);
		}
		
		/*Player should be on bottom of room now*/
		this.assertAllCollisionVariables(this.player, true, false, false, false, false, false, false, false, false, false);
		
		/*Calculate number of ticks needed for player to collide with first rock*/
		int moveFrames = Math.abs(200 - this.player.getXLoc());
		moveFrames /= this.player.getXIncr();
		
		/*Execute*/
		for (int i = 0; i < moveFrames + 1; i++) {
			this.game.tick(this.engine, true, false, false, false);
			//this.assertAllCollisionVariables(this.player, true, false, false, false, false, false, false, false, false, false);
			System.out.println(this.player.getXLoc());
			System.out.flush();
		}
		
		/*Player's right edge should be against the first rock*/
		this.assertAllCollisionVariables(this.player, true, false, true, false, false, false, false, false, false, false);
	}
	
	@Test
	public void basicCollisionTest2() { //stationary autonomous objects
		this.init();
	}
	
	@Test
	public void basicCollisionTest3() { //controllable objects
		this.init();
	}
	
	@Test
	public void basicCollisionTest4() { //force objects
		this.init();
	}
	
	@Test
	public void basicCollisionTest5() { //damage area objects + regen area objects
		this.init();
	}
	
	public void assertAllCollisionVariables(Controllable c, boolean bottom, boolean left, boolean right, boolean top, boolean oMBottom, boolean aMBottom, boolean aMLeft, boolean aMRight, boolean aMTop, boolean plat) {
		assertEquals(bottom, c.isOnSurfaceBottom());
		assertEquals(left, c.isAgainstSurfaceLeft());
		assertEquals(right, c.isAgainstSurfaceRight());
		assertEquals(top, c.isAgainstSurfaceTop());
		assertEquals(oMBottom, c.isOnMovingSurfaceBottom());
		assertEquals(aMBottom, c.isAgainstMovingSurfaceBottom());
		assertEquals(aMLeft, c.isAgainstMovingSurfaceLeft());
		assertEquals(aMRight, c.isAgainstMovingSurfaceRight());
		assertEquals(aMTop, c.isAgainstMovingSurfaceTop());
		assertEquals(plat, c.isOnPlatform());
	}

}
