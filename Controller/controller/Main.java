package controller;

import enums.AppState;
import enums.Direction;
import enums.GameState;
import enums.PauseState;
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
 * -implement serializable
 * -file system (no new game button, just save slots)
 * -room transitions
 * -sound
 * -slopes/stairs
 * 
 * BUGS
 * -new game button (after 1st click)
 * -WASD keys
 */

public class Main implements KeyListener, MouseListener, MouseMotionListener {
	private boolean play = true;
	private Game1 game1 = new Game1(); //avoid null pointer in updateViewStates()
	private MainView mainView;
	private SettingsView settingsView;
	private Game1View game1View;
	private BetweenView betweenView;
	private ArrayList<View> allViews  = new ArrayList<View>();
	private AppState currentState = AppState.START;
	private AppState intendedState = AppState.SATISFIED;
	private JFrame frame = new JFrame();
	private boolean firstPlay = true;
	private boolean rightPressed = false;
	private boolean leftPressed = false;
	private boolean spacePressed = false;
	private boolean downPressed = false;
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
	private boolean screenHandled = false;
	private boolean fullScreen = true;
	private static final int sleepTime = 30; //Time in milliseconds to wait each cycle of the main loop
	
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
	
	public void init() {
		this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)this.screenSize.getWidth();
		int height = (int)this.screenSize.getHeight();
		this.mainView = new MainView(width, height);
		this.mainView.setFocusable(true);
		this.mainView.addKeyListener(this);
		this.settingsView = new SettingsView(width, height);
		this.game1View = new Game1View(width, height);
		this.betweenView = new BetweenView(width, height);
		this.allViews.add(this.mainView);
		this.allViews.add(this.settingsView);
		this.allViews.add(this.game1View);
		this.allViews.add(this.betweenView);
		for (View v : this.allViews) {
			v.initButtonLocations();
			v.initButtons();
			v.setDebugMode(true); //default to debug mode on
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
		this.betweenView.getNewGameButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1.setLastState(GameState.UNINITIALIZED);
				intendedState = AppState.GAME1;
			}
    	});
		this.betweenView.getLoadButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentState = AppState.GAME1;
				unpauseGame(game1, game1View);
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
		this.game1View.getPlayerInfoButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1View.setPauseState(PauseState.PLAYERINFO);
				screenHandled = false;
			}
    	});
		this.game1View.getSystemButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1View.setPauseState(PauseState.SYSTEM);
				screenHandled = false;
			}
    	});
		this.game1View.getDebugButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1View.setPauseState(PauseState.DEBUG);
				screenHandled = false;
			}
    	});
		this.game1View.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				intendedState = AppState.SELECT;
				game1View.setPauseState(PauseState.PLAYERINFO);
				screenHandled = false;
			}
    	});
		this.game1View.getResumeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unpauseGame(game1, game1View);
			}
    	});
		this.game1View.getRestoreDefaultsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game1.getPlayer().restoreDefaultAttributes();
			}
    	});
		this.game1View.getEditJumpsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeMaxJumps(game1View.getEditJumpsField().getText());
				game1View.getEditJumpsField().setText("");
			}
    	});
		this.game1View.getEditFloatButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeFloatingThreshold(game1View.getEditFloatField().getText());
				game1View.getEditFloatField().setText("");
			}
    	});
		this.game1View.getEditXIncrButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeXIncr(game1View.getEditXIncrField().getText());
				game1View.getEditXIncrField().setText("");
			}
    	});
		this.game1View.getEditYIncrButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeYIncr(game1View.getEditYIncrField().getText());
				game1View.getEditYIncrField().setText("");
			}
    	});
	}
	
	public void updateCurrentState() {
		if (this.intendedState != AppState.SATISFIED) {
			/*Reset the frame*/
			this.frame.getContentPane().removeAll();
			switch (this.intendedState) {
				case GAME1:
					this.loadGame1();
					this.addViewToFrame(this.game1View);
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
					this.frame.add(this.betweenView.getNewGameButton());
					if (this.firstPlay) {
						this.firstPlay = false;
					} else {
						this.frame.add(this.betweenView.getLoadButton());
					}
					this.addViewToFrame(this.betweenView);
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
	
	public void loadGame1() {
		this.game1.setJumping(false);
		this.rightPressed = false;
		this.spacePressed = false;
		switch (this.game1.getLastState()) {
			case UNINITIALIZED:
				this.game1 = new Game1();
				this.game1.setGameState(GameState.PLAY);
				break;
			
			case PAUSE:
				this.game1.setGameState(GameState.PLAY);
				break;
			
			default:
				break;
		}
		this.game1.setLastState(GameState.LOAD);
		this.game1View.load(this.game1);
	}
	
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
	
	public void handleGame1() {
		switch (this.game1.getGameState()) {
			case PAUSE:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					
					//Add tabs
					this.frame.add(this.game1View.getPlayerInfoButton());
					this.frame.add(this.game1View.getSystemButton());
					if (this.game1View.getDebugMode()) {
						this.frame.add(this.game1View.getDebugButton());
					}
					
					switch (this.game1View.getPauseState()) {
						case PLAYERINFO:
							
							break;
						
						case SYSTEM:
							this.frame.add(this.game1View.getBackButton());
							this.frame.add(this.game1View.getResumeButton());
							break;
							
						case DEBUG:
							this.frame.add(this.game1View.getRestoreDefaultsButton());
							this.frame.add(this.game1View.getEditJumpsField());
							this.frame.add(this.game1View.getEditJumpsButton());
							this.frame.add(this.game1View.getEditFloatField());
							this.frame.add(this.game1View.getEditFloatButton());
							this.frame.add(this.game1View.getEditXIncrField());
							this.frame.add(this.game1View.getEditXIncrButton());
							this.frame.add(this.game1View.getEditYIncrField());
							this.frame.add(this.game1View.getEditYIncrButton());
							break;
							
						default:
							break;
					}
					this.addViewToFrame(this.game1View);
				}
				break;
				
			case DEATH:
				this.game1.respawn();
				
				this.game1.setJumping(false);
				
				this.game1View.setStartingOffsets();
				
				this.game1.setGameState(GameState.PLAY);
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
					if (this.game1.getRoomChangeEvent()) {
						this.game1.changeRoom(Direction.EAST);
						this.game1View.updateView(this.game1);
					}
				}
				if (this.leftPressed) {
					this.game1.moveLeft();
					if (this.game1.getRoomChangeEvent()) {
						this.game1.changeRoom(Direction.WEST);
						this.game1View.updateView(this.game1);
					}
				}
				if (this.spacePressed) {
					this.game1.setJumping(true);
				}
				if (this.downPressed) {
					this.game1.phaseThroughPlatform();
				}
				this.game1.evaluateJumping();
				this.game1.gameStateCheck();
				break;
		}
	}
	
	public void paint() {
		this.frame.repaint();
	}
	
	public void addViewToFrame(View view) {
		this.frame.setBackground(Color.GRAY);
		this.frame.getContentPane().add(view);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
	}
	
	public void unpauseGame(Game game, GameView view) {
		game.setGameState(game.getLastState());
		game.setLastState(GameState.PAUSE);
		this.frame.getContentPane().removeAll();
		this.screenHandled = false;
		this.game1View.setPauseState(PauseState.PLAYERINFO);
		addViewToFrame(view);
	}
	
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
	
	public void updateViewStates() {
		for (View v : this.allViews) {
			v.updateStates(this.currentState, this.game1.getGameState(), this.game1.getLastState());
		}
	}
	
	public void changeMaxJumps(String input) {
		int num = Integer.parseInt(input);
		this.game1.getPlayer().setMaxJumps(num);
	}
	
	public void changeFloatingThreshold(String input) {
		int num = Integer.parseInt(input);
		this.game1.getPlayer().setFloatingThreshold(num);
	}
	
	public void changeXIncr(String input) {
		int num = Integer.parseInt(input);
		this.game1.getPlayer().setXIncr(num);
	}
	
	public void changeYIncr(String input) {
		int num = Integer.parseInt(input);
		this.game1.getPlayer().setYIncr(num);
	}
	
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
					
				case "DownPressed":
					downPressed = true;
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
					
				case "DownReleased":
					downPressed = false;
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
	
	public void bindKeysToViews() {
		for (View v : this.allViews) {
			if (v instanceof views.Game1View) {
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RightPressed");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LeftPressed");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "SpacePressed");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DownPressed");
				v.getActionMap().put("RightPressed", new ArrowKeyEvent("RightPressed"));
				v.getActionMap().put("LeftPressed", new ArrowKeyEvent("LeftPressed"));
				v.getActionMap().put("SpacePressed", new ArrowKeyEvent("SpacePressed"));
				v.getActionMap().put("DownPressed", new ArrowKeyEvent("DownPressed"));
				
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "SpaceReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownReleased");
				v.getActionMap().put("RightReleased", new ArrowKeyEvent("RightReleased"));
				v.getActionMap().put("LeftReleased", new ArrowKeyEvent("LeftReleased"));
				v.getActionMap().put("SpaceReleased", new ArrowKeyEvent("SpaceReleased"));
				v.getActionMap().put("DownReleased", new ArrowKeyEvent("DownReleased"));
				
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "Pause");
				v.getActionMap().put("Pause", new ArrowKeyEvent("Pause"));
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (this.currentState == AppState.START) {
			this.mainView.setSelect();
			this.mainView.setFocusable(false);
			this.mainView.removeKeyListener(this);
			this.intendedState = AppState.SELECT;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent click) {}

	@Override
	public void mouseEntered(MouseEvent click) {}

	@Override
	public void mouseExited(MouseEvent click) {}

	@Override
	public void mousePressed(MouseEvent click) {}

	@Override
	public void mouseReleased(MouseEvent release) {}
	
	@Override
	public void mouseDragged(MouseEvent drag) {}

	@Override
	public void mouseMoved(MouseEvent e) {}
	
	public AppState getAppState() {
		return this.currentState;
	}
}