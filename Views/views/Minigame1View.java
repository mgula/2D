package views;

import enums.AppState;
import enums.Direction;
import enums.GameState;
import games.Minigame1;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import minigame1Models.*;

/**
 * Serves as the view of Minigame1. Uses a "following camera" feature to follow
 * the player around the map.
 * 
 * @author marcusgula
 *
 */

public class Minigame1View extends GameView {
	private JButton winButton;
	private JButton loseButton;
	private JButton playAgainButton;
	private Crab drawPlayer;
	private Map map;
	private ArrayList<Minigame1Model> draw;
	private BufferedImage heart;
	private final int heartXloc = 40;
	private final int heartYloc = 5;
	private final int heartOffset = 10;
	private BufferedImage arrow;
	private final int textureDimensions = 100;
	private boolean playerDrawable = true;
	private int flash = 0;
	private final int flashMod = 10;
	private boolean rightArrow = false;
	private boolean leftArrow = false;
	private boolean spaceBar = false;
	private int lastYloc;
	private final int staticScreenAreaX;
	private final int staticScreenAreaY;
	private final int extraSand = 6;
	private final int sandLayers = 4;
	private final int initialThresholdXR;
	private final int initialThresholdXL;
	private final int initialThresholdYU;
	private final int initialThresholdYD;
	private final double upperRatio = 5.0/7.0;
	private final double lowerRatio = 2.0/7.0;
	private int thresholdXR; // signal for the screen to start moving right
	private int thresholdXL; // signal for the screen to start moving left
	private int thresholdYU; // signal for the screen to start moving up
	private int thresholdYD; // signal for the screen to start moving down
	private int playerOffsetX; // x offset that drawscreen() will use to draw the player and evironment
	private int playerOffsetY; // y offset that drawscreen() will use to draw the player and environment
	private final int laterStageXR = 550;
	private final int laterStageYU = 200;
	private final int laterStageOffsetX = -365;
	private boolean viewStationaryX = true; // flag that is true if the screen is not moving left or right
	private boolean viewStationaryY = true; // flag that is true if the screen is not moving up or down
	private boolean viewMovingRight = false; // flag that is true only if the screen is moving right
	private boolean viewMovingLeft = false; // flag that is true only if the screen is moving left
	private boolean viewMovingUp = false; // etc.
	private boolean viewMovingDown = false;
	private final int debugMsgOffset1X = 20;
	private final int debugMsgOffset1Y = 5;
	private final int debugMsgOffset2 = 20;
	private final int debugMsg3X = 5;
	private final int debugMsg3Y = 380;
	private final int debugMsg4X = 550;
	private final int debugMsg4Y = 690;
	private final int[] debugMsgXlocs = {10, 10, 10, 10, 10, 10, 1120, 1120, 10, 10, 350, 350, 550};
	private final int[] debugMsgYlocs = {70, 85, 100, 125, 140, 155, 690, 705, 690, 705, 690, 705, 705};
	
	public Minigame1View(int w, int h) {
		super(w, h);
		this.initialThresholdXR = (int)((double)w * this.upperRatio);
		this.thresholdXR = this.initialThresholdXR;
		this.initialThresholdXL = (int)((double)w * this.lowerRatio);
		this.thresholdXL = this.initialThresholdXL;
		this.initialThresholdYU = (int)((double)h * this.lowerRatio);
		this.thresholdYU = this.initialThresholdYU;
		this.initialThresholdYD = (int)((double)h * this.upperRatio);
		this.thresholdYD = this.initialThresholdYD;
		this.staticScreenAreaX = this.initialThresholdXR - this.initialThresholdXL;
		this.staticScreenAreaY = this.initialThresholdYD - this.initialThresholdYU;
	}
	
	public Crab getPlayer() {
		return this.drawPlayer;
	}
	
	public ArrayList<Minigame1Model> getDraw() {
		return this.draw;
	}
	
	public JButton getWinButton() {
		return this.winButton;
	}
	
	public JButton getLoseButton() {
		return this.loseButton;
	}
	
	public JButton getPlayAgainButton() {
		return this.playAgainButton;
	}
	
	public void setRightArrow(boolean b) {
		this.rightArrow = b;
	}
	
	public void setLeftArrow(boolean b) {
		this.leftArrow = b;
	}
	
	public void setSpaceBar(boolean b) {
		this.spaceBar = b;
	}
	
	/**
	 * Draw the game to the screen.
	 */
	public void paint(Graphics g) {
		this.drawScreen(g);
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}
	
	public void drawScreen(Graphics g) {
		this.updateOffsets();
		/*Draw the ground - shouldn't be completely static (will fit to screen when y is increasing)*/
		g.drawLine(0, this.map.getGroundLevel() + this.drawPlayer.getHeight() - this.playerOffsetY, this.map.getWidth(), this.map.getGroundLevel() + this.drawPlayer.getHeight() - this.playerOffsetY);
		/*for (int i = -this.extraSand; i < this.map.getWidth()/this.textureDimensions + this.extraSand; i++) {
			for (int j = 0; j < this.sandLayers; j++) {
				g.drawImage(this.sand, (i*this.textureDimensions)  - this.playerOffsetX, (j*this.textureDimensions) + (this.map.getGroundLevel() + this.drawPlayer.getHeight() - this.playerOffsetY), null, this);
			}
		}*/
		this.drawEnvironment(g);
		this.drawPlayer(g);
		/*Draw HUD components: start with life*/
		for (int i = 0; i < this.drawPlayer.getHealth(); i++) {
			g.drawImage(this.heart, this.heartOffset + (i*this.heartXloc), this.heartYloc, null, this);
		}
		/*Draw pause menu on top everything else, if paused*/ 
		if (this.getGame1State() == GameState.PAUSE){
			this.drawPauseMenu(g, AppState.GAME1);
		}
		/*Draw additional messages, if applicable*/
		if (this.getGame1State() == GameState.WIN || this.getGame1State() == GameState.LOSE) {
			this.drawGameString(g, AppState.GAME1, this.getGame1State(), this.getLastGame1State());
		}
		/*Update last y location, in order to know when to use falling animation*/
		this.lastYloc = this.drawPlayer.getYloc();
	}
	
	/**
	 * Draw each object in the environment array list.
	 * 
	 * @param g Graphics object
	 */
	public void drawEnvironment(Graphics g) {
		for (Minigame1Model m : this.draw) {
			if (m instanceof minigame1Models.Sand) {
				g.drawRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof minigame1Models.Rock) {
				g.drawRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof minigame1Models.SeaDebris) {
				g.drawRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof minigame1Models.Enemy) {
				g.setColor(Color.RED);
				g.fillRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof minigame1Models.Marker) {
				//img = this.arrow;
			} else if (m instanceof minigame1Models.RegenArea) {
				g.setColor(Color.GREEN);
				g.fillRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof minigame1Models.CurrentDrawable) {
				g.setColor(Color.CYAN);
				g.fillRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof minigame1Models.Interactable) {
				g.setColor(Color.YELLOW);
				g.fillRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			}
		}
		g.setColor(Color.BLACK);
	}
	
	/**
	 * Draw the player on the correct frame.
	 *  
	 * @param g Graphics object
	 */
	public void drawPlayer(Graphics g) {
		if (this.drawPlayer.getEnemyCollision()) {
			if (this.getGame1State() != GameState.PAUSE) {
				this.flash = this.drawPlayer.getDamageCoolDown();
			}
			if (this.flash % this.flashMod == 0) {
				this.playerDrawable = !this.playerDrawable;
			}
		} else {
			this.flash = 0;
		}
		if (this.playerDrawable) {
			g.drawRect(this.drawPlayer.getXloc() - this.playerOffsetX, this.drawPlayer.getYloc() - this.playerOffsetY, this.drawPlayer.getWidth(), this.drawPlayer.getHeight());
		}
	}
	
	/**
	 * Draw debug information.
	 */
	@Override
	public void drawDebugOutput(Graphics g) {
		/*App state info*/
		super.drawDebugOutput(g);
		/*Player info*/
		g.setFont(new JLabel().getFont());
		g.setColor(Color.BLACK);
		g.drawRect(this.drawPlayer.getXloc() - this.playerOffsetX, this.drawPlayer.getYloc() - this.playerOffsetY, this.drawPlayer.getWidth(), this.drawPlayer.getHeight()); //hitbox
		String message = "X: " + this.drawPlayer.getXloc() + ", Y: " + this.drawPlayer.getYloc();
		g.drawString(message, this.drawPlayer.getXloc() - this.playerOffsetX - this.debugMsgOffset1X, this.drawPlayer.getYloc() - this.debugMsgOffset1Y - this.playerOffsetY); //location
		message = "damaged: " + this.drawPlayer.getEnemyCollision();
		g.drawString(message, this.drawPlayer.getXloc() - this.playerOffsetX - this.debugMsgOffset2, this.drawPlayer.getYloc() - this.debugMsgOffset2 - this.playerOffsetY); //damage boolean
		double salinityPercent = (double)this.drawPlayer.getXloc() / (double)this.map.getWidth() * 100;
		g.drawString(String.format("%.2f", 100 - salinityPercent) + " / 100", this.debugMsg3X, this.debugMsg3Y); //exact salinity %
		g.drawString(message, this.debugMsg4X, this.debugMsg4Y); //animation info
		String[] debugMessages = {"Stat bool (X): " + this.viewStationaryX, "Left bool: " + this.viewMovingLeft, "Right bool: " + this.viewMovingRight,
				"Stat bool (Y): " + this.viewStationaryY, "Up bool: " + this.viewMovingUp, "Down bool: " + this.viewMovingDown, "Y Thresh (U): " + this.thresholdYU,
				"Y Thresh (D): " + this.thresholdYD, "X Thresh (R): " + this.thresholdXR, "X Thresh (L): " + this.thresholdXL, "Player offset X: " + this.playerOffsetX,
				"Player offset Y: " + this.playerOffsetY};
		for (int i = 0; i < debugMessages.length; i++) {
			g.drawString(debugMessages[i], this.debugMsgXlocs[i], this.debugMsgYlocs[i]);
		}
		/*Environment and Enemy info*/ 
		for (Minigame1Model m : this.draw) {
			message = "X: " + m.getXloc() + ", Y: " + m.getYloc();
			g.drawString(message, m.getXloc() - this.playerOffsetX - 20, m.getYloc() - 5 - this.playerOffsetY);
		}
	}
	
	/**
	 * Initialize button locations (after the view has established the width and
	 * height of the device).
	 */
	@Override
	public void initButtons() {
		super.initButtons();
		this.winButton = new JButton("Continue to the next level");
		this.winButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth() + 2 * this.getExtraTextOffset(), this.getButtonHeight());
		this.loseButton = new JButton("Try again");
		this.loseButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
		this.playAgainButton = new JButton("Play again");
		this.playAgainButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
	}
	
	/**
	 * Sync the array list to draw with given environment from the given game.
	 * 
	 * @param game game to be synced with
	 */
	public void load(Minigame1 game) {
		this.offsetManager();
		this.playerDrawable = true;
		this.flash = 0;
		this.drawPlayer = game.getPlayer();
		this.map = game.getMap();
		this.lastYloc = this.drawPlayer.getYloc();
		this.draw = game.getEnvironment();
	}
	
	/**
	 * Load the given offsets based on the current state of the game.
	 */
	public void offsetManager() {
		if (this.getGame1State() == GameState.STAGE1) {
			this.restoreInitialOffsets();
			return;
		}
		if (this.getGame1State() == GameState.STAGE2 || this.getGame1State() == GameState.STAGE3) {
			this.loadLaterStageOffsets();
			return;
		}
	}
	
	/**
	 * Default offsets.
	 */
	public void restoreInitialOffsets() {
		this.thresholdXR = this.initialThresholdXR;
		this.thresholdXL = this.initialThresholdXL;
		this.thresholdYU = this.initialThresholdYU;
		this.thresholdYD = this.initialThresholdYD;
		
		this.playerOffsetX = 0;
		this.playerOffsetY = 0;
		
		/*Reset flash effect variables*/
		this.flash = 0;
		this.playerDrawable = true;
	}
	
	/**
	 * Offsets for stage 2 and 3.
	 */
	public void loadLaterStageOffsets() {
		this.thresholdXR = this.laterStageXR;
		this.thresholdXL = 0;
		this.thresholdYU = this.laterStageYU;
		
		this.playerOffsetX = this.laterStageOffsetX;
		this.playerOffsetY = 0;
	}
	
	/**
	 * Load the view's images that it will draw.
	 */
	@Override
	public void loadImgs() {
		/*BufferedImage im = ImageIO.read(new File(getClass().getResource("/resources/image.jpg").toURI()));*/
		this.heart = this.createImage("images/heart40x40.png");
		this.arrow = this.createImage("images/arrow.png");
	}
	
	/**
	 * Update offsets for the moving camera.
	 */
	public void updateOffsets() {
		/*Update left/right booleans, x thresholds, and x object offset. First case: in between the two thresholds*/
		if (this.drawPlayer.getXloc() <= this.thresholdXR && this.drawPlayer.getXloc() >= this.thresholdXL) {
			/*Update player x offset only once when entering stationary view*/
			if (!this.viewStationaryX) {
				if (this.drawPlayer.getXloc() > this.thresholdXR) {
					this.playerOffsetX = this.drawPlayer.getXloc() - this.initialThresholdXR;
				} else if (this.drawPlayer.getXloc() < this.thresholdXL){
					this.playerOffsetX = this.drawPlayer.getXloc() - this.initialThresholdXL;
				} else {
					this.playerOffsetX = this.thresholdXR - this.initialThresholdXR;
				}
			}
			/*Update booleans*/
			this.viewMovingRight = false;
			this.viewMovingLeft = false;
			this.viewStationaryX = true;
		/*Second case: greater than right threshold*/
		} else if (this.drawPlayer.getXloc() > this.thresholdXR) {
			/*Update player x offset*/
			this.playerOffsetX = this.drawPlayer.getXloc() - this.initialThresholdXR;
			/*Shift both thresholds right*/
			this.thresholdXR = this.drawPlayer.getXloc();
			this.thresholdXL = this.thresholdXR - this.staticScreenAreaX;
			/*Update booleans*/
			this.viewMovingRight = true;
			this.viewMovingLeft = false;
			this.viewStationaryX = false;
		/*Third case: less than left threshold*/
		} else if (this.drawPlayer.getXloc() < this.thresholdXL) {
			/*Update player x offset*/
			this.playerOffsetX = this.drawPlayer.getXloc() - this.initialThresholdXL;
			/*Shift both thresholds left*/
			this.thresholdXL = this.drawPlayer.getXloc();
			this.thresholdXR = this.thresholdXL + this.staticScreenAreaX;
			/*Update booleans*/
			this.viewMovingLeft = true;
			this.viewMovingRight = false;
			this.viewStationaryX = false;
		}
		/*Update up/down booleans, y thresholds, and y object offset. First case: in between the two thresholds*/
		if (this.drawPlayer.getYloc() >= this.thresholdYU && this.drawPlayer.getYloc() <= this.thresholdYD) {
			/*Update player y offset only once when entering stationary view*/
			if (!this.viewStationaryY) {
				if (this.drawPlayer.getYloc() < this.thresholdYU) {
					this.playerOffsetY = this.drawPlayer.getYloc() - this.initialThresholdYU;
				} else if (this.drawPlayer.getYloc() > this.thresholdYD){
					this.playerOffsetY = this.drawPlayer.getYloc() - this.initialThresholdYD;
				} else {
					this.playerOffsetY = this.thresholdYU - this.initialThresholdYU;
				}
			}
			/*Update booleans*/
			this.viewMovingUp = false;
			this.viewMovingDown = false;
			this.viewStationaryY = true;
		/*Second case: less than the upper threshold*/
		} else if (this.drawPlayer.getYloc() < this.thresholdYU) {
			/*Update player y offset*/
			this.playerOffsetY = this.drawPlayer.getYloc() - this.initialThresholdYU;
			/*Shift both thresholds up*/
			this.thresholdYU = this.drawPlayer.getYloc();
			this.thresholdYD = this.thresholdYU + this.staticScreenAreaY;
			/*Update booleans*/
			this.viewMovingUp = true;
			this.viewMovingDown = false;
			this.viewStationaryY = false;
		/*Third case: greater than the lower threshold*/
		} else if (this.drawPlayer.getYloc() > this.thresholdYD) {
			/*Update player y offset*/
			this.playerOffsetY = this.drawPlayer.getYloc() - this.initialThresholdYD;
			/*Shift both thresholds down*/
			this.thresholdYD = this.drawPlayer.getYloc();
			this.thresholdYU = this.thresholdYD - this.staticScreenAreaY;
			/*Update booleans*/
			this.viewMovingDown = true;
			this.viewMovingUp = false;
			this.viewStationaryY = false;
		}
	}
}
