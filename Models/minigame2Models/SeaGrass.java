package minigame2Models;

/**
 * A Minigame2 Sea Grass class
 * @author David
 *
 */
public class SeaGrass extends Structure {
	
	/**
	 * Sea Grass constructor 
	 * @param xClick x location of mouse click
	 * @param yClick y location of mouse click
	 */
	public SeaGrass(int xClick, int yClick){
		setCost(3);
		setXLoc(xClick-20);
		setYLoc(yClick);
	}
	
	/**
	 * Generates new sea grass objects
	 * @param xClick x location of mouse click
	 * @param yClick y location of mouse click
	 * @return a new sea grass object
	 */
	public static SeaGrass generateSeaGrass(int xClick, int yClick){
		return new SeaGrass(xClick, yClick);
	}
	
	/**
	 * Moves the Sea Grass
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
