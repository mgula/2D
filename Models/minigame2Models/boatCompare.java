package minigame2Models;

import java.util.Comparator;

/**
 * Comparator for organizing the boats by increasing y location on screen within allModels. This allows proper painting of the images so
 * that there movement and interaction with one another looks more realistic.
 * @author npompetti
 *
 */
public class boatCompare implements Comparator<Minigame2Model>{
	
	/**
	 * Compare method to compare two boats based on there y location, if the boats y location is less than boat 2 it returns a -1, if greater
	 * it returns 1 and if equal it returns a -1.
	 */
	@Override
	public int compare(Minigame2Model o1, Minigame2Model o2) {
		if(o1 instanceof minigame2Models.Boat && o2 instanceof minigame2Models.Boat){
			Boat b1 = (Boat) o1;
			Boat b2 = (Boat) o2;
			
			if(b1.getYloc() < b2.getYloc()){return -1;}
			else if(b1.getYloc() > b2.getYloc()){return 1;}
			else{return -1;}
		}
		return 0;
	}

}
