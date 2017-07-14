package minigame2Models;

/**
 * A MiniGame2 Gabian class
 * @author David
 *
 */
public class Gabian extends Structure {
	
	/**
	 * Gabian constructor 
	 * @param xClick x location of mouse click
	 * @param yClick y location of mouse click
	 */
	public Gabian(int xClick, int yClick){
		setCost(2);
		setXLoc(xClick-20);
		setYLoc(yClick);
		this.setXSpan(60);
		this.setHealth(20);
	}
	
	/**
	 * Generates new gabian objects
	 * @param xClick x location of mouse click
	 * @param yClick y location of mouse click
	 * @return a new gabian object
	 */
	public static Gabian generateGabian(int xClick, int yClick){
		return new Gabian(xClick, yClick);
	}

	/**
	 * Moves the gabian
	 */
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns y location of gabian
	 * @return y location of gabian
	 */
	@Override
	public int getYloc() {
		// TODO Auto-generated method stub
		return super.getYLoc();
	}

	/**
	 * Getter for drawable status
	 * @return boolean status of drawable
	 */
	@Override
	public boolean getDrawable() {
		return super.getDrawable();
	}

	/**
	 * Method to set drawable status
	 * @param b boolean determining if gabian is drawable
	 */
	@Override
	public void setDrawable(boolean b) {
		super.setDrawable(b);		
	}
}

