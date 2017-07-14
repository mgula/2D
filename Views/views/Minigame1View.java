package views;

import enums.AppState;
import enums.Direction;
import enums.GameState;
import enums.TutorialState;
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
	private boolean tutorialMode;
	private Crab drawPlayer;
	private Map map;
	private ArrayList<Minigame1Model> draw;
	private BufferedImage heart;
	private final int heartXloc = 40;
	private final int heartYloc = 5;
	private final int heartOffset = 10;
	private BufferedImage seaDebris;
	private BufferedImage movingSeaDebris;
	private BufferedImage background1;
	private BufferedImage background2;
	private BufferedImage background3;
	private BufferedImage currBackground;
	private BufferedImage arrow;
	private BufferedImage sand;
	private BufferedImage rock;
	private final int textureDimensions = 100;
	private BufferedImage[] verticalFish;
	private int vFishHeight = 70;
	private int vFishWidth = 80;
	private int vFishImgs = 4;
	private int currentVFishFrame = 0;
	private int vFishFrameCounter = 0;
	private int vFishFrameThresh = 20;
	private BufferedImage[] horizontalFishRight;
	private BufferedImage[] horizontalFishLeft;
	private int hFishImgs = 4;
	private int currentHFishFrame = 0;
	private int hFishFrameCounter = 0;
	private int hFishFrameThresh = 20;
	private BufferedImage[][] crab;
	private int crabHeight = 40;
	private int crabWidth = 50;
	private int crabMoveStates = 4; // left or right, up, down, idle
	private int animationFrames = 2; // important: crab animations will simply alternate between 2 images.
	private int crabAnimState = 0;
	private int crabCurrentFrame = 0;
	private int crabAnimationCounter = 0;
	private int crabCurrentMod = 50;
	private BufferedImage[] salinityBar;
	private final int salinityBarXloc = 10;
	private final int salinityBarYloc = 260;
	private int salinityBarImgs = 13;
	private BufferedImage[] current;
	private final int currentHeight = 50;
	private final int currentWidth = 185;
	private int currentImgs = 7;
	private int currentCurrentFrame = 0;
	private int currentFrameCounter = 0;
	private int currentFrameThresh = 20;
	private BufferedImage[] kelp;
	private final int kelpDimensions = 50;
	private int kelpImgs = 2;
	private int currentKelpFrame = 0;
	private int kelpFrameCounter = 0;
	private int kelpFrameThresh = 30;
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
	private TutorialState tutState = TutorialState.CONTROLS1;
	private int tutTimer = 0;
	private int tutTimerThresh = 300;
	private boolean tutDrawSalin = false;
	private boolean tutDrawHealth = false;
	private boolean advanceConditions = false;
	private boolean tutRightPressed = false;
	private boolean tutLeftPressed = false;
	private boolean tutSpacePressed = false;
	private final int tutTimerDecrease = 80;
	private final int tutlTitleOffsetX = 70;
	private final int tutTitleOffsetY = 30;
	private final int numTutRects = 3;
	private final int tutRectX = 150;
	private final int tutRectY = 100;
	private final int tutRectWidth = 1030;
	private final int tutRectHeight = 340;
	private final int tutOffsetX = 400;
	private final int tutOffsetY = 80;
	private final int tutControls2OffsetX = 430;
	private final int tutControls3OffsetX = 460;
	private final int tutPredators1OffsetX = 150;
	private final int tutPredators1OffsetY = 30;
	private final int tutPredators2OffsetX = 380;
	private final int tutRegen1OffsetX = 440;
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
	
	public Minigame1View(int w, int h, boolean tutorial) {
		super(w, h);
		this.tutorialMode = tutorial;
		if (tutorial) {
			this.setFlashingText("Click anywhere to skip");
		}
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
	
	public TutorialState getTutState() {
		return this.tutState;
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
		if (!this.tutRightPressed) {
			this.tutRightPressed = true;
		}
	}
	
	public void setLeftArrow(boolean b) {
		this.leftArrow = b;
		if (!this.tutLeftPressed) {
			this.tutLeftPressed = true;
		}
	}
	
	public void setSpaceBar(boolean b) {
		this.spaceBar = b;
		if (!this.tutSpacePressed && this.tutState == TutorialState.CONTROLS2) {
			this.tutSpacePressed = true;
		}
	}
	
	public void setTutState(TutorialState t) {
		this.tutState = t;
		this.tutTimer = 0;
	}
	
	/**
	 * Draw the game to the screen.
	 */
	public void paint(Graphics g) {
		if (this.tutorialMode) {
			this.drawTutorialScreen(g);
		} else {
			this.drawRegularScreen(g);
		}
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}
	
	/**
	 * Draw the tutorial screen.
	 * 
	 * @param g Graphics object
	 */
	public void drawTutorialScreen(Graphics g) {
		g.drawImage(this.background1, 0, 0, this.getWidth(), this.getHeight(), null);
		g.drawString("Tutorial", this.getScreenWidth() / 2 - this.tutlTitleOffsetX, this.tutTitleOffsetY);
		if (this.tutTimer < this.tutTimerThresh) {
			this.tutTimer++;
		}
		for (int i = 0; i < this.numTutRects; i++) {
			g.drawRect(this.tutRectX - i, this.tutRectY - i, this.tutRectWidth + (2*i), this.tutRectHeight + (2*i));
		}
		String tutString = "";
		int tutStringX = 0;
		int tutStringY = 0;
		switch (this.tutState) {
			case CONTROLS1:
				tutString = "Use the left and right arrow keys to move around!";
				tutStringX = this.tutOffsetX;
				tutStringY = this.tutOffsetY;
				if (this.rightArrow || this.leftArrow) {
					this.advanceConditions = true;
				}
				break;
				
			case CONTROLS2:
				tutString = "Move right to decrease the salinity meter!";
				tutStringX = this.tutControls2OffsetX;
				tutStringY = this.tutOffsetY;
				this.advanceConditions = true;
				break;
				
			case CONTROLS3:
				tutString = "Press the space bar to jump!";
				tutStringX = this.tutControls3OffsetX;
				tutStringY = this.tutOffsetY;
				if (this.spaceBar) {
					if ((this.tutTimer > this.tutTimerThresh / 2) && !this.advanceConditions) {
						this.tutTimer -= this.tutTimerDecrease;
					}
					this.advanceConditions = true;
				}
				break;
				
			case ENVIRONMENT1:
				tutString = "Jump on objects like driftwood to get around!";
				tutStringX = this.tutOffsetX;
				tutStringY = this.tutOffsetY;
				this.advanceConditions = true;
				break;
				
			case PREDATORS1:
				tutString = "Watch your health!";
				tutStringX = this.tutPredators1OffsetX;
				tutStringY = this.tutPredators1OffsetY;
				this.advanceConditions = true;
				break;
				
			case PREDATORS2:
				tutString = "Avoid predators like bass and trout! They damage your health!";
				tutStringX = this.tutPredators2OffsetX;
				tutStringY = this.tutOffsetY;
				this.advanceConditions = true;
				break;
				
			case REGEN1:
				tutString = "Don't worry, kelp restores health!";
				tutStringX = this.tutRegen1OffsetX;
				tutStringY =this.tutOffsetY;
				this.advanceConditions = true;
				break;
				
			default: 
				break;
		}
		g.drawString(tutString, tutStringX, tutStringY);
		if (this.tutDrawHealth) {
			for (int i = 0; i < this.drawPlayer.getHealth(); i++) {
				g.drawImage(this.heart, this.heartOffset + (i*this.heartXloc), this.heartYloc, null, this);
			}
		}
		if (this.tutDrawSalin) {
			g.drawImage(this.getSalinityBar(), this.salinityBarXloc, this.salinityBarYloc, null, this);
		}
		if (this.tutTimer == this.tutTimerThresh && advanceConditions) {
			this.tutTimer = 0;
			this.advanceConditions = false;
			this.advanceTutState();
		}
		this.drawEnvironment(g);
		this.updateAnimations();
		this.drawPlayer(g);
		this.drawFlashingText(g);
	}
	
	/**
	 * Advance the tutorial to the next stage.
	 */
	public void advanceTutState() {
		this.draw = new ArrayList<Minigame1Model>();
		switch (this.tutState) {
			case CONTROLS1:
				this.tutDrawSalin = true;
				this.tutState = TutorialState.CONTROLS2;
				break;
				
			case CONTROLS2:
				this.tutState = TutorialState.CONTROLS3;
				break;
				
			case CONTROLS3:
				this.draw.add(new SeaDebris(345, 315));
				this.draw.add(new SeaDebris(700, 315));
				this.drawPlayer = new Crab(345, 200, 40, 50);
				this.tutState = TutorialState.ENVIRONMENT1;
				break;
				
			case ENVIRONMENT1:
				this.tutDrawHealth = true;
				this.tutState = TutorialState.PREDATORS1;
				break;
				
			case PREDATORS1:
				this.draw.add(new EnemyA(700, 350, 50, 50, Direction.EAST, 100, 4));
				this.drawPlayer = new Crab(170, 400, 40, 50);
				this.tutState = TutorialState.PREDATORS2;
				break;
				
			case PREDATORS2:
				this.draw.add(new RegenArea(350, 390, 50, 50));
				this.tutState = TutorialState.REGEN1;
				break;
				
			case REGEN1:
				this.tutDrawHealth = false;
				this.tutDrawSalin = false;
				this.tutState = TutorialState.FINAL;
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * Draw the non-tutorial screen.
	 * 
	 * @param g Graphics object
	 */
	public void drawRegularScreen(Graphics g) {
		/*Draw the background first.*/
		if (this.getGame1State() == GameState.STAGE3) {
			this.currBackground = this.background1;
		} else if (this.getGame1State() == GameState.STAGE2) {
			this.currBackground = this.background2;
		} else if (this.getGame1State() == GameState.STAGE1){
			this.currBackground = this.background3;
		}
		g.drawImage(this.currBackground, 0, 0, this.getWidth(), this.getHeight(), null);
		this.updateOffsets();
		/*Draw the ground - shouldn't be completely static (will fit to screen when y is increasing)*/
		for (int i = -this.extraSand; i < this.map.getWidth()/this.textureDimensions + this.extraSand; i++) {
			for (int j = 0; j < this.sandLayers; j++) {
				g.drawImage(this.sand, (i*this.textureDimensions)  - this.playerOffsetX, (j*this.textureDimensions) + (this.map.getGroundLevel() + this.drawPlayer.getHeight() - this.playerOffsetY), null, this);
			}
		}
		this.drawEnvironment(g);
		this.updateAnimations();
		this.drawPlayer(g);
		/*Draw HUD components: start with the salinity bar*/
		g.drawImage(this.getSalinityBar(), this.salinityBarXloc, this.salinityBarYloc, null, this);
		/*Draw life*/
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
			BufferedImage img = null;
			if (m instanceof minigame1Models.Sand) {
				this.drawTexture(g, m, this.sand, this.textureDimensions);
				continue;
			} else if (m instanceof minigame1Models.Rock) {
				g.drawImage(this.rock, m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight(), null, this);
				continue;
			} else if (m instanceof minigame1Models.SeaDebris) {
				img = this.seaDebris;
			} else if (m instanceof minigame1Models.EnemyA) {
				switch (((minigame1Models.EnemyA) m).getCurrDir()) {
					case EAST:
						img = this.horizontalFishRight[this.currentHFishFrame];
						break;
						
					case WEST:
						img = this.horizontalFishLeft[this.currentHFishFrame];
						break;
						
					default:
						break;
				}
			} else if (m instanceof minigame1Models.EnemyB) {
				img = this.verticalFish[this.currentVFishFrame];
			}else if (m instanceof minigame1Models.Marker) {
				img = this.arrow;
			} else if (m instanceof minigame1Models.RegenArea) {
				img = this.kelp[this.currentKelpFrame];
			} else if (m instanceof minigame1Models.CurrentDrawable) {
				img = this.current[this.currentCurrentFrame];
			} else if (m instanceof minigame1Models.Interactable) {
				img = this.movingSeaDebris;
			}
			g.drawImage(img, m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, null, this);
		}
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
			if (this.getGame1State() == GameState.STAGE1 || this.getGame1State() == GameState.STAGE2 || this.getGame1State() == GameState.STAGE3 || this.getGame1State() == GameState.DEBUG || this.getGame1State() == GameState.UNINITIALIZED) {
				this.updateCrabAnimationState(); // decide which crab animation to draw
				this.updateCrabFrame(); // decide which frame in the animation to draw
				this.crabAnimationCounter = (this.crabAnimationCounter + 1) % this.crabCurrentMod;
			}
			g.drawImage(this.crab[this.crabAnimState][this.crabCurrentFrame], this.drawPlayer.getXloc() - this.playerOffsetX, this.drawPlayer.getYloc() - this.playerOffsetY, null, this);
		}
	}
	
	/**
	 * Update the current frames for all enemies, currents, and kelp.
	 */
	public void updateAnimations() {
		if (this.vFishFrameCounter < this.vFishFrameThresh) {
			this.vFishFrameCounter++;
		} else {
			this.vFishFrameCounter = 0;
			this.currentVFishFrame++;
			if (this.currentVFishFrame == this.vFishImgs) {
				this.currentVFishFrame = 0;
			}
		}
		if (this.hFishFrameCounter < this.hFishFrameThresh) {
			this.hFishFrameCounter++;
		} else {
			this.hFishFrameCounter = 0;
			this.currentHFishFrame++;
			if (this.currentHFishFrame == this.hFishImgs) {
				this.currentHFishFrame = 0;
			}
		}
		if (this.currentFrameCounter < this.currentFrameThresh) {
			this.currentFrameCounter++;
		} else {
			this.currentFrameCounter = 0;
			this.currentCurrentFrame++;
			if (this.currentCurrentFrame == this.currentImgs) {
				this.currentCurrentFrame = 0;
			}
		}
		if (this.kelpFrameCounter < this.kelpFrameThresh) {
			this.kelpFrameCounter++;
		} else {
			this.kelpFrameCounter = 0;
			this.currentKelpFrame++;
			if (this.currentKelpFrame == this.kelpImgs) {
				this.currentKelpFrame = 0;
			}
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
		switch (this.crabAnimState) {
			case 0:
				message = "Animation state: crab idle";
				break;
				
			case 1:
				message = "Animation state: crab jumpin";
				break;
				
			case 2:
				message = "Animation state: crab falling";
				break;
				
			case 3:
				message = "Animation state: crab walking";
				break;
		}
		g.drawString(message, this.debugMsg4X, this.debugMsg4Y); //animation info
		String[] debugMessages = {"Stat bool (X): " + this.viewStationaryX, "Left bool: " + this.viewMovingLeft, "Right bool: " + this.viewMovingRight,
				"Stat bool (Y): " + this.viewStationaryY, "Up bool: " + this.viewMovingUp, "Down bool: " + this.viewMovingDown, "Y Thresh (U): " + this.thresholdYU,
				"Y Thresh (D): " + this.thresholdYD, "X Thresh (R): " + this.thresholdXR, "X Thresh (L): " + this.thresholdXL, "Player offset X: " + this.playerOffsetX,
				"Player offset Y: " + this.playerOffsetY, "Animation frame number: " + this.crabCurrentFrame};
		for (int i = 0; i < debugMessages.length; i++) {
			g.drawString(debugMessages[i], this.debugMsgXlocs[i], this.debugMsgYlocs[i]);
		}
		/*Environment and Enemy info*/ 
		for (Minigame1Model m : this.draw) {
			message = "X: " + m.getXloc() + ", Y: " + m.getYloc();
			g.drawString(message, m.getXloc() - this.playerOffsetX - 20, m.getYloc() - 5 - this.playerOffsetY);
			g.drawRect(m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, m.getWidth(), m.getHeight());
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
		if (this.tutorialMode) {
			this.loadTutOffsets();
			return;
		}
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
	 * Tutorial offsets.
	 */
	public void loadTutOffsets() {
		/*Don't care about offsets here, but they must be initialized to something*/
		this.thresholdXR = 0;
		this.thresholdXL = 0;
		this.thresholdYU = 0;
		this.thresholdYD = 0;
		
		this.playerOffsetX = 0;
		this.playerOffsetY = 0;
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
		this.sand = this.createImage("images/sand.png");
		this.background1 = this.createImage("images/underwater2.png");
		this.background2 = this.background1.getSubimage(0, 100, this.background1.getWidth(), this.background1.getHeight() - 100);
		this.background3 = this.background1.getSubimage(0, 200, this.background1.getWidth(), this.background1.getHeight() - 200);
		this.currBackground = this.background3;
		this.rock = this.createImage("images/biggerrocks.jpg");
		this.seaDebris = this.createImage("images/seadebris.png");
		this.movingSeaDebris = this.createImage("images/movingseadebris.png");
		this.arrow = this.createImage("images/arrow.png");
		this.crab = new BufferedImage[this.crabMoveStates][this.animationFrames];
		for (int i = 0; i < this.crabMoveStates; i++) {
			BufferedImage img = null;
			switch (i) {
				case 0:
					/*Idle animation - the image is 100x40.*/
					img = this.createImage("images/crabIdle.png");
					for (int j = 0; j < this.animationFrames; j++) {
						this.crab[i][j] = img.getSubimage(50*j, 0, this.crabWidth, this.crabHeight);
					}
					break;
					
				case 1:
					/*Jumping animation - only 1 frame*/
					this.crab[i][0] = this.createImage("images/crabJumping.png");
					break;
					
				case 2:
					/*Falling animation - only 1 frame*/
					this.crab[i][0] = this.createImage("images/crabFalling.png");
					break;
					
				case 3:
					/*Left/right animation*/
					img = this.createImage("images/crabWalk.png");
					for (int j = 0; j < this.animationFrames; j++) {
						this.crab[i][j] = img.getSubimage(50*j, 0, this.crabWidth, this.crabHeight);
					}
					break;
			}
		}
		
		this.verticalFish = new BufferedImage[this.vFishImgs];
		BufferedImage tempVFish = this.createImage("images/verticalfish.png");
		for (int i = 0; i < this.vFishImgs; i++) {
			this.verticalFish[i] = tempVFish.getSubimage(i * this.vFishWidth, 0, this.vFishWidth, this.vFishHeight);
		}
		
		this.horizontalFishLeft = new BufferedImage[this.hFishImgs];
		for (int i = 0; i < this.vFishImgs; i++) {
			this.horizontalFishLeft[i] = this.createImage("images/bass_left_" + i + ".png");
		}
		
		this.horizontalFishRight = new BufferedImage[this.hFishImgs];
		for (int i = 0; i < this.vFishImgs; i++) {
			this.horizontalFishRight[i] = this.createImage("images/bass_right_" + i + ".png");
		}
		
		this.salinityBar = new BufferedImage[this.salinityBarImgs];
		String salinImg = "";
		for (int i = 0; i < this.salinityBarImgs; i++) {
			salinImg = "images/salin" + i + ".png";
			this.salinityBar[i] = this.createImage(salinImg);
		}
		
		this.current = new BufferedImage[this.currentImgs];
		BufferedImage tempCurrent = this.createImage("images/Current.png");
		for (int i = 0; i < this.currentImgs; i++) {
			this.current[i] = tempCurrent.getSubimage(0, i * this.currentHeight, this.currentWidth, this.currentHeight);
		}
		
		this.kelp = new BufferedImage[this.kelpImgs];
		BufferedImage tempKelp = this.createImage("images/kelp.png");
		for (int i = 0; i < this.kelpImgs; i++) {
			this.kelp[i] = tempKelp.getSubimage(i * this.kelpDimensions, 0, this.kelpDimensions, this.kelpDimensions);
		}
	}
	
	/**
	 * Based on the current x location, return the correct salinity bar image.
	 * 
	 * @return salinity bar image
	 */
	public BufferedImage getSalinityBar() {
		double salinPercent = 100 - (double)this.drawPlayer.getXloc() / (double)this.map.getWidth() * 100;
		if (salinPercent == 100) {
			return this.salinityBar[0];
		} else if (salinPercent >= 91) {
			return this.salinityBar[1];
		} else if (salinPercent < 91 && salinPercent >= 82) {
			return this.salinityBar[2];
		} else if (salinPercent < 82 && salinPercent >= 73) {
			return this.salinityBar[3];
		} else if (salinPercent < 73 && salinPercent >= 64) {
			return this.salinityBar[4];
		} else if (salinPercent < 64 && salinPercent >= 55) {
			return this.salinityBar[5];
		} else if (salinPercent < 55 && salinPercent >= 46) {
			return this.salinityBar[6];
		} else if (salinPercent < 46 && salinPercent >= 37) {
			return this.salinityBar[7];
		} else if (salinPercent < 37 && salinPercent >= 28) {
			return this.salinityBar[8];
		} else if (salinPercent < 28 && salinPercent >= 19) {
			return this.salinityBar[9];
		} else if (salinPercent < 19 && salinPercent >= 10) {
			return this.salinityBar[10];
		} else if (salinPercent < 10 && salinPercent >= .01) {
			return this.salinityBar[11];
		} else if (salinPercent == 0) {
			return this.salinityBar[12];
		}
		return null;
	}
	
	/**
	 * Updates the crab's animation state.
	 */
	public void updateCrabAnimationState() {
		// 0 - idle
		// 1 - jump
		// 2 - fall
		// 3 - left/right
		if (!this.rightArrow && !this.leftArrow && !this.spaceBar && this.drawPlayer.getOnASurface()) {
			this.crabAnimState = 0;
		} else if (this.spaceBar) {
			this.crabAnimState = 1;
		} else if (this.drawPlayer.getYloc() < this.lastYloc && !this.drawPlayer.getOnASurface()) {
			this.crabAnimState = 1;
		} else if (this.rightArrow || this.leftArrow) {
			this.crabAnimState = 3;
		} else {
			this.crabAnimState = 2;
		}
	}
	
	/**
	 * Update the crab's frame.
	 */
	public void updateCrabFrame() {
		switch (this.crabAnimState) {
			case 0:
				this.crabCurrentMod = 60;
				if (this.crabAnimationCounter < 30) {
					this.crabCurrentFrame = 0;
					return;
				} else {
					this.crabCurrentFrame = 1;
					return;
				}
				
			case 1:
				this.crabCurrentFrame = 0;
				this.crabAnimationCounter = 0;
				return;
				
			case 2:
				this.crabCurrentFrame = 0;
				this.crabAnimationCounter = 0;
				return;
				
			case 3:
				this.crabCurrentMod = 10;
				if (this.crabAnimationCounter < 5) {
					this.crabCurrentFrame = 0;
					return;
				} else {
					this.crabCurrentFrame = 1;
					return;
				}
		}
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
	
	/**
	 * Given a sand object, fill the region described by the width and height completely with sand images 
	 * (and subimages). This method covers all sizes of sand objects (sand objects that are too large for the 
	 * 100x100 image, and sand objects that are too small for the 100x100 image).
	 * 
	 * @param g Graphics object
	 * @param m the given minigame1 model
	 * @param img the given image
	 * @param imgDimensions the image's dimensions
	 */
	public void drawTexture(Graphics g, Minigame1Model m, BufferedImage img, int imgDimensions) {
		/*If the dimensions are the same as the image (100x100), just draw the image and return.*/
		if (m.getWidth() == imgDimensions && m.getHeight() == imgDimensions) {
			g.drawImage(img, m.getXloc() - this.playerOffsetX, m.getYloc() - this.playerOffsetY, null, this);
			return;
		}
		/*Calculate the portions that will fall inside/outside of the 100x100 image.*/
		int xLeftover = m.getWidth() % imgDimensions;
		int yLeftover = m.getHeight() % imgDimensions;
		/*Calculate the bounds of the doubly nested for loop.*/
		int iBoundX = m.getWidth() / imgDimensions;
		int iBoundY = m.getHeight() / imgDimensions;
		/*Create subimages that will fill the gaps left when the width/height aren't cleanly divisible by the image dimensions.*/
		BufferedImage leftoversX = null;
		BufferedImage leftoversY = null;
		BufferedImage leftoversLast = null;
		/*If both dimensions are incompatible, the very last section will be a rectangle of size xLeftover, yLeftover.*/
		if (xLeftover != 0 && yLeftover != 0) {
			leftoversLast = img.getSubimage(0, 0, xLeftover, yLeftover);
		}
		/*Create subimage that will fill gaps for the x dimension.*/
		if (xLeftover != 0 && m.getWidth() != imgDimensions) {
			iBoundX++;
			int fillHeight = 0;
			if (m.getHeight() > imgDimensions) {
				fillHeight = imgDimensions;
			} else {
				fillHeight = m.getHeight();
			}
			leftoversX = img.getSubimage(0, 0, xLeftover, fillHeight);
		}
		/*Create subimage that will fill gaps for the y dimension.*/
		if (yLeftover != 0 && m.getHeight() != imgDimensions) {
			iBoundY++;
			int fillWidth = 0;
			if (m.getWidth() > imgDimensions) {
				fillWidth = imgDimensions;
			} else {
				fillWidth = m.getWidth();
			}
			leftoversY = img.getSubimage(0, 0, fillWidth, yLeftover);
		}
		/*Fill the region of the sand object using a doubly nested for loop.*/
		BufferedImage tempImg = null;
		for (int i = 0; i < iBoundX; i++) {
			for (int j = 0; j < iBoundY; j++) {
				if (i == iBoundX - 1) {
					if (j == iBoundY - 1) {
						tempImg = leftoversLast; // use the final chunk of size xLeftover, yLeftover
					} else {
						tempImg = leftoversX; // use the chunk that fills gaps for the x dimension
					}
				} else if (j == iBoundY - 1) {
					tempImg = leftoversY; // use the chunk that fills gaps for the y dimension
				} else {
					tempImg = img; // draw full sized (100x100) sand image
				}
				g.drawImage(tempImg, m.getXloc() - this.playerOffsetX + (i*imgDimensions), m.getYloc() - this.playerOffsetY + (j*imgDimensions), null, this);
			}
		}
	}
}
