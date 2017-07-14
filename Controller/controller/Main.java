package controller;

import enums.AppState;

import enums.GameState;
import games.*;
import views.*;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.Color;
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
	private Minigame1 game1 = new Minigame1(false);
	private MainView mainView;
	private SettingsView settingsView;
	private Minigame1View game1View;
	private BetweenView game1ViewBV;
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
		this.game1View = new Minigame1View(width, height);
		this.game1ViewBV = new BetweenView(width, height, AppState.GAME1);
		this.allViews.add(this.mainView);
		this.allViews.add(this.settingsView);
		this.allViews.add(this.game1View);
		this.allViews.add(this.game1ViewBV);
		for (View v : this.allViews) {
			v.initButtonLocations();
			v.initButtons();
			if (v instanceof views.GameView) {
				((views.GameView) v).loadImgs();
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
				intendedState = AppState.INBETWEEN1;
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
					
				case SELECT:
					/*Add the menu buttons to the frame*/
					this.frame.add(this.mainView.getMg1Button());
					this.frame.add(this.mainView.getControlsButton());
					this.frame.add(this.mainView.getExitButton());
					/*Add main view to the frame*/
					this.addViewToFrame(this.mainView);
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
					}
					
					this.addViewToFrame(this.game1ViewBV);
					break;
					
				case SETTINGS:
					/*Add radio buttons and back button to the frame*/
					this.frame.add(this.settingsView.getBackButton());
					this.frame.add(this.settingsView.getDebugToggleOn());
					this.frame.add(this.settingsView.getDebugToggleOff());
					this.frame.add(this.settingsView.getWindowedOn());
					this.frame.add(this.settingsView.getWindowedOff());
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
		this.frame.setBackground(Color.GRAY);
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
			v.updateStates(this.currentState, this.game1.getGameState(), this.game1.getLastState());
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
					break;
					
				case "RightPressed":
					rightPressed = true;
					game1View.setRightArrow(true);
					break;
					
				case "SpacePressed":
					spacePressed = true;
					game1View.setSpaceBar(true);
					break;
					
				/*Key releases set their respective booleans to false*/
				case "LeftReleased":
					leftPressed = false;
					game1View.setLeftArrow(false);
					break;
					
				case "RightReleased":
					rightPressed = false;
					game1View.setRightArrow(false);
					break;
					
				case "SpaceReleased":
					spacePressed = false;
					game1View.setSpaceBar(false);
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
		
	}

	/**
	 * Used to update the mouse released boolean for game 3.
	 */
	@Override
	public void mouseReleased(MouseEvent release) {
		
	}
	
	/**
	 * Used to update the mouse dragged boolean for game 3.
	 */
	@Override
	public void mouseDragged(MouseEvent drag) {
		
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