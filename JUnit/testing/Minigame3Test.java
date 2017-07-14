package testing;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Test;

import enums.GameState;
import games.Minigame3;
import minigame3Models.*;

public class Minigame3Test {

	@Test
	public void startupTest() {
		//First ratio test
		Minigame3 testGame=new Minigame3();
		Dimension d = new Dimension(1000,1000);
		
		testGame.setScreenDimensions(d);
		testGame.initGame();
		
		int ratio=130;
		int offset=(1000/8)-ratio;
		
		int[] cubeX=new int[7];
		
		for(int i=0;i<7;i++){
			cubeX[i]=ratio+(offset+ratio)*i;
		}
		
		int tileHeight=1000/(8/2);
		int placeHeight=1000/(8/4);
		
		for(int j=0;j<testGame.allModels.size();j++){
			assertEquals(testGame.allModels.get(j).getXloc(),cubeX[j%(testGame.allModels.size()/2)]);
			if(j<7){
				assertEquals(testGame.allModels.get(j).getYloc(),tileHeight);
			}
			else{
				assertEquals(testGame.allModels.get(j).getYloc(),placeHeight);
			}
		}
		
		//Second ratio test
		Minigame3 testGame2=new Minigame3();
		Dimension d2 = new Dimension(2000,2000);
		
		testGame2.setScreenDimensions(d2);
		testGame2.initGame();
		
		int ratio2=200;
		int offset2=(2000/8)-ratio2;
		
		int[] cubeX2=new int[7];
		
		for(int l=0;l<7;l++){
			cubeX2[l]=ratio2+(offset2+ratio2)*l;
		}
		
		int tileHeight2=2000/(8/2);
		int placeHeight2=2000/(8/4);
		
		for(int p=0;p<testGame2.allModels.size();p++){
			assertEquals(testGame2.allModels.get(p).getXloc(),cubeX2[p%(testGame2.allModels.size()/2)]);
			if(p<7){
				assertEquals(testGame2.allModels.get(p).getYloc(),tileHeight2);
			}
			else{
				assertEquals(testGame2.allModels.get(p).getYloc(),placeHeight2);
			}
		}
	}

	 
	
	@Test
	public void finishedTest(){
		//True test
		Minigame3 testGame=new Minigame3();
		Dimension d = new Dimension(1000,1000);
		
		testGame.setScreenDimensions(d);
		testGame.initGame();
		
		for(Minigame3Model m : testGame.allModels){
			if(m.getClass().equals(CubePad.class)){
				((minigame3Models.CubePad)m).setOccupied(true);
			}
		}
		
		assertEquals(true,testGame.isFinished());
		
		//False test
		Minigame3 testGame2=new Minigame3();
		Dimension d2 = new Dimension(1000,1000);
		
		testGame2.setScreenDimensions(d2);
		testGame2.initGame();
		
		for(Minigame3Model m : testGame2.allModels){
			if(m.getClass().equals(CubePad.class)){
				((minigame3Models.CubePad)m).setOccupied(false);
			}
		}
		
		assertEquals(false,testGame2.isFinished());
	}
	
	@Test 
	public void submitTest(){
		Minigame3 testGame=new Minigame3();
		Dimension d = new Dimension(1000,1000);
		int tileHeight=1000/(8/2);
		
		testGame.setScreenDimensions(d);
		testGame.initGame();
		testGame.submit();
		
		for(Minigame3Model m : testGame.allModels){
			assertEquals(tileHeight,m.getYloc());
		}
	}
	
	@Test
	public void clickTest(){
		Minigame3 testGame=new Minigame3();
		Dimension d = new Dimension(1000,1000);
		
		testGame.setScreenDimensions(d);
		testGame.initGame();
		
		int ratio=130;
		int offset=(1000/8)-ratio;
		
		int[] cubeX=new int[7];
		
		for(int i=0;i<7;i++){
			cubeX[i]=ratio+(offset+ratio)*i;
		}
		
		int tileHeight=1000/(8/2);
		
		testGame.checkIfClickedCube(cubeX[0], tileHeight+20);
		
		assertEquals(true,((minigame3Models.Cube)testGame.allModels.get(0)).getMovingStatus());
	}
	
	@Test
	public void dragAndReleaseTest(){
		Minigame3 testGame=new Minigame3();
		Dimension d = new Dimension(1000,1000);
		
		testGame.setScreenDimensions(d);
		testGame.initGame();
		
		int ratio=130;
		int offset=(1000/8)-ratio;
		
		int[] cubeX=new int[7];
		
		for(int i=0;i<7;i++){
			cubeX[i]=ratio+(offset+ratio)*i;
		}
		
		int tileHeight=1000/(8/2);
		int placeHeight=1000/(8/4);
		
		testGame.checkIfClickedCube(cubeX[0], tileHeight+20);
		testGame.dragCube(cubeX[0], placeHeight+20);
		
		assertEquals(false,((minigame3Models.CubePad)testGame.allModels.get(7)).getOccupied());
		
		testGame.handleRelease(cubeX[0], placeHeight+20);
		assertEquals(true,((minigame3Models.CubePad)testGame.allModels.get(7)).getOccupied());
		
		testGame.dragCube(cubeX[0], placeHeight+20);
		testGame.handleRelease(cubeX[0], tileHeight);
		assertEquals(false,((minigame3Models.CubePad)testGame.allModels.get(7)).getOccupied());
	}
	
	@Test
	public void releaseTest(){
		
	}
}
