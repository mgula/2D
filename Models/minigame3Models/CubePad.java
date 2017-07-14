package minigame3Models;

/**
 * A Minigame Cube Pad
 * Represents location where cube will be placed
 * @author kylew
 */
public class CubePad implements Minigame3Model{
	
	private int xloc;
    private int yloc;
    private int dimensions;
    private int imageNumber;
    private int resetX;
    private int resetY;
    private int numCubes;
    private boolean occupied;
	
    /**
     * CubePad Constructor
     * Initializes occupied status to false
     * @param x x location of cube, also used to set the original location
     * @param y y location of cube, also used to set the original location
     * @param dimensions gives height and width of cube pad
     * @param numCubes number of cubes in game matches with number of pictures
     */
	public CubePad(int x, int y, int dimensions,int numCubes){
		this.xloc = x;
    	this.yloc = y;
    	this.dimensions = dimensions;
    	this.imageNumber=numCubes+1;
    	this.resetX=x;
    	this.resetY=y;
    	this.numCubes=numCubes;
    	this.occupied=false;
	}

	public int getXloc() {
		return xloc;
	}

	public int getYloc() {
		return yloc;
	}

	public int getDimensions() {
		return dimensions;
	}
	
	public void setImageNumber(Cube c){
		imageNumber=c.getImageNumber();
	}
	
	public void clearImageNumber(){
		imageNumber=numCubes+1;
	}
	
	public void setOccupied(boolean newStatus){
		this.occupied=newStatus;
	}
	
	public boolean getOccupied(){
		return occupied;
	}
	
	public int getImageNumber(){
		return imageNumber;
	}
	
	public void setYLoc(int newY){
		this.yloc=newY;
	}
	
	/**
	 * Method to reset the x and y coordinates of the cube pad
	 */
	public void resetCoords(){
		xloc=resetX;
		yloc=resetY;
	}
}