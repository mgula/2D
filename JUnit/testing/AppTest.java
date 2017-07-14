package testing;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import org.junit.Test;

import views.MainView;
import controller.Main;
import enums.AppState;

/*This test will start up a new Main object and automatically click various buttons in the application.
 *Author note: my computer is old and slow, so I typically have to run this test about 3-4
 *times before it will pass. This may or may not be the case for other users.*/
public class AppTest {
	Main main;
	MainView view;
	
	Robot robot;
	
	Dimension screenSize;
	
	int buttonX;
	int buttonY1;
	int buttonY2;
	int buttonY3;
	int buttonY4;
	int buttonY5;
	int buttonY6;
	
	int toggleOffsetX = 45;
	int toggleOffsetY = 365;
	
	int initialCycles = 150;
	int numCycles = 14; // number of cycles of the main while loop to simulate
	
	int extraMousePixels = 50; // add a few pixels in order to press middle(ish) of button
	
	@Test
	public void test() {
		/*Create a mainview instance to get button locations*/
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)this.screenSize.getWidth();
		int height = (int)this.screenSize.getHeight();
		this.view = new MainView(width, height);
		this.view.initButtonLocations();
		this.buttonX = view.getButtonXloc() + this.extraMousePixels;
		this.buttonY1 = view.getButtonSlot1Y() + this.extraMousePixels;
		this.buttonY2 = view.getButtonSlot2Y() + this.extraMousePixels;
		this.buttonY3 = view.getButtonSlot3Y() + this.extraMousePixels;
		this.buttonY4 = view.getButtonSlot4Y() + this.extraMousePixels;
		this.buttonY5 = view.getButtonSlot5Y() + this.extraMousePixels;
		this.buttonY6 = view.getButtonSlot6Y() + this.extraMousePixels;
		
		/*Create a new main*/
		this.main = new Main();
		this.main.init();
		this.simulate(this.initialCycles);  // duration = 150 here because there's a bit of loading to do
		try {
			this.robot = new Robot();
			this.robot.keyPress(KeyEvent.VK_SPACE);
			this.robot.keyRelease(KeyEvent.VK_SPACE);
			this.simulate(this.numCycles); // game registers key press and switches to select mode
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY1); // click game 1 button
			//assertEquals(this.main.getAppState(), AppState.TUTORIAL1);
			this.clickButton(this.buttonX, this.buttonY4); // click anywhere on screen
			assertEquals(this.main.getAppState(), AppState.GAME1);
			this.pauseAndReturnToMenu(); // return to menu
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY2); // click game 2 button
			//assertEquals(this.main.getAppState(), AppState.INBETWEEN2);
			this.clickButton(this.buttonX, this.buttonY2); // click ok button
			//assertEquals(this.main.getAppState(), AppState.GAME2);
			this.pauseAndReturnToMenu(); // return to menu
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY3); // click game 3 button
			//assertEquals(this.main.getAppState(), AppState.INBETWEEN3);
			this.clickButton(this.buttonX, this.buttonY4); // click ok button
			//assertEquals(this.main.getAppState(), AppState.GAME3);
			this.clickButton(10, this.buttonY6); // click shuffle button
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY4); // click controls button
			assertEquals(this.main.getAppState(), AppState.SETTINGS);
			this.clickButton(this.buttonX, this.buttonY1); // return to menu
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY1); // click game 1 button
			assertEquals(this.main.getAppState(), AppState.INBETWEEN1);
			this.clickButton(this.buttonX, this.buttonY1); // load previous game
			assertEquals(this.main.getAppState(), AppState.GAME1);
			this.robot.keyPress(KeyEvent.VK_P); // pause
			this.robot.keyRelease(KeyEvent.VK_P);
			this.simulate (this.numCycles);
			this.robot.keyPress(KeyEvent.VK_P); // unpause with P key
			this.robot.keyRelease(KeyEvent.VK_P);
			this.simulate (this.numCycles);
			this.pauseAndReturnToMenu(); // return to menu
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY4); // click controls button
			this.clickButton(this.buttonX - this.toggleOffsetX, this.toggleOffsetY); // toggle debug mode
			this.clickButton(this.buttonX, this.buttonY1); // return to menu
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY1); // click game 1 button
			assertEquals(this.main.getAppState(), AppState.INBETWEEN1);
			this.clickButton(this.buttonX, this.buttonY4); // enter debug room
			assertEquals(this.main.getAppState(), AppState.GAME1);
			this.pauseAndReturnToMenu(); // return to menu
			assertEquals(this.main.getAppState(), AppState.SELECT);
			this.clickButton(this.buttonX, this.buttonY5); // click exit button
			this.simulate(1); // simulate a cycle to let button press propagate
			assertEquals(this.main.getAppState(), AppState.END);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	/*Simulate the while loop in Main.java's main method.*/
	public void simulate(int duration) {
		for (int i = 0; i < duration; i++) {
			this.main.updateCurrentState();
			this.main.tick();
			this.main.paint();
			try {
				Thread.sleep(30); // sleep time from main
			} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
		}
	}
	
	/*Assuming the application is in a game, press the pause key and return to the menu.*/
	public void pauseAndReturnToMenu() {
		this.robot.keyPress(KeyEvent.VK_P);
		this.robot.keyRelease(KeyEvent.VK_P);
		simulate (this.numCycles); // give the application a few ticks to register button/key presses
		this.robot.mouseMove(this.buttonX, this.buttonY3);
		this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		simulate (this.numCycles);
	}
	
	/*Click a button at the given x and y location.*/
	public void clickButton(int clickX, int clickY) {
		this.robot.mouseMove(clickX, clickY);
		this.robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		this.robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		simulate (this.numCycles);
	}
	
	@Test
	public void testKeys() {
		
	}
}
