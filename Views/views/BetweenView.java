package views;

import java.awt.Graphics;

import javax.swing.JButton;

import enums.AppState;

public class BetweenView extends MainView {
	private JButton okButton;
	private JButton newGameButton;
	private JButton loadPreviousGameButton;
	private JButton debugButton;
	private JButton stage1Button;
	private JButton stage2Button;
	private JButton stage3Button;
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
