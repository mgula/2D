package views;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

public class SettingsView extends MainView {
	private JButton backButton;
	private ButtonGroup debugToggle;
	private JRadioButton debugToggleOn;
	private JRadioButton debugToggleOff;
	private ButtonGroup windowedToggle;
	private JRadioButton windowedOn;
	private JRadioButton windowedOff;
	
	private JButton clearDataButton1;
	private JButton clearDataButton2;
	private JButton clearDataButton3;
	
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
	
	public JButton getClearDataButton1() {
		return this.clearDataButton1;
	}
	
	public JButton getClearDataButton2() {
		return this.clearDataButton2;
	}
	
	public JButton getClearDataButton3() {
		return this.clearDataButton3;
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
	
	public void paint(Graphics g) {
		for (int i = 0; i < settingsMsgs.length; i++) {
			g.drawString(this.settingsMsgs[i], this.settingsMsgsX[i], this.settingsMsgsY[i]);
		}
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}
	
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
		this.debugToggleOn.setSelected(true); //default to debug mode on
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
		this.clearDataButton1 = new JButton("clear data 1");
		this.clearDataButton1.setBounds(400, 500, this.getButtonWidth(), this.getButtonHeight());
		this.clearDataButton2 = new JButton("clear data 2");
		this.clearDataButton2.setBounds(600, 500, this.getButtonWidth(), this.getButtonHeight());
		this.clearDataButton3 = new JButton("clear data 3");
		this.clearDataButton3.setBounds(800, 500, this.getButtonWidth(), this.getButtonHeight());
		
		/*Add all components to array list for easier access later*/
		this.initComponentsArrayList();
		this.addComponent(this.clearDataButton1);
		this.addComponent(this.clearDataButton2);
		this.addComponent(this.clearDataButton3);
		this.addComponent(this.debugToggleOn);
		this.addComponent(this.debugToggleOff);
		this.addComponent(this.windowedOn);
		this.addComponent(this.windowedOff);
		this.addComponent(this.backButton);
	}
}
