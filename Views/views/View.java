package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import enums.AppState;
import enums.GameState;

/**
 * The underlying view class that all views will extend. All views will
 * override the paint(Graphics g) method.
 * 
 * @author marcusgula
 *
 */
public abstract class View extends JPanel {
	private int screenWidth;
	private int screenHeight;
	private boolean debugMode = false;
	private AppState currentAppState;
	private GameState game1State;
	private GameState lastGame1State;
	private int buttonWidth = 150;
	private int buttonHeight = 35;
	private int buttonXloc;
	private int buttonYloc;
	private int extraTextOffset = 50;
	private String flashingText;
	private int flashingTextCounter = 0;
	private int flashingTextThresh = 60;
	private final int fontSize = 20;
	private int buttonSlot1Y;
	private int buttonSlot2Y;
	private int buttonSlot3Y;
	private int buttonSlot4Y;
	private int buttonSlot5Y;
	private int buttonSlot6Y;
	private int buttonXlocB;
	
	public View(int w, int h) {
		this.screenHeight = h;
		this.screenWidth = w;
	}
	
	public int getScreenWidth() {
		return this.screenWidth;
	}
	
	public int getScreenHeight() {
		return this.screenHeight;
	}
	
	public int getButtonXloc() {
		return this.buttonXloc;
	}
	
	public int getButtonXlocB() {
		return this.buttonXlocB;
	}
	
	public int getButtonYloc(){
		return this.buttonYloc;
	}
	
	public int getButtonWidth() {
		return this.buttonWidth;
	}
	
	public int getButtonHeight() {
		return this.buttonHeight;
	}
	
	public int getExtraTextOffset() {
		return this.extraTextOffset;
	}
	
	public int getButtonSlot1Y() {
		return this.buttonSlot1Y;
	}
	
	public int getButtonSlot2Y() {
		return this.buttonSlot2Y;
	}
	
	public int getButtonSlot3Y() {
		return this.buttonSlot3Y;
	}
	
	public int getButtonSlot4Y() {
		return this.buttonSlot4Y;
	}
	
	public int getButtonSlot5Y() {
		return this.buttonSlot5Y;
	}
	
	public int getButtonSlot6Y() {
		return this.buttonSlot6Y;
	}
	
	public GameState getGame1State() {
		return this.game1State;
	}
	
	public GameState getLastGame1State() {
		return this.lastGame1State;
	}
	
	public boolean getDebugMode() {
		return this.debugMode;
	}
	
	public int getFlashingTextThresh() {
		return this.flashingTextThresh;
	}
	
	public String getFlashingText() {
		return this.flashingText;
	}
	
	public void setFlashingText(String message) {
		this.flashingText = message;
	}
	
	public void setDebugMode(boolean b) {
		this.debugMode = b;
	}
	
	/**
	 * Initialize buttons (after the view has established the width and height of 
	 * the device).
	 */
	public void initButtonLocations() {
		this.buttonXloc = (this.screenWidth / 2) - (this.buttonWidth / 2);
		this.buttonXlocB = ((this.screenWidth / 5) * 4) - (this.buttonWidth / 2);
		this.buttonYloc = this.screenHeight / 7;
		this.buttonSlot1Y = this.buttonYloc;
		this.buttonSlot2Y = this.buttonYloc * 2;
		this.buttonSlot3Y = this.buttonYloc * 3;
		this.buttonSlot4Y = this.buttonYloc * 4;
		this.buttonSlot5Y = this.buttonYloc * 5;
		this.buttonSlot6Y = (this.buttonYloc * 6) - (this.buttonYloc / 2);
	}
	
	/**
	 * Draw debug information to the screen.
	 * 
	 * @param g Graphics object
	 */
	public void drawDebugOutput(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(875, 0, 500, 60);
		g.setColor(Color.BLACK);
		g.drawString("curr app state: " + this.currentAppState, 1000, 10);
		g.drawString("curr game 1 state: " + this.game1State + ", last state: " + this.lastGame1State, 890, 25);
	}

	/**
	 * Update the view with information about all games, and the applciation.
	 * 
	 * @param curr current application state
	 * @param g1 current game 1 state
	 * @param g1last last game 1 state
	 * @param g2 current game 2 state
	 * @param g2last last game 2 state
	 * @param g3 current game 3 state
	 * @param g3last last game 3 state
	 */
	public void updateStates(AppState curr, GameState g1, 
			GameState g1last) {
		this.currentAppState = curr;
		this.game1State = g1;
		this.lastGame1State = g1last;
	}
	
	/**
	 * Draw flashing text on the screen.
	 * 
	 * @param g Graphics object
	 */
	public void drawFlashingText(Graphics g) {
		if (this.flashingTextCounter < this.getFlashingTextThresh()/2) {
			this.flashingTextCounter++;
		} else {
			this.flashingTextCounter++;
			if (this.flashingTextCounter > this.getFlashingTextThresh()) {
				this.flashingTextCounter = 0;
			}
			g.drawString(this.getFlashingText(), this.getButtonXloc() - 50, this.getButtonSlot5Y());
		}
	}
	
	/**
	 * Create the image at the given filepath.
	 * 
	 * @param filepath given filepath
	 * @return the created image
	 */
	public BufferedImage createImage(String filepath) {
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new FileInputStream(filepath));
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setSizes(int h, int w) {
		this.screenHeight = h;
		this.screenWidth = w;
	}
	
	/**
	 * Empty method to be overriden.
	 */
	public void initButtons(){};

}
