package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import minigame3Models.Cube;

public class CubeTest {

	@Test
	public void basicCubeTest(){
		Cube testCube=new Cube(100,100,100,8);
		
		testCube.setLockedStatus(true);
		testCube.setMovingStatus(true);
		
		assertEquals(100,testCube.getXloc());
		assertEquals(100,testCube.getYloc());
		assertEquals(100,testCube.getDimensions());
		assertEquals(true,testCube.getMovingStatus());
		assertEquals(true,testCube.getLockedStatus());
	}
	
	@Test
	public void resetCoordsTest() {
		Cube testCube=new Cube(100,100,100,8);
		
		testCube.setXloc(50);
		testCube.setYloc(50);
		testCube.resetCoords();
		
		assertEquals(100,testCube.getXloc());
		assertEquals(100,testCube.getYloc());
	}
	
	@Test
	public void shuffleTest(){
		Cube testCube=new Cube(100,100,100,100000);
		int testNumber=testCube.getImageNumber();
		
		testCube.setXloc(50);
		testCube.setYloc(50);
		testCube.shuffle();
		
		assertEquals(100,testCube.getXloc());
		assertEquals(100,testCube.getYloc());
		assertNotEquals(testNumber,testCube.getImageNumber());
	}
}
