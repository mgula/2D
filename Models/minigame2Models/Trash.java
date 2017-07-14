package minigame2Models;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Extends items and has a static generate trash method for in game use.
 * Trash has a value that increments the players score as well as a x and y coordinate.
 * 
 * @author npompetti
 *
 */
public class Trash extends Items {
	private Trash(int screenWidth, int screenHeight, int shoreBreak){
		setValue(5);
		setXLoc(generateRandomXCoord(screenWidth));
		setYLoc(generateRandomYCoord(screenHeight, shoreBreak));
	}
	
	public static Trash generateTrash(int screenWidth, int screenHeight, int shoreBreak){
		return new Trash(screenWidth, screenHeight, shoreBreak);
	}

	
	public void move() {
		// TODO Auto-generated method stub
		
	}

	public int getYloc() {
		// TODO Auto-generated method stub
		return super.getYLoc();
	}

	
}
