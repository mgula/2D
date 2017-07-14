package minigame2Models;
/**
 * The beach class determines the coordinates for where structures can be place and also handles the health of the shore.
 * @author npompetti
 *
 */
public class Beach {
	private int health = 100;
	private int shoreStartYLoc;
	private int designatedAreaForStructYLoc;
	private int designatedAreaForStructHeight;

	
	
	public int getHealth(){
		return this.health;
	}
	
	public void setHealth(int value){
		this.health = value;
	}
	
	public void decrementHealth(int value){
		this.health -= value;
	}
	
	public void incrementHealth(int value){
		this.health+=value;
	}
	
	public void setShoreStart(int yLoc){
		this.shoreStartYLoc = yLoc;
		this.designatedAreaForStructYLoc = yLoc;
		this.designatedAreaForStructHeight = yLoc+20;
	}
	
	public int getShoreStart(){
		return this.shoreStartYLoc;
	}
	
	public int getDesigAreaForStructHeight(){
		return this.designatedAreaForStructHeight;
	}
	
	public int getDesigAreaForStructYLoc(){
		return this.designatedAreaForStructYLoc;
	}

}
