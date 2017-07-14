package views;

import minigame2Models.Beach;
import minigame2Models.Boat;
import minigame2Models.Player;
import minigame2Models.Wave;
import minigame2Models.Minigame2Model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import enums.AppState;
import enums.BoatType;
import enums.Direction;
import enums.GameState;
import enums.TutorialState;
import games.Minigame2;

public class Minigame2View extends GameView {
	
	private JButton winButton;
	private JButton loseButton;
	private JButton playAgainButton;
	private JButton nextButton1;
	private JButton skipTutButton;
	
	private ArrayList<Minigame2Model> draw;
	
	private Player p1;
	private Beach beach;
	
	final private int sandDimensions = 100;
	final private int oceanR = 12;
	final private int oceanG = 164;
	final private int oceanB = 255;
	final private Color oceanColor = new Color(oceanR, oceanG, oceanB);
	
	private BufferedImage sand;
	private BufferedImage sandMeetsOcean;
	private int sandYLoc = this.getScreenHeight()-100;
	private int sandMeetsOceanYLoc = this.getScreenHeight()-200;
	
	private TutorialState tutState = TutorialState.NEXT1;
	
	
	private BufferedImage concreteWall;
	private BufferedImage gabian;
	private BufferedImage gabiandmg;
	private BufferedImage seaGrass;
	
	private BufferedImage banana;
	private BufferedImage oysterShell;
	
	private BufferedImage sailBoatEast;
	private BufferedImage sailBoatWest;
	private BufferedImage sailBoatWaveEast;
	private BufferedImage sailBoatWaveWest;
	private BufferedImage speedBoatEast;
	private BufferedImage speedBoatWest;
	private BufferedImage speedBoatWave;
	private BufferedImage tankerEast;
	private BufferedImage tankerWest;
	private BufferedImage tankerWave;
	
	private int sailBoatEastXOffset = 0;
	private int sailBoatEastYOffset = 80;
	private int sailBoatWestXOffset= 50;
	private int sailBoatWestYOffset = 50;
	private int speedBoatEastXOffset = 20;
	private int speedBoatEastYOffset = 80;
	private int speedBoatWestXOffset = 80;
	private int speedBoatWestYOffset = 80;
	private int tankerEastXOffset = 20;
	private int tankerEastYOffset = 80;
	private int tankerWestXOffset = 80;
	private int tankerWestYOffset = 80;
	
	private int bananaXOffset = 20;
	private int bananaYOffset = 5;
	private int oysterXOffset = 20;
	private int oysterYOffset = 20;



	
	private BufferedImage[] beach_Health;
	private int numBeachHealthImgs = 11;
	
	private int beachHealthXLoc = this.getScreenWidth() - 150;
	private int beachHealthYLoc = 3;
	private int beachHealthStringXLoc = this.beachHealthXLoc - 160;
	private int beachHealthStringYLoc = 20;
	
	private int tutTankerXLoc = 750;
	private int tutTankerYLoc = 150;
	private int tutSpeedBoatXLoc = 350;
	private int tutSpeedBoatYLoc = 300;
	private int tutSailBoatXLoc = 200;
	private int tutSailBoatYLoc = 400;
	
	//Attributes for graphics draw string, the location on screen and the font style
	private int scoreLabelXLoc = (this.getScreenWidth()/2);
	private int scoreLabelYLoc = 20;
	
	
	private int introduction1XLoc = this.scoreLabelXLoc-150;
	private int introduction1Yloc = this.sandMeetsOceanYLoc-40;
	private int introduction2XLoc = introduction1XLoc-100;
	private int introduction2YLoc = introduction1Yloc+30;
	
	//These variable are for modifying area where items to protect the shore can be placed 
	private int designatedStructureLocY = this.sandMeetsOceanYLoc;
	private int designatedStructureHeight = 20;
	private int designatedStructureWidth = 1600;
	private int desgintatedStructureR = 211;
	private int designatedStructureG = 211;
	private int designatedStructureB = 211;
	private int designatedStructureAlpha = 190;
	private int tutHighlightR = 255;
	private int tutHighlightG = 255;
	private int tutHighlightB = 0;
	private Color designatedStructureColor = new Color(this.desgintatedStructureR, this.designatedStructureG, this.designatedStructureB, this.designatedStructureAlpha);
	private Color tutorialHighlightColor = new Color(this.tutHighlightR, this.tutHighlightG, this.tutHighlightB, this.designatedStructureAlpha-100);
	private Color tutDesignatedStructureColor = designatedStructureColor;
	
	//Clickable Structure Variables
	private int selectGabianXLoc = 90;
	private int selectGabianYLoc = 40;
	private int selectWallXLoc = 90;
	private int selectWallYLoc = 85;
	private int selectSeaGrassXloc = 90;
	private int selectSeaGrassYloc = 130;
	private int selectBoxXLoc = 0;
	private int selectBoxGabianYLoc = 15;
	private int selectBoxWallYLoc = 55;
	private int selectBoxGrassYLoc = 95;
	private int selectBoxWidth = 75;
	private int selectBoxStructureHeight = 40;
	private int selectBoxGrassHeight = 55;
	private int gabianIconXLoc = 25;
	private int gabianIconYLoc = 20;
	private int wallIconXLoc = 25;
	private int wallIconYLoc = 60;
	private int seaGrassIconXLoc = 25;
	private int seaGrassIconYLoc = 100;
	
	private int skipTutButtonXLoc = this.selectGabianXLoc-85;
	private int skipTutButtonYLoc = this.sandMeetsOceanYLoc+50;
	private int skipTutButtonWidth = this.getButtonWidth()+50;
	
	
	
	public Minigame2View(int w, int h) {
		super(w, h);
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
	
	public JButton getNextButton() {
		return this.nextButton1;	
	}
	public JButton getSkipTutButton(){
		return this.skipTutButton;
	}
	
	/**
	 * Tethers the minigame2model objects, player and beach to the minigame2view.
	 * 
	 * @param game 	The minigame 2 model.x
	 */
	public void load(Minigame2 game){
		this.p1 = game.getPlayer();
		this.draw = game.getCurrentModels();
		this.beach = game.getBeach();
		
	}
	
	
	/**
	 * Paint method for minigame2 visuals that draws selection rectangles, the rectangle indicating where structures can be placed,
	 * all moving images on screen such as the boats and the waves as well as the clickable oysters and trash.
	 */
	public void paint(Graphics g) {	
		//Draw background
		g.setColor(oceanColor);
		g.fillRect(0,0, this.getScreenWidth(), this.getScreenHeight());
		
		this.drawScreen(g);
				
		
		
		g.setColor(Color.yellow);
		if(this.p1.getSelect() == 1) {
			g.drawRect(this.selectBoxXLoc, this.selectBoxGabianYLoc, this.selectBoxWidth, this.selectBoxStructureHeight);
		}
		else if(this.p1.getSelect() == 2){
			g.drawRect(this.selectBoxXLoc, this.selectBoxWallYLoc, this.selectBoxWidth, this.selectBoxStructureHeight);
		}
		else if(this.p1.getSelect() == 3){
			g.drawRect(this.selectBoxXLoc, this.selectBoxGrassYLoc, this.selectBoxWidth, this.selectBoxGrassHeight);
		}
		g.setColor(Color.BLACK);
		
		//draws zone where structures can be placed
		g.setColor(this.designatedStructureColor);
		g.fillRect(0, this.designatedStructureLocY, this.designatedStructureWidth, this.designatedStructureHeight);
		g.setColor(Color.black);
		
		
		
		//For loop that goes through all current items within draw which is tied to allModels within minigame2
		for (Minigame2Model m : draw) {
			
			//this statement draws the boats within the draw arrayList
			if (m instanceof minigame2Models.Boat) {
				Boat b = (Boat) m;
				if (b.getDrawable()) {
					if(b.getDirection() == Direction.EAST && b.getType() == BoatType.SAILBOAT){g.drawImage(this.sailBoatEast, b.getXloc(), b.getYloc()-this.sailBoatEastYOffset, null);}
					else if(b.getDirection() == Direction.WEST && b.getType() == BoatType.SAILBOAT){g.drawImage(this.sailBoatWest, b.getXloc()-this.sailBoatWestXOffset, b.getYloc()-this.sailBoatWestYOffset, null);}
					else if(b.getDirection() == Direction.EAST && b.getType() == BoatType.SPEEDBOAT){g.drawImage(this.speedBoatEast, b.getXloc()-this.speedBoatEastXOffset, b.getYloc()-this.speedBoatEastYOffset, null);}
					else if(b.getDirection() == Direction.WEST && b.getType() == BoatType.SPEEDBOAT){g.drawImage(this.speedBoatWest, b.getXloc()-this.speedBoatWestXOffset, b.getYloc()-this.speedBoatWestYOffset, null);}
					else if(b.getDirection() == Direction.EAST && b.getType() == BoatType.TANKER){g.drawImage(this.tankerEast, b.getXloc()-this.tankerEastXOffset, b.getYloc()-this.tankerEastYOffset, null);}
					else if(b.getDirection() == Direction.WEST && b.getType() == BoatType.TANKER){g.drawImage(this.tankerWest, b.getXloc()-this.tankerWestXOffset, b.getYloc()-this.tankerWestYOffset, null);}
				}
				
				//Sailboat Wave
				if(b.getType() == BoatType.SAILBOAT){
					if(b.getDirection() == Direction.EAST){
						for(Wave w : b.getWaves()){
							if(w.getDrawable()){ g.drawImage(this.sailBoatWaveEast, w.getXloc(), w.getYloc(), null);}
						}
					}
					else if(b.getDirection() == Direction.WEST){
						for(Wave w : b.getWaves()){
							if(w.getDrawable()){ g.drawImage(this.sailBoatWaveWest, w.getXloc(), w.getYloc(), null);}
						}
					}
				}
				
				
				
				else if(b.getType() == BoatType.SPEEDBOAT){
					for(Wave w : b.getWaves()){
						if(w.getDrawable()){ g.drawImage(this.speedBoatWave, w.getXloc(), w.getYloc(), null);}
					}
				}
				
				
				else if(b.getType() == BoatType.TANKER){
					for(Wave w : b.getWaves()){
						if(w.getDrawable()){ g.drawImage(this.tankerWave, w.getXloc(), w.getYloc(), null);}
					}
				}
				
				
				
			}
			
			//This if statement draws the active oysterShells within the draw arrayList
			if (m instanceof minigame2Models.OysterShell) {
				//Checking to see if oyster is still on the screen or has been clicked
				//If it has not been clicked its drawable status will remain true
				if (m.getDrawable()) {
					g.drawImage(this.banana, m.getXloc()-this.bananaXOffset, m.getYloc()-this.bananaYOffset, null);

				}
			}
			
			//This if statement draws the active Trash items within the draw arrayList
			if (m instanceof minigame2Models.Trash){
				//Same as the above for oyster
				if (m.getDrawable()) {
					g.drawImage(this.oysterShell, m.getXloc()-this.oysterXOffset, m.getYloc()-this.oysterYOffset, null);
				}
			}	
			
			//Draws Gabians
			if (m instanceof minigame2Models.Gabian){
				if (m.getDrawable()) {
					if (((minigame2Models.Gabian) m).getHealth() == 10){
						g.drawImage(this.gabiandmg, m.getXloc(), m.getYloc(), null);
					}
					else if (((minigame2Models.Gabian) m).getHealth() == 20){
						g.drawImage(this.gabian, m.getXloc(), m.getYloc(), null);
					}
				}
			}
			
			//Draw Walls
			if (m instanceof minigame2Models.Wall){
				if (m.getDrawable()) {
					g.drawImage(this.concreteWall, m.getXloc(), m.getYloc(), null);
				}
			}
			
			//Draw SeaGrass
			if (m instanceof minigame2Models.SeaGrass){
				if (m.getDrawable()) {
					g.drawImage(this.seaGrass, m.getXloc(), m.getYloc(), null);
				}
			}
			
		}
		
		
		/*Draw debug messages, if in debug mode.*/
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
		
		if (this.getGame2State() == GameState.WIN || this.getGame2State() == GameState.LOSE){
			this.drawGameString(g, AppState.GAME2, this.getGame2State(), this.getLastGame2State());
		}
		
		/*Draw pause menu on top everything else, if paused*/ 
		if (this.getGame2State() == GameState.PAUSE){
			this.drawPauseMenu(g, AppState.GAME2);
		}
	}
	/**
	 * loadImgs creates all necessary BufferedImages for Minigame2View by accessing the images folder.
	 */
	public void loadImgs(){
		this.sand = this.createImage("images/sand.png");
		this.sandMeetsOcean = this.createImage("images/sandMeetsOcean.png");
		this.concreteWall = this.createImage("images/concreteWall2.png");
		this.concreteWall = this.concreteWall.getSubimage(1, 1, 66, 30);
		this.gabian = this.createImage("images/Gabian2.png");
		this.gabiandmg = this.createImage("images/GabianDmg.png");
		this.seaGrass = this.createImage("images/kelp.png");
		this.seaGrass = this.seaGrass.getSubimage(1, 1, 60, 45);
		this.banana = this.createImage("images/banana.png");
		this.oysterShell = this.createImage("images/oysterShell.png");
		this.sailBoatEast = this.createImage("images/sailboatEast.png");
		this.sailBoatEast = this.sailBoatEast.getSubimage(0, 0, 120, 100);
		this.sailBoatWaveEast = this.createImage("images/sailBoatWaveEast.png");
		this.sailBoatWest = this.createImage("images/sailboatWest.png");
		this.sailBoatWest = this.sailBoatWest.getSubimage(0, 0, 120, 100);
		this.sailBoatWaveWest = this.createImage("images/sailBoatWaveWest.png");
		this.tankerEast = this.createImage("images/tankerEast.png");
		this.tankerWest = this.createImage("images/tankerWest.png");
		this.tankerWave = this.createImage("images/tankerWave.png");
		this.speedBoatEast = this.createImage("images/speedBoatEast.png");
		this.speedBoatWest = this.createImage("images/speedBoatWest.png");
		this.speedBoatWave = this.createImage("images/speedBoatWave.png");
		
		
		this.beach_Health = new BufferedImage[this.numBeachHealthImgs];
		String beach_img = "";
		for(int i = 0; i < this.numBeachHealthImgs; i++){
			beach_img = "images/Health"+i+"0.png";
			this.beach_Health[i] = this.createImage(beach_img);
		}
		
		
	}
	
	/**
	 * Generates a new BufferedImage for the beachHealth dependent upon how much health the beach has.
	 * 
	 * @return BufferedImage of the correct beachHealth
	 */
	
	public BufferedImage getBeachHealthImg(){
		if(this.beach.getHealth() == 100){
			return this.beach_Health[10];
		}
		else if(this.beach.getHealth() >= 90){
			return this.beach_Health[9];
		}
		else if(this.beach.getHealth() >= 80){
			return this.beach_Health[8];
		}
		else if(this.beach.getHealth() >= 70){
			return this.beach_Health[7];
		}
		else if(this.beach.getHealth() >= 60){
			return this.beach_Health[6];
		}
		else if(this.beach.getHealth() >= 50){
			return this.beach_Health[5];
		}
		else if(this.beach.getHealth() >= 40){
			return this.beach_Health[4];
		}
		else if(this.beach.getHealth() >= 30){
			return this.beach_Health[3];
		}
		else if(this.beach.getHealth() >= 20){
			return this.beach_Health[2];
		}
		else if(this.beach.getHealth() >= 10){
			return this.beach_Health[1];
		}
		else{
			return this.beach_Health[0];
		}
	}
	
	/**
	 * Draws all UI for minigame2 including beachHealth bar and clickable structures/seagrass that can be placed within the game in the upper right of the screen.
	 * It also handles all tutorial visuals which is dependent upon the tutState field and this field is incremented by pressing the continue button.
	 * @param g
	 */
	public void drawScreen(Graphics g) {
		/*Draw the score*/
		g.setColor(Color.BLACK);;
		g.drawString("Score: " + this.p1.getCurrentScore(), this.scoreLabelXLoc, this.scoreLabelYLoc);		
		
		
		//Draw beach health progress bar
		g.drawImage(getBeachHealthImg(), this.beachHealthXLoc, this.beachHealthYLoc, null);
		g.drawString("Shoreline Health: ", this.beachHealthStringXLoc, this.beachHealthStringYLoc);
		
		//Draw structure selector
		g.drawString("Click for Oyster Gabion", this.selectGabianXLoc, this.selectGabianYLoc);
		g.drawString("Click for Wall", this.selectWallXLoc, this.selectWallYLoc);
		g.drawString("Click for SeaGrass", this.selectSeaGrassXloc, this.selectSeaGrassYloc);
		//g.setFont(this.defaultFont); //revert back to default font 
		
		//Draw the beach from the sand and sandMeetsOcean images
		for(int i = 0; i <= this.getScreenWidth()/this.sandDimensions; i++){
			g.drawImage(this.sand, i*this.sandDimensions, this.sandYLoc, null);
			g.drawImage(this.sandMeetsOcean, i*this.sandDimensions, this.sandMeetsOceanYLoc, null);
		}
		
		g.setColor(Color.WHITE);
		if(this.tutState == TutorialState.NEXT1){
			g.drawString("Welcome to Shore Defenders!", this.introduction1XLoc, this.introduction1Yloc);
			g.drawString("Your Objective: To Clean And Protect The Shore From Erosion!", this.introduction2XLoc, this.introduction2YLoc);
		}
		else if(this.tutState == TutorialState.NEXT2){
			g.drawString("Pick Up Trash And Oystershells Along The Shore To Increase Your Score", this.introduction1XLoc - 200, this.introduction1Yloc);
			g.drawString("Use Your Score To Purchase Concrete Walls and Gabions For Defense", this.selectGabianXLoc + 250, this.selectGabianYLoc+10);
			g.setColor(Color.red);
			g.drawRect(this.selectBoxXLoc, this.selectBoxGabianYLoc, 320, 135);
			g.setColor(this.tutorialHighlightColor);
			g.fillRect(this.scoreLabelXLoc-5, this.scoreLabelYLoc-20, 90, 30);
			g.setColor(Color.black);
		}
		else if(this.tutState == TutorialState.NEXT3){
			g.drawString("You Could Place Concrete Walls Which Are Cheap..", this.selectGabianXLoc + 250, this.selectGabianYLoc + 30);
			g.drawString("But Actually Increase Coastal Erosion And Will Hurt Your Beach",this.selectGabianXLoc + 250, this.selectGabianYLoc + 55);
			g.setColor(this.tutorialHighlightColor);
			g.fillRect(this.wallIconXLoc-30, this.wallIconYLoc-5, 80, 40);
			g.setColor(Color.BLACK);
			
		}
		else if(this.tutState == TutorialState.NEXT4){
			g.drawString("Oyster Gabions Mimic Nature And Provide Great Defense Against Erosion", this.selectGabianXLoc + 250, this.selectGabianYLoc + 30);
			g.setColor(this.tutorialHighlightColor);
			g.fillRect(this.gabianIconXLoc-30, this.gabianIconYLoc-5, 80, 40);
			g.setColor(Color.BLACK);
		}
		else if(this.tutState == TutorialState.NEXT5){
			g.drawString("Spike Grass(aka salt grass) Can Withstand Salty Environments", this.selectGabianXLoc + 250, this.selectGabianYLoc + 30);
			g.drawString("Plant This Grass To Restore The Shore's Health", this.selectGabianXLoc + 250, this.selectGabianYLoc + 55);
			g.setColor(this.tutorialHighlightColor);
			g.fillRect(this.seaGrassIconXLoc-30, this.seaGrassIconYLoc, 80, 45);
			g.setColor(Color.BLACK);
		}
		
		else if(this.tutState == TutorialState.NEXT6){			
			g.drawString("Tankers Travel Slow..", this.tutTankerXLoc+75, this.tutTankerYLoc);
			g.drawString("But They Inflict The Most Erosion!", this.tutTankerXLoc+75, this.tutTankerYLoc+25);
			g.setColor(Color.RED);
			g.drawRect(520, 100, 300, 150);
			g.setColor(Color.BLACK);
		}
		else if(this.tutState == TutorialState.NEXT7){
			g.drawString("Speedboats Are Fast..", this.tutTankerXLoc+75, this.tutTankerYLoc);
			g.drawString("And Inflict Medium Erosion", this.tutTankerXLoc+75, this.tutTankerYLoc+25);
			g.setColor(Color.RED);
			g.drawRect(this.tutSpeedBoatXLoc-10, this.tutSpeedBoatYLoc-20, 200, 80);
			g.setColor(Color.BLACK);
		}
		else if(this.tutState == TutorialState.NEXT8){
			g.drawString("SailBoats Are Quick..", this.tutTankerXLoc+75, this.tutTankerYLoc);
			g.drawString("But Inflict Little Erosion", this.tutTankerXLoc+75, this.tutTankerYLoc+25);
			g.setColor(Color.RED);
			g.drawRect(this.tutSailBoatXLoc-20, this.tutSailBoatYLoc-60, 170, 100);
			g.setColor(Color.BLACK);
		}
		
		else if(this.tutState == TutorialState.NEXT9){
			g.drawString("Erosion From Waves Will Decrease The Shores Health", this.introduction1XLoc, this.introduction1Yloc);
			g.drawString("Place Structures Along The Shore To Protect It!", this.introduction1XLoc, this.introduction1Yloc + 30);
			g.setColor(this.tutorialHighlightColor);
			this.designatedStructureColor = this.tutorialHighlightColor;
			g.setColor(Color.red);
			g.drawRect(this.beachHealthXLoc - 3, this.beachHealthYLoc - 3, 125, 30);
			g.setColor(Color.black);
		}
		else if(this.tutState == TutorialState.FINAL){
			this.designatedStructureColor = this.tutDesignatedStructureColor;
			g.drawString("Press Continue To Begin Playing!", this.introduction1XLoc, this.introduction1Yloc);
		}
		
		
		g.setColor(Color.BLACK);
				
		
	}
	
	public TutorialState getTutState(){
		return this.tutState;
	}
	
	public void setTutStatus(TutorialState state){
		this.tutState = state;
	} 
	
	@Override
	public void initButtons() {
		super.initButtons();
		
		this.winButton = new JButton("Continue to the next level");
		this.winButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth() + 2 * this.getExtraTextOffset(), this.getButtonHeight());
		
		this.loseButton = new JButton("Try again");
		this.loseButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.playAgainButton = new JButton("Play again");
		this.playAgainButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.nextButton1 = new JButton("Continue");
		this.nextButton1.setBounds(this.getButtonXloc(), this.getButtonSlot6Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.skipTutButton = new JButton("Click Here To Skip Tutorial");
		this.skipTutButton.setBounds(this.skipTutButtonXLoc, this.skipTutButtonYLoc, this.skipTutButtonWidth, this.getButtonHeight());
		
	}
}
