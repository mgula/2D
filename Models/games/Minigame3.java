package games;

import java.awt.Dimension;
import java.util.ArrayList;


import enums.GameState;
import minigame3Models.Cube;
import minigame3Models.CubePad;
import minigame3Models.Minigame3Model;

/**
 * Minigame 3 class
 * Represents the interactions of minigame 3
 * Uses default constructor
 * @author kylew
 */
public class Minigame3 implements Minigame {
	
	private GameState gameState = GameState.UNINITIALIZED;
	private GameState lastState = GameState.UNINITIALIZED;
	
	private boolean firstTime = true;
	
	public ArrayList<Minigame3Model> allModels;
	
	private int screenWidth;
	private int screenHeight;
	private int ratio;
	private int tileHeight;
	private int placeHeight;
	private int offset;
	
	private int cube1Xloc;
	private int cube2Xloc;
	private int cube3Xloc;
	private int cube4Xloc;
	private int cube5Xloc;
	private int cube6Xloc;
	private int cube7Xloc;
	
	private final int ASPECT_DIVIDE=1400; //divides the ratios into 2 at a width of 1400p
	private final int NUM_CUBES=8;
	private final int POINTER_OFFSET=75; //offset of mouse pointer to model corner
	
	public boolean getFirstTime() {
		return this.firstTime;
	}
	
	public void setFirstTime() {
		this.firstTime = false;
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public void setGameState (GameState state) {
		this.gameState = state;
	}
	
	public GameState getLastState() {
		return this.lastState;
	}
	
	public void setLastState (GameState state) {
		this.lastState = state;
	}
	
	/**
	 * Sets dimensions of the game based off of the dimensions of user's screen
	 * Uses dimensions to set up ratios for cube x and y locations
	 * @param d dimensions of the screen
	 */
	public void setScreenDimensions(Dimension d) {
		screenWidth = (int)d.getWidth();
		screenHeight = (int)d.getHeight();
		if(screenWidth<ASPECT_DIVIDE){
			ratio=130;
		}
		else{
			ratio=200;
		}
		offset=(screenWidth/NUM_CUBES)-ratio;
		cube1Xloc=ratio+(offset+ratio)*0;
		cube2Xloc=ratio+(offset+ratio)*1;
		cube3Xloc=ratio+(offset+ratio)*2;
		cube4Xloc=ratio+(offset+ratio)*3;
		cube5Xloc=ratio+(offset+ratio)*4;
		cube6Xloc=ratio+(offset+ratio)*5;
		cube7Xloc=ratio+(offset+ratio)*6;
		
		tileHeight=screenHeight/(NUM_CUBES/2);
		placeHeight=screenHeight/(NUM_CUBES/4);
	}
	
	/**
	 * Initializes the game and creates all models to be placed within
	 * Shuffles models to ensure no matches and random pictures
	 */
	public void initGame() {
		this.allModels = new ArrayList<Minigame3Model>();
		this.allModels.add(new Cube(this.cube1Xloc, tileHeight, ratio, NUM_CUBES));
		this.allModels.add(new Cube(this.cube2Xloc, tileHeight, ratio, NUM_CUBES));
		this.allModels.add(new Cube(this.cube3Xloc, tileHeight, ratio, NUM_CUBES));
		this.allModels.add(new Cube(this.cube4Xloc, tileHeight, ratio, NUM_CUBES));
		this.allModels.add(new Cube(this.cube5Xloc, tileHeight, ratio, NUM_CUBES));
		this.allModels.add(new Cube(this.cube6Xloc, tileHeight, ratio, NUM_CUBES));
		this.allModels.add(new Cube(this.cube7Xloc, tileHeight, ratio, NUM_CUBES));
		this.allModels.add(new CubePad(this.cube1Xloc, placeHeight, ratio, NUM_CUBES));
		this.allModels.add(new CubePad(this.cube2Xloc,placeHeight, ratio, NUM_CUBES));
		this.allModels.add(new CubePad(this.cube3Xloc, placeHeight, ratio, NUM_CUBES));
		this.allModels.add(new CubePad(this.cube4Xloc, placeHeight, ratio, NUM_CUBES));
		this.allModels.add(new CubePad(this.cube5Xloc, placeHeight, ratio, NUM_CUBES));
		this.allModels.add(new CubePad(this.cube6Xloc, placeHeight, ratio, NUM_CUBES));
		this.allModels.add(new CubePad(this.cube7Xloc, placeHeight, ratio, NUM_CUBES));
		shuffle();
	}
	
	/**
	 * Handles when cube is being dragged and has cube follow pointer
	 * If cube was in cube pad sets occupied to false to free it 
	 * @param dragX x location of pointer during drag
	 * @param dragY y location of pointer during drag
	 */
	public void dragCube(int dragX, int dragY) {
		for(Minigame3Model m : allModels){
			if(m instanceof minigame3Models.Cube){
				if(((minigame3Models.Cube) m).getMovingStatus()==true){
					for(Minigame3Model n : allModels){
						if(n instanceof minigame3Models.CubePad){
							if(n.getXloc()==m.getXloc() && n.getYloc()==m.getYloc()){
								((minigame3Models.CubePad) n).setOccupied(false);
							}
						}
					}
					((minigame3Models.Cube) m).setXloc(dragX-POINTER_OFFSET);
					((minigame3Models.Cube) m).setYloc(dragY-POINTER_OFFSET);
				}
			}
		}
	}
	
	/**
	 * Handles when a cube has been clicked
	 * Enables cube's moving status so it can be dragged
	 * @param clickX x location of click
	 * @param clickY y location of click
	 */
	public void checkIfClickedCube(int clickX, int clickY) {
		for (Minigame3Model m : allModels) {
			if (m instanceof minigame3Models.Cube) {
				int x = m.getXloc();
				int y = m.getYloc();
				int w = m.getDimensions();
				int h = m.getDimensions();
				for (int i = x; i < x + w; i++) {
					for (int j = y; j < y + h; j++) {
						if (clickX == i && clickY == j) {
							((minigame3Models.Cube) m).setMovingStatus(true);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Handles when a dragged cube has been released
	 * If a cube has been released in a non occupied pad it places down
	 * Otherwise the cube snaps back to its default location
	 * @param releaseX x location of release
	 * @param releaseY y location of release
	 */
	public void handleRelease(int releaseX, int releaseY) {
		for(Minigame3Model m : allModels){
			if(m instanceof minigame3Models.Cube){
				if(((minigame3Models.Cube) m).getMovingStatus()==true){
					for (Minigame3Model n : allModels) {
						if (n instanceof minigame3Models.CubePad) {
							int x = n.getXloc();
							int y = n.getYloc();
							int w = n.getDimensions();
							int h = n.getDimensions();
							for (int i = x; i < x + w; i++) {
								for (int j = y; j < y + h; j++) {
									if (releaseX == i && releaseY == j) {
										if(((minigame3Models.CubePad) n).getOccupied()==false){
											((minigame3Models.Cube) m).setMovingStatus(false);
											((minigame3Models.Cube) m).setLockedStatus(true);
											((minigame3Models.Cube) m).setXloc(x);
											((minigame3Models.Cube) m).setYloc(y);
											((minigame3Models.CubePad) n).setImageNumber(((minigame3Models.Cube) m));
											((minigame3Models.CubePad) n).setOccupied(true);
											return;
										}
									}
								}
							}
						}
					}
					((minigame3Models.Cube) m).setMovingStatus(false);
					((minigame3Models.Cube) m).resetCoords();
				}
			}
		}
	}
	
	/**
	 * Shuffles cubes within game and resets all model locations and statuses
	 * Checks to make sure that no more than 2 cubes have the same picture
	 */
	public void shuffle() {
		int typeCount;
		boolean valid=false;
		for (int i=0;i<allModels.size();i++) {
			if (allModels.get(i).getClass().equals(Cube.class)) {
				while(!valid){
					((minigame3Models.Cube)allModels.get(i)).shuffle();
					typeCount=1;
					for(int j=i-1;j>=0;j--){
						if(((minigame3Models.Cube)allModels.get(i)).getImageNumber()==((minigame3Models.Cube)allModels.get(j)).getImageNumber()){
							typeCount++;
						}
					}
					if(typeCount<3){
						valid=true;
					}
				}
			}
			else{
				((minigame3Models.CubePad)allModels.get(i)).clearImageNumber();
				((minigame3Models.CubePad)allModels.get(i)).resetCoords();
				((minigame3Models.CubePad)allModels.get(i)).setOccupied(false);
			}
			valid=false;
		}
	}
	
	/**
	 * Moves locations of cube pads to final submit screen
	 */
	public void submit(){
		for(Minigame3Model m : allModels){
			if(m.getClass().equals(CubePad.class)){
				((minigame3Models.CubePad)m).setYLoc(tileHeight);
			}
		}
	}
	
	/**
	 * Checks to see if game is finished
	 * Game is ready when all cube pads have been occupied 
	 * @return boolean status of game finish state
	 */
	public boolean isFinished(){
		for(Minigame3Model m : allModels){
			if(m.getClass().equals(CubePad.class)){
				if(!((minigame3Models.CubePad)m).getOccupied()){
					return false;
				}
			}
		}
		return true;
	}
}