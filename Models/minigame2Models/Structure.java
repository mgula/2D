package minigame2Models;

/**
 * Structure in minigame 2. Oyster Gabian, Concrete Wall or Sea Grass
 * @author David
 *
 */
public abstract class Structure implements Minigame2Model{
	
	private int cost;
	private int xLoc;
	private int yLoc;
	private int xSpan;
	private int health;
	
	private boolean drawable = true;
	private boolean clickable = true;
	
	/**
	 * Sets the status of a structure to either true, is on the screen
	 * or false, is not on the screen
	 * @param status boolean value for state of structure
	 */
	public void setDrawable(boolean status){
		this.drawable = status;
	}
	
	/**
	 * Returns either true or false based on if the structure is visible or not
	 * @return true or false based on if the structure is visible or not
	 */
	public boolean getDrawable(){
		return this.drawable;
	}
	
	/**
	 * Sets the cost to place a structure on screen using points
	 * @param cost number of points used to purchase structure
	 */
	public void setCost(int cost){
		this.cost = cost;
	}
	
	/**
	 * Returns the number of points needed to place a structure
	 * @return the number of points needed to place a structure
	 */
	public int getCost(){
		return this.cost;
	}
	
	/**
	 * Sets the x coordinate of the structure on screen
	 * @param x coordinate of structure on screen
	 */
	public void setXLoc(int x){
		this.xLoc = x;
	}
	
	/**
	 * Returns x coordinate of structure on screen
	 * @return x coordinate of structure on screen
	 */
	public int getXloc() {
		return this.xLoc;
	}
	
	public void setXSpan(int x){
		this.xSpan = x;
	}
	
	public int getXlocPlusSpan(){
		return this.xLoc+this.xSpan;
	}
	
	public int getXlocMinusSpan(){
		return this.xLoc-this.xSpan;
	}
	
	/**
	 * Sets the y coordinate of the structure on screen
	 * @param y coordinate of structure on screen
	 */
	public void setYLoc(int y){
		this.yLoc = y;
	}
	
	/**
	 * Returns y coordinate of structure on screen
	 * @return y coordinate of structure on screen
	 */
	public int getYLoc(){
		return this.yLoc;
	}
	
	public void setHealth(int health){
		if(health == 0){
			this.health = 0;
			this.drawable = false;
		}
		else{
			this.health = health;
		}

	}
	
	public int getHealth(){
		return this.health;
	}
	
	/**
	 * Lowers the health of a structure object based on the damage of an incoming wave
	 * Sets the structure to not drawable if health reaches zero
	 * @param damage of incoming wave
	 */
	public void decrementHealth(int damage){
		int newHealth = this.health - damage;
		if((newHealth) > 0){
		this.health = newHealth;
		}
		else if(newHealth == 0){
			this.health = 0;
			this.drawable = false;
		}
		else{
			this.health = 0;
			this.drawable = false;
		}
	}
	
	/**
	 * Returns true if the click is within the structure and returns false
	 * if the click is outside of the structure
	 * @param xRange width of the structure
	 * @param yRange height of the structure
	 * @param xClick x coordinate of the click
	 * @param yClick y coordinate of the click
	 * @return boolean if click is in range or not
	 */
	public boolean calcRange(int xRange, int yRange, int xClick, int yClick){
		boolean withinXRange=false;
		boolean withinYRange=false;
		if(xClick <= xLoc+xRange && xClick >= xLoc-xRange){withinXRange = true;}
		if(yClick <= yLoc+yRange && yClick >= yLoc){withinYRange = true;}
		if(withinXRange && withinYRange){return true;}
		else{return false;}
	}
	
	public void setClickable(boolean b) {
		this.clickable = b;
	}
	
	public boolean getClickable() {
		return this.clickable;
	}

}
