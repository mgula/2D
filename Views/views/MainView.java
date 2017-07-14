package views;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

/**
 * This view serves as the select menu.
 * 
 * @author marcusgula
 *
 */
public class MainView extends View { 
	private JButton mg1Button;
	private JButton controls;
	private JButton exitButton;
	private boolean select = false;
	
	public MainView(int w, int h) {
		super(w, h);
		this.setFlashingText("press any key to continue");
	}
	
	public JButton getMg1Button() {
		return this.mg1Button;
	}
	
	public JButton getControlsButton() {
		return this.controls;
	}
	
	public JButton getExitButton() {
		return this.exitButton;
	}
	
	/**
	 * Draw the menu view.
	 */
	public void paint(Graphics g) {
		if (!this.select) {
			this.drawFlashingText(g);
		}
		
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}

	/**
	 * Initialize buttons (after the view has established the width and height of 
	 * the device).
	 */
	@Override
	public void initButtons() {
		this.mg1Button = new JButton("go");
		this.mg1Button.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.controls = new JButton("settings");
		this.controls.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.exitButton = new JButton("exit");
		this.exitButton.setBounds(this.getButtonXloc(), this.getButtonSlot4Y(), this.getButtonWidth(), this.getButtonHeight());
	}
	
	public boolean getSelect() {
		return this.select;
	}
	
	public void setSelect() {
		this.select = true;
	}
}
