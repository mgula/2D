package views;

import engine.GameEngine;
import enums.AppState;
import enums.GameState;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

import models.*;

public class Game1View extends GameView {
	private GameEngine engine;
	private Room currRoom;
	private ArrayList<Model> environment;
	private Controllable player;
	
	private int mainSleepTime;
	
	private final int healthXloc = 5;
	private final int healthtYloc = 5;
	private final int pixelsPerHealth = 2;
	private int healthBarHeight = 20;
	private boolean playerDrawable = true;
	private int flash = 0;
	private final int flashMod = 10;
	private boolean rightArrow = false;
	private boolean leftArrow = false;
	private boolean spaceBar = false;
	private int lastYloc;
	private int staticScreenAreaX;
	private int staticScreenAreaY;
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
	private boolean viewStationaryX = true; // flag that is true if the screen is not moving left or right
	private boolean viewStationaryY = true; // flag that is true if the screen is not moving up or down
	private boolean viewMovingRight = false; // flag that is true only if the screen is moving right
	private boolean viewMovingLeft = false; // flag that is true only if the screen is moving left
	private boolean viewMovingUp = false; // etc.
	private boolean viewMovingDown = false;
	private final int debugMsgOffset1X = 20;
	private final int debugMsgOffset1Y = 5;
	private final int debugMsgOffset2 = 20;
	private final int[] debugMsgXlocs = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 350, 350, 550, 10, 10, 10, 10, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
	private final int[] debugMsgYlocs = {50, 70, 85, 100, 125, 140, 155, 660, 675, 690, 705, 690, 705, 705, 185, 200, 215, 230, 70, 85, 100, 115, 130, 145, 160, 175, 190, 220, 250, 265, 280, 295, 320, 335};
	
	public Game1View(int w, int h) {
		super(w, h);
		this.initialThresholdXR = (int)((double)w * this.upperRatio);
		this.initialThresholdXL = (int)((double)w * this.lowerRatio);
		this.initialThresholdYU = (int)((double)h * this.lowerRatio);
		this.initialThresholdYD = (int)((double)h * this.upperRatio);
		
		this.setStartingOffsets();
	}
	
	public void setStartingOffsets() {
		this.thresholdXR = this.initialThresholdXR;
		this.thresholdXL = this.initialThresholdXL;
		this.thresholdYU = this.initialThresholdYU;
		this.thresholdYD = this.initialThresholdYD;
		
		/*???*/
		this.thresholdYU = -343;
		this.thresholdYD = 0;
		this.playerOffsetY = -571;
		
		this.staticScreenAreaX = this.initialThresholdXR - this.initialThresholdXL;
		this.staticScreenAreaY = this.initialThresholdYD - this.initialThresholdYU;
	}
	
	public ArrayList<Model> getDraw() {
		return this.environment;
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
	
	public void paint(Graphics g) {
		this.drawScreen(g);
		
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
		
		if (this.getGame1State() == GameState.PAUSE){
			this.drawPauseMenu(g, AppState.GAME1);
		}
	}
	
	@Override
	public void drawPauseMenu(Graphics g, AppState appState) {
		super.drawPauseMenu(g, appState);
		
		switch (this.getPauseState()) {
		case PLAYER_INFO:
			g.drawString("Health: " + this.player.getCurrHealth() + "/" + this.player.getMaxHealth(), 500, 200);
			break;
		case SYSTEM:
			
			break;
		case DEBUG:
			g.drawString("Set max jumps: ", 500, 200);
			g.drawString("Current: " + this.engine.getMaxJumps(), 700, 200);
			g.drawString("Set floating threshold: ", 450, 230);
			g.drawString("Current: " + this.engine.getFloatingThreshold(), 700, 230);
			g.drawString("Set X increase: ", 480, 260);
			g.drawString("Current: " + this.player.getXIncr(), 700, 260);
			g.drawString("Set Y increase: ", 480, 290);
			g.drawString("Current: " + this.player.getYIncr(), 700, 290);
			g.drawString("Set Sleep Time: ", 480, 320);
			g.drawString("Current: " + this.mainSleepTime, 700, 320);
			break;
		}
	}
	
	public void drawScreen(Graphics g) {
		this.updateOffsets();
		
		/*Draw the current room*/
		g.drawRect(this.currRoom.getXLoc() - this.playerOffsetX, this.currRoom.getYLoc() - this.playerOffsetY - this.currRoom.getHeight(), this.currRoom.getWidth(), this.currRoom.getHeight());
		
		this.drawExits(g);
		this.drawEnvironment(g);
		this.drawPlayer(g);
		
		/*Draw HUD components: start with life*/
		g.setColor(Color.RED);
		g.fillRect(this.healthXloc, this.healthtYloc, this.pixelsPerHealth * this.player.getCurrHealth(), this.healthBarHeight);
		g.setColor(Color.BLACK);
		g.fillRect(this.healthXloc + (this.pixelsPerHealth * this.player.getCurrHealth()), this.healthtYloc, this.pixelsPerHealth * (this.player.getMaxHealth() - this.player.getCurrHealth()), this.healthBarHeight);
		
		/*Update last y location, in order to know when to use falling animation*/
		this.lastYloc = this.player.getYLoc();
	}
	
	public void drawExits(Graphics g) {
		g.setColor(Color.ORANGE);
		for (Exit e : this.currRoom.getRoomLinks()) {
			g.drawLine(e.getXLoc() - this.playerOffsetX, e.getYLoc() - this.playerOffsetY, e.getXLoc() + e.getWidth() - this.playerOffsetX, e.getYLoc() - e.getHeight() - this.playerOffsetY);
		}
		g.setColor(Color.BLACK);
	}
	
	public void drawEnvironment(Graphics g) {
		for (Model m : this.environment) {
			if (m instanceof models.Rock) {
				g.drawRect(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof models.Enemy) {
				g.setColor(Color.RED);
				g.fillRect(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof models.Marker) {
				
			} else if (m instanceof models.RegenArea) {
				g.setColor(Color.GREEN);
				g.fillRect(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof models.DamageArea) {
				g.setColor(Color.MAGENTA);
				g.fillRect(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof models.ForceDrawable) {
				g.setColor(Color.CYAN);
				g.fillRect(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof models.Autonomous) {
				g.setColor(Color.YELLOW);
				g.fillRect(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getWidth(), m.getHeight());
			} else if (m instanceof models.Platform) {
				g.setColor(Color.BLACK);
				g.drawLine(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getXLoc() + m.getWidth() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY);
			} else if (m instanceof models.Controllable) {
				if (m != this.player) {
					g.setColor(Color.CYAN);
					g.drawRect(m.getXLoc() - this.playerOffsetX, m.getYLoc() - this.playerOffsetY, m.getWidth(), m.getHeight());
				}
			}
		}
		g.setColor(Color.BLACK);
	}
	
	public void drawPlayer(Graphics g) {
		if (this.engine.getEnemyCollision()) {
			if (this.getGame1State() != GameState.PAUSE) {
				this.flash = this.engine.getDamageCoolDown();
			}
			if (this.flash % this.flashMod == 0) {
				this.playerDrawable = !this.playerDrawable;
			}
		} else {
			this.flash = 0;
		}
		
		if (this.playerDrawable) {
			g.drawRect(this.player.getXLoc() - this.playerOffsetX, this.player.getYLoc() - this.playerOffsetY, this.player.getWidth(), this.player.getHeight());
		}
	}
	
	@Override
	public void drawDebugOutput(Graphics g) {
		/*App state info*/
		super.drawDebugOutput(g);
		/*Player info*/
		g.setFont(new JLabel().getFont());
		g.setColor(Color.BLACK);
		g.drawRect(this.player.getXLoc() - this.playerOffsetX, this.player.getYLoc() - this.playerOffsetY, this.player.getWidth(), this.player.getHeight()); //hitbox
		String message = "X: " + this.player.getXLoc() + ", Y: " + this.player.getYLoc();
		g.drawString(message, this.player.getXLoc() - this.playerOffsetX - this.debugMsgOffset1X, this.player.getYLoc() - this.debugMsgOffset1Y - this.playerOffsetY); //location
		message = "damaged: " + this.engine.getEnemyCollision();
		g.drawString(message, this.player.getXLoc() - this.playerOffsetX - this.debugMsgOffset2, this.player.getYLoc() - this.debugMsgOffset2 - this.playerOffsetY); //damage boolean
		String[] debugMessages = {"Health: " + this.player.getCurrHealth() + "/" + this.player.getMaxHealth(), "Static screen bool (X): " + this.viewStationaryX, "Screen moving left bool: " + this.viewMovingLeft, 
				"Screen moving right bool: " + this.viewMovingRight, "Static screen bool (Y): " + this.viewStationaryY, "Screen moving up bool: " + this.viewMovingUp, "screen moving down bool: " + this.viewMovingDown, 
				"Y screen thresh (U): " + this.thresholdYU, "Y screen thresh (D): " + this.thresholdYD, "X screen thresh (R): " + this.thresholdXR, "X screen thresh (L): " + this.thresholdXL, 
				"Player offset X: " + this.playerOffsetX, "Player offset Y: " + this.playerOffsetY, "Current room: " + this.currRoom.getID(), "Initial X Threshold (R): " + this.initialThresholdXR,
				"Initial X Threshold (L): " + this.initialThresholdXL, "Initial Y Threshold (U): " + this.initialThresholdYU, "Initial Y Threshold (D): " + this.initialThresholdYD, "On surface bottom: " + 
				this.player.isOnSurfaceBottom(), "Against surface top: " + this.player.isAgainstSurfaceTop(), "Against surface right: " + this.player.isAgainstSurfaceRight(), "Against surface left: " +
				this.player.isAgainstSurfaceLeft(), "On moving surface bottom: " + this.player.isOnMovingSurfaceBottom(), "Against moving surface bottom: " + this.player.isAgainstMovingSurfaceBottom(), 
				"Against moving surface top: " + this.player.isAgainstMovingSurfaceTop(), "Against moving surface right: " + this.player.isAgainstMovingSurfaceRight(), "Against moving surface left: " +
				this.player.isAgainstMovingSurfaceLeft(), "On platform: " + this.player.isOnPlatform(), "Jump duration (final): " + this.engine.getJumpDuration(), "Max jumps: " + this.engine.getMaxJumps(), 
				"Jump counter: " + this.engine.getJumpCounter(), "Jump number: " + this.engine.getJumpNumber(), "Floating threshold: " + this.engine.getFloatingThreshold(), "Floating counter: " + this.engine.getFloatingCounter()};
		for (int i = 0; i < debugMessages.length; i++) {
			g.drawString(debugMessages[i], this.debugMsgXlocs[i], this.debugMsgYlocs[i]);
		}
		/*Environment and Enemy info*/ 
		for (Model m : this.environment) {
			message = "X: " + m.getXLoc() + ", Y: " + m.getYLoc();
			g.drawString(message, m.getXLoc() - this.playerOffsetX - 20, m.getYLoc() - 5 - this.playerOffsetY);
		}
	}
	
	public void load(GameEngine engine) {
		this.playerDrawable = true;
		this.flash = 0;
		this.engine = engine;
		this.player = engine.getPlayer();
		this.currRoom = engine.getCurrRoom();
		this.lastYloc = this.player.getYLoc();
		this.environment = engine.getEnvironment();
		
		this.updateOffsets();
	}
	
	public void loadPlayer(GameEngine engine) {
		this.player = engine.getPlayer();
		this.environment = engine.getEnvironment();
		
		this.updateOffsets();
	}
	
	public void updateView(GameEngine engine) {
		this.currRoom = engine.getCurrRoom();
		this.environment = engine.getEnvironment();
		this.lastYloc = this.player.getYLoc();
	}
	
	public void updateSleepTime(int s) {
		this.mainSleepTime = s;
	}
	
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
	
	@Override
	public void loadImgs() {
		
	}
	
	public void updateOffsets() {
		int playerX = this.player.getXLoc();
		int playerY = this.player.getYLoc();
		
		/*Update left/right booleans, x thresholds, and x object offset. First case: in between the two thresholds*/
		if (playerX <= this.thresholdXR && playerX >= this.thresholdXL) {
			/*Update player x offset only once when entering stationary view*/
			if (!this.viewStationaryX) {
				if (playerX > this.thresholdXR) {
					this.playerOffsetX = playerX - this.initialThresholdXR;
				} else if (playerX < this.thresholdXL){
					this.playerOffsetX = playerX - this.initialThresholdXL;
				} else {
					this.playerOffsetX = this.thresholdXR - this.initialThresholdXR;
				}
			}
			/*Update booleans*/
			this.viewMovingRight = false;
			this.viewMovingLeft = false;
			this.viewStationaryX = true;
		/*Second case: greater than right threshold*/
		} else if (playerX > this.thresholdXR) {
			/*Update player x offset*/
			this.playerOffsetX = playerX - this.initialThresholdXR;
			/*Shift both thresholds right*/
			this.thresholdXR = playerX;
			this.thresholdXL = this.thresholdXR - this.staticScreenAreaX;
			/*Update booleans*/
			this.viewMovingRight = true;
			this.viewMovingLeft = false;
			this.viewStationaryX = false;
		/*Third case: less than left threshold*/
		} else if (playerX < this.thresholdXL) {
			/*Update player x offset*/
			this.playerOffsetX = playerX - this.initialThresholdXL;
			/*Shift both thresholds left*/
			this.thresholdXL = playerX;
			this.thresholdXR = this.thresholdXL + this.staticScreenAreaX;
			/*Update booleans*/
			this.viewMovingLeft = true;
			this.viewMovingRight = false;
			this.viewStationaryX = false;
		}
		/*Update up/down booleans, y thresholds, and y object offset. First case: in between the two thresholds*/
		if (playerY >= this.thresholdYU && playerY <= this.thresholdYD) {
			/*Update player y offset only once when entering stationary view*/
			if (!this.viewStationaryY) {
				if (playerY < this.thresholdYU) {
					this.playerOffsetY = playerY - this.initialThresholdYU;
				} else if (playerY > this.thresholdYD){
					this.playerOffsetY = playerY - this.initialThresholdYD;
				} else {
					this.playerOffsetY = this.thresholdYU - this.initialThresholdYU;
				}
			}
			/*Update booleans*/
			this.viewMovingUp = false;
			this.viewMovingDown = false;
			this.viewStationaryY = true;
		/*Second case: less than the upper threshold*/
		} else if (playerY < this.thresholdYU) {
			/*Update player y offset*/
			this.playerOffsetY = playerY - this.initialThresholdYU;
			/*Shift both thresholds up*/
			this.thresholdYU = playerY;
			this.thresholdYD = this.thresholdYU + this.staticScreenAreaY;
			/*Update booleans*/
			this.viewMovingUp = true;
			this.viewMovingDown = false;
			this.viewStationaryY = false;
		/*Third case: greater than the lower threshold*/
		} else if (playerY > this.thresholdYD) {
			/*Update player y offset*/
			this.playerOffsetY = playerY - this.initialThresholdYD;
			/*Shift both thresholds down*/
			this.thresholdYD = playerY;
			this.thresholdYU = this.thresholdYD - this.staticScreenAreaY;
			/*Update booleans*/
			this.viewMovingDown = true;
			this.viewMovingUp = false;
			this.viewStationaryY = false;
		}
	}
}
