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
	private JButton mg2Button;
	private JButton mg3Button;
	private JButton controls;
	private JButton exitButton;
	private BufferedImage mainBackground;
	private String state = "Delaware";
	private boolean select = false;
	
	public MainView(int w, int h) {
		super(w, h);
		this.setFlashingText("Press Any Key to Continue");
	}
	
	public JButton getMg1Button() {
		return this.mg1Button;
	}
	
	public JButton getMg2Button() {
		return this.mg2Button;
	}
	
	public JButton getMg3Button() {
		return this.mg3Button;
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
		g.drawImage(mainBackground, 0, 0, null);
		if (!this.select) {
			this.drawFlashingText(g);
		} else {
			g.drawString(this.state, 10, 30);
		}
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}
	
	/**
	 * Based on the device's screen size, load the background image.
	 */
	public void loadBackgroundImgs() {
		if (this.getScreenHeight() > 900){
			this.mainBackground = this.createImage("images/mainTitle1080.png");
		} else {
			this.mainBackground = this.createImage("images/mainTitle720.png");
		}
	}
	
	/**
	 * Initialize buttons (after the view has established the width and height of 
	 * the device).
	 */
	@Override
	public void initButtons() {
		this.mg1Button = new JButton("Estuary Run");
		this.mg1Button.setBounds(this.getButtonXloc(), this.getButtonSlot1Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.mg2Button = new JButton("Shore Defense");
		this.mg2Button.setBounds(this.getButtonXloc(), this.getButtonSlot2Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.mg3Button = new JButton("Story Cubes");
		this.mg3Button.setBounds(this.getButtonXloc(), this.getButtonSlot3Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.controls = new JButton("Settings");
		this.controls.setBounds(this.getButtonXloc(), this.getButtonSlot4Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.exitButton = new JButton("Exit");
		this.exitButton.setBounds(this.getButtonXloc(), this.getButtonSlot5Y(), this.getButtonWidth(), this.getButtonHeight());
	}
	
	public BufferedImage getBackgroundImg() {
		return this.mainBackground;
	}
	
	public boolean getSelect() {
		return this.select;
	}
	
	public void setSelect() {
		this.select = true;
	}
	
	public void setState(String state) {
		this.state = state;
	}
}
