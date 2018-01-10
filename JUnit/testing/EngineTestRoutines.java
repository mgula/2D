package testing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import map.Exit;
import map.Room;
import models.*;
import engine.*;
import enums.*;

public class EngineTestRoutines {
	/****************************************************************************************************/
	/***************************************** Movement testing *****************************************/
	/****************************************************************************************************/
	
	/*Test player moving right, player moving left, and player jumping (no combined input)*/
	public static void basicPlayerMovementTest1(Controllable c) {
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
		
		/*Make a room that just acts as a box - no environment or room links*/
		Room r = new Room(RoomID.SPAWN, 0, 0, 10000, 10000, new ArrayList<Model>(), new ArrayList<Exit>());
		
		/*Pass the player and empty room to the engine*/
		e.setPlayer(c);
		e.setRoom(r);
		
		/*Initial tick to get booleans situated*/
		simulate(w, e, 1, false, false, false, false, false);
		
		/*We should start in the air, not touching anything*/
		assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*Player should be on the ground now*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll move right a bit*/
		int moveFrames = 20;
		
		/*Save old x loc*/
		int oldX = c.getXLoc();
		
		/*Execute*/
		simulate(w, e, moveFrames, true, false, false, false, false);
		
		/*Get new x loc*/
		int newX = oldX + (c.getXIncr() * moveFrames);
		
		/*We should have moved right*/
		assertEquals(newX, c.getXLoc());
		
		/*Now we'll move left - save old x loc first*/
		oldX = c.getXLoc();
		simulate(w, e, moveFrames, false, true, false, false, false);
		
		/*Get new x loc*/
		newX = oldX - (c.getXIncr() * moveFrames);
		
		/*We should have moved left*/
		assertEquals(newX, c.getXLoc());
		
		/*Now we'll jump - save old y loc first*/
		int oldY = c.getYLoc();
		
		/*Start the jump*/
		simulate(w, e, 1, false, false, true, false, false);
		
		/*At the very least we should be in the air now*/
		assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let the controllable reach the peak of the jump*/
		simulate(w, e, c.getJumpDuration() - 1, false, false, false, false, false);
		
		/*We should now be at the peak of the jump*/
		int newY = oldY - (c.getYIncr() * c.getJumpDuration());
		
		assertEquals(newY, c.getYLoc());
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*We should be back on the ground*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
	}
	
	/*Test player moving right and left while jumping*/
	public static void basicPlayerMovementTest2(Controllable c) {
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
		
		/*Make a room that just acts as a box - no environment or room links*/
		Room r = new Room(RoomID.SPAWN, 0, 0, 10000, 10000, new ArrayList<Model>(), new ArrayList<Exit>());
		
		/*Pass the player and empty room to the engine*/
		e.setPlayer(c);
		e.setRoom(r);
		
		/*Initial tick to get booleans situated*/
		simulate(w, e, 1, false, false, false, false, false);
		
		/*We should start in the air, not touching anything*/
		assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*Player should be on the ground now*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll jump and move right - save old coords first*/
		int oldX = c.getXLoc();
		int oldY = c.getYLoc();
		
		/*Initiate jump + right*/
		simulate(w, e, 1, true, false, true, false, false);
		
		/*Hold right the rest of the way*/
		simulate(w, e, c.getJumpDuration() - 1, true, false, false, false, false);
		
		/*Calculate new coords*/
		int newX = oldX + (c.getXIncr() * c.getJumpDuration());
		int newY = oldY - (c.getYIncr() * c.getJumpDuration());
		
		/*We should have moved right and should also be at the peak of the jump*/
		assertEquals(newX, c.getXLoc());
		assertEquals(newY, c.getYLoc());
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*Player should be on the ground now*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*Now we'll jump and move left - save old coords first*/
		oldX = c.getXLoc();
		oldY = c.getYLoc();
		
		/*Initiate jump + left*/
		simulate(w, e, 1, false, true, true, false, false);
		
		/*Hold left the rest of the way*/
		simulate(w, e, c.getJumpDuration() - 1, false, true, false, false, false);
		
		/*Calculate new coords*/
		newX = oldX - (c.getXIncr() * c.getJumpDuration());
		newY = oldY - (c.getYIncr() * c.getJumpDuration());
		
		/*We should have moved left and should also be at the peak of the jump*/
		assertEquals(newX, c.getXLoc());
		assertEquals(newY, c.getYLoc());
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*Player should be on the ground now*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
	}
	
	/****************************************************************************************************/
	/**************************************Basic Collision Testing **************************************/
	/****************************************************************************************************/
	
	/*Test collisions with a solid object (rock in this case) and a platform*/
	public static void basicCollisionTest1(Controllable c) {
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
		
		/*Make a small test environment with rocks and a platform*/
		Rock rockA = new Rock(500, -50, 50, 50);
		Rock rockB = new Rock(1000, -500, 20, 100);
		Platform platform = new Platform(1500, -100, 200);
		ArrayList<Model> env = new ArrayList<Model>();
		env.add(rockA);
		env.add(rockB);
		env.add(platform);
		
		/*Make a room that just acts as a box - no room links*/
		Room r = new Room(RoomID.SPAWN, 0, 0, 2000, 2000, env, new ArrayList<Exit>());
		
		/*Pass the player and room to the engine*/
		e.setPlayer(c);
		e.setRoom(r);
		
		/*Initial tick to get booleans situated*/
		simulate(w, e, 1, false, false, false, false, false);
		
		/*We should start in the air, with left edge against the left wall*/
		assertAllCollisionVariables(c, false, true, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*Player should be on the ground now*/
		assertAllCollisionVariables(c, true, true, false, false, false, false, false, false, false, false);
		
		/*Calculate number of ticks needed for player to collide with first rock*/
		int playerRightEdgeX = c.getXLoc() + c.getWidth();
		int rockLeftEdgeX = rockA.getXLoc();
		
		int distance = getDistance(playerRightEdgeX, rockLeftEdgeX, 0, 0); //we don't care about y loc in this instance, just pass 0 for those
		
		int moveFrames = (distance / c.getXIncr()) + 1; //divide by how far the player travels each tick, add a frame to be sure
		
		/*Execute*/
		simulate(w, e, moveFrames, true, false, false, false, false);
		
		/*Player's right edge should be against the first rock*/
		assertAllCollisionVariables(c, true, false, true, false, false, false, false, false, false, false);
		
		/*Now we'll jump over the rock*/
		int playerBottomEdgeY = c.getYLoc() + c.getHeight();
		int rockTopEdgeY = rockA.getYLoc();
		
		int distanceToJump = getDistance(0, 0, playerBottomEdgeY, rockTopEdgeY);

		int distancePerJump = c.getJumpDuration() * c.getYIncr();
		
		int numJumps = 0;
		
		if (distanceToJump > distancePerJump) {
			numJumps = 1;
		} else {
			numJumps = (distanceToJump / distancePerJump) + 1;
		}
		
		/*Initiate jump*/
		for (int i = 0; i < numJumps; i++) {
			simulate(w, e, 1, false, false, true, false, false);
			
			/*Player should be off the ground but still against first rock (or not, if on second jump)*/
			if (i == 0) {
				assertAllCollisionVariables(c, false, false, true, false, false, false, false, false, false, false);
			} else {
				assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
			}
			
			/*Execute the rest of the rest of the jump*/
			simulate(w, e, c.getJumpDuration() - 1, false, false, false, false, false); //subtract a tick for the one that initiated the jump
			
			if (numJumps != 1) { //add the tick back if jumping more than once (let jump counter completely exhaust)
				simulate(w, e, 1, false, false, false, false, false);
			}
		}
		
		/*Player should now be in the air, touching nothing*/
		assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Move right for one frame*/
		simulate(w, e, 1, true, false, false, false, false);
		
		/*Calculate frames needed to land on top of the first rock*/
		playerBottomEdgeY = c.getYLoc() + c.getHeight();
		rockTopEdgeY = rockA.getYLoc();
		
		distance = getDistance(0, 0, playerBottomEdgeY, rockTopEdgeY); //we don't care about x loc in this instance, just pass 0 for those
		
		int fallFrames = (distance / c.getYIncr()) + 1; //add a frame for good measure
		
		fallFrames += c.getFloatingThreshold() - 1; //subtract a frame for the tick where we moved right
		
		/*Execute*/
		simulate(w, e, fallFrames, false, false, false, false, false);
		
		/*Player should now be resting on top of the first rock*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll now move right, off of the rock*/
		int playerLeftEdgeX = c.getXLoc();
		int rockRightEdgeX = rockA.getXLoc() + rockA.getWidth();
		
		distance = getDistance(playerLeftEdgeX, rockRightEdgeX, 0, 0);
		
		moveFrames = (distance / c.getXIncr()) + 1; //divide by how far the player travels each tick, add a frame for good luck
		
		/*Execute*/
		simulate(w, e, moveFrames, true, false, false, false, false);
		
		/*Player should now be in the air right of the first rock, touching nothing*/
		assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*There's some cases where the player's left edge may be against rock A. Move right one frame to avoid these cases*/
		simulate(w, e, 1, true, false, false, false, false);
		
		/*Player should be only touching the ground now*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*We'll now bump into rock A from the left, just to say goodbye*/
		playerLeftEdgeX = c.getXLoc();
		int rockARightEdgeX = rockA.getXLoc() + rockA.getWidth();
		
		distance = getDistance(playerLeftEdgeX, rockARightEdgeX, 0, 0);
		
		moveFrames = (distance / c.getXIncr()) + 1;
		
		/*Execute*/
		simulate(w, e, moveFrames, false, true, false, false, false);
		
		/*Player should be against left edge of rock A*/
		assertAllCollisionVariables(c, true, true, false, false, false, false, false, false, false, false);
		
		/*Now we'll place the player under the second rock*/
		playerRightEdgeX = c.getXLoc() + c.getWidth();
		rockRightEdgeX = rockB.getXLoc() + rockB.getWidth();
		
		distance = getDistance(playerRightEdgeX, rockRightEdgeX, 0, 0); //we don't care about y loc in this instance, just pass 0 for those
		
		moveFrames = distance / c.getXIncr();
		
		/*Execute*/
		simulate(w, e, moveFrames, true, false, false, false, false);
		
		/*Player should be underneath the second rock, only touching the ground*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
		/*Now we'll bump our top edge into the second rock's bottom edge*/
		int playerTopEdgeY = c.getYLoc();
		int rockBottomEdgeY = rockB.getYLoc() + rockB.getHeight();
		
		distanceToJump = getDistance(0, 0, playerTopEdgeY, rockBottomEdgeY);
		
		/*We'll need to keep track of some variables in order to hit the bottom edge*/
		int oldY = c.getYLoc();
		int distanceRemaining = distanceToJump;
		
		/*Start to jump*/
		simulate(w, e, 1, false, false, true, false, false);
		
		/*Update distance remaining*/
		distanceRemaining -= (oldY - c.getYLoc());

		/*Keep going until we hit the bottom edge of rock B*/
		while (distanceRemaining > 0) {
			/*Player should be off the ground*/
			assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
			
			oldY = c.getYLoc();
			
			/*Continue to jump*/
			simulate(w, e, 1, false, false, true, false, false);
			
			/*Update distance remaining*/
			distanceRemaining -= (oldY - c.getYLoc());
		}
		/*One last tick*/
		simulate(w, e, 1, false, false, false, false, false);
		
		/*Player's top edge should meet rock B's bottom edge*/
		assertAllCollisionVariables(c, false, false, false, true, false, false, false, false, false, false);
		
		/*Let player fall to the ground*/
		fallToGround(w, e, c, r);
		
		/*Now we'll place the player under the platform*/
		playerRightEdgeX = c.getXLoc() + c.getWidth();
		int platRightEdgeX = platform.getXLoc() + platform.getWidth();
		
		distance = getDistance(playerRightEdgeX, platRightEdgeX, 0, 0); //we don't care about y loc in this instance, just pass 0 for those
		
		moveFrames = distance / c.getXIncr();
		
		/*Execute*/
		simulate(w, e, moveFrames, true, false, false, false, false);
		
		/*Player should be underneath the second rock, only touching the ground*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, false);
		
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
			simulate(w, e, 1, false, false, true, false, false);
			
			/*Player should be off the ground and touching nothing*/
			assertAllCollisionVariables(c, false, false, false, false, false, false, false, false, false, false);
			
			/*Execute the rest of the rest of the jump*/
			simulate(w, e, c.getJumpDuration() - 1, false, false, false, false, false); //subtract a tick for the one that initiated the jump
			
			if (numJumps != 1) { //add the tick back if jumping more than once (let jump counter completely exhaust)
				simulate(w, e, 1, false, false, false, false, false);
			}
		}
		
		/*Call the fall to ground method - the platform will catch them first*/
		fallToGround(w, e, c, r);
		
		/*Player should be resting on the platform*/
		assertAllCollisionVariables(c, true, false, false, false, false, false, false, false, false, true);
	}
	
	/*Test basic collisions with stationary autonomous objects*/
	public static void basicCollisionTest2() {
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
	}
	
	/*Test basic collisions with controllable objects*/
	public static void basicCollisionTest3() {
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
	}
	
	/*Test basic collisions with force objects*/
	public static void basicCollisionTest4() {
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
	}
	
	/*Test basic collisions with damage area objects*/
	public static void basicCollisionTest5() { 
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
	}
	
	/*Test basic collisions with regen area objects*/
	public static void basicCollisionTest6() { 
		/*Initialize the engine and wrapper*/
		GameWrapper w = new GameWrapper();
		GameEngine e = new GameEngine();
	}
	
	/****************************************************************************************************/
	/*********************************************** Other **********************************************/
	/****************************************************************************************************/
	
	/*Assert each of a controllable objects variable are in a given state. It's up to
	*you to provide the expected state*/
	public static void assertAllCollisionVariables(Controllable c, boolean bottom, boolean left, boolean right, boolean top, boolean oMBottom, boolean aMBottom, boolean aMLeft, boolean aMRight, boolean aMTop, boolean plat) {
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
	public static void simulate(GameWrapper w, GameEngine e, int numFrames, boolean right, boolean left, boolean space, boolean down, boolean debugOutput) {
		for (int i = 0; i < numFrames; i++) {
			if (debugOutput) {
				printPlayerInfo(e.getPlayer());
			}
			w.tick(e, right, left, space, down);
		}
	}
	
	/*Get the distance between two points*/
	public static int getDistance(int x1, int x2, int y1, int y2) {
		double x = Math.pow(x2 - x1, 2.0);
		double y = Math.pow(y2 - y1, 2.0);
		double distance = Math.sqrt(x + y);
		return (int)Math.ceil(distance); //always rounds up
	}
	
	/*Given that a controllable object is in the air with nothing below it, calculate 
	*the amount of frames needed to put the controllable on the ground, and execute 
	*that many ticks*/
	public static void fallToGround(GameWrapper w, GameEngine e, Controllable c, Room r) {
		int playerBottomEdgeY = c.getYLoc() + c.getHeight();
		int groundY = r.getYLoc();
		
		int distance = getDistance(0, 0, playerBottomEdgeY, groundY); //we don't care about x loc in this instance, just pass 0 for those
		
		int fallFrames = (distance / c.getYIncr()) + 1; //divide by how far the player travels each tick, add a frame to be sure
		
		fallFrames += c.getFloatingThreshold(); //add frames for the floating threshold (player doesn't start falling until that many frames)
		
		/*Execute*/
		simulate(w, e, fallFrames, false, false, false, false, false);
	}
	
	/*Print some information about the given controllable*/
	public static void printPlayerInfo(Controllable c) {
		System.out.println("x: " + c.getXLoc() + ", y: " + c.getYLoc());
		System.out.println("w: " + c.getWidth() + ", h: " + c.getHeight());
		System.out.println("x incr: " + c.getXIncr() + ", y incr: " + c.getYIncr());
		System.out.flush();
	}
}
