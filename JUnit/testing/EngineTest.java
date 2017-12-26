package testing;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import engine.*;
import enums.RoomID;
import models.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*Tests include ample comments to narrate exactly what should be happening on any given frame.*/
public class EngineTest {
	GameEngine engine;
	GameWrapper game;
	
	int numTests = 0;
	
	/*Attributes of the player that will vary among tests*/
	int minPlayerDimensions = 1;
	int maxPlayerDimensions = 150;
	int minMoveSpeed = 1;
	int maxMoveSpeed = 15;
	
	/*This might not be necessary but I was getting some cases where System.out.print wasn't showing
	 *anything (maybe it has something to do with the large number of asserts? No idea really).
	 *This is a workaround - printing isn't used by the tests, but printing information is useful
	 *when making the tests. These before and after methods simply set System.out to some temporary output
	 *stream, which seems to print correctly.*/
	private final PrintStream stdout = System.out;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    
    @Before
    public void setUp() throws UnsupportedEncodingException {
    	System.setOut(new PrintStream(this.output, true, "UTF-8"));
    }
    
    @After
    public void cleanUp() {
    	System.setOut(this.stdout);
    	System.out.print(this.output.toString());
    }
	
	public void init() {
		this.engine = new GameEngine();
		this.game = new GameWrapper();
	}
	
	public void setEngine(Controllable c, Room r, ArrayList<Model> e) {
		this.engine.setPlayer(c);
		this.engine.setRoom(r);
		this.engine.setEnvironment(e);
	}
	
	@Test
	public void testBasicMovement() {
		/*Initialize the engine and wrapper*/
		this.init();
		
		/*Make a room that just acts as a box - no environment or room links*/
		Room r = new Room(RoomID.SPAWN, 0, 0, 1000, 1000, new ArrayList<Model>(), new ArrayList<Exit>());
		
		/*Make a controllable object*/
		Controllable c = new Controllable(500, -100, 30, 30, 4, 4, 100, 100);
		
		/*Pass the player, room and environment to the engine*/
		this.setEngine(c, r, new ArrayList<Model>());
		
		/**/
		
		System.out.println("Basic movement test passed.");
	}
	
	@Test
	public void testBasicCollisions() {
		int playerStartX = 0;
		int playerStartY = -200; //should be greater in magnitude than the player's height
		
		int playerHealth = 100;
		
		/*Run the tests on various player sizes - static move speed*/
		int staticMoveSpeed = 4; //pick some random move speed
		for (int i = this.minPlayerDimensions; i <= this.maxPlayerDimensions; i++) {
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, staticMoveSpeed, staticMoveSpeed, playerHealth, playerHealth));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, staticMoveSpeed, staticMoveSpeed, playerHealth, playerHealth));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, staticMoveSpeed, staticMoveSpeed, playerHealth, playerHealth));
			
			this.numTests += 3;
		}
		
		/*Run the tests on various movement speeds - static player size*/
		int staticPlayerSize = 30; //pick some random player dimensions
		for (int i = this.minMoveSpeed; i <= this.maxMoveSpeed; i++) {
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, this.minMoveSpeed, i, playerHealth, playerHealth));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, i, this.minMoveSpeed, playerHealth, playerHealth));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, i, i, playerHealth, playerHealth));
			
			this.numTests += 3;
		}
		
		/*Run the tests on various sizes and movement speeds*/
		for (int i = this.minPlayerDimensions; i <= this.maxPlayerDimensions; i++) {
			for (int j = this.minMoveSpeed; j <= this.maxMoveSpeed; j++) {
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, this.minMoveSpeed, j, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, j, this.minMoveSpeed, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, j, j, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, this.minMoveSpeed, j, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, j, this.minMoveSpeed, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, j, j, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, this.minMoveSpeed, j, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, j, this.minMoveSpeed, playerHealth, playerHealth));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, j, j, playerHealth, playerHealth));
				
				this.numTests += 9;
			}
		}
		
		System.out.println("Basic collision tests passed (" + this.numTests + " run(s)).");
	}
	
	/*These tests checks basic collisions - only the player and non-moving objects*/
	public void basicCollisionTest1(Controllable c) { //rock objects and platform objects
		/*Initialize the engine and wrapper*/
		this.init();
		
		/*Make a small test environment with rocks and a platform*/
		Rock rockA = new Rock(500, -50, 50, 50);
		Rock rockB = new Rock(1000, -150, 50, 100);
		Platform platform = new Platform(1500, -100, 200);
		ArrayList<Model> e = new ArrayList<Model>();
		e.add(rockA);
		e.add(rockB);
		e.add(platform);
		
		/*Make a room that just acts as a box - no environment (we'll set that ourselves) or room links*/
		Room r = new Room(RoomID.SPAWN, 0, 0, 2000, 2000, new ArrayList<Model>(), new ArrayList<Exit>());
		
		/*Pass the player, room and environment to the engine*/
		this.setEngine(c, r, e);
		
		/*Initial tick to get booleans situated*/
		this.simulate(1, false, false, false, false);
		
		/*We should start in the air, with left edge against the left wall*/
		this.assertAllCollisionVariables(c, false, true, false, false, false, false, false, false, false, false);
		
		/*Calculate number of ticks needed for the player to fall to the bottom of the room*/
		int playerBottomEdgeY = c.getYLoc() + c.getHeight();
		int groundY = r.getYLoc();
		
		int distance = getDistance(0, 0, playerBottomEdgeY, groundY); //we don't care about x loc in this instance, just pass 0 for those
		
		int fallFrames = (distance / c.getYIncr()) + 1; //divide by how far the player travels each tick, add a frame to be sure
		
		fallFrames += this.engine.getFloatingThreshold(); //add frames for the floating threshold (player doesn't start falling until that many frames)
		
		/*Execute*/
		this.simulate(fallFrames, false, false, false, false);
		
		/*Player should be on the ground now*/
		this.assertAllCollisionVariables(c, true, true, false, false, false, false, false, false, false, false);
		
		/*Calculate number of ticks needed for player to collide with first rock*/
		int playerRightEdgeX = c.getXLoc() + c.getWidth();
		int rockLeftEdgeX = rockA.getXLoc();
		
		distance = getDistance(playerRightEdgeX, rockLeftEdgeX, 0, 0); //we don't care about y loc in this instance, just pass 0 for those
		
		int moveFrames = (distance / c.getXIncr()) + 1; //divide by how far the player travels each tick, add a frame to be sure
		
		/*Execute*/
		this.simulate(moveFrames, true, false, false, false);
		
		/*Player's right edge should be against the first rock*/
		this.assertAllCollisionVariables(c, true, false, true, false, false, false, false, false, false, false);
		
		/*Now we'll jump over the rock*/
		playerBottomEdgeY = c.getYLoc() + c.getHeight();
		int rockTopEdgeY = rockA.getYLoc();
		
		int distanceToJump = getDistance(0, 0, playerBottomEdgeY, rockTopEdgeY);
		
		int jumpFrames = this.engine.getJumpDuration();

		int distancePerJump = jumpFrames * c.getYIncr();
		
		int numJumps = 0;
		
		if (distanceToJump > distancePerJump) {
			numJumps = 1;
		} else {
			numJumps = (distanceToJump / distancePerJump) + 1;
		}
		
		/*Initiate jump*/
		for (int i = 0; i < numJumps; i++) {
			this.simulate(1, false, false, true, false);
			
			/*Player should be off the ground but still against first rock (or not, if on second jump)*/
			if (i == 0) {
				this.assertAllCollisionVariables(c, false, false, true, false, false, false, false, false, false, false);
			} else {
				this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
			}
			
			/*Execute the rest of the rest of the jump*/
			this.simulate(jumpFrames - 1, false, false, false, false); //subtract a tick for the one that initiated the jump
			
			if (numJumps != 1) { //add the tick back if jumping more than once (let jump counter completely exhaust)
				this.simulate(1, false, false, false, false);
			}
		}
		
		/*Player should now be in the air, touching nothing*/
		this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Move right for one frame*/
		this.simulate(1, true, false, false, false);
		
		/*Calculate frames needed to land on top of the first rock*/
		playerBottomEdgeY = c.getYLoc() + c.getHeight();
		rockTopEdgeY = rockA.getYLoc();
		
		distance = getDistance(0, 0, playerBottomEdgeY, rockTopEdgeY); //we don't care about x loc in this instance, just pass 0 for those
		
		fallFrames = (distance / c.getYIncr()) + 1; //add a frame for good measure
		
		fallFrames += this.engine.getFloatingThreshold() - 1; //subtract a frame for the tick where we moved right
		
		/*Execute*/
		this.simulate(fallFrames, false, false, false, false);
		
		/*Player should now be resting on top of the first rock*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll now move right, off of the rock*/
		int playerLeftEdgeX = c.getXLoc();
		int rockRightEdgeX = rockA.getXLoc() + rockA.getWidth();
		
		distance = getDistance(playerLeftEdgeX, rockRightEdgeX, 0, 0);
		
		moveFrames = (distance / c.getXIncr()) + 1; //divide by how far the player travels each tick, add a frame for good luck
		
		/*Execute*/
		this.simulate(moveFrames, true, false, false, false);
		
		/*Player should now be in the air right of the first rock, touching nothing*/
		this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Wait for player to fall to ground*/
		playerBottomEdgeY = c.getYLoc() + c.getHeight();
		
		distance = getDistance(0, 0, playerBottomEdgeY, groundY); //ground y loc hasn't changed
		
		fallFrames = (distance / c.getYIncr()) + 1;
		
		fallFrames += this.engine.getFloatingThreshold() + 1;
		
		/*Execute*/
		this.simulate(fallFrames, false, false, false, false);
		
		/*There's some cases where the player's left edge may be against rock A. Move right one frame to avoid these cases*/
		this.simulate(1, true, false, false, false);
		
		/*Player should be only touching the ground now*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll now bump into rock A from the left, just to say goodbye*/
		playerLeftEdgeX = c.getXLoc();
		int rockARightEdgeX = rockA.getXLoc() + rockA.getWidth();
		
		distance = getDistance(playerLeftEdgeX, rockARightEdgeX, 0, 0);
		
		moveFrames = (distance / c.getXIncr()) + 1;
		
		/*Execute*/
		this.simulate(moveFrames, false, true, false, false);
		
		/*Player should be against left edge of rock A*/
		this.assertAllCollisionVariables(c, true, true, false, false, false, false, false, false, false, false);
		
		/**/
	}
	
	public void basicCollisionTest2() { //stationary autonomous objects
		this.init();
	}
	
	public void basicCollisionTest3() { //controllable objects
		this.init();
	}
	
	public void basicCollisionTest4() { //force objects
		this.init();
	}
	
	public void basicCollisionTest5() { //damage area objects + regen area objects
		this.init();
	}
	
	/*Methods that tests will be using*/
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
	
	public void simulate(int numFrames, boolean right, boolean left, boolean space, boolean down) {
		for (int i = 0; i < numFrames; i++) {
			this.game.tick(this.engine, right, left, space, down);
		}
	}
	
	public int getDistance(int x1, int x2, int y1, int y2) {
		double x = Math.pow(x2 - x1, 2.0);
		double y = Math.pow(y2 - y1, 2.0);
		double distance = Math.sqrt(x + y);
		return (int)Math.ceil(distance); //always rounds up
	}

	public void printPlayerInfo(Controllable c) {
		System.out.println("x: " + c.getXLoc() + ", y: " + c.getYLoc());
		System.out.println("w: " + c.getWidth() + ", h: " + c.getHeight());
		System.out.println("x incr: " + c.getXIncr() + ", y incr: " + c.getYIncr());
		System.out.flush();
	}
}
