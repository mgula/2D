package controller;

import enums.AppState;
import enums.GameState;
import enums.KeyCommand;
import enums.PauseState;
import enums.SaveFile;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

/*TODO:
 * -BIG REFACTOR (tidy up import statements)
 * -magic numbers in views --> ratios of screen size
 * -do all buttons need listeners all the time?
 * -develop system for keeping track of view offsets across saves
 * -room transitions
 * -squishing
 * -sound
 * -slopes/stairs
 * -change body
 * -pushable objects
 * -JUnit ... ? :/
 * 
 * BUGS
 * -most keys other than p
 */

public class Main implements KeyListener, MouseListener {
	private final String savePath1 = "data/game1data.txt";
	private final String savePath2 = "data/game2data.txt";
	private final String savePath3 = "data/game3data.txt";
	
	private File save1;
	private File save2;
	private File save3;
	
	private FileOutputStream fileOut;
	private ObjectOutputStream objectOut;
	private FileInputStream fileIn;
	private ObjectInputStream objectIn;
	
	private SaveFile currentSaveFile;
	private boolean loadingFromFile = false;
	
	private boolean play = true;
	private Game1 currentGame = new Game1(); //avoid null pointer in updateViewStates()
	private MainView mainView;
	private SettingsView settingsView;
	private Game1View game1View;
	private BetweenView betweenView;
	private ArrayList<View> allViews  = new ArrayList<View>();
	private AppState currentState = AppState.START;
	private AppState nextState = AppState.START;
	private JFrame frame = new JFrame();
	private boolean rightPressed = false;
	private boolean leftPressed = false;
	private boolean spacePressed = false;
	private boolean downPressed = false;
	private Dimension screenSize;
	private boolean screenHandled = false;
	private boolean fullScreen = true;
	private static final int defaultSleepTime = 30;
	private static int sleepTime = defaultSleepTime; //Time in milliseconds to wait each cycle of the main loop
	
	public static void main(String[] args) {
		Main main = new Main();
    	while (main.play) {
    		main.updateCurrentState();
    		main.tick();
    		main.paint();
    		try {
    			Thread.sleep(sleepTime);
    		} catch (InterruptedException e) {
    			System.out.println("From main(String[] args): Interrupted Exception (" + e.getMessage() + ")");
    		}
    	}
    	main.frame.setVisible(false);
    	main.frame.dispose();
	}
	
	public Main() {
		this.save1 = new File(this.savePath1);
		this.save2 = new File(this.savePath2);
		this.save3 = new File(this.savePath3);
		
		File[] files = {this.save1, this.save2, this.save3};
		
		for (int i = 0; i < files.length; i++) {
			if (!files[i].exists()) {
				try {
					files[i].createNewFile();
					this.clearSaveData(files[i]);
				} catch (IOException e) {
					System.out.println("From Main(): IO Exception (" + e.getMessage() + ")");
				}
			}
		}
		
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
				nextState = AppState.INBETWEEN1;
			}
    		});
		this.mainView.getControlsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextState = AppState.SETTINGS;
			}
    		});
		this.mainView.getExitButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextState = AppState.END;
			}
    		});
		/*In between view 1*/
		this.betweenView.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextState = AppState.SELECT;
			}
    		});
		this.betweenView.getNewGameButton1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSaveFile = SaveFile.SAVE1;
				currentGame.setGameState(GameState.UNINITIALIZED);
				nextState = AppState.GAME1;
			}
    		});
		this.betweenView.getLoadGameButton1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSaveFile = SaveFile.SAVE1;
				loadingFromFile = true;
				nextState = AppState.GAME1;
			}
    		});
		this.betweenView.getNewGameButton2().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSaveFile = SaveFile.SAVE2;
				currentGame.setGameState(GameState.UNINITIALIZED);
				nextState = AppState.GAME1;
			}
    		});
		this.betweenView.getLoadGameButton2().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSaveFile = SaveFile.SAVE2;
				loadingFromFile = true;
				nextState = AppState.GAME1;
			}
    		});
		this.betweenView.getNewGameButton3().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSaveFile = SaveFile.SAVE3;
				currentGame.setGameState(GameState.UNINITIALIZED);
				nextState = AppState.GAME1;
			}
    		});
		this.betweenView.getLoadGameButton3().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSaveFile = SaveFile.SAVE3;
				loadingFromFile = true;
				nextState = AppState.GAME1;
			}
    		});
		/*Settings view*/
		this.settingsView.getBackButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextState = AppState.SELECT;
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
		this.settingsView.getClearDataButton1().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSaveData(save1);
			}
    		});
		this.settingsView.getClearDataButton2().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSaveData(save2);
			}
    		});
		this.settingsView.getClearDataButton3().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSaveData(save3);
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
				saveGame();
				nextState = AppState.SELECT;
				currentGame.setGameState(GameState.QUIT);
				currentGame.setLastState(GameState.PAUSE);
				game1View.setPauseState(PauseState.PLAYERINFO);
				screenHandled = false;
			}
    		});
		this.game1View.getResumeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unpauseGame(currentGame, game1View);
			}
    		});
		this.game1View.getRestoreDefaultsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentGame.getEngine().restoreDefaultAttributes(currentGame.getPlayer());
				sleepTime = defaultSleepTime;
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
		this.game1View.getEditSleepTimeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeSleepTime(game1View.getEditSleepTimeField().getText());
				game1View.getEditSleepTimeField().setText("");
			}
    		});
	}
	
	public void updateCurrentState() {
		if (this.currentState != this.nextState) {
			/*Reset the frame*/
			this.frame.getContentPane().removeAll();
			switch (this.nextState) {
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
					this.frame.add(this.betweenView.getBackButton());
					if (this.save1.length() == 0) {
						this.frame.add(this.betweenView.getNewGameButton1());
					} else {
						this.frame.add(this.betweenView.getLoadGameButton1());
					}
					if (this.save2.length() == 0) {
						this.frame.add(this.betweenView.getNewGameButton2());
					} else {
						this.frame.add(this.betweenView.getLoadGameButton2());
					}
					if (this.save3.length() == 0) {
						this.frame.add(this.betweenView.getNewGameButton3());
					} else {
						this.frame.add(this.betweenView.getLoadGameButton3());
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
					this.frame.add(this.settingsView.getClearDataButton1());
					this.frame.add(this.settingsView.getClearDataButton2());
					this.frame.add(this.settingsView.getClearDataButton3());
					/*Add control view to the frame*/
					this.addViewToFrame(this.settingsView);
					break;
					
				default:
					break;
			}
			this.currentState = this.nextState;
		}
	}
	
	public void loadGame1() {
		//this.currentGame.setJumping(false);
		this.rightPressed = false;
		this.spacePressed = false;
		
		if (this.loadingFromFile) {
			this.loadGame();
			this.loadingFromFile = false;
		} else {
			this.currentGame = new Game1();
		}
		
		this.currentGame.setGameState(GameState.PLAY);
		this.currentGame.setLastState(GameState.LOAD);
		this.game1View.load(this.currentGame);
	}
	
	public void tick() {
		this.updateViewStates();
		switch (this.currentState) {
			case GAME1:
				this.game1View.updateSleepTime(sleepTime);
				this.handleGame1();
				break;
			
			case INBETWEEN1:
				this.updateSaveDataStatus();
				break;
				
			case END:
				this.play = false;
				break;
				
			default:
				break;
		}
	}
	
	public void handleGame1() {
		switch (this.currentGame.getGameState()) {
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
							this.frame.add(this.game1View.getEditSleepTimeField());
							this.frame.add(this.game1View.getEditSleepTimeButton());
							break;
							
						default:
							break;
					}
					this.addViewToFrame(this.game1View);
				}
				break;
				
			case DEATH:
				this.currentGame.respawn();
				
				this.game1View.setStartingOffsets();
				
				this.currentGame.setGameState(GameState.PLAY);
				break;
				
			case LOAD:
				this.loadGame1();
				break;
				
			case PLAY:
				this.currentGame.tick(this.rightPressed, this.leftPressed, this.spacePressed, this.downPressed);
				
				if (this.currentGame.getRoomChangeEvent()) {
					this.game1View.updateView(this.currentGame);
					this.currentGame.setRoomChangeEvent(false);
				}
				break;
				
			default:
				break;
		}
	}
	
	public void paint() {
		this.frame.repaint();
	}
	
	public void saveGame() {
		switch (this.currentSaveFile) {
			case SAVE1:
				this.writeSaveDataToFile(this.save1);
				break;
			case SAVE2:
				this.writeSaveDataToFile(this.save2);
				break;
			case SAVE3:
				this.writeSaveDataToFile(this.save3);
				break;
				
			default:
				break;
		}
	}
	
	public void writeSaveDataToFile(File save) {
		try {
			this.fileOut = new FileOutputStream(save);
			this.objectOut = new ObjectOutputStream(this.fileOut);
			this.objectOut.writeObject(this.currentGame);
			this.objectOut.close();
			this.fileOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("From writeSaveDataToFile(File save): File Not Found Exception (" + e.getMessage() + ")");
		} catch (IOException e) {
			System.out.println("From writeSaveDataToFile(File save): IO Exception (" + e.getMessage() + ")");
		}
	}
	
	public void loadGame() {
		switch (this.currentSaveFile) {
			case SAVE1:
				this.loadSaveDataFromFile(this.save1);
				break;
			case SAVE2:
				this.loadSaveDataFromFile(this.save2);
				break;
			case SAVE3:
				this.loadSaveDataFromFile(this.save3);
				break;
			
			default:
				break;
		}
	}
	
	public void loadSaveDataFromFile(File save) {
		try {
			this.fileIn = new FileInputStream(save);
			this.objectIn = new ObjectInputStream(this.fileIn);
			this.currentGame = (Game1)this.objectIn.readObject();
			this.objectIn.close();
			this.fileIn.close();
		} catch (FileNotFoundException e) {
			System.out.println("From loadSaveDataFromFile(File save): File Not Found Exception (" + e.getMessage() + ")");
		} catch (IOException e) {
			System.out.println("From loadSaveDataFromFile(File save): IO Exception (" + e.getMessage() + ")");
		} catch (ClassNotFoundException e) {
			System.out.println("From loadSaveDataFromFile(File save): Class Not Found Exception (" + e.getMessage() + ")");
		}
	}
	
	public void clearSaveData(File file) {
		try {
			PrintWriter w = new PrintWriter(file);
			w.print("");
			w.close();
		} catch (FileNotFoundException e) {
			System.out.println("From clearSaveData(File file): File Not Found Exception (" + e.getMessage() + ")");
		}
	}
	
	
	public void updateSaveDataStatus() {
		String s1 = "No Save Data";
		String s2 = "No Save Data";
		String s3 = "No Save Data";
		
		if (this.save1.length() != 0) {
			s1 = "Has data";
		}
		if (this.save2.length() != 0) {
			s2 = "Has data";
		}
		if (this.save3.length() != 0) {
			s3 = "Has data";
		}
		
		this.betweenView.setDataString(s1, s2, s3);
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
			v.updateStates(this.currentState, this.currentGame.getGameState(), this.currentGame.getLastState());
		}
	}
	
	public void changeMaxJumps(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getEngine().setMaxJumps(num);
	}
	
	public void changeFloatingThreshold(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getEngine().setFloatingThreshold(num);
	}
	
	public void changeXIncr(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getPlayer().setXIncr(num);
	}
	
	public void changeYIncr(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getPlayer().setYIncr(num);
	}
	
	public static void changeSleepTime(String input) {
		int num = Integer.parseInt(input);
		sleepTime = num;
	}
	
	public class ArrowKeyEvent extends AbstractAction {
		private KeyCommand command;
		public ArrowKeyEvent(KeyCommand command) {
			this.command = command;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (this.command) {
				/*Key presses set their respective booleans to true*/
				case LEFTPRESSED:
					leftPressed = true;
					game1View.setLeftArrow(true);
					break;
					
				case RIGHTPRESSED:
					rightPressed = true;
					game1View.setRightArrow(true);
					break;
					
				case SPACEPRESSED:
					spacePressed = true;
					game1View.setSpaceBar(true);
					break;
					
				case DOWNPRESSED:
					downPressed = true;
					break;
					
				/*Key releases set their respective booleans to false*/
				case LEFTRELEASED:
					leftPressed = false;
					game1View.setLeftArrow(false);
					break;
					
				case RIGHTRELEASED:
					rightPressed = false;
					game1View.setRightArrow(false);
					break;
					
				case SPACERELEASED:
					spacePressed = false;
					game1View.setSpaceBar(false);
					break;
					
				case DOWNRELEASED:
					downPressed = false;
					break;
					
				/*P key activates pause menu*/	
				case PAUSE:
					switch (currentState) {
						case GAME1:
							if (currentGame.getGameState() != GameState.PAUSE) {
								currentGame.setLastState(currentGame.getGameState());
								currentGame.setGameState(GameState.PAUSE);
							} else if (currentGame.getGameState() == GameState.PAUSE) {
								unpauseGame(currentGame, game1View);
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
				v.getActionMap().put("RightPressed", new ArrowKeyEvent(KeyCommand.RIGHTPRESSED));
				v.getActionMap().put("LeftPressed", new ArrowKeyEvent(KeyCommand.LEFTPRESSED));
				v.getActionMap().put("SpacePressed", new ArrowKeyEvent(KeyCommand.SPACEPRESSED));
				v.getActionMap().put("DownPressed", new ArrowKeyEvent(KeyCommand.DOWNPRESSED));
				
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RightReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LeftReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "SpaceReleased");
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DownReleased");
				v.getActionMap().put("RightReleased", new ArrowKeyEvent(KeyCommand.RIGHTRELEASED));
				v.getActionMap().put("LeftReleased", new ArrowKeyEvent(KeyCommand.LEFTRELEASED));
				v.getActionMap().put("SpaceReleased", new ArrowKeyEvent(KeyCommand.SPACERELEASED));
				v.getActionMap().put("DownReleased", new ArrowKeyEvent(KeyCommand.DOWNRELEASED));
				
				v.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "Pause");
				v.getActionMap().put("Pause", new ArrowKeyEvent(KeyCommand.PAUSE));
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
			this.nextState = AppState.SELECT;
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
	
	public AppState getAppState() {
		return this.currentState;
	}
}
