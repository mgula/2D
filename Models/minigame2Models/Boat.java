package minigame2Models;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;
import javax.swing.text.View;

import enums.BoatType;
import enums.Direction;

/**
 * The boat class models all boats within minigame 2 which are the sailboat, speedboat, and tanker. There are specified speeds for all of these 
 * boats respectively. Each boat also has its own arrayList of waves that the boat will release as it moves.
 * @author npompetti
 *
 */
public class Boat implements Minigame2Model {
	private int xloc;
    private int yloc;
    private int xIncr = 2;
    private Direction currDir;
    private boolean drawable = true;
    private BoatType typeBoat;
    
    private int speedBoatSpeed = 5;
    private int speedBoatWaveSpeed = 3;
    private int speedBoatWaveDamage = 20;
    private int speedBoatWaveBreakOffset = 30;
    private int sailBoatSpeed = 4;
    private int sailBoatWaveSpeed = 2;
    private int sailBoatWaveDamage = 10;
    private int sailBoatWaveBreakOffset = 20;
    private int tankerSpeed = 3;
    private int tankerWaveSpeed = 2;
    private int tankerWaveDamage = 30;
    private int tankerWaveBreakOffset = 60;
    private int sailBoatXSpan = 40;
	private int speedBoatXSpan = 70;
	private int tankerXSpan = 80;
	private int screenWidthBounds = 50;
	private int xPositiveOffscreen = 1600;
	private int xNegativeOffscreen = -200;
	private int screenWidthWaveGenOffset = 15;
	private int tutSailBoatX = 160;
	private int tutSailBoatY = 450;
	private int tutSpeedBoatX = 325;
	private int tutSpeedBoatY = 325;
	private int tutTankerX = 510;
	private int tutTankerY = 215;
    
    //Each boat has its amount own waves that will be dependent on the type of boat
    private ArrayList<Wave> respectiveWaves = new ArrayList<Wave>();


    //Where the wave is released at on screen and a boolean so that it can be turned on/off drawable
    public int releaseWaveAtXCoord;
    private boolean releasedWave = false;
    
    
    
    /**
     * Constructor for the boat type.
     * 
     * @param x 	The x location the boat will be generated.
     * @param y 	The y location the boat will be generated.
     * @param d 	What direction the boat will be moving in.
     * @param screenWidth 	How large of a screen is the boat moving across.
     * @param shoreStart	Where the shore starts which is then passed as an argument to the wave generator.
     * @param type			The type of boat that is being constructed.
     */
    public Boat(int x, int y, Direction d, int screenWidth, int shoreStart, BoatType type) {
    	this.xloc = x;
    	this.yloc = y;
    	this.currDir = d;
    	this.setWaveXCoord(screenWidth);
    	    	
    	if(type == type.SAILBOAT){
    		this.typeBoat = type.SAILBOAT;
    		//Sailboat releases three waves
    		for(int i=0; i<3;i++){
    			this.respectiveWaves.add(Wave.generateWave(this.setWaveXCoord(screenWidth-this.screenWidthBounds), this.yloc, this.sailBoatWaveDamage, shoreStart-this.sailBoatWaveBreakOffset, this.sailBoatWaveSpeed, this.sailBoatXSpan));
    		}
    		this.xIncr = this.sailBoatSpeed;
    	}
    	
    	if(type == typeBoat.SPEEDBOAT){
    		this.typeBoat = type.SPEEDBOAT;
    		//SpeedBoat releases two waves
    		for(int i=0; i<2; i++){
    			this.respectiveWaves.add(Wave.generateWave(this.setWaveXCoord(screenWidth-this.screenWidthBounds), this.yloc, this.speedBoatWaveDamage, shoreStart-this.speedBoatWaveBreakOffset, this.speedBoatWaveSpeed, this.speedBoatXSpan));
    		}
    		this.xIncr = this.speedBoatSpeed;
    	}
    	
    	if(type == typeBoat.TANKER){
    		this.typeBoat = type.TANKER;
    		//Tanker releases one wave
    		this.respectiveWaves.add(Wave.generateWave(this.setWaveXCoord(screenWidth-this.screenWidthBounds), this.yloc, this.tankerWaveDamage, shoreStart-this.tankerWaveBreakOffset, this.tankerWaveSpeed, this.tankerXSpan));
    		
    		this.xIncr = this.tankerSpeed;
    		
    	}
  
    }
    
    public Direction getDirection(){
    	return this.currDir;
    }
    
    public BoatType getType(){
    	return this.typeBoat;
    }
    
    /**
     * Move updates the boats x location dependent upon what direction it is heading in, either East or West.
     * This directionality is also important for when the boat is deemed as undrawable(offscreen) and that is indicated by the xPositiveOffscreen
     * and xNegativeOffscreen.
     */
    @Override
    public void move() {
    	switch (currDir) {
    		case EAST:
    	    	xloc += xIncr;
    			break;
    		case WEST:
    	    	xloc -= xIncr;
    			break;
    		default:
    			break;
    	}
    	if (xloc >= this.xPositiveOffscreen || xloc <= this.xNegativeOffscreen) {
    		this.drawable = false;
    	}
    }
    
    
    public int getXloc() {
    	return this.xloc;
    }
    
    public int getYloc() {
    	return this.yloc;
    }
    
    public void setWaveReleased(boolean b){
    	this.releasedWave = b;
    }
    
    public boolean getWaveReleased(){
    	return this.releasedWave;
    }

	@Override
	public boolean getDrawable() {
		return this.drawable;
	}

	@Override
	public void setDrawable(boolean b) {
		this.drawable = b;
	}

	@Override
	public boolean calcRange(int xRange, int yRange, int xClick, int yClick) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//This method generates the x location where the boat will release its wave
	/**
	 * Generates a random xcoordinate for the wave to be generated on screen. It takes the screenwidth as a parameter to determine between 0 and
	 * another to generate a random number.
	 * @param screenWidth
	 * @return
	 */
	public int setWaveXCoord(int screenWidth){
		int minWidth = 0;
		int randomNum = ThreadLocalRandom.current().nextInt(minWidth, screenWidth - this.screenWidthWaveGenOffset);
		return randomNum;
	}
	
	
	
	public ArrayList<Wave> getWaves(){
		return this.respectiveWaves;
	}
	
	/**
	 * setTutWaves generates specific boats with predetermined x and y coordinates to be displayed during the tutorial.
	 * @param shoreBreakYCoord
	 */
	public void setTutWaves(int shoreBreakYCoord){
		this.respectiveWaves = new ArrayList<Wave>();
		
		if(this.typeBoat == BoatType.SAILBOAT){
			this.respectiveWaves.add(Wave.generateWave(this.tutSailBoatX, this.tutSailBoatY, this.sailBoatWaveDamage, shoreBreakYCoord-this.sailBoatWaveBreakOffset, this.sailBoatWaveSpeed, this.sailBoatXSpan));
			for(Wave w: this.respectiveWaves){
				w.setDrawable(true);
				w.setReleased(true);
			}
		}
		else if(this.typeBoat == BoatType.SPEEDBOAT){
			this.respectiveWaves.add(Wave.generateWave(this.tutSpeedBoatX, this.tutSpeedBoatY, this.speedBoatWaveDamage, shoreBreakYCoord-this.speedBoatWaveBreakOffset, this.speedBoatWaveSpeed, this.speedBoatXSpan));
			for(Wave w: this.respectiveWaves){
				w.setDrawable(true);
				w.setReleased(true);
			}
			
		}
		else if(this.typeBoat == BoatType.TANKER){
			this.respectiveWaves.add(Wave.generateWave(this.tutTankerX, this.tutTankerY, this.tankerWaveDamage, shoreBreakYCoord-this.tankerWaveBreakOffset, this.tankerWaveSpeed, this.tankerXSpan));
			for(Wave w: this.respectiveWaves){
				w.setDrawable(true);
				w.setReleased(true);
			}
		}
		
	}
	
	/**
	 * Used to see if all waves on screen are still drawable, total is used as a counter to see how many waves are not on screen. If it is equql to the size of
	 *  the boats wave array list then it will return false indicating all waves have crashed.
	 *  
	 * @return boolean true or false dependent upon whether if there are still waves on screen.
	 */
	public boolean checkWavesDrawable(){
		boolean bool = false;
		int total = 0;
		for (Wave w : this.respectiveWaves){
			bool = w.getDrawable(); 
			if(bool){w.move();}
			else{total++;}
		}
		if(total == this.respectiveWaves.size()){
			return false;
		}
		return true;
	}
	
	/**
	 * Cycles through all waves within the boats wave arraylist and checks to see if the boat has reached the xcoord to release the wave.
	 */
	public void checkForWaveRelease(){
		for (Wave w: this.respectiveWaves){
			//If boat is heading East, it then checks is at the specificied release xLoc or if the boat has passed it by the single increment of its movement
			if(this.currDir == enums.Direction.EAST && this.xloc >= w.getWaveReleaseXCoord() && this.xloc < this.xIncr+w.getWaveReleaseXCoord() && w.getReleased() == false){
				w.setDrawable(true);
				w.setReleased(true);
			}
			else if(this.currDir == enums.Direction.WEST && this.xloc <= w.getWaveReleaseXCoord() && this.xloc > w.getWaveReleaseXCoord()-this.xIncr && w.getReleased() == false){
				w.setDrawable(true);
				w.setReleased(true);
			}
			else{}
		}
	}
	

	
	
}
