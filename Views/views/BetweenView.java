package views;

import java.awt.Graphics;

import javax.swing.JButton;

import enums.AppState;

/**
 * BetweenView serves as the in-between screen for pressing a main view button
 * and actually entering the game. The view typically provides 2 options to the 
 * player: start a new game, or resume the paused one.
 * 
 * @author marcusgula
 *
 */
public class BetweenView extends MainView {
	private JButton okButton;
	private JButton newGameButton;
	private JButton loadPreviousGameButton;
	private JButton game1TutButton;
	private JButton debugButton;
	private JButton stage1Button;
	private JButton stage2Button;
	private JButton stage3Button;
	private int textXloc;
	private boolean firstTime = true;
	private AppState game; // need to know if this is the screen before game 1, 2 or 3
	
	public BetweenView(int w, int h, AppState s) {
		super(w, h);
		this.game = s;
	}
	
	public JButton getOkButton() {
		return this.okButton;
	}
	
	public JButton getNewGameButton() {
		return this.newGameButton;
	}
	
	public JButton getLoadButton() {
		return this.loadPreviousGameButton;
	}
	
	public JButton getGame1TutButton() {
		return this.game1TutButton;
	}
	
	public JButton getDebugButton() {
		return this.debugButton;
	}
	
	public JButton getStage1Button() {
		return this.stage1Button;
	}
	
	public JButton getStage2Button() {
		return this.stage2Button;
	}
	
	public JButton getStage3Button() {
		return this.stage3Button;
	}
	
	public boolean getFirstTime() {
		return this.firstTime;
	}
	
	public void setFirstTime(boolean b) {
		this.firstTime = b;
	}
	
	/**
	 * Only game 3 needs text drawn to the screen on the between view. Minigames 1 and 2
	 * have tutorials to explain the controls.
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(this.getBackgroundImg(), 0, 0, null);
		if (this.firstTime && this.game == AppState.GAME3) {
			String[] g3Text = {"Drag the cubes to create your story! ", 
					"Press the shuffle button for a new selection of cubes.",
					"You can submit your story after you have placed all of the cubes."};
			for (int i = 0; i < g3Text.length; i++) {
				g.drawString(g3Text[i], this.textXloc - 50 - (60*i), this.getButtonSlot1Y() + 30*i);
			}
		}
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}
	
	/**
	 * Initialize button locations (after the view has established the width and
	 * height of the device).
	 */
	@Override
	public void initButtonLocations() {
		super.initButtonLocations();
		this.textXloc = this.getButtonXloc();
	}
	
	/**
	 * Initialize buttons (after the view has established the width and height of 
	 * the device).
	 */
	@Override
	public void initButtons() {
		this.okButton = new JButton("Got it");
		this.okButton.setBounds(this.getButtonXloc(), this.getButtonSlot4Y(), this.getButtonWidth(), this.getButtonHeight());
		this.newGameButton = new JButton("New Game");
		this.newGameButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
		this.loadPreviousGameButton = new JButton("Continue last game");
		this.loadPreviousGameButton.setBounds(this.getButtonXloc(), this.getButtonSlot1Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		this.game1TutButton = new JButton("Tutorial");
		this.game1TutButton.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth(), this.getButtonHeight());
		/*Debug buttons*/
		this.stage1Button = new JButton("Stage 1");
		this.stage1Button.setBounds(this.getButtonXloc(), this.getButtonSlot1Y(), this.getButtonWidth(), this.getButtonHeight());
		this.stage2Button = new JButton("Stage 2");
		this.stage2Button.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
		this.stage3Button = new JButton("Stage 3");
		this.stage3Button.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth(), this.getButtonHeight());
		this.debugButton = new JButton("Debug room");
		this.debugButton.setBounds(this.getButtonXloc(), this.getButtonSlot4Y(), this.getButtonWidth(), this.getButtonHeight());
	}
}
