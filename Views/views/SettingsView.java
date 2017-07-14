package views;

import java.awt.Graphics;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;

/**
 * This view allows the player to toggle debug mode, toggle full screen
 * or windowed mode, and toggle the current state they are in.
 * 
 * @author marcusgula
 *
 */

public class SettingsView extends MainView {
	private JButton backButton;
	private ButtonGroup debugToggle;
	private JRadioButton debugToggleOn;
	private JRadioButton debugToggleOff;
	private ButtonGroup windowedToggle;
	private JRadioButton windowedOn;
	private JRadioButton windowedOff;
	private final int YLOC = 300;
	private final int Y_OFFSET = 30;
	private final int Y_OFFSET_2 = 20;
	private final int debugToggleSize = 25;
	private final String[] settingsMsgs = {"toggle debug mode:", "on", "off", "toggle windowed mode:",
			"windowed", "fullscreen"};
	private int[] settingsMsgsX;
	private final int[] settingsMsgsY = {this.YLOC, this.YLOC + this.Y_OFFSET, this.YLOC + this.Y_OFFSET, this.YLOC + (this.Y_OFFSET * 3),
			this.YLOC + (this.Y_OFFSET * 4), this.YLOC + (this.Y_OFFSET * 4), this.YLOC + (this.Y_OFFSET * 6), this.YLOC + (this.Y_OFFSET * 7),
			this.YLOC + (this.Y_OFFSET * 7), this.YLOC + (this.Y_OFFSET * 7)};
	
	public SettingsView(int w, int h) {
		super(w, h);
	}
	
	public JRadioButton getDebugToggleOn() {
		return this.debugToggleOn;
	}
	
	public JRadioButton getDebugToggleOff() {
		return this.debugToggleOff;
	}
	
	public JRadioButton getWindowedOn() {
		return this.windowedOn;
	}
	
	public JRadioButton getWindowedOff() {
		return this.windowedOff;
	}
	
	public JButton getBackButton() {
		return this.backButton;
	}
	
	/**
	 * Draw the settings view.
	 */
	public void paint(Graphics g) {
		for (int i = 0; i < settingsMsgs.length; i++) {
			g.drawString(this.settingsMsgs[i], this.settingsMsgsX[i], this.settingsMsgsY[i]);
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
		int[] temp = {this.getButtonXloc(), this.getButtonXloc(), this.getButtonXloc() + 45, this.getButtonXloc(), this.getButtonXloc(), this.getButtonXloc() + 120, this.getButtonXloc(), this.getButtonXloc(), this.getButtonXloc() + 100, this.getButtonXloc() + 220};
		this.settingsMsgsX = temp;
		this.backButton = new JButton("Back to select menu");
		this.backButton.setBounds(this.getButtonXloc(), this.getButtonSlot1Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		this.debugToggle = new ButtonGroup();
		this.debugToggleOn = new JRadioButton("On");
		this.debugToggleOn.setBounds(this.getButtonXloc() -5, this.YLOC + (this.Y_OFFSET * 2) - this.Y_OFFSET_2, this.debugToggleSize, this.debugToggleSize);
		this.debugToggleOff = new JRadioButton("Off");
		this.debugToggleOff.setSelected(true);
		this.debugToggleOff.setBounds(this.getButtonXloc() + 40, this.YLOC + (this.Y_OFFSET * 2) - this.Y_OFFSET_2, this.debugToggleSize, this.debugToggleSize);
		this.debugToggle.add(this.debugToggleOn);
		this.debugToggle.add(this.debugToggleOff);
		this.windowedToggle = new ButtonGroup();
		this.windowedOn = new JRadioButton("On");
		this.windowedOn.setBounds(this.getButtonXloc() + 15, this.YLOC + (this.Y_OFFSET * 5) - this.Y_OFFSET_2, this.debugToggleSize, this.debugToggleSize);
		this.windowedOff = new JRadioButton("Off");
		this.windowedOff.setSelected(true);
		this.windowedOff.setBounds(this.getButtonXloc() + 130, this.YLOC + (this.Y_OFFSET * 5) - this.Y_OFFSET_2, this.debugToggleSize, this.debugToggleSize);
		this.windowedToggle.add(this.windowedOn);
		this.windowedToggle.add(this.windowedOff);
	}
}
