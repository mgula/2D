package minigame1Models;

/**
 * Enemies move and deal damage to the player. Crab's collision detection doesn't treat enemies
 * as surfaces - the player can move through (and cannot land on) an enemy.
 * 
 * @author marcusgula
 *
 */
public abstract class Enemy implements Minigame1Model {

	public void move(){};
	public int getDamage(){return 0;};
}
