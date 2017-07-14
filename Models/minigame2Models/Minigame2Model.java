package minigame2Models;

public interface Minigame2Model {
	
	public boolean getDrawable();
	public void setDrawable(boolean b);
	public void move();
	public int getXloc();
	public int getYloc();
	public boolean calcRange(int xRange, int yRange, int xClick, int yClick);
}
