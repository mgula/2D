package views;

import java.awt.Graphics;

import javax.swing.JButton;

import enums.AppState;

public class BetweenView extends MainView {
	private JButton okButton;
	private JButton newGameButton;
	private JButton loadPreviousGameButton;
	private int textXloc;
	private boolean firstTime = true;
	
	public BetweenView(int w, int h) {
		super(w, h);
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
	
	public boolean getFirstTime() {
		return this.firstTime;
	}
	
	public void setFirstTime(boolean b) {
		this.firstTime = b;
	}
	
	@Override
	public void paint(Graphics g) {
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}
	
	@Override
	public void initButtonLocations() {
		super.initButtonLocations();
		this.textXloc = this.getButtonXloc();
	}
	
	@Override
	public void initButtons() {
		this.okButton = new JButton("Got it");
		this.okButton.setBounds(this.getButtonXloc(), this.getButtonSlot4Y(), this.getButtonWidth(), this.getButtonHeight());
		this.newGameButton = new JButton("new game");
		this.newGameButton.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth(), this.getButtonHeight());
		this.loadPreviousGameButton = new JButton("resume");
		this.loadPreviousGameButton.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
	}
}
