package testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import games.Minigame1;
import games.Minigame3;
import minigame1Models.*;

import org.junit.Test;

import enums.Direction;
import enums.GameState;

public class Minigame1Test {

	@Test
	public void minigame1Test() {
		Minigame1 game = new Minigame1(false);
		game.initPlayer();
		game.initMap1();
		game.makeStage1();
		game.passEnvironmentAndMapToPlayer();
		
		Crab player = game.getPlayer();
		EnemyA enemy = new EnemyA(0, 0, 0, 0, Direction.WEST, 0, 0);
		EnemyB enemyB = new EnemyB(0, 0, 0, 0, 0, 0);
		Current c = new Current(0, 0, 0, 0, Direction.EAST, 0);
		CurrentDrawable cD = new CurrentDrawable(0, 0);
		
		/*Player x and y loc will be at the starting positions.*/
		assertEquals(player.getXloc(), game.getPlayerStartingXloc());
		assertEquals(player.getYloc(), game.getPlayerStartingYloc());
		
		/*Let an arbitrary amount of time pass so player can float to the ground.*/
		for (int i = 0; i < 100; i++) {
			game.assertGravity();
		}
		game.makeDebugStage();
		game.makeStage1();
		game.makeStage2();
		game.makeStage3();
		game.makeTutStage1();
		game.moveAll();
		game.moveLeft();
		game.moveRight();
		game.checkMovingSurfaces();
		
		c.getFlowDirection();
		c.getHeight();
		c.getXloc();
		c.getWidth();
		c.getIncr();
		c.getYloc();
		c.setFlowDirection(Direction.EAST);
		c.setIncr(5);
		
		cD.getHeight();
		cD.getWidth();
		cD.getXloc();
		cD.getYloc();
		
		
		enemy.getHeight();
		enemy.getWidth();
		enemy.getXloc();
		enemy.getYloc();
		enemy.getDamage();
		enemyB.getDamage();
		enemyB.getHeight();
		enemyB.getWidth();
		enemyB.getXloc();
		enemyB.move();
		
		
		/*Player's bottom edge should be at ground level.*/
		assertEquals(player.getYloc(), game.getMap().getGroundLevel());
		
		game.initMap1();
		
		game.initMap2();
		game.initMap3();
		game.initPlayer();
		game.initTutLevel();
		
	}
	
	@Test
	public void mech1Test() {
		Crab crab = new Crab(0, 0, 50, 40);
		ArrayList<Minigame1Model> environment = new ArrayList<Minigame1Model>();
		Map map = new Map(2000, 2000, 500);
		
		crab.loadEnvironmentAndMap(environment, map);
		crab.initiateJumpArc();
		crab.checkBottomEdgeCollisions();
		crab.checkAreaCollisions();
		crab.getEnemyCollision();
		crab.evaluateAreaCollisions();
	
			
	}
	
	@Test
	public void crabTest1(){
		Crab crab = new Crab(0,0,50,40);
		assertEquals(crab.getXloc(), 0);
		assertEquals(crab.getWidth(), 40);
		assertEquals(crab.getHeight(), 50);
		assertEquals(crab.getHealth(), 3);
		assertEquals(crab.getEnemyCollision(), false);
		crab.incrX();
		crab.decrX();
		crab.incrY();
		crab.decrY();
		assertEquals(crab.getXloc(),0);
		assertEquals(crab.getYloc(), 0);
	}
	
	@Test
	public void statesTest(){
		Minigame1 testGame=new Minigame1(false);
		GameState gameState = GameState.PLAY;
		
		testGame.setFirstTime();
		
		assertEquals(false,testGame.getFirstTime());
		
		testGame.setGameState(gameState);
		testGame.setLastState(gameState);
		
		assertEquals(GameState.PLAY,testGame.getGameState());
		assertEquals(GameState.PLAY,testGame.getLastState());
	}
	
	
	

}
