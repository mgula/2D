package minigame2Models;

/**
 * A Minigame2 Concrete Wall class
 * @author David
 *
 */
public class Wall extends Structure {
	
	 /**
	 * Wall constructor 
	 * @param xClick x location of mouse click
	 * @param yClick y location of mouse click
	 */
	public Wall(int xClick, int yClick){
		setCost(1);
		setXLoc(xClick);
		setYLoc(yClick);	
		this.setHealth(10);
		this.setXSpan(60);
	}
	
	/**
	 * Generates new wall objects
	 * @param xClick x location of mouse click
	 * @param yClick y location of mouse click
	 * @return a new wall object
	 */
	public static Wall generateWall(int xClick, int yClick){
		return new Wall(xClick-20, yClick);
	}
	


	/**
	 * Moves the Wall
	 */
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public int getYloc() {
		// TODO Auto-generated method stub
		return super.getYLoc();
	}

	@Override
	public boolean getDrawable() {
		return super.getDrawable();
	}

	@Override
	public void setDrawable(boolean b) {
		super.setDrawable(b);		
	}
}
