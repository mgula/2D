package models;

import java.awt.Color;
import java.awt.Font;

public class TextArea extends EventArea {
	private static final long serialVersionUID = 1L;
	private String text;
	private boolean needsAction;
	private boolean activated;
	
	private int textXLoc;
	private int textYLoc;
	
	private int textSpeed;
	private Color textColor;
	private Font textFont;
	
	public TextArea(int x, int y, int h, int w, String text, boolean n, int s, Color c) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.text = text;
		this.needsAction = n;
		this.activated = false;
		
		this.textXLoc = x;
		this.textYLoc = y;
		
		this.textSpeed = s;
		this.textColor = c;
	}
	
	public TextArea(int x, int y, int h, int w, String text, boolean n, int s, Color c, int textX, int textY) {
		this.setXLoc(x);
		this.setYLoc(y);
		this.setHeight(h);
		this.setWidth(w);
		this.text = text;
		this.needsAction = n;
		this.activated = false;
		
		this.textXLoc = textX;
		this.textYLoc = textY;
		
		this.textSpeed = s;
		this.textColor = c;
	}
	
	public String getText() {
		return this.text;
	}
	
	public boolean needsAction() {
		return this.needsAction;
	}
	
	public boolean getActivated() {
		return this.activated;
	}
	
	public int getTextXLoc() {
		return this.textXLoc;
	}
	
	public int getTextYLoc() {
		return this.textYLoc;
	}
	
	public int getTextSpeed() {
		return this.textSpeed;
	}
	
	public Color getTextColor() {
		return this.textColor;
	}
	
	public Font getTextFont() {
		return this.textFont;
	}
	
	public void setActivated(boolean b) {
		this.activated = b;
	}
}
