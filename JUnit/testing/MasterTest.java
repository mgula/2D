package testing;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import models.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/*This file changes a lot, and not necessarily in a meaningful needs-to-be-committed way, so I'm just going
 *to "remove" this file from version control (git update-index --assume-unchanged <path-to-this-file>) and 
 *update it occasionally when it makes sense to (git update-index --no-assume-unchanged <path-to-this-file>). 
 *Routines will be committed as they are finished.*/
public class MasterTest {
	/*Attributes of the player that will vary among tests*/
	private int minPlayerDimensions = 1;
	private int maxPlayerDimensions = 300;
	private int minMoveSpeed = 1;
	private int maxMoveSpeed = 30;
	
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
	
	public void testPlayerMovement() {
		int numRuns = 0;
    	
		/*Test on a few combinations of movement speeds*/
		for (int i = this.minMoveSpeed; i < this.maxMoveSpeed; i++) {
			EngineTestRoutines.basicPlayerMovementTest1(new Controllable(1000, -100, this.maxMoveSpeed, i, 3, 3, 100, 100, 20, 2, 50));
			EngineTestRoutines.basicPlayerMovementTest1(new Controllable(1000, -100, i, this.maxMoveSpeed, 3, 3, 100, 100, 20, 2, 50));
			EngineTestRoutines.basicPlayerMovementTest1(new Controllable(1000, -100, i, i, 3, 3, 100, 100, 20, 2, 50));
			
			EngineTestRoutines.basicPlayerMovementTest2(new Controllable(1000, -100, this.maxMoveSpeed, i, 3, 3, 100, 100, 20, 2, 50));
			EngineTestRoutines.basicPlayerMovementTest2(new Controllable(1000, -100, i, this.maxMoveSpeed, 3, 3, 100, 100, 20, 2, 50));
			EngineTestRoutines.basicPlayerMovementTest2(new Controllable(1000, -100, i, i, 3, 3, 100, 100, 20, 2, 50));
        	
			numRuns += 6;
		}
    	
		System.out.println("Movements tests passed (" + numRuns + " runs).");
	}
	
	/*Test basic collisions (one on one collisions - only the player and one other non-moving object). The
	*sizes of the non-moving objects are static.*/
	public void testBasicCollisions() {
		int numRuns = 0;
		
		int playerStartX = 0;
		int playerStartY = -350; //should be greater in magnitude than the player's height
		
		/*Run the tests on various combinations of player sizes - static move speed*/
		int staticMoveSpeed = 4; //pick some random move speed
		for (int i = this.minPlayerDimensions; i <= this.maxPlayerDimensions; i++) {
			EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, staticMoveSpeed, staticMoveSpeed, 100, 100, 20, 100, 50));
			EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, staticMoveSpeed, staticMoveSpeed, 100, 100, 20, 100, 50));
			EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, staticMoveSpeed, staticMoveSpeed, 100, 100, 20, 100, 50));
			
			numRuns += 3;
		}
		
		/*Run the tests on various combinations of movement speeds - static player size*/
		int staticPlayerSize = 30; //pick some random player dimensions
		for (int i = this.minMoveSpeed; i <= this.maxMoveSpeed; i++) {
			EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, this.minMoveSpeed, i, 100, 100, 20, 100, 50));
			EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, i, this.minMoveSpeed, 100, 100, 20, 100, 50));
			EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, staticPlayerSize, i, i, 100, 100, 20, 100, 50));
			
			numRuns += 3;
		}
		
		/*Run the tests on various combinations of sizes and movement speeds
		for (int i = this.minPlayerDimensions; i <= this.maxPlayerDimensions; i++) {
			for (int j = this.minMoveSpeed; j <= this.maxMoveSpeed; j++) {
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, this.minMoveSpeed, j, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, j, this.minMoveSpeed, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, this.minPlayerDimensions, i, j, j, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, this.minMoveSpeed, j, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, j, this.minMoveSpeed, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, this.minPlayerDimensions, j, j, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, this.minMoveSpeed, j, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, j, this.minMoveSpeed, 100, 100, 20, 100, 50));
				EngineTestRoutines.basicCollisionTest1(new Controllable(playerStartX, playerStartY, i, i, j, j, 100, 100, 20, 100, 50));
				
				numRuns += 9;
			}
		}*/
		
		System.out.println("Basic collision tests passed (" + numRuns + " runs).");
	}
}
