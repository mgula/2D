package views;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

import enums.AppState;
import enums.GameState;

/**
 * All Minigame views will extend this class in order to use the 
 * drawGameString() and drawPauseMenu() methods.
 * 
 * @author marcusgula
 *
 */

public abstract class GameView extends View {
	private final int pauseMenuHeight = 500;
	private final int pauseMenuWidth = 500;
	private final int pauseMenuYloc = 50;
	private final int TEXT_OFFSET = 20;
	private JButton backButton;
	private JButton resumeButton;
	
	public GameView(int w, int h) {
		super(w, h);
	}
	
	public JButton getBackButton() {
		return this.backButton;
	}
	
	public JButton getResumeButton() {
		return this.resumeButton;
	}
	
	/**
	 * Initialize buttons (after the view has established the width and height of 
	 * the device).
	 */
	@Override
	public void initButtons() {
		this.resumeButton = new JButton("Resume");
		this.resumeButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		this.backButton = new JButton("Back to select menu");
		this.backButton.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
	}
	
	/**
	 * Draw the pause menu and controls for the current game.
	 * 
	 * @param g Graphics object
	 * @param appState current app state
	 */
	public void drawPauseMenu(Graphics g, AppState appState) {
		g.setColor(Color.BLACK);
		for(int p=0;p<3;p++){
			g.drawRect(this.getButtonXloc() - ((this.pauseMenuWidth - this.getButtonWidth() - this.getExtraTextOffset())/2)+p, this.pauseMenuYloc+p, this.pauseMenuWidth-2*p, this.pauseMenuHeight-2*p);
		}
		g.setColor(Color.WHITE);
		switch (appState) {
			case GAME1:
				g.drawString("Move Right: right arrow", this.getButtonXloc(), this.getButtonSlot1Y());
				g.drawString("Move Left: left arrow", this.getButtonXloc(), this.getButtonSlot1Y() + TEXT_OFFSET);
				g.drawString("Jump: space bar", this.getButtonXloc(), this.getButtonSlot1Y() + (TEXT_OFFSET*2));
				g.drawString("Pause the game: P key", this.getButtonXloc(), this.getButtonSlot1Y() + (TEXT_OFFSET*3));
				break;
				
			default:
				break;
		}
		g.setColor(Color.BLACK);
	}
	
	/**
	 * Draws a message to the screen based on the given app state and game state.
	 * 
	 * @param g graphics object 
	 * @param appState current application state
	 * @param gameState current game state
	 * @param lastState last game state
	 */
	public void drawGameString(Graphics g, AppState appState, GameState gameState, GameState lastState) {
		g.setColor(Color.BLACK);
		for (int p = 0; p < 3; p++){
			g.drawRect(this.getButtonXloc() - ((this.pauseMenuWidth - this.getButtonWidth() - this.getExtraTextOffset())/2)+p, this.pauseMenuYloc+p, this.pauseMenuWidth-2*p, this.pauseMenuHeight-2*p);
		}
		g.setColor(Color.WHITE);
		switch (appState) {
			case GAME1:
				switch (gameState) {
					case WIN:
						if (lastState == GameState.STAGE3) {
							g.drawString("Congratulations, you made it ", this.getButtonXloc() - 20, this.getButtonSlot1Y());
							g.drawString("to the estuary!", this.getButtonXloc(), this.getButtonSlot1Y() + 20);
						} else if (lastState == GameState.STAGE1 || lastState == GameState.STAGE2) {
							g.drawString("Nice, you migrated to lower salinity!", this.getButtonXloc() - 40, this.getButtonSlot1Y());
						}
						break;
						
					case LOSE:
						g.drawString("Your crab got tired and ", this.getButtonXloc() - 10, this.getButtonSlot1Y());
						g.drawString("decided to take a break.", this.getButtonXloc() - 10, this.getButtonSlot1Y() + 20);
						break;
						
					default:
						break;
				}
				break;
				
			default:
				break;
		}
		g.setColor(Color.BLACK);
	}
	
	/**
	 * Empty method to be overriden.
	 */
	public void loadImgs() {}
}
