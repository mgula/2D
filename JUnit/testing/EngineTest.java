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
	
	/*Attributes of the player that will vary among tests*/
	int minPlayerDimensions = 1;
	int maxPlayerDimensions = 300;
	int minMoveSpeed = 1;
	int maxMoveSpeed = 30;
	
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
    
	@Test
	public void allTests() {
		this.testPlayerMovement();
		this.testBasicCollisions();
	}
    
	@After
	public void cleanUp() {
		System.setOut(this.stdout);
		System.out.print(this.output.toString());
	}
	
	/****************************************************************************************************/
	/***************************************** Movement testing *****************************************/
	/****************************************************************************************************/
	
	public void testPlayerMovement() {
		int numRuns = 0;
    	
		/*Test on a few combinations of movement speeds*/
		for (int i = this.minMoveSpeed; i < this.maxMoveSpeed; i++) {
			this.basicPlayerMovementTest1(new Controllable(1000, -100, this.maxMoveSpeed, i, 3, 3, 100, 100));
			this.basicPlayerMovementTest1(new Controllable(1000, -100, i, this.maxMoveSpeed, 3, 3, 100, 100));
			this.basicPlayerMovementTest1(new Controllable(1000, -100, i, i, 3, 3, 100, 100));
			
			this.basicPlayerMovementTest2(new Controllable(1000, -100, this.maxMoveSpeed, i, 3, 3, 100, 100));
			this.basicPlayerMovementTest2(new Controllable(1000, -100, i, this.maxMoveSpeed, 3, 3, 100, 100));
			this.basicPlayerMovementTest2(new Controllable(1000, -100, i, i, 3, 3, 100, 100));
        	
			numRuns += 6;
		}
    	
		System.out.println("Movements tests passed (" + numRuns + " runs).");
	}
    
	/*Test player moving right, player moving left, and player jumping (no combined input)*/
	public void basicPlayerMovementTest1(Controllable c) {
		/*Initialize the engine and wrapper*/
		this.init();
		
		/*Make a room that just acts as a box - no environment or room links*/
		Room r = new Room(RoomID.SPAWN, 0, 0, 2000, 2000, new ArrayList<Model>(), new ArrayList<Exit>());
		
		/*Pass the player, room and an empty environment to the engine*/
		this.setEngine(c, r, new ArrayList<Model>());
		
		/*Initial tick to get booleans situated*/
		this.simulate(1, false, false, false, false, false);
		
		/*We should start in the air, not touching anything*/
		this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*Player should be on the ground now*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll move right a bit*/
		int moveFrames = 20;
		
		/*Save old x loc*/
		int oldX = c.getXLoc();
		
		/*Execute*/
		this.simulate(moveFrames, true, false, false, false, false);
		
		/*Get new x loc*/
		int newX = oldX + (c.getXIncr() * moveFrames);
		
		/*We should have moved right*/
		assertEquals(newX, c.getXLoc());
		
		/*Now we'll move left - save old x loc first*/
		oldX = c.getXLoc();
		this.simulate(moveFrames, false, true, false, false, false);
		
		/*Get new x loc*/
		newX = oldX - (c.getXIncr() * moveFrames);
		
		/*We should have moved left*/
		assertEquals(newX, c.getXLoc());
		
		/*Now we'll jump - save old y loc first*/
		int oldY = c.getYLoc();
		
		/*Start the jump*/
		this.simulate(1, false, false, true, false, false);
		
		/*At the very least we should be in the air now*/
		this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let the controllable reach the peak of the jump*/
		this.simulate(this.engine.getJumpDuration() - 1, false, false, false, false, false);
		
		/*We should now be at the peak of the jump*/
		int newY = oldY - (c.getYIncr() * this.engine.getJumpDuration());
		
		assertEquals(newY, c.getYLoc());
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*We should be back on the ground*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
	}
	
	/*Test player moving right and left while jumping*/
	public void basicPlayerMovementTest2(Controllable c) {
		/*Initialize the engine and wrapper*/
		this.init();
		
		/*Make a room that just acts as a box - no environment or room links*/
		Room r = new Room(RoomID.SPAWN, 0, 0, 2000, 2000, new ArrayList<Model>(), new ArrayList<Exit>());
		
		/*Pass the player, room and an empty environment to the engine*/
		this.setEngine(c, r, new ArrayList<Model>());
		
		/*Initial tick to get booleans situated*/
		this.simulate(1, false, false, false, false, false);
		
		/*We should start in the air, not touching anything*/
		this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*Player should be on the ground now*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll jump and move right - save old coords first*/
		int oldX = c.getXLoc();
		int oldY = c.getYLoc();
		
		/*Initiate jump + right*/
		this.simulate(1, true, false, true, false, false);
		
		/*Hold right the rest of the way*/
		this.simulate(this.engine.getJumpDuration() - 1, true, false, false, false, false);
		
		/*Calculate new coords*/
		int newX = oldX + (c.getXIncr() * this.engine.getJumpDuration());
		int newY = oldY - (c.getYIncr() * this.engine.getJumpDuration());
		
		/*We should have moved right and should also be at the peak of the jump*/
		assertEquals(newX, c.getXLoc());
		assertEquals(newY, c.getYLoc());
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*Player should be on the ground now*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*Now we'll jump and move left - save old coords first*/
		oldX = c.getXLoc();
		oldY = c.getYLoc();
		
		/*Initiate jump + left*/
		this.simulate(1, false, true, true, false, false);
		
		/*Hold left the rest of the way*/
		this.simulate(this.engine.getJumpDuration() - 1, false, true, false, false, false);
		
		/*Calculate new coords*/
		newX = oldX - (c.getXIncr() * this.engine.getJumpDuration());
		newY = oldY - (c.getYIncr() * this.engine.getJumpDuration());
		
		/*We should have moved left and should also be at the peak of the jump*/
		assertEquals(newX, c.getXLoc());
		assertEquals(newY, c.getYLoc());
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*Player should be on the ground now*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
	}
	
	/****************************************************************************************************/
	/**************************************Basic Collision Testing **************************************/
	/****************************************************************************************************/
	
	/*Test basic collisions (one on one collisions - only the player and one other non-moving object). The
	*sizes of the non-moving objects are static.*/
	public void testBasicCollisions() {
		int numRuns = 0;
		
		int playerStartX = 0;
		int playerStartY = -350; //should be greater in magnitude than the player's height
		
		/*Run the tests on various combinations of player sizes - static move speed*/
		int staticMoveSpeed = 4; //pick some random move speed
		for (int i = this.minPlayerDimensions; i <= this.maxPlayerDimensions; i++) {
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, staticMoveSpeed, staticMoveSpeed, 100, 100));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, staticMoveSpeed, staticMoveSpeed, 100, 100));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, staticMoveSpeed, staticMoveSpeed, 100, 100));
			
			numRuns += 3;
		}
		
		/*Run the tests on various combinations of movement speeds - static player size*/
		int staticPlayerSize = 30; //pick some random player dimensions
		for (int i = this.minMoveSpeed; i <= this.maxMoveSpeed; i++) {
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, this.minMoveSpeed, i, 100, 100));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, i, this.minMoveSpeed, 100, 100));
			this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, i, i, 100, 100));
			
			numRuns += 3;
		}
		
		/*Run the tests on various combinations of sizes and movement speeds*/
		for (int i = this.minPlayerDimensions; i <= this.maxPlayerDimensions; i++) {
			for (int j = this.minMoveSpeed; j <= this.maxMoveSpeed; j++) {
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, this.minMoveSpeed, j, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, j, this.minMoveSpeed, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, j, j, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, this.minMoveSpeed, j, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, j, this.minMoveSpeed, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, j, j, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, this.minMoveSpeed, j, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, j, this.minMoveSpeed, 100, 100));
				this.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, j, j, 100, 100));
				
				numRuns += 9;
			}
		}
		
		System.out.println("Basic collision tests passed (" + numRuns + " runs).");
	}
	
	/*Test collisions with a solid object (rock in this case) and a platform*/
	public void basicCollisionTest1(Controllable c) {
		/*Initialize the engine and wrapper*/
		this.init();
		
		/*This test might require many consecutive jumps - set max jumps to some arbitrary high number*/
		this.engine.setMaxJumps(100);
		
		/*Make a small test environment with rocks and a platform*/
		Rock rockA = new Rock(500, -50, 50, 50);
		Rock rockB = new Rock(1000, -500, 20, 100);
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
		this.simulate(1, false, false, false, false, false);
		
		/*We should start in the air, with left edge against the left wall*/
		this.assertAllCollisionVariables(c, false, true, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*Player should be on the ground now*/
		this.assertAllCollisionVariables(c, true, true, false, false, false, false, false, false, false, false);
		
		/*Calculate number of ticks needed for player to collide with first rock*/
		int playerRightEdgeX = c.getXLoc() + c.getWidth();
		int rockLeftEdgeX = rockA.getXLoc();
		
		int distance = getDistance(playerRightEdgeX, rockLeftEdgeX, 0, 0); //we don't care about y loc in this instance, just pass 0 for those
		
		int moveFrames = (distance / c.getXIncr()) + 1; //divide by how far the player travels each tick, add a frame to be sure
		
		/*Execute*/
		this.simulate(moveFrames, true, false, false, false, false);
		
		/*Player's right edge should be against the first rock*/
		this.assertAllCollisionVariables(c, true, false, true, false, false, false, false, false, false, false);
		
		/*Now we'll jump over the rock*/
		int playerBottomEdgeY = c.getYLoc() + c.getHeight();
		int rockTopEdgeY = rockA.getYLoc();
		
		int distanceToJump = getDistance(0, 0, playerBottomEdgeY, rockTopEdgeY);

		int distancePerJump = this.engine.getJumpDuration() * c.getYIncr();
		
		int numJumps = 0;
		
		if (distanceToJump > distancePerJump) {
			numJumps = 1;
		} else {
			numJumps = (distanceToJump / distancePerJump) + 1;
		}
		
		/*Initiate jump*/
		for (int i = 0; i < numJumps; i++) {
			this.simulate(1, false, false, true, false, false);
			
			/*Player should be off the ground but still against first rock (or not, if on second jump)*/
			if (i == 0) {
				this.assertAllCollisionVariables(c, false, false, true, false, false, false, false, false, false, false);
			} else {
				this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
			}
			
			/*Execute the rest of the rest of the jump*/
			this.simulate(this.engine.getJumpDuration() - 1, false, false, false, false, false); //subtract a tick for the one that initiated the jump
			
			if (numJumps != 1) { //add the tick back if jumping more than once (let jump counter completely exhaust)
				this.simulate(1, false, false, false, false, false);
			}
		}
		
		/*Player should now be in the air, touching nothing*/
		this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Move right for one frame*/
		this.simulate(1, true, false, false, false, false);
		
		/*Calculate frames needed to land on top of the first rock*/
		playerBottomEdgeY = c.getYLoc() + c.getHeight();
		rockTopEdgeY = rockA.getYLoc();
		
		distance = getDistance(0, 0, playerBottomEdgeY, rockTopEdgeY); //we don't care about x loc in this instance, just pass 0 for those
		
		int fallFrames = (distance / c.getYIncr()) + 1; //add a frame for good measure
		
		fallFrames += this.engine.getFloatingThreshold() - 1; //subtract a frame for the tick where we moved right
		
		/*Execute*/
		this.simulate(fallFrames, false, false, false, false, false);
		
		/*Player should now be resting on top of the first rock*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll now move right, off of the rock*/
		int playerLeftEdgeX = c.getXLoc();
		int rockRightEdgeX = rockA.getXLoc() + rockA.getWidth();
		
		distance = getDistance(playerLeftEdgeX, rockRightEdgeX, 0, 0);
		
		moveFrames = (distance / c.getXIncr()) + 1; //divide by how far the player travels each tick, add a frame for good luck
		
		/*Execute*/
		this.simulate(moveFrames, true, false, false, false, false);
		
		/*Player should now be in the air right of the first rock, touching nothing*/
		this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*There's some cases where the player's left edge may be against rock A. Move right one frame to avoid these cases*/
		this.simulate(1, true, false, false, false, false);
		
		/*Player should be only touching the ground now*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll now bump into rock A from the left, just to say goodbye*/
		playerLeftEdgeX = c.getXLoc();
		int rockARightEdgeX = rockA.getXLoc() + rockA.getWidth();
		
		distance = getDistance(playerLeftEdgeX, rockARightEdgeX, 0, 0);
		
		moveFrames = (distance / c.getXIncr()) + 1;
		
		/*Execute*/
		this.simulate(moveFrames, false, true, false, false, false);
		
		/*Player should be against left edge of rock A*/
		this.assertAllCollisionVariables(c, true, true, false, false, false, false, false, false, false, false);
		
		/*Now we'll place the player under the second rock*/
		playerRightEdgeX = c.getXLoc() + c.getWidth();
		rockRightEdgeX = rockB.getXLoc() + rockB.getWidth();
		
		distance = getDistance(playerRightEdgeX, rockRightEdgeX, 0, 0); //we don't care about y loc in this instance, just pass 0 for those
		
		moveFrames = distance / c.getXIncr();
		
		/*Execute*/
		this.simulate(moveFrames, true, false, false, false, false);
		
		/*Player should be underneath the second rock, only touching the ground*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*Now we'll bump our top edge into the second rock's bottom edge*/
		int playerTopEdgeY = c.getYLoc();
		int rockBottomEdgeY = rockB.getYLoc() + rockB.getHeight();
		
		distanceToJump = getDistance(0, 0, playerTopEdgeY, rockBottomEdgeY);
		
		/*We'll need to keep track of some variables in order to hit the bottom edge*/
		int oldY = c.getYLoc();
		int distanceRemaining = distanceToJump;
		
		/*Start to jump*/
		this.simulate(1, false, false, true, false, false);
		
		/*Update distance remaining*/
		distanceRemaining -= (oldY - c.getYLoc());

		/*Keep going until we hit the bottom edge of rock B*/
		while (distanceRemaining > 0) {
			/*Player should be off the ground*/
			this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
			
			oldY = c.getYLoc();
			
			/*Continue to jump*/
			this.simulate(1, false, false, true, false, false);
			
			/*Update distance remaining*/
			distanceRemaining -= (oldY - c.getYLoc());
		}
		/*One last tick*/
		this.simulate(1, false, false, false, false, false);
		
		/*Player's top edge should meet rock B's bottom edge*/
		this.assertAllCollisionVariables(c, false, false, false, true, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		this.fallToGround(c, r);
		
		/*Now we'll place the player under the platform*/
		playerRightEdgeX = c.getXLoc() + c.getWidth();
		int platRightEdgeX = platform.getXLoc() + platform.getWidth();
		
		distance = getDistance(playerRightEdgeX, platRightEdgeX, 0, 0); //we don't care about y loc in this instance, just pass 0 for those
		
		moveFrames = distance / c.getXIncr();
		
		/*Execute*/
		this.simulate(moveFrames, true, false, false, false, false);
		
		/*Player should be underneath the second rock, only touching the ground*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*Lastly, we need to jump up onto the platform*/
		playerBottomEdgeY = c.getYLoc() + c.getHeight();
		
		distanceToJump = getDistance(0, 0, playerBottomEdgeY, platform.getYLoc());

		numJumps = 0;
		
		if (distanceToJump > distancePerJump) {
			numJumps = 1;
		} else {
			numJumps = (distanceToJump / distancePerJump) + 1;
		}
		
		/*Add a jump to whatever we got above*/
		numJumps++;
		
		/*Initiate jump*/
		for (int i = 0; i < numJumps; i++) {
			this.simulate(1, false, false, true, false, false);
			
			/*Player should be off the ground and touching nothing*/
			this.assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
			
			/*Execute the rest of the rest of the jump*/
			this.simulate(this.engine.getJumpDuration() - 1, false, false, false, false, false); //subtract a tick for the one that initiated the jump
			
			if (numJumps != 1) { //add the tick back if jumping more than once (let jump counter completely exhaust)
				this.simulate(1, false, false, false, false, false);
			}
		}
		
		/*Call the fall to ground method - the platform will catch them first*/
		this.fallToGround(c, r);
		
		/*Player should be resting on the platform*/
		this.assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, true);
	}
	
	/*Test basic collisions with stationary autonomous objects*/
	public void basicCollisionTest2() {
		this.init();
	}
	
	/*Test basic collisions with controllable objects*/
	public void basicCollisionTest3() {
		this.init();
	}
	
	/*Test basic collisions with force objects*/
	public void basicCollisionTest4() {
		this.init();
	}
	
	/*Test basic collisions with damage area objects*/
	public void basicCollisionTest5() { 
		this.init();
	}
	
	/*Test basic collisions with regen area objects*/
	public void basicCollisionTest6() { 
		this.init();
	}
	
	/****************************************************************************************************/
	/*********************************************** Other **********************************************/
	/****************************************************************************************************/
	
	/*Set this class's GameEngine and GameWrapper references to brand new objects*/
	public void init() {
		this.engine = new GameEngine();
		this.game = new GameWrapper();
	}
	
	/*Pass the given controllable, room, and environment to this class's engine*/
	public void setEngine(Controllable c, Room r, ArrayList<Model> e) {
		this.engine.setPlayer(c);
		this.engine.setRoom(r);
		this.engine.setEnvironment(e);
	}
	
	/*Assert each of a controllable objects variable are in a given state. It's up to
	*you to provide the expected state*/
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
	
	/*Run the engine for the given number of frames with the given inputs booleans*/
	public void simulate(int numFrames, boolean right, boolean left, boolean space, boolean down, boolean debugOutput) {
		for (int i = 0; i < numFrames; i++) {
			if (debugOutput) {
				this.printPlayerInfo(this.engine.getPlayer());
			}
			this.game.tick(this.engine, right, left, space, down);
		}
	}
	
	/*Get the distance between two points*/
	public int getDistance(int x1, int x2, int y1, int y2) {
		double x = Math.pow(x2 - x1, 2.0);
		double y = Math.pow(y2 - y1, 2.0);
		double distance = Math.sqrt(x + y);
		return (int)Math.ceil(distance); //always rounds up
	}
	
	/*Given that a controllable object is in the air with nothing below it, calculate 
	*the amount of frames needed to put the controllable on the ground, and execute 
	*that many ticks*/
	public void fallToGround(Controllable c, Room r) {
		int playerBottomEdgeY = c.getYLoc() + c.getHeight();
		int groundY = r.getYLoc();
		
		int distance = getDistance(0, 0, playerBottomEdgeY, groundY); //we don't care about x loc in this instance, just pass 0 for those
		
		int fallFrames = (distance / c.getYIncr()) + 1; //divide by how far the player travels each tick, add a frame to be sure
		
		fallFrames += this.engine.getFloatingThreshold(); //add frames for the floating threshold (player doesn't start falling until that many frames)
		
		/*Execute*/
		this.simulate(fallFrames, false, false, false, false, false);
	}

	/*Print some information about the given controllable*/
	public void printPlayerInfo(Controllable c) {
		System.out.println("x: " + c.getXLoc() + ", y: " + c.getYLoc());
		System.out.println("w: " + c.getWidth() + ", h: " + c.getHeight());
		System.out.println("x incr: " + c.getXIncr() + ", y incr: " + c.getYIncr());
		System.out.flush();
	}
}
