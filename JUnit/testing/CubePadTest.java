package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import minigame3Models.Cube;
import minigame3Models.CubePad;

public class CubePadTest {

	@Test
	public void basicCubePadTest(){
		CubePad testCubePad=new CubePad(100,100,100,8);
		
		testCubePad.setOccupied(true);
		
		assertEquals(100,testCubePad.getXloc());
		assertEquals(100,testCubePad.getYloc());
		assertEquals(100,testCubePad.getDimensions());
		assertEquals(9,testCubePad.getImageNumber());
		assertEquals(true,testCubePad.getOccupied());
	}
	
	@Test
	public void resetCoordsTest() {
		CubePad testCubePad=new CubePad(100,100,100,8);
		
		testCubePad.setYLoc(50);
		testCubePad.resetCoords();
		
		assertEquals(100,testCubePad.getYloc());
	}
	
	@Test
	public void clearImageTest(){
		CubePad testCubePad=new CubePad(100,100,100,8);
		Cube testCube=new Cube(100,100,100,8);
		
		testCubePad.setImageNumber(testCube);
		
		assertEquals(testCube.getImageNumber(),testCubePad.getImageNumber());
		
		testCubePad.clearImageNumber();
		
		assertEquals(9,testCubePad.getImageNumber());
	}
}
