package views;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JTextField;

import enums.AppState;
import enums.GameState;
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
	
	private JTextField editJumps;
	private JButton editJumpsButton;
	
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
	
	public JTextField getEditJumpsField() {
		return this.editJumps;
	}
	
	public JButton getEditJumpsButton() {
		return this.editJumpsButton;
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
		
		this.editJumps = new JTextField("");
		this.editJumps.setBounds(600, 200, 100, 20);
		
		this.editJumpsButton = new JButton("set");
		this.editJumpsButton.setBounds(700, 200, this.getButtonWidth(), this.getButtonHeight());
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
