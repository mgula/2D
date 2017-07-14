package minigame2Models;

/**
 * Player has a incremental score that is updated as the player picks up items. It also has a select attribute that is updated as the player selects
 * gabions, walls, and seagrass from the item selection in the upper right of the screen.
 * @author npompetti
 *
 */
public class Player {
	
	private int currentScore = 0;
	private int select = 0;
	
	public int getCurrentScore(){
		return currentScore;
	}
	
	public void incrementScore(int itemsValue){
		currentScore+=itemsValue;
	}
	
	public void decrementScore(int itemsValue){
		currentScore-=itemsValue;
	}
	
	public void setSelect(int select){
		this.select = select;
	}
	
	public int getSelect(){
		return this.select;
	}

}
