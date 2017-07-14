package minigame3Models;

import java.util.Random;

import minigame1Models.Minigame1Model;

/**
 * A MiniGame 3 cube
 * Represents a rollable cube with pictures on it
 * @author kylew
 */
public class Cube implements Minigame3Model {

	private int xloc;
    private int yloc;
    private int resetX;
    private int resetY;
    private int dimensions;
    private Random rand;
    private int imageNumber;
    private boolean isLocked;
    private boolean moving;
    private int numCubes;
    
    /**
     * Cube Constructor
     * Initializes locked and moving statuses to false and generates random picture
     * @param x x location of cube, also used to set the original location
     * @param y y location of cube, also used to set the original location
     * @param dimensions gives height and width of cube
     * @param numCubes number of cubes in game matches with number of pictures
     */
    public Cube(int x, int y, int dimensions, int numCubes) {
    	this.xloc = x;
    	this.yloc = y;
    	this.resetX=x;
    	this.resetY=y;
    	this.dimensions = dimensions;
    	this.rand=new Random();
    	this.imageNumber=rand.nextInt(numCubes);
    	this.isLocked=false;
    	this.moving=false;
    	this.numCubes=numCubes;
    }
    
    public int getXloc() {
    	return this.xloc;
    }
    
    public int getYloc() {
    	return this.yloc;
    }
    
    public int getDimensions() {
    	return dimensions;
    }
    
	public boolean getLockedStatus(){
		return isLocked;
	}
	
	public void setLockedStatus(boolean set){
		isLocked=set;
	}
	
	public int getImageNumber(){
		return imageNumber;
	}
	
	public boolean getMovingStatus(){
		return moving;
	}
	
	public void setMovingStatus(boolean set){
		moving=set;
	}
	
	public void setXloc(int x){
		xloc=x;
	}
	
	public void setYloc(int y){
		yloc=y;
	}
	
	/**
	 * Method to shuffle cube images using random int
	 * After changing image method then resets the coordinates
	 */
	public void shuffle(){
		imageNumber=rand.nextInt(numCubes);
		resetCoords();
	}

	/**
	 * Method to reset the x and y coordinates of the cube
	 */
	public void resetCoords(){
		xloc=resetX;
		yloc=resetY;
	}
}