package minigame2Models;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 * Oystershell extends Item and is a clickable item on screen for the player to increase their score.
 * It has a value, x and y location.
 * 
 * @author npompetti
 *
 */
public class OysterShell extends Items {
	
	private OysterShell(int screenWidth, int screenHeight, int shoreBreak){
		setValue(2);
		setXLoc(generateRandomXCoord(screenWidth));
		setYLoc(generateRandomYCoord(screenHeight, shoreBreak));
	}
	
	public void move() {
		
	}
	
	public static OysterShell generateOyster(int screenWidth, int screenHeight, int shoreBreak ){
		return new OysterShell(screenWidth, screenHeight, shoreBreak);
	}

	
	public int getYloc() {
		return super.getYLoc();
	}
}
