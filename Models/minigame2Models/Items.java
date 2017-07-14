package minigame2Models;
import views.MainView;

import java.awt.Image;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;

/**
 * The item class is extended by oystershell and trash which are clickable items to be picked up by the user during the game.
 * 
 * @author npompetti
 *
 */
public abstract class Items implements Minigame2Model {
	//Add Visual Component and Images to address
	
	//Value is the amount of gold the player will be awarded for picking it up
	private int value;
	//x and yloc on screeen, will be randomly generated but have to be generated with respect to set up defenses
	private int xLoc;
	private int yLoc;
	private int screenWidthBoundsOffset = 10;
	private int screenHeightBoundsOffset = 100;
	
	private boolean drawable = true;
	
	@Override
	//if the item is not drawable the value must be set to 0 so that it can't still increase the users score even if on screen
	public void setDrawable(boolean b){
		this.drawable = b;
		if(b == false){
			this.value = 0;
		}
	}
	
	@Override
	public boolean getDrawable(){
		return this.drawable;
	}
		
	
	//Set items worth
	public void setValue(int value){
		this.value = value;
	}
	
	//Return items worth to update players usable score
	public int getValue(){
		return this.value;
	}
	
	//setter for x location on screen
	public void setXLoc(int x){
		this.xLoc = x;
	}
	
	public int getXloc() {
		return this.xLoc;
	}
	
	//setter for y location on screen
	public void setYLoc(int y){
		this.yLoc = y;
	}
	
	
	public int getYLoc(){
		return this.yLoc;
	}
	
	
	//generate random x coordinate for the item [0, maxHeight]
	/**
	 * Generates a random x coordinate for the item with the bounds of [minWidth, screenWidth - this.screenWidthBoundsOffset]
	 * 
	 * @param screenWidth The width of the players screen
	 * @return returns the randomly generated int.
	 */
	public int generateRandomXCoord(int screenWidth){
		int minWidth = 10;
		int randomNum = ThreadLocalRandom.current().nextInt(minWidth, screenWidth - this.screenWidthBoundsOffset);
		return randomNum;
	}
	
		
	/**
	 * Generate random y coordinate for the item with bounds of [shoreBreak, screenHeight - this.screenHeightBoundsOffset]
	 * Beach starts at 450, so the items will not spawn higher on the screen then that set value.
	 * 
	 * @param screenHeight
	 * @param shoreBreak
	 * @return returns the randomly generated int with specified by bounds.
	 */
	public int generateRandomYCoord(int screenHeight, int shoreBreak){
		int minHeight = 600;
		int randomNum = ThreadLocalRandom.current().nextInt(shoreBreak, screenHeight - this.screenHeightBoundsOffset);
		return randomNum;
		
	}
	
	
	
	
	/**
	 *  A method to consider the range of coordinates the items image spans and test to see if the mouse click lands within those coords
	 *  Returns true if the Click satisfies two conditions, it is within both the x and y range. It returns false if otherwise.
	 */
	public boolean calcRange(int xRange, int yRange, int xClick, int yClick){
		boolean withinXRange=false;
		boolean withinYRange=false;
		if(xClick <= xLoc+xRange && xClick >= xLoc-xRange){withinXRange = true;}
		if(yClick <= yLoc+yRange && yClick >= yLoc-yRange){withinYRange = true;}
		if(withinXRange && withinYRange){return true;}
		else{return false;}
	}

	

	
	

	
	
	

}
