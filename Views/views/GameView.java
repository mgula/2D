package views;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

import enums.AppState;
import enums.GameState;

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
	
	@Override
	public void initButtons() {
		this.resumeButton = new JButton("resume");
		this.resumeButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		this.backButton = new JButton("back to main menu");
		this.backButton.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
	}
	
	public void drawPauseMenu(Graphics g, AppState appState) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < 2; i++) {
			g.drawRect(this.getButtonXloc() - ((this.pauseMenuWidth - this.getButtonWidth() - this.getExtraTextOffset())/2) + i, this.pauseMenuYloc + i, this.pauseMenuWidth - 2 * i, this.pauseMenuHeight - 2 * i);
		}
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(this.getButtonXloc() - ((this.pauseMenuWidth - this.getButtonWidth() - this.getExtraTextOffset())/2) + 2, this.pauseMenuYloc + 2, this.pauseMenuWidth - 3, this.pauseMenuHeight - 3);
		g.setColor(Color.BLACK);
	}
	
	public void loadImgs() {}
}
