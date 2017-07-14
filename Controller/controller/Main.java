package controller;

import enums.AppState;
import enums.GameState;
import enums.TutorialState;
import games.*;
import views.*;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/*TODO:
 *-JUnit testing
 *-magic #s
 *-duplicate code cleanup
 */

/**
 * The Main class serves as the controller that mediates between the model and view. This class
 * also handles all key and button presses, as well as mouse interaction.
 * 
 * @author marcusgula
 *
 */
public class Main implements KeyListener, MouseListener, MouseMotionListener {
	private boolean play = true;
	private Minigame1 game1 = new Minigame1(false); //Initialize empty games so views can call getGameState()
	private Minigame1 game1Tut = new Minigame1(true);
	private Minigame2 game2 = new Minigame2();
	private Minigame3 game3 = new Minigame3();
	private MainView mainView;
	private SettingsView settingsView;
	private Minigame1View game1View;
	private Minigame1View game1TutView;
	private Minigame2View game2View;
	private Minigame3View game3View;
	private BetweenView game1ViewBV;
	private BetweenView game2ViewBV;
	private BetweenView game3ViewBV;
	private ArrayList<View> allViews  = new ArrayList<View>();
	private AppState currentState = AppState.START;
	private AppState intendedState = AppState.SATISFIED;
	private JFrame frame = new JFrame();
	private boolean rightPressed = false;
	private boolean leftPressed = false;
	private boolean spacePressed = false;
	private boolean clicked = false; //used to handle click events
	private int clickX;
	private int clickY;
	private boolean dragged = false; //used to handle mouse drag events
	private int dragX;
	private int dragY;
	private boolean released = false; //used to handle mouse release events
	private int releaseX;
	private int releaseY;
	private Dimension screenSize;
	private boolean game1DebugLevel = false;
	private boolean tutLock = false;
	private boolean game1Lost = false;
	private boolean screenHandled = false;
	private boolean fullScreen = true;
	private boolean newGame2 = false;
	private static final int sleepTime = 30; //Time in milliseconds to wait each cycle of the main loop
	
	/**
	 * The main method initializes the main instance and begins a while loop. In the while loop,
	 * the main instance is constantly updating the application's current state, ticking, and repainting.
	 * When the play boolean becomes false (by clicking the exit button), the loop ends and frame is 
	 * disposed.
	 * 
	 * @param args unused
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.init();
    	while (main.play) {
    		main.updateCurrentState();
    		main.tick();
    		main.paint();
    		try {
    			Thread.sleep(sleepTime);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	main.frame.setVisible(false);
    	main.frame.dispose();
	}
	
	/**
	 * This method gets the screen size of the device and initializes all views using this screen size.
	 * An array list of all views is created for easier view maintenance. This method then calls a method
	 * that adds button listeners to all buttons, and a method that binds certain keys to their respective 
	 * views. Lastly, this method adds the main menu to the frame.
	 */
	public void init() {
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)this.screenSize.getWidth();
		int height = (int)this.screenSize.getHeight();
		this.mainView = new MainView(width, height);
		this.mainView.setFocusable(true);
		this.mainView.addKeyListener(this);
		this.settingsView = new SettingsView(width, height);
		this.game1View = new Minigame1View(width, height, false);
		this.game1TutView = new Minigame1View(width, height, true);
		this.game2View = new Minigame2View(width, height);
		this.game3View = new Minigame3View(width, height);
		this.game1ViewBV = new BetweenView(width, height, AppState.GAME1);
		this.game2ViewBV = new BetweenView(width, height, AppState.GAME2);
		this.game2ViewBV.setFirstTime(false);
		this.game3ViewBV = new BetweenView(width, height, AppState.GAME3);
		this.allViews.add(this.mainView);
		this.allViews.add(this.settingsView);
		this.allViews.add(this.game1View);
		this.allViews.add(this.game1TutView);
		this.allViews.add(this.game2View);
		this.allViews.add(this.game3View);
		this.allViews.add(this.game1ViewBV);
		this.allViews.add(this.game2ViewBV);
		this.allViews.add(this.game3ViewBV);
		for (View v : this.allViews) {
			v.setViewFont();
			v.initButtonLocations();
			v.initButtons();
			if (v instanceof views.GameView) {
				((views.GameView) v).loadImgs();
			} else if (v instanceof views.MainView) {
				((views.MainView) v).loadBackgroundImgs();
			}
		}
		this.setButtonListeners();
		this.bindKeysToViews();
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
		this.frame.setUndecorated(true);
		this.addViewToFrame(this.mainView);
	}
	
	/**
	 * This method adds action listeners to every button in the application. Listeners
	 * vary from button to button.
	 */
	public void setButtonListeners() {
		/*Start with main view*/
		this.mainView.getMg1Button().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game1.getFirstTime()) {
					game1.setFirstTime();
					intendedState = AppState.TUTORIAL1;
				} else {
					intendedState = AppState.INBETWEEN1;
				}
			}
    	});
		this.mainView.getMg2Button().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.INBETWEEN2;
			}
    	});
		this.mainView.getMg3Button().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game3ViewBV.getFirstTime()) {
					intendedState = AppState.INBETWEEN3;
				} else {
					intendedState = AppState.GAME3;
				}
			}
    	});
		this.mainView.getControlsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.SETTINGS;
			}
    	});
		this.mainView.getExitButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.END;
			}
    	});
		/*In between view 1*/
		this.game1ViewBV.getNewGameButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.GAME1;
				game1.setLastState(GameState.UNINITIALIZED);
			}
    	});
		this.game1ViewBV.getLoadButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentState = AppState.GAME1;
				unpauseGame(game1, game1View);
			}
    	});
		this.game1ViewBV.getGame1TutButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.TUTORIAL1;
			}
    	});
		this.game1ViewBV.getStage1Button().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game1ViewBV.getFirstTime()) {
					game1ViewBV.setFirstTime(false);
				}
				intendedState = AppState.GAME1;
				game1.setLastState(GameState.UNINITIALIZED);
			}
    	});
		this.game1ViewBV.getStage2Button().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game1ViewBV.getFirstTime()) {
					game1ViewBV.setFirstTime(false);
				}
				intendedState = AppState.GAME1;
				game1.setLastState(GameState.STAGE1);
			}
    	});
		this.game1ViewBV.getStage3Button().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game1ViewBV.getFirstTime()) {
					game1ViewBV.setFirstTime(false);
				}
				intendedState = AppState.GAME1;
				game1.setLastState(GameState.STAGE2);
			}
    	});
		this.game1ViewBV.getDebugButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (game1ViewBV.getFirstTime()) {
					game1ViewBV.setFirstTime(false);
				}
				game1DebugLevel = true;
				intendedState = AppState.GAME1;
			}
    	});
		/*In between view 2*/
		this.game2ViewBV.getNewGameButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.GAME2;
				newGame2 = true;
			}
    	});
		this.game2ViewBV.getLoadButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentState = AppState.GAME2;
				unpauseGame(game2, game2View);
			}
    	});
		/*In between view 3*/
		this.game3ViewBV.getOkButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.GAME3;
				if (game3ViewBV.getFirstTime()) {
					game3ViewBV.setFirstTime(false);
				}
			}
    	});
		
		/*Settings view*/
		this.settingsView.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.SELECT;
			}
    	});
		this.settingsView.getDebugToggleOn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (View v : allViews) {
					v.setDebugMode(true);
				}
			}
    	});
		this.settingsView.getDebugToggleOff().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (View v : allViews) {
					v.setDebugMode(false);
				}
			}
    	});
		this.settingsView.getWindowedOn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleWindowedMode(true);
			}
    	});
		this.settingsView.getWindowedOff().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleWindowedMode(false);
			}
    	});
		this.settingsView.getDelaware().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.setState("Delaware");
			}
    	});
		this.settingsView.getFlorida().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.setState("Florida");
			}
    	});
		this.settingsView.getTexas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.setState("Texas");
			}
    	});
		/*Game 1*/
		this.game1View.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.SELECT;
				screenHandled = false;
			}
    	});
		this.game1View.getResumeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unpauseGame(game1, game1View);
			}
    	});
		this.game1View.getWinButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1.setGameState(GameState.LOAD);
				frame.getContentPane().removeAll();
				screenHandled = false;
			}
    	});
		this.game1View.getLoseButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1Lost = true;
				game1.setGameState(GameState.LOAD);
				frame.getContentPane().removeAll();
				screenHandled = false;
			}
    	});
		this.game1View.getPlayAgainButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1.setGameState(GameState.LOAD);
				frame.getContentPane().removeAll();
				screenHandled = false;
			}
    	});
		/*Game 2*/
		this.game2View.getWinButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*Change game state, remove everything from JFrame*/
				game2.setGameState(GameState.LOAD);
				frame.getContentPane().removeAll();
				screenHandled = false;
			}
    	});
		this.game2View.getLoseButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game2 = new Minigame2();
				newGame2 = true;
				game2.setGameState(GameState.LOAD);
				frame.getContentPane().removeAll();
				screenHandled = false;
			}
    	});
		this.game2View.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.SELECT;
				if (game2.getLastState() == GameState.ROUND4) {
					game2 = new Minigame2();
					newGame2 = true;
				}
				screenHandled = false;
			}
    	});
		this.game2View.getResumeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unpauseGame(game2, game2View);
			}
    	});
		this.game2View.getPlayAgainButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game2.setGameState(GameState.LOAD);
				frame.getContentPane().removeAll();
				screenHandled = false;
			}
    	});
		this.game2View.getNextButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (game2View.getTutState()) {
					case NEXT1:
						game2View.setTutStatus(TutorialState.NEXT2);
						break;
						
					case NEXT2:
						game2View.setTutStatus(TutorialState.NEXT3);
						break;
						
					case NEXT3:
						game2View.setTutStatus(TutorialState.NEXT4);
						break;
						
					case NEXT4:
						game2View.setTutStatus(TutorialState.NEXT5);
						break;
						
					case NEXT5:
						game2View.setTutStatus(TutorialState.NEXT6);
						break;
						
					case NEXT6:
						game2View.setTutStatus(TutorialState.NEXT7);
						break;
						
					case NEXT7:
						game2View.setTutStatus(TutorialState.NEXT8);
						break;
						
					case NEXT8:
						game2View.setTutStatus(TutorialState.NEXT9);
						break;
						
					case NEXT9:
						game2View.setTutStatus(TutorialState.FINAL);
						break;
						
					case FINAL:
						game2View.setTutStatus(TutorialState.FINISHED);
						game2.setGameState(GameState.LOAD);
						game2.setLastState(GameState.ROUND1);
						break;
						
					default:
						break;
				}
			}
    	});
		this.game2View.getSkipTutButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game2View.setTutStatus(TutorialState.FINISHED);
				game2.setGameState(GameState.LOAD);
				game2.setLastState(GameState.ROUND1);
			}
    	});
		/*Game 3*/
		this.game3View.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.SELECT;
				screenHandled = false;
			}
    	});
		this.game3View.getResumeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unpauseGame(game3, game3View);
			}
    	});
		this.game3View.getShuffleButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game3.shuffle();
			}
    	});
		this.game3View.getSubmitButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(game3.isFinished()){
					game3.submit();
					game3View.setStatus(true);
					frame.getContentPane().removeAll();
					frame.add(game3View.getResetButton());
					addViewToFrame(game3View);
				}
			}
    	});
		this.game3View.getResetButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game3.shuffle();
				game3View.setStatus(false);
				game3View.resetText();
				frame.getContentPane().removeAll();
				frame.add(game3View.getBackButton());
				frame.add(game3View.getSubmitButton());
				frame.add(game3View.getShuffleButton());
				frame.add(game3View.getTextBox());
				addViewToFrame(game3View);
			}
    	});
	}
	
	/**
	 * This method updates the state of the application only when intendedState isn't 
	 * satisfied (this is to avoid loading the same things multiple times since this method 
	 * is called every iteration of the main while loop). For each case, it adds each specific
	 * view's buttons to the frame (if any), and then adds the view to the frame. At the end of the 
	 * method, intendedState is set to satisfied.
	 */
	public void updateCurrentState() {
		if (this.intendedState != AppState.SATISFIED) {
			/*Reset the frame*/
			this.frame.getContentPane().removeAll();
			switch (this.intendedState) {
				case GAME1:
					this.loadGame1();
					break;
					
				case GAME2:
					this.loadGame2();
					break;
					
				case GAME3:
					this.loadGame3();
					break;
					
				case SELECT:
					/*Add the menu buttons to the frame*/
					this.frame.add(this.mainView.getMg1Button());
					this.frame.add(this.mainView.getMg2Button());
					this.frame.add(this.mainView.getMg3Button());
					this.frame.add(this.mainView.getControlsButton());
					this.frame.add(this.mainView.getExitButton());
					/*Add main view to the frame*/
					this.addViewToFrame(this.mainView);
					break;
					
				case TUTORIAL1:
					this.tutLock = false;
					this.game1Tut.initTutLevel();
					this.game1Tut.makeTutStage1();
					this.game1Tut.initTutPlayer((int)this.screenSize.getWidth()/2, (int)this.screenSize.getHeight()/2); //Spawn player in middle of screen
					this.game1Tut.passEnvironmentAndMapToPlayer();
					this.game1TutView.load(this.game1Tut);
					this.game1TutView.addMouseListener(this);
					this.addViewToFrame(this.game1TutView);
					break;
					
				case INBETWEEN1:
					if (this.game1View.getDebugMode() == true) {
						this.frame.add(this.game1ViewBV.getStage1Button());
						this.frame.add(this.game1ViewBV.getStage2Button());
						this.frame.add(this.game1ViewBV.getStage3Button());
						this.frame.add(this.game1ViewBV.getDebugButton());
					} else {
						this.frame.add(this.game1ViewBV.getNewGameButton());
						this.frame.add(this.game1ViewBV.getLoadButton());
						this.frame.add(this.game1ViewBV.getGame1TutButton());
					}
					
					this.addViewToFrame(this.game1ViewBV);
					break;
					
				case INBETWEEN2:
					this.frame.add(this.game2ViewBV.getNewGameButton());
					if (this.game2.getGameState() == GameState.PAUSE) {
						this.frame.add(this.game2ViewBV.getLoadButton());
					}
					this.addViewToFrame(this.game2ViewBV);
					break;
					
				case INBETWEEN3:
					if (this.game3ViewBV.getFirstTime()) {
						this.frame.add(this.game3ViewBV.getOkButton());
						this.addViewToFrame(this.game3ViewBV);
					} else {
						this.intendedState = AppState.GAME3;
					}
					break;
					
				case SETTINGS:
					/*Add radio buttons and back button to the frame*/
					this.frame.add(this.settingsView.getBackButton());
					this.frame.add(this.settingsView.getDebugToggleOn());
					this.frame.add(this.settingsView.getDebugToggleOff());
					this.frame.add(this.settingsView.getWindowedOn());
					this.frame.add(this.settingsView.getWindowedOff());
					this.frame.add(this.settingsView.getDelaware());
					this.frame.add(this.settingsView.getFlorida());
					this.frame.add(this.settingsView.getTexas());
					/*Add control view to the frame*/
					this.addViewToFrame(this.settingsView);
					break;
					
				default:
					break;
			}
			/*Set current state to the intended state, and set intended state to satisfied*/
			this.currentState = this.intendedState;
			this.intendedState = AppState.SATISFIED;
		}
	}
	
	/**
	 * This method uses the concept of a state machine to load game 1 - it looks
	 * at game 1's last state to determine which level to load.  This method call's 
	 * Minigame1View's load() method to sync the game view with the model.
	 */
	public void loadGame1() {
		this.game1.setJumping(false);
		this.rightPressed = false;
		this.spacePressed = false;
		if (this.game1DebugLevel) {
			this.game1DebugLevel = false;
			this.game1.setGameState(GameState.DEBUG);
			this.game1.initDebugLevel();
			this.game1.makeDebugStage();
		} else {
			switch (this.game1.getLastState()) {
				case UNINITIALIZED:
					this.game1.setGameState(GameState.STAGE1);
					this.game1.initMap1();
					this.game1.makeStage1();
					break;
					
				case STAGE1:
					if (this.game1Lost) {
						this.game1Lost = false;
						this.game1.setGameState(GameState.STAGE1);
						this.game1.initMap1();
						this.game1.makeStage1();
					} else {
						this.game1.setGameState(GameState.STAGE2);
						this.game1.initMap2();
						this.game1.makeStage2();
					}
					break;
					
				case STAGE2:
					if (this.game1Lost) {
						this.game1Lost = false;
						this.game1.setGameState(GameState.STAGE2);
						this.game1.initMap2();
						this.game1.makeStage2();
					} else {
						this.game1.setGameState(GameState.STAGE3);
						this.game1.initMap3();
						this.game1.makeStage3();
					}
					break;
					
				case STAGE3:
					if (this.game1Lost) {
						this.game1Lost = false;
						this.game1.setGameState(GameState.STAGE3);
						this.game1.initMap3();
						this.game1.makeStage3();
					} else {
						this.game1.setGameState(GameState.STAGE1);
						this.game1.initMap1();
						this.game1.makeStage1();
					}
					break;
					
				default:
					break;
			}
		}
		this.game1.setLastState(GameState.LOAD);
		this.game1.initPlayer();
		this.game1.passEnvironmentAndMapToPlayer();
		this.game1View.load(this.game1);
		this.addViewToFrame(this.game1View);
	}
	
	/**
	 * This method uses the concept of a state machine to load game 2 - it looks
	 * at the newGame2 flag to determine whether it should load from the tutorial,
	 * and then looks at game 2's last state to determine which level to load.
	 * This method call's  Minigame2View's load() method to sync the game view
	 * with the model.
	 */
	public void loadGame2() {
		this.game2.initModels();
		if (this.newGame2) {
			this.newGame2 = false;
			this.game2View.setTutStatus(TutorialState.NEXT1);
			this.game2.generateLevel(1, this.mainView.getScreenWidth(), this.mainView.getScreenHeight());
			this.game2.setGameState(GameState.ROUND1);
			this.game2View.addMouseListener(this);
			this.frame.add(this.game2View.getNextButton());
			this.frame.add(this.game2View.getSkipTutButton());
		} else {
			switch (this.game2.getLastState()) {
				case ROUND1:
					this.frame.getContentPane().removeAll();
					this.game2.generateLevel(2, this.mainView.getScreenWidth(), this.mainView.getScreenHeight());
					this.game2.setGameState(GameState.ROUND2);
					this.game2.setLastState(GameState.LOAD);
					break;
					
				case ROUND2:
					this.game2.generateLevel(3, this.mainView.getScreenWidth(), this.mainView.getScreenHeight());
					this.game2.setGameState(GameState.ROUND3);
					this.game2.setLastState(GameState.LOAD);
					break;
					
				case ROUND3:
					this.game2.generateLevel(4, this.mainView.getScreenWidth(), this.mainView.getScreenHeight());
					this.game2.setGameState(GameState.ROUND4);
					this.game2.setLastState(GameState.LOAD);
					break;
					
				case ROUND4:
					this.game2 = new Minigame2();
					this.game2.initModels();
					this.game2.generateLevel(2, this.mainView.getScreenWidth(), this.mainView.getScreenHeight());
					this.game2.setGameState(GameState.ROUND2);
					this.game2.setLastState(GameState.LOAD);
					break;
					
				case LOSE:
					this.game2 = new Minigame2();
					this.game2.initModels();
					this.game2.generateLevel(2, this.mainView.getScreenWidth(), this.mainView.getScreenHeight());
					this.game2.setGameState(GameState.ROUND2);
					this.game2.setLastState(GameState.LOAD);
					break;
					
				default:
					break;
			}
		}
		this.game2View.load(this.game2);
		this.addViewToFrame(this.game2View);
	}
	
	/**
	 * This method loads game 3 - loading game 3 is much simpler than game 1
	 * or 2, since the only state of game 3 is play. This method call's  Minigame2View's load() method
	 * to sync the game view with the model.
	 */
	public void loadGame3() {
		this.game3.setScreenDimensions(this.screenSize);
		this.game3.initGame();
		this.game3.setGameState(GameState.PLAY);
		this.game3View.addMouseListener(this);
		this.game3View.addMouseMotionListener(this);
		this.game3View.load(this.game3);
		this.frame.add(game3View.getBackButton());
		this.frame.add(this.game3View.getShuffleButton());
		this.frame.add(this.game3View.getSubmitButton());
		this.frame.add(this.game3View.getTextBox());
		this.addViewToFrame(this.game3View);
	}
	
	/**
	 * This method is called every iteration of the main while loop to update model 
	 * information. This method does nothing in other cases (settings view, for example,
	 * doesn't need to update each tick - buttons are listening for presses and that's it
	 * essentially). The code in the tutorial1 case is nearly identical to a case in 
	 * handleGame1(), except it's not checking win or lose conditions (it is impossible
	 * to die in the tutorial).
	 */
	public void tick() {
		this.updateViewStates();
		switch (this.currentState) {
			case GAME1:
				this.handleGame1();
				break;
				
			case TUTORIAL1:
				if (!this.tutLock) {
					if (this.game1TutView.getTutState() == TutorialState.ENVIRONMENT1) {
						this.game1Tut.setJumping(false);
						this.tutLock = true;
					}
				}
				this.game1Tut.balanceEnvironmentArrayListsAndCrab(this.game1TutView.getDraw(), this.game1TutView.getPlayer());
				this.game1Tut.assertGravity();
				this.game1Tut.checkMovingSurfaces();
				this.game1Tut.moveAll();
				this.game1Tut.checkAreaCollisions();
				if (this.rightPressed) {
					this.game1Tut.moveRight();
				}
				if (this.leftPressed) {
					this.game1Tut.moveLeft();
				}
				if (this.spacePressed) {
					this.game1Tut.setJumping(true);
				}
				this.game1Tut.evaluateJumping();
				if (this.game1TutView.getTutState() == TutorialState.FINAL) {
					this.game1TutView.setTutState(TutorialState.CONTROLS1);
					if (this.game1.getGameState() == GameState.UNINITIALIZED) {
						intendedState = AppState.GAME1;
					} else {
						intendedState = AppState.INBETWEEN1;
					}
				}
				break;
				
			case GAME2:
				this.handleGame2();
				break;
				
			case GAME3:
				this.handleGame3();
				break;
				
			case END:
				this.play = false;
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * This method is called every iteration of the main while loop to update model
	 * information for game 1. In the cases of pause, win, and lose, a flag is used to
	 * debounce (for lack of a better word) so that the screen is only redrawn with the
	 * correct buttons a single time. The flag is reset on button press. The load case
	 * calls loadGame1(), which changes the game state to ensure that it is only loaded 
	 * a single time.
	 */
	public void handleGame1() {
		switch (this.game1.getGameState()) {
			case PAUSE:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					this.frame.add(this.game1View.getBackButton());
					this.frame.add(this.game1View.getResumeButton());
					this.addViewToFrame(this.game1View);
				}
				break;
				
			case WIN:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					if (this.game1.getLastState() == GameState.STAGE3) {
						this.frame.add(this.game1View.getPlayAgainButton());
						this.frame.add(this.game1View.getBackButton());
						this.addViewToFrame(this.game1View);
					} else {
						this.frame.add(this.game1View.getWinButton());
						this.addViewToFrame(this.game1View);
					}
				}
				break;
				
			case LOSE:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					this.frame.add(this.game1View.getLoseButton());
					this.frame.add(this.game1View.getBackButton());
					this.addViewToFrame(this.game1View);
				}
				break;
				
			case LOAD:
				this.loadGame1();
				break;
				
			default:
				this.game1.assertGravity();
				this.game1.checkMovingSurfaces();
				this.game1.moveAll();
				this.game1.checkAreaCollisions();
				if (this.rightPressed) {
					this.game1.moveRight();
				}
				if (this.leftPressed) {
					this.game1.moveLeft();
				}
				if (this.spacePressed) {
					this.game1.setJumping(true);
				}
				this.game1.evaluateJumping();
				this.game1.gameStateCheck();
				break;
		}
	}
	
	/**
	 * This method is called every iteration of the main while loop to update model
	 * information for game 2. In the cases of pause, win, and lose, a flag is used to
	 * debounce (for lack of a better word) so that the screen is only redrawn with the
	 * correct buttons a single time. The flag is reset on button press. The load case
	 * calls loadGame2(), which changes the game state to ensure that it is only loaded 
	 * a single time.
	 */
	public void handleGame2() {
		switch (this.game2.getGameState()) {
			case PAUSE:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					this.frame.add(this.game2View.getBackButton());
					this.frame.add(this.game2View.getResumeButton());
					this.addViewToFrame(this.game2View);
				}
				break;
				
			case WIN:
				if (!this.screenHandled) {
					this.screenHandled = true;
					if (this.game2.getLastState() == GameState.ROUND4) {
						this.frame.getContentPane().removeAll();
						this.frame.add(this.game2View.getPlayAgainButton());
						this.frame.add(this.game2View.getBackButton());
						this.addViewToFrame(this.game2View);
					} else {
						this.frame.getContentPane().removeAll();
						this.frame.add(this.game2View.getWinButton());
						this.addViewToFrame(this.game2View);
					}
				}
				break;
				
			case LOSE:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					this.frame.add(this.game2View.getLoseButton());
					this.frame.add(this.game2View.getBackButton());
					this.addViewToFrame(this.game2View);
				}
				break;
				
			case LOAD:
				this.loadGame2();
				break;
				
			default:
				if (this.clicked) {
					this.game2.handleClick(this.clickX, this.clickY);
					this.clicked = false;
				}
				if (this.game2.getGameState() != GameState.ROUND1) {
					this.game2.updateBoats();
				}
				this.game2.winCheck();
				break;
		}
	}

	/**
	 * This method is called every iteration of the main while loop to update model
	 * information for game 3. In the cases of pause, a flag is used to debounce (for 
	 * lack of a better word) so that the screen is only redrawn with the correct buttons 
	 * a single time. The flag is reset on button press.
	 */
	public void handleGame3() {
		switch (this.game3.getGameState()) {
			case PAUSE:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					this.frame.add(this.game3View.getBackButton());
					this.frame.add(this.game3View.getResumeButton());
					this.addViewToFrame(this.game3View);
				}
				break;
				
			default:
				if (this.clicked) {
					this.game3.checkIfClickedCube(this.clickX, this.clickY);
					this.clicked = false;
				}
				if (this.dragged) {
					this.dragged = false;
					this.game3.dragCube(this.dragX, this.dragY);
				}
				if (this.released) {
					this.released = false;
					this.game3.handleRelease(this.releaseX, this.releaseY);
				}
				break;
		}
	}
	
	/**
	 * This method simply calls the frame's repaint method.
	 */
	public void paint() {
		this.frame.repaint();
	}
	
	/**
	 * This method adds the given view to the frame, and performs
	 * some other small setup for the frame.
	 * 
	 * @param view view to be added to the frame
	 */
	public void addViewToFrame(View view) {
		this.frame.getContentPane().add(view);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
	}
	
	/**
	 * This method unpauses the given game, and adds the given view to the frame. This method assumes
	 * you are adding a game's corresponding view; i.e., that you are not unpausing game 1
	 * and adding view 2 to the frame.
	 * 
	 * @param	game	the game to be unpaused
	 * @param	view	the view to be added to the frame after unpausing
	 * 
	 */
	public void unpauseGame(Minigame game, GameView view) {
		game.setGameState(game.getLastState());
		game.setLastState(GameState.PAUSE);
		this.frame.getContentPane().removeAll();
		if (view instanceof views.Minigame3View) { //need to add some extra buttons for game 3
			if(!((views.Minigame3View) view).getStatus()){
				this.frame.add(((views.Minigame3View) view).getShuffleButton());
				this.frame.add(((views.Minigame3View) view).getSubmitButton());
				this.frame.add(((views.Minigame3View) view).getTextBox());
			}
			else{
				this.frame.add(((views.Minigame3View) view).getResetButton());
			}
		}
		if (game instanceof games.Minigame2) {
			if (this.game2.getGameState() == GameState.ROUND1) {
				this.frame.add(this.game2View.getSkipTutButton());
				this.frame.add(this.game2View.getNextButton());
			}
		}
		this.screenHandled = false;
		addViewToFrame(view);
	}
	
	/**
	 * This method switches the frame from full screen to windowed or vice
	 * versa depending on the argument passed.
	 * 
	 * @param b desired frame state: true -> full screen, false -> windowed
	 */
	public void toggleWindowedMode(boolean b) {
		this.frame.dispose();
		this.frame = new JFrame();
		if (b) {
			if (this.fullScreen) {
				this.fullScreen = false;
				this.frame.setSize((int)this.screenSize.getWidth(), (int)this.screenSize.getHeight());
				this.frame.add(this.settingsView.getBackButton());
				this.frame.add(this.settingsView.getDebugToggleOn());
				this.frame.add(this.settingsView.getDebugToggleOff());
				this.frame.add(this.settingsView.getWindowedOn());
				this.frame.add(this.settingsView.getWindowedOff());
				this.frame.add(this.settingsView.getDelaware());
				this.frame.add(this.settingsView.getFlorida());
				this.frame.add(this.settingsView.getTexas());
			}
		} else {
			if (!this.fullScreen) {
				this.fullScreen = true;
				this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
				this.frame.setUndecorated(true);
				this.frame.add(this.settingsView.getBackButton());
				this.frame.add(this.settingsView.getDebugToggleOn());
				this.frame.add(this.settingsView.getDebugToggleOff());
				this.frame.add(this.settingsView.getWindowedOn());
				this.frame.add(this.settingsView.getWindowedOff());
				this.frame.add(this.settingsView.getDelaware());
				this.frame.add(this.settingsView.getFlorida());
				this.frame.add(this.settingsView.getTexas());
			}
		}
		this.addViewToFrame(this.settingsView);
	}
	
	/**
	 * This method is called by tick to update every view with game state information
	 * (some view methods depend on the current game state).
	 */
	public void updateViewStates() {
		for (View v : this.allViews) {
			v.updateStates(this.currentState, this.game1.getGameState(), this.game1.getLastState(), this.game2.getGameState(), this.game2.getLastState(), this.game3.getGameState(), this.game3.getLastState());
		}
	}
	
	/**
	 * Key event class for handling key presses. The application only cares about the P key,
	 * space bar, right arrow, and left arrow. Each key press updates its respective boolean,
	 * and updates a boolean in game 1 view.
	 * 
	 * @author marcusgula
	 *
	 */
	public class ArrowKeyEvent extends AbstractAction {
		private String command;
		public ArrowKeyEvent(String command) {
			this.command = command;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (this.command) {
				/*Key presses set their respective booleans to true*/
				case "LeftPressed":
					leftPressed = true;
					game1View.setLeftArrow(true);
					game1TutView.setLeftArrow(true);
					break;
					
				case "RightPressed":
					rightPressed = true;
					game1View.setRightArrow(true);
					game1TutView.setRightArrow(true);
					break;
					
				case "SpacePressed":
					spacePressed = true;
					game1View.setSpaceBar(true);
					game1TutView.setSpaceBar(true);
					break;
					
				/*Key releases set their respective booleans to false*/
				case "LeftReleased":
					leftPressed = false;
					game1View.setLeftArrow(false);
					game1TutView.setLeftArrow(false);
					break;
					
				case "RightReleased":
					rightPressed = false;
					game1View.setRightArrow(false);
					game1TutView.setRightArrow(false);
					break;
					
				case "SpaceReleased":
					spacePressed = false;
					game1View.setSpaceBar(false);
					game1TutView.setSpaceBar(false);
					break;
					
				/*P key activates pause menu*/	
				case "Pause":
					switch (currentState) {
						case GAME1:
							if (game1.getGameState() != GameState.PAUSE) {
								game1.setLastState(game1.getGameState());
								game1.setGameState(GameState.PAUSE);
							} else if (game1.getGameState() == GameState.PAUSE) {
								unpauseGame(game1, game1View);
							}
							break;
							
						case GAME2:
							if (game2.getGameState() != GameState.PAUSE) {
								game2.setLastState(game2.getGameState());
								game2.setGameState(GameState.PAUSE);
							} else if (game2.getGameState() == GameState.PAUSE) {
								unpauseGame(game2, game2View);
							}
							break;
							
						case GAME3:
							if (game3.getGameState() != GameState.PAUSE) {
								game3.setLastState(game3.getGameState());
								game3.setGameState(GameState.PAUSE);
							} else if (game3.getGameState() == GameState.PAUSE) {
								unpauseGame(game3, game3View);
							}
							break;
							
						default:
							break;
						}
					break;
					
				default:
					break;
			}
		}
	}
	
	/**
	 * This method binds certain keys to certain views.
	 */
	public void bindKeysToViews() {
		for (View v : this.allViews) {
			/*Bind arrow key and space bar presses and releases for game 1 and game 1 tut view*/
			if (v instanceof views.Minigame1View) {
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightPressed");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftPressed");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "SpacePressed");
				v.getActionMap().put("RightPressed", new ArrowKeyEvent("RightPressed"));
				v.getActionMap().put("LeftPressed", new ArrowKeyEvent("LeftPressed"));
				v.getActionMap().put("SpacePressed", new ArrowKeyEvent("SpacePressed"));
				
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "SpaceReleased");
				v.getActionMap().put("RightReleased", new ArrowKeyEvent("RightReleased"));
				v.getActionMap().put("LeftReleased", new ArrowKeyEvent("LeftReleased"));
				v.getActionMap().put("SpaceReleased", new ArrowKeyEvent("SpaceReleased"));
			}
			/*Add pause functionality to the P button for game 1 and game 2*/
			if (v instanceof views.Minigame1View || v instanceof views.Minigame2View) {
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "Pause");
				v.getActionMap().put("Pause", new ArrowKeyEvent("Pause"));
			}
		}
	}

	/**
	 * Unused KeyListener method.
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * Only used to change main view from start mode to select mode.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (this.currentState == AppState.START) {
			this.mainView.setSelect();
			this.mainView.setFocusable(false);
			this.mainView.removeKeyListener(this);
			this.intendedState = AppState.SELECT;
		}
	}
	
	/**
	 * Unused KeyListener method.
	 */
	@Override
	public void keyReleased(KeyEvent e) {}
	
	/**
	 * Unused MouseListener method.
	 */
	@Override
	public void mouseClicked(MouseEvent click) {}

	/**
	 * Unused MouseListener method.
	 */
	@Override
	public void mouseEntered(MouseEvent click) {}

	/**
	 * Unused MouseListener method.
	 */
	@Override
	public void mouseExited(MouseEvent click) {}

	/**
	 * Used to update the respective click booleans. Also used to skip
	 * tutorial 1.
	 */
	@Override
	public void mousePressed(MouseEvent click) {
		if (currentState == AppState.GAME2) {
			if (game2.getGameState() != GameState.PAUSE) {
				this.clicked = true;
				this.clickX = click.getX();
				this.clickY = click.getY();
			}
		} else if (currentState == AppState.GAME3) {
			if (game3.getGameState() != GameState.PAUSE) {
				this.clicked = true;
				this.clickX = click.getX();
				this.clickY = click.getY();
			}
		} else if (currentState == AppState.TUTORIAL1) {
			this.game1TutView.setTutState(TutorialState.CONTROLS1);
			if (game1.getGameState() == GameState.UNINITIALIZED) {
				intendedState = AppState.GAME1;
			} else {
				intendedState = AppState.INBETWEEN1;
			}
		}
	}

	/**
	 * Used to update the mouse released boolean for game 3.
	 */
	@Override
	public void mouseReleased(MouseEvent release) {
		if (currentState == AppState.GAME3) {
			if (game3.getGameState() != GameState.PAUSE) {
				this.released = true;
				this.releaseX = release.getX();
				this.releaseY = release.getY();
			}
		}
	}
	
	/**
	 * Used to update the mouse dragged boolean for game 3.
	 */
	@Override
	public void mouseDragged(MouseEvent drag) {
		if (currentState == AppState.GAME3) {
			if (game3.getGameState() != GameState.PAUSE) {
				this.dragged = true;
				this.dragX = drag.getX();
				this.dragY = drag.getY();
			}
		}
	}

	/**
	 * Unused MouseMotionListener method.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	/**
	 * Used to enable easier JUnit testing.
	 * @return current state of the application
	 */
	public AppState getAppState() {
		return this.currentState;
	}
}