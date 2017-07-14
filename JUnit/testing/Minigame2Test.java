package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import enums.GameState;
import games.Minigame2;
import minigame2Models.*;

public class Minigame2Test {

	@Test
	public void minigame2Test() {
		Minigame2 game = new Minigame2();
		game.initModels();
		game.updateBoats();		
		Player player = game.getPlayer();
		for(int i = 1; i < 5; i++){
			game.generateLevel(i, 2000, 2000);
		}
		player.incrementScore(100);
		game.getCurrentModels();
		for(int k = 0; k < 4; k++){
			for(int i = 0; i < 2000; i++){
				for(int j = 0; j < 2000; j++){
					player.setSelect(k);
					game.handleClick(i, j);
				}
			}
		}	
		for(Minigame2Model m : game.getCurrentModels()){
			game.updateBoats();
			game.winCheck();
			if(m.getClass().equals(Boat.class)){
				game.checkWaveHit((Boat)m);
			}		
		}
	}
	@Test
	public void beachTest(){
		Minigame2 game = new Minigame2();
		Beach beach = game.getBeach();	
		beach.setHealth(90);
		assertEquals(beach.getHealth(), 90);
		beach.incrementHealth(10);
		assertEquals(beach.getHealth(), 100);
	}
	@Test
	public void playerTest(){
		Minigame2 game = new Minigame2();
		Player player = game.getPlayer();		
		player.getCurrentScore();
		player.decrementScore(10);
		player.incrementScore(10);
		assertEquals(player.getCurrentScore(), 0);
	}
	@Test 
	public void structureTest(){
		Gabian g = minigame2Models.Gabian.generateGabian(40, 20);
		g.setDrawable(false);
		g.setDrawable(true);
		assertEquals(g.getDrawable(), true);
		assertEquals(g.getXloc(), 20);
		assertEquals(g.getYloc(), 20);
		g.setXSpan(5);
		assertEquals(g.getXlocMinusSpan(), 15);
		assertEquals(g.getXlocPlusSpan(), 25);
		g.setHealth(0);
		g.setHealth(20);
		assertEquals(g.getHealth(), 20);
		g.decrementHealth(10);
		assertEquals(g.getHealth(), 10);
		g.decrementHealth(10);
		assertEquals(g.getHealth(), 0);
		g.decrementHealth(10);
		assertEquals(g.getHealth(), 0);
	}
	@Test
	public void statesTest(){
		Minigame2 testGame=new Minigame2();
		GameState gameState = GameState.PLAY;
		
		testGame.setFirstTime();
		
		assertEquals(false,testGame.getFirstTime());
		
		testGame.setGameState(gameState);
		testGame.setLastState(gameState);
		
		assertEquals(GameState.PLAY,testGame.getGameState());
		assertEquals(GameState.PLAY,testGame.getLastState());
	}

}
