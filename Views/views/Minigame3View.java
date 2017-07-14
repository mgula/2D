package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import games.Minigame3;
import minigame3Models.Cube;
import minigame3Models.CubePad;
import minigame3Models.Minigame3Model;

/**
 * Minigame 3's view class
 * Handles graphics and what user sees for game
 * @author kylew
 */
public class Minigame3View extends GameView {

	private JButton shuffleButton;
	private JButton submitButton;
	private JButton resetButton;
	private JTextArea textBox;
	private JScrollPane textScroller;

	private ArrayList<Minigame3Model> draw;
	private int screenWidth;
	private int ratio;
	private int offset;
	private boolean submit=false;
	
	private final int ASPECT_DIVIDE=1400; //divides screen
	private final int TITLE_SIZE=25;
	private final String DEFAULT_TEXT="Type the story you see here";
	private final int fontSize = 15;
	private final String font = "GillSansUltraBold";
	
	//Array of names of pictures
	private final String[] PICTURES = {"BassFrame","BoatFrame","CrabFrame","FisherFrame","IslandFrame","TreeFrame","ClamFrame","TrashFrame"};
	
	private BufferedImage[] cubeImages=new BufferedImage[PICTURES.length];
	private BufferedImage background=createImage("images/mini3Background.jpg");
	private BufferedImage title=createImage("images/mini3Title.png.png");
	
	/**
	 * Minigame 3 view constructor
	 * Sets aspect ratio based on user screen width
	 * @param w width of screen
	 * @param h height of screen
	 */
	public Minigame3View(int w, int h) {
		super(w, h);
		screenWidth=w;
		if(screenWidth<ASPECT_DIVIDE){
			ratio=130;
		}
		else{
			ratio=200;
		}
		offset=(screenWidth/PICTURES.length)-ratio;
	}
	
	public JButton getShuffleButton() {
		return this.shuffleButton;
	}
	
	public JButton getSubmitButton() {
		return this.submitButton;
	}
	
	public JButton getResetButton() {
		return this.resetButton;
	}
	
	public JScrollPane getTextBox() {
		return this.textScroller;
	}
	
	public void setStatus(boolean status){
		this.submit=status;
	}
	
	public void setSubmit(boolean status){
		this.submit=status;
	}
	
	public boolean getStatus(){
		return submit;
	}
	
	/**
	 * Resets the text in the textbox to the default
	 */
	public void resetText(){
		this.textBox.setText(DEFAULT_TEXT);
	}
	
	/**
	 * Paints the screen based on the submit mode
	 * If not in submit draws all of the cubes and cube pads in their locations
	 * If in submit draws images in cube pad location and draws textbox text
	 */
	public void paint(Graphics g) {
		g.drawImage(background,0,0,super.getWidth(),super.getHeight(),null);
		
		if(!submit){
			for (Minigame3Model m : draw) {
				if(m.getClass().equals(Cube.class))
					g.drawImage(cubeImages[((Cube)m).getImageNumber()], m.getXloc(), m.getYloc(),null);
				else
					g.drawRect(m.getXloc(), m.getYloc(), m.getDimensions(), m.getDimensions());
			}
		}
		else{
			for (Minigame3Model m : draw) {
				if(m.getClass().equals(CubePad.class))
					g.drawImage(cubeImages[((CubePad)m).getImageNumber()], m.getXloc(), m.getYloc(),null);
					g.setFont(new Font(this.font, Font.BOLD, this.fontSize));
					g.setColor(Color.WHITE);
					g.drawString(textBox.getText(),this.getScreenWidth()/3,(this.getScreenHeight()/3)*2);
			}
		}
		
		//title image
		g.drawImage(title,screenWidth/3,TITLE_SIZE,null);
		
		//Draw debug messages, if in debug mode
		if (this.getDebugMode()) {
			this.drawDebugOutput(g);
		}
	}
	
	/**
	 * Loops through string of images and adds all images to array
	 */
	public void loadImgs() {
		for(int i=0;i<PICTURES.length;i++){
			cubeImages[i]=this.createImage("images/"+PICTURES[i]+ratio+".png");
		}
	}
	
	/**
	 * Loads all of the models from minigame model
	 * @param game game mechanics and models to be loaed
	 */
	public void load(Minigame3 game) {
		this.draw = game.allModels;
	}
	
	/**
	 * Initializes all of the buttons for the minigame and readies them to be placed
	 */
	@Override
	public void initButtons() {
		super.initButtons();
		
		this.getBackButton().setBounds(ratio+(offset+ratio)-(2*this.getButtonWidth()), this.getButtonSlot6Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.shuffleButton = new JButton("Shuffle");
		this.shuffleButton.setBounds(this.getButtonXlocB(), this.getButtonSlot6Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.submitButton=new JButton("Submit");
		this.submitButton.setBounds(this.getButtonXlocB()+this.getButtonWidth(), this.getButtonSlot6Y(), this.getButtonWidth(), this.getButtonHeight());
		
		this.textBox=new JTextArea(DEFAULT_TEXT);
		this.textBox.setBounds(ratio+(offset+ratio), this.getButtonSlot6Y(), ratio+(offset+ratio)*3, this.getButtonHeight()*3);
		
		this.textScroller=new JScrollPane(textBox);
		this.textScroller.setBounds(ratio+(offset+ratio), this.getButtonSlot6Y(), ratio+(offset+ratio)*3, this.getButtonHeight()*3);
		
		this.resetButton=new JButton("Reset");
		this.resetButton.setBounds(this.getButtonXlocB()+this.getButtonWidth(), this.getButtonSlot6Y(), this.getButtonWidth(), this.getButtonHeight());
	}
}