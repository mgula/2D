package views;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JTextField;

import enums.AppState;
import enums.PauseState;

public abstract class GameView extends View {
	private final int pauseMenuHeight = 500;
	private final int pauseMenuWidth = 700;
	private final int pauseMenuYloc = 50;
	
	private final int numTangles = 2;
	
	private PauseState pauseState = PauseState.PLAYERINFO;
	
	/*"Tabs"*/
	private JButton playerInfoButton;
	private JButton systemButton;
	private JButton debugButton;
	
	/*Buttons and fields*/
	private JButton backButton;
	private JButton resumeButton;
	private JButton restoreDefaultsButton;
	
	private JTextField editJumps;
	private JButton editJumpsButton;
	
	private JTextField editFloat;
	private JButton editFloatButton;
	
	private JTextField editXIncr;
	private JButton editXIncrButton;
	
	private JTextField editYIncr;
	private JButton editYIncrButton;
	
	private JTextField editSleepTime;
	private JButton editSleepTimeButton;
	
	public GameView(int w, int h) {
		super(w, h);
	}
	
	public JButton getPlayerInfoButton() {
		return this.playerInfoButton;
	}
	
	public JButton getSystemButton() {
		return this.systemButton;
	}
	
	public JButton getBackButton() {
		return this.backButton;
	}
	
	public JButton getDebugButton() {
		return this.debugButton;
	}
	
	public JButton getResumeButton() {
		return this.resumeButton;
	}
	
	public JButton getRestoreDefaultsButton() {
		return this.restoreDefaultsButton;
	}
	
	public JTextField getEditJumpsField() {
		return this.editJumps;
	}
	
	public JButton getEditJumpsButton() {
		return this.editJumpsButton;
	}
	
	public JTextField getEditFloatField() {
		return this.editFloat;
	}
	
	public JButton getEditFloatButton() {
		return this.editFloatButton;
	}
	
	public JTextField getEditXIncrField() {
		return this.editXIncr;
	}
	
	public JButton getEditXIncrButton() {
		return this.editXIncrButton;
	}
	
	public JTextField getEditYIncrField() {
		return this.editYIncr;
	}
	
	public JButton getEditYIncrButton() {
		return this.editYIncrButton;
	}
	
	public JTextField getEditSleepTimeField() {
		return this.editSleepTime;
	}
	
	public JButton getEditSleepTimeButton() {
		return this.editSleepTimeButton;
	}
	
	public PauseState getPauseState() {
		return this.pauseState;
	}
	
	public void setPauseState(PauseState state) {
		this.pauseState = state;
	}
	
	@Override
	public void initButtons() {
		this.playerInfoButton = new JButton("player");
		this.playerInfoButton.setBounds(350, 100, this.getButtonWidth(), this.getButtonHeight());
		
		this.systemButton = new JButton("system");
		this.systemButton.setBounds(500, 100, this.getButtonWidth(), this.getButtonHeight());
		
		this.debugButton = new JButton("debug");
		this.debugButton.setBounds(650, 100, this.getButtonWidth(), this.getButtonHeight());
		
		this.resumeButton = new JButton("resume");
		this.resumeButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		
		this.backButton = new JButton("back to main menu");
		this.backButton.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		
		this.restoreDefaultsButton = new JButton("restore defaults");
		this.restoreDefaultsButton.setBounds(500, 140, this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		
		this.editJumps = new JTextField("");
		this.editJumps.setBounds(600, 187, 50, 20);
		
		this.editJumpsButton = new JButton("set");
		this.editJumpsButton.setBounds(650, 180, this.getSetButtonWidth(), this.getButtonHeight());
		
		this.editFloat = new JTextField("");
		this.editFloat.setBounds(600, 217, 50, 20);
		
		this.editFloatButton = new JButton("set");
		this.editFloatButton.setBounds(650, 210, this.getSetButtonWidth(), this.getButtonHeight());
		
		this.editXIncr = new JTextField("");
		this.editXIncr.setBounds(600, 247, 50, 20);
		
		this.editXIncrButton = new JButton("set");
		this.editXIncrButton.setBounds(650, 240, this.getSetButtonWidth(), this.getButtonHeight());
		
		this.editYIncr = new JTextField("");
		this.editYIncr.setBounds(600, 277, 50, 20);
		
		this.editYIncrButton = new JButton("set");
		this.editYIncrButton.setBounds(650, 270, this.getSetButtonWidth(), this.getButtonHeight());
		
		this.editSleepTime = new JTextField("");
		this.editSleepTime.setBounds(600, 307, 50, 20);
		
		this.editSleepTimeButton = new JButton("set");
		this.editSleepTimeButton.setBounds(650, 300, this.getSetButtonWidth(), this.getButtonHeight());
	}
	
	public void drawPauseMenu(Graphics g, AppState appState) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < this.numTangles; i++) {
			g.drawRect(this.getButtonXloc() - ((this.pauseMenuWidth - this.getButtonWidth() - this.getExtraTextOffset())/2) + i, this.pauseMenuYloc + i, this.pauseMenuWidth - 2 * i, this.pauseMenuHeight - 2 * i);
		}
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(this.getButtonXloc() - ((this.pauseMenuWidth - this.getButtonWidth() - this.getExtraTextOffset())/2) + 2, this.pauseMenuYloc + 2, this.pauseMenuWidth - 3, this.pauseMenuHeight - 3);
		g.setColor(Color.BLACK);
	}
	
	public void loadImgs() {}
}
