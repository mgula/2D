
package games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import enums.BoatType;
import enums.Direction;
import enums.GameState;
import minigame2Models.*;


/**
 * Model system for minigame 2 that handles creating/updating/removing boats, structures, and items that in play within each round. It also
 * handles de/incrementing player score and beach health. 
 * @author npompetti
 *
 */
public class Minigame2 implements Minigame {

	private GameState gameState = GameState.UNINITIALIZED;
	private GameState lastState = GameState.UNINITIALIZED;
	private boolean round1tutorial = true;
	
	private boolean firstTime = true;
	
	private ArrayList<Minigame2Model> allModels;
	private boatCompare bc = new boatCompare();
	private ArrayList<Structure> placedStructures = new ArrayList<Structure>();
	private Player player = new Player();
	private Beach beach = new Beach();
	private Random rand;
	private int gabianIconXLoc = 25;
	private int gabianIconYLoc = 20;
	private int wallIconXLoc = 25;
	private int wallIconYLoc = 60;
	private int seaGrassIconXLoc = 25;
	private int seaGrassIconYLoc = 100;
	
	private int level1Sailboat1XCoord = 200;
	private int level1Sailboat1YCoord = 400;
	private int level1SpeedboatXCoord = 350;
	private int level1Speedboat1YCoord = 300;
	private int level1TankerXCoord = 600;
	private int level1TankerYCoord = 200;
	
	private int level2Sailboat1XCoord = -50;
	private int level2Sailboat1YCoord = 400;
	private int level2Sailboat2XCoord = 1400;
	private int level2Sailboat2YCoord = 150;
	
	private int level3Sailboat1XCoord = 1450;
	private int level3Sailboat1YCoord = 300;
	private int level3SpeedboatXCoord = -150;
	private int level3Speedboat1YCoord = 100;
	private int level3TankerXCoord = -200;
	private int level3TankerYCoord = 200;
	
	private int level4Sailboat1XCoord = 1450;
	private int level4Sailboat1YCoord = 300;
	private int level4Speedboat1XCoord = -150;
	private int level4Speedboat1YCoord = 100;
	private int level4Speedboat2XCoord = 1500;
	private int level4Speedboat2YCoord = 150;
	private int level4TankerXCoord = -200;
	private int level4TankerYCoord = 400;
	
	private boolean roundOver = false;
	private boolean gameOver = false;
	
	
	/**
	 * Necessary for updating Minigame2 in real time, this method updates all instantiated object information pertaining to minigame2.
	 * It first checks to see if the boat is visible on screen to the player through b.getDrawable, if so it checks for wave release conditions as well
	 * as updating the boats x position on screen.
	 * This method also handles collision detection of the waves between instantiated structures and the beach through checkWaveHit(Boat b).
	 * Finally, it constantly checks the beach's health for a gameover situation. 
	 *  
	 */
	public void updateBoats() {
		//boolean variable set to true and is changed to false only if a boat is still drawable within
		//the current Models on the map
		this.roundOver = true;
		for (Minigame2Model m : this.allModels) {
			if (m.getClass().equals(Boat.class)) {
				Boat b = (Boat) m;
				
				//See if the boats waves are still drawable even if it is off screen.
				if(checkWaveHit(b)){
					this.roundOver = false;
				}
				
				//Check to see if the boat is still drawable
				if (b.getDrawable()) {
					//Check to see if boat has reached coord to release its wave
					
					b.checkForWaveRelease();
					
					b.move();
					this.roundOver = false;
				}
				
				if (beach.getHealth() <= 0){
					
					this.roundOver = true;
					this.gameOver = true;
				}
			}
		}
	}
	/**
	 * Used within minigame2's controller handleGame2(), this method checks the gameOver and roundOver booleans which are set to true for 0 beach health and 
	 * round completion respectively. It then updates the game's state. 
	 * 
	 * 
	 * <p> The gameOver and roundOver booleans are modified within updateBoats().
	 */
	public void winCheck() {
		if (this.roundOver) {
			this.lastState = this.gameState;
			
			if(this.gameOver == true){this.gameState = GameState.LOSE;}
			else{
			this.gameState = GameState.WIN;
			}
		}
	}
	
	/**
	 * initModels() instantiates a new allModels object array for minigame2 and is called at the start of every round through the handleGame2() method
	 * within Main.
	 * 
	 *  this.allModels contains all items, structures, and boats utilized within each round and is used within other methods
	 *  to update coordinates, image visibility, etc.
	 */
	public void initModels() {
		this.allModels = new ArrayList<Minigame2Model>();
	}
	
	
	/**
	 * Used within handleGame2 and loadGame2, generateLevel instantiates the desired amount of boats and items(oyster shells and trash) for each level as well as 
	 * the clickable structures at the upper left of the game screen. 
	 * 
	 * @param level The round that the game is currently in.
	 * @param screenWidth	Width of the screen being used.
	 * @param screenHeight	Height of the screen being used.
	 */
	public void generateLevel(int level, int screenWidth, int screenHeight){
		this.beach.setShoreStart(screenHeight - 200);
		this.rand = new Random();
		/*Remove all objects from the array list*/
		for (int i = 0; i < allModels.size(); i++) {
			this.allModels.remove(i);
		}
		/*Add objects*/
			/*Add boats*/
			this.generateLevelBoats(level, screenWidth);
			
			//Add the items to the level
			this.generateLevelItems(level, screenWidth, screenHeight); 
			
			Gabian g = minigame2Models.Gabian.generateGabian(this.gabianIconXLoc, this.gabianIconYLoc);
			g.setClickable(false);
			this.allModels.add(g);
			
			Wall w = minigame2Models.Wall.generateWall(this.wallIconXLoc, this.wallIconYLoc);
			w.setClickable(false);
			this.allModels.add(w);
			
			SeaGrass s = minigame2Models.SeaGrass.generateSeaGrass(this.seaGrassIconXLoc, this.seaGrassIconYLoc);
			s.setClickable(false);
			this.allModels.add(s);
	}
	
	/**
	 * Handles all clicks by the user within each round and checks the click's x and y coordinates against the objects' locations within allModels for a match
	 * as well as specified areas on screen such as the beach for seagrass or structure placement.
	 * Once it finds what is specifically being clicked, it then modifies the game accordingly.
	 * 
	 * 
	 * @param clickX 	The mouse pointers X location.
	 * @param clickY 	The mouse pointers Y location.
	 */
	public void handleClick(int clickX, int clickY) {
		int height_range = 20;
		int width_range = 20;
		int structure_width_range = 65;
		int structure_height_range = 30;
		
		for(Minigame2Model m: allModels){
			//Three if then else statements to determine if the player is touching an item on the beach or a struct
			if(m.getClass().equals(OysterShell.class) || m.getClass().equals(Trash.class)){
				if(m.calcRange(width_range, height_range, clickX, clickY)){
					this.player.incrementScore(((Items)m).getValue());
					m.setDrawable(false);	
					
				}
			}
			else if(m.getClass().equals(Gabian.class) && !((Structure)m).getClickable()){
				if(m.calcRange(structure_width_range, structure_height_range, clickX, clickY)){
					this.player.setSelect(1);
				}
			}
			else if(m.getClass().equals(Wall.class) && !((Structure)m).getClickable()){
				if(m.calcRange(structure_width_range, structure_height_range, clickX, clickY)){
					this.player.setSelect(2);
				}
			}
			else if(m.getClass().equals(SeaGrass.class) && !((Structure)m).getClickable()) {
				if(m.calcRange(structure_width_range, structure_height_range+15, clickX, clickY)){
					this.player.setSelect(3);
				}
			}
		}
		
		//Check if the click is within the bounds of area for placing structures
		if(clickY > this.beach.getDesigAreaForStructYLoc() && clickY < this.beach.getDesigAreaForStructHeight()) {
			if(this.player.getSelect() == 1 && this.player.getCurrentScore() > 1){
				Gabian g = minigame2Models.Gabian.generateGabian(clickX, this.beach.getDesigAreaForStructYLoc());
				this.allModels.add(g);
				this.placedStructures.add(g);
				this.player.decrementScore(((Structure)g).getCost());
			}
			if(this.player.getSelect() == 2 && this.player.getCurrentScore() > 0){
				Wall w = minigame2Models.Wall.generateWall(clickX, this.beach.getDesigAreaForStructYLoc());
				this.allModels.add(w);
				this.placedStructures.add(w);
				this.beach.decrementHealth(5);
				this.player.decrementScore(((Structure)w).getCost());
			}	
		}
		//Check if the click is on the beach to place seagrass
		else if(clickY > this.beach.getShoreStart()){
			if(this.player.getSelect() == 3 && this.player.getCurrentScore() > 2){
				SeaGrass s = minigame2Models.SeaGrass.generateSeaGrass(clickX, clickY);
				this.allModels.add(s);
				this.player.decrementScore(((Structure)s).getCost());
				if(this.beach.getHealth() < 100){
					this.beach.incrementHealth(10);
				}
				this.player.setSelect(0);
			}	
		}
		
	}
	
	/**
	 * Method for initiating wave/beach and wave/structure collision detection. It is called from within updateBoats().
	 * It loops through every waves within the boat b's wave array and checks for the following case in the order written:
	 *<p>  	No structures placed, hit the beach.
	 *<p> 	Structures are placed. It loops through every structure placed and goes to the checkCollision(Wave w, Structure s) to see if there is a collision.
	 *		If there is a collision with a structure, it calculates residual damage if the structure can not absorb it all. It then applies this residual damage to the beach.
	 *<p> 	Structures are placed but none are lined up with the incoming wave. It then inflicts damage to the shore health. 
	 *<p> 	There is an incremental integer variable employed that is incremented for every wave that has not hit a structure or the beach yet. If total is >0 this
	 * 		indicates there are still waves in play and the round is not over yet.
	 * 
	 * @param b		Each boat has its own array of Waves, and every boat passed into this method has everyone of its waves checked for collisions.
	 * @return boolean	Returns a boolean to updateBoats letting the game know true if all waves have crashed and the round is over or false that there are still
	 *					waves in play and the round is not over.
	 */
	public boolean checkWaveHit(Boat b){
		boolean onScreen;
		boolean released;
		boolean waveHit;
		int total = 0;
		//Wave is released and moving across the screen
		for(Wave w: b.getWaves()){
			onScreen = w.getDrawable();
			released = w.getReleased();
			waveHit = w.getWaveHit();
			w.setWeakened(false);
			if(onScreen){total++; w.move();}
			
			else if(this.placedStructures.size() == 0 && !onScreen && released && !waveHit){
				this.beach.decrementHealth(w.getWaveDamage());
				w.setWaveHit(true);
			}
			
			else if(this.placedStructures.size() != 0 && !onScreen && released && !waveHit){
				for(Structure s : this.placedStructures){
					if(checkCollision(w,s)){
						w.setResidualWaveDamage(w.getWaveDamage()-s.getHealth());
						if(w.getResidualDamage() == 0){
							s.decrementHealth(w.getWaveDamage());
							w.setWaveDamage(w.getResidualDamage());
							w.setWaveHit(true);
							
						}
						else{

							s.decrementHealth(w.getWaveDamage());
							w.setWaveDamage(w.getResidualDamage());
							w.setWaveHit(false);
							w.setWeakened(true);
							total++;
						}
					}
				}
				//Case when structures are placed but are not aligned with wave
				if(w.getWaveHit() == false && w.getWeakend() == false){

					this.beach.decrementHealth(w.getWaveDamage());
					w.setWaveHit(true);
				}
			}
		}
		if(total != 0){
			return true;
		}
		return false;
	
	}
	/**
	 * Called from within generateLevel(int level_number, int screen_width, int screen_height), this method creates the desired amount of boats and type of boat
	 * to be used in each level. It then places these generated boats within the allModels arrayList for the current Round and then sorts the collection using the
	 * boatCompare comparator so that the images are organized based upon increasing Y location on screen. The sort ensures that the images are not drawn on top of each
	 * other and look more realistic when passing on screen.
	 *  
	 * @param level_number	The current round the player is in.
	 * @param screenWidth	The width of the screen, necessary for generating proper object placement.
	 */
	public void generateLevelBoats(int level_number, int screenWidth){
		
		if(level_number == 1){
			Boat sailBoat1 = new Boat(this.level1Sailboat1XCoord, this.level1Sailboat1YCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.SAILBOAT);
			Boat speedBoat1 = new Boat(this.level1SpeedboatXCoord, this.level1Speedboat1YCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.SPEEDBOAT);
			Boat tanker1 = new Boat(this.level1TankerXCoord, this.level1TankerYCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.TANKER);
			sailBoat1.setTutWaves(this.beach.getShoreStart());
			speedBoat1.setTutWaves(this.beach.getShoreStart());
			tanker1.setTutWaves(this.beach.getShoreStart());
			this.allModels.add(sailBoat1);
			this.allModels.add(speedBoat1);
			this.allModels.add(tanker1);
			Collections.sort(this.allModels, bc);

		}
		
		if(level_number == 2){
			Boat sailBoat1 = new Boat(this.level2Sailboat1XCoord, this.level2Sailboat1YCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.SAILBOAT);
			Boat sailBoat2 = new Boat(this.level2Sailboat2XCoord, this.level2Sailboat2YCoord , Direction.WEST, screenWidth, this.beach.getShoreStart(), BoatType.SAILBOAT);
			
			this.allModels.add(sailBoat1);
			this.allModels.add(sailBoat2);
			Collections.sort(this.allModels, bc);
		}
		
		if(level_number == 3){
			Boat sailBoat1 = new Boat(this.level3Sailboat1XCoord, this.level3Sailboat1YCoord , Direction.WEST, screenWidth, this.beach.getShoreStart(), BoatType.SAILBOAT);
			Boat speedBoat1 = new Boat(this.level3SpeedboatXCoord, this.level3Speedboat1YCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.SPEEDBOAT);
			Boat tanker1 = new Boat(this.level3TankerXCoord, this.level3TankerYCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.TANKER);
			
			this.allModels.add(tanker1);
			this.allModels.add(sailBoat1);
			this.allModels.add(speedBoat1);
		}
		
		if(level_number == 4){
			Boat sailBoat1 = new Boat(this.level4Sailboat1XCoord, this.level4Sailboat1YCoord , Direction.WEST, screenWidth, this.beach.getShoreStart(), BoatType.SAILBOAT);
			Boat speedBoat1 = new Boat(this.level4Speedboat1XCoord, this.level4Speedboat1YCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.SPEEDBOAT);
			Boat speedBoat2 = new Boat(this.level4Speedboat2XCoord, this.level4Speedboat2YCoord, Direction.WEST, screenWidth, this.beach.getShoreStart(), BoatType.SPEEDBOAT);
			Boat tanker1 = new Boat(this.level4TankerXCoord, this.level4TankerYCoord , Direction.EAST, screenWidth, this.beach.getShoreStart(), BoatType.TANKER);
			
			this.allModels.add(tanker1);
			this.allModels.add(sailBoat1);
			this.allModels.add(speedBoat1);
			this.allModels.add(speedBoat2);
		}
		
	}
	
	/**
	 * Generates the items for the current round the player is in, which is passed as the level parameter. This generates a specified amount
	 * of items on screen for the user to pick up during the round. The generated objects are added to the allModels arrayList.
	 * 
	 * @param level 	The current round the player is on.
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void generateLevelItems(int level, int screenWidth, int screenHeight){
		//generate Oysters
		for (int i = 0; i < 3; i++) {
			OysterShell o = minigame2Models.OysterShell.generateOyster(screenWidth,screenHeight, this.beach.getShoreStart());
			this.allModels.add(o);

		}
		
		//generateTrash
		for (int j=0; j <2; j++ ){
			Trash t = minigame2Models.Trash.generateTrash(screenWidth, screenHeight, this.beach.getShoreStart());
			this.allModels.add(t);			
		}
		
		
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public void setGameState (GameState state) {
		this.gameState = state;
	}
	
	public GameState getLastState() {
		return this.lastState;
	}
	
	public void setLastState (GameState state) {
		this.lastState = state;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public ArrayList<Minigame2Model> getCurrentModels(){
		return this.allModels;
	}
	
	public Beach getBeach(){
		return this.beach;
	}
	
	
	public Direction genRandomDirection(){
		int n = rand.nextInt(2);
		Direction dir = null;
		switch (n) {
			case 0:
				/*Choose east if 0*/
				dir = Direction.EAST;
				break;
			case 1:	
				/*Choose west if 1*/
				dir = Direction.WEST;
				break;
		}
		return dir;
	}
	
	public int genRandomBoatYCoord(){
		int startingY = rand.nextInt(400);
		return startingY;
	}
	
	/**
	 * Checks for an equality of x coordinates between the passed in Wave w and Structure s. If there is an equality then the method
	 * returns true which indicates a collision between the wave and the structure has occurred. Returns false if otherwise
	 * 
	 * @param w 	The Wave that has reached the where the beach starts.
	 * @param s		The structure that could be potentially hit.
	 * @return	Returns true or false dependent upon whether there was a collision or not.
	 */
	public boolean checkCollision(Wave w, Structure s){
		//System.out.println("Within collision method");
		if(s.getDrawable() == false){return false;}
		for(int wX0 = w.getXloc(); wX0 < w.getXPlusSpan(); wX0++){
		//	System.out.println("wx0, its span and the current are: "+w.getXloc()+" "+(w.getXPlusSpan())+" "+wX0);
		//	System.out.println("sx0, its span and the current are: "+s.getXloc()+" "+(s.getXlocPlusSpan()));
			for(int sX0 = s.getXloc(); sX0 < (s.getXlocPlusSpan()); sX0++){
		//		System.out.println("wX0 and sX0 are currently: "+wX0+" "+sX0);
				if(wX0 == sX0){return true;}
			}
		}
		return false;
	}
	
	@Override
	public boolean getFirstTime() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setFirstTime() {
		// TODO Auto-generated method stub
		
	}
}

