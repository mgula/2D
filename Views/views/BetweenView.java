package views;

import java.awt.Graphics;
import javax.swing.JButton;

public class BetweenView extends MainView {
	private JButton backButton;
	private JButton newGameButton1;
	private JButton newGameButton2;
	private JButton newGameButton3;
	private JButton loadGameButton1;
	private JButton loadGameButton2;
	private JButton loadGameButton3;
	
	private boolean firstTime = true;
	
	private String dataString1 = "";
	private String dataString2 = "";
	private String dataString3 = "";
	
	private int buttonXLoc = 950;
	
	public BetweenView(int w, int h) {
		super(w, h);
	}
	
	public JButton getBackButton() {
		return this.backButton;
	}
	
	public JButton getNewGameButton1() {
		return this.newGameButton1;
	}
	
	public JButton getNewGameButton2() {
		return this.newGameButton2;
	}
	
	public JButton getNewGameButton3() {
		return this.newGameButton3;
	}
	
	public JButton getLoadGameButton1() {
		return this.loadGameButton1;
	}
	
	public JButton getLoadGameButton2() {
		return this.loadGameButton2;
	}
	
	public JButton getLoadGameButton3() {
		return this.loadGameButton3;
	}
	
	public boolean getFirstTime() {
		return this.firstTime;
	}
	
	public void setFirstTime(boolean b) {
		this.firstTime = b;
	}
	
	public void setDataString(String s1, String s2, String s3) {
		this.dataString1 = s1;
		this.dataString2 = s2;
		this.dataString3 = s3;
	}
	
	@Override
	public void paint(Graphics g) {
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
		g.drawString("Save 1", 155, 165);
		g.drawString("Save 2", 155, 365);
		g.drawString("Save 3", 155, 565);
		
		g.drawRect(150, 150, 1000, 100);
		g.drawRect(150, 350, 1000, 100);
		g.drawRect(150, 550, 1000, 100);
		
		g.drawString(this.dataString1, 210, 170);
		g.drawString(this.dataString2, 210, 370);
		g.drawString(this.dataString3, 210, 570);
	}
	
	@Override
	public void initButtons() {
		this.backButton = new JButton("Back to select menu");
		this.backButton.setBounds(this.getButtonXloc(), this.getButtonSlot1Y(), this.getButtonWidth() + this.getExtraTextOffset(), this.getButtonHeight());
		this.newGameButton1 = new JButton("new game 1");
		this.newGameButton1.setBounds(this.buttonXLoc, 200, this.getButtonWidth(), this.getButtonHeight());
		this.loadGameButton1 = new JButton("continue 1");
		this.loadGameButton1.setBounds(this.buttonXLoc, 200, this.getButtonWidth(), this.getButtonHeight());
		this.newGameButton2 = new JButton("new game 2");
		this.newGameButton2.setBounds(this.buttonXLoc, 400, this.getButtonWidth(), this.getButtonHeight());
		this.loadGameButton2 = new JButton("continue 2");
		this.loadGameButton2.setBounds(this.buttonXLoc, 400, this.getButtonWidth(), this.getButtonHeight());
		this.newGameButton3 = new JButton("new game 3");
		this.newGameButton3.setBounds(this.buttonXLoc, 600, this.getButtonWidth(), this.getButtonHeight());
		this.loadGameButton3 = new JButton("continue 3");
		this.loadGameButton3.setBounds(this.buttonXLoc, 600, this.getButtonWidth(), this.getButtonHeight());
	}
}
