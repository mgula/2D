package controller;

import engine.*;
import enums.AppState;
import enums.GameState;
import enums.PauseState;
import enums.SaveFile;
import views.*;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
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
 * -magic numbers in views --> ratios of screen size
 * -do all buttons need listeners all the time?
 * -develop system for keeping track of view offsets across saves
 * -room transitions (visuals)
 * -squishing
 * -sound
 * -slopes/stairs
 * -pushable objects
 * -JUnit tests.
 * -set adjacent autonomous back to null where relevant (in gameengine)
 * 
 * Things to remove if there's ever a final version:
 * -frame by frame debugging (q and w keys)
 * -junit test suite and debug enums
 */

public class Game implements MouseListener {
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
	
	private GameEngine currentGame = new GameEngine();
	private GameWrapper wrapper = new GameWrapper(); //avoid null pointer in updateViewStates()
	
	private MainView mainView;
	private SettingsView settingsView;
	private Game1View game1View;
	private BetweenView betweenView;
	private ArrayList<View> allViews  = new ArrayList<View>();
	private AppState currentState = AppState.START;
	private AppState nextState = AppState.START;
	private JFrame frame;
	
	private boolean rightPressed = false;
	private boolean leftPressed = false;
	private boolean spacePressed = false;
	private boolean downPressed = false;
	private boolean cPressed = false;
	private boolean lPressed = false;
	
	private boolean cLock = false;
	private boolean lLock = false;
	
	private Dimension screenSize;
	private boolean screenHandled = false;
	private boolean fullScreen = true;
	private final int defaultSleepTime = 15;
	
	private int sleepTime = this.defaultSleepTime; //Time in milliseconds to wait each cycle of the main loop
	
	private boolean byTick = false; //for debugging one tick at a time
	private boolean advanceTick = false;
	
	public boolean isRunning() {
		return this.play;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public void run() {
		this.updateCurrentState();
		this.tick();
		this.paint();
		try {
			Thread.sleep(this.sleepTime);
		} catch (InterruptedException e) {
			System.out.println("From run(): Interrupted Exception (" + e.getMessage() + ")");
		}
	}
	
	public Game() {
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
			if (v instanceof views.Game1View) {
				this.bindGameKeys(v);
			}
			if (v instanceof views.MainView) {
				this.bindSpaceBar(v);
			}
		}
		this.setButtonListeners();
		this.initFrame();
		this.addViewToFrame(this.mainView);
	}
	
	public void initFrame() {
		this.frame = new JFrame();
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
		this.frame.setUndecorated(true);
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
				wrapper.setGameState(GameState.UNINITIALIZED);
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
				wrapper.setGameState(GameState.UNINITIALIZED);
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
				wrapper.setGameState(GameState.UNINITIALIZED);
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
				game1View.setPauseState(PauseState.PLAYER_INFO);
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
				wrapper.setGameState(GameState.QUIT);
				wrapper.setLastState(GameState.PAUSE);
				game1View.setPauseState(PauseState.PLAYER_INFO);
				screenHandled = false;
			}
		});
		this.game1View.getResumeButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unpauseGame(wrapper, game1View);
			}
		});
		this.game1View.getRestoreDefaultsButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentGame.restoreDefaultAttributes(currentGame.getPlayer());
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
					for (JComponent j : this.mainView.allComponents()) {
						this.frame.getContentPane().add(j);
					}
					/*Add main view to the frame*/
					this.addViewToFrame(this.mainView);
					break;
					
				case INBETWEEN1:
					this.frame.getContentPane().add(this.betweenView.getBackButton());
					if (this.save1.length() == 0) {
						this.frame.getContentPane().add(this.betweenView.getNewGameButton1());
					} else {
						this.frame.getContentPane().add(this.betweenView.getLoadGameButton1());
					}
					if (this.save2.length() == 0) {
						this.frame.getContentPane().add(this.betweenView.getNewGameButton2());
					} else {
						this.frame.getContentPane().add(this.betweenView.getLoadGameButton2());
					}
					if (this.save3.length() == 0) {
						this.frame.getContentPane().add(this.betweenView.getNewGameButton3());
					} else {
						this.frame.getContentPane().add(this.betweenView.getLoadGameButton3());
					}
					this.addViewToFrame(this.betweenView);
					break;
					
				case SETTINGS:
					/*Add radio buttons and back button to the frame*/
					for (JComponent j : this.settingsView.allComponents()) {
						this.frame.getContentPane().add(j);
					}
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
		this.leftPressed = false;
		this.rightPressed = false;
		this.spacePressed = false;
		this.downPressed = false;
		this.cPressed = false;
		this.leftPressed = false;
		
		if (this.loadingFromFile) {
			this.loadGame();
			this.loadingFromFile = false;
		} else {
			this.currentGame = new GameEngine();
			this.wrapper = new GameWrapper();
			
			this.currentGame.setMap(this.wrapper.getCurrMap());
			this.currentGame.initMapAndRoom();
		}
		
		this.wrapper.setGameState(GameState.PLAY);
		this.wrapper.setLastState(GameState.LOAD);
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
		switch (this.wrapper.getGameState()) {
			case PAUSE:
				if (!this.screenHandled) {
					this.screenHandled = true;
					this.frame.getContentPane().removeAll();
					
					//Add tabs
					this.frame.getContentPane().add(this.game1View.getPlayerInfoButton());
					this.frame.getContentPane().add(this.game1View.getSystemButton());
					if (this.game1View.getDebugMode()) {
						this.frame.getContentPane().add(this.game1View.getDebugButton());
					}
					
					switch (this.game1View.getPauseState()) {
						case PLAYER_INFO:
							break;
						
						case SYSTEM:
							this.frame.getContentPane().add(this.game1View.getBackButton());
							this.frame.getContentPane().add(this.game1View.getResumeButton());
							break;
							
						case DEBUG:
							this.frame.getContentPane().add(this.game1View.getRestoreDefaultsButton());
							this.frame.getContentPane().add(this.game1View.getEditJumpsField());
							this.frame.getContentPane().add(this.game1View.getEditJumpsButton());
							this.frame.getContentPane().add(this.game1View.getEditFloatField());
							this.frame.getContentPane().add(this.game1View.getEditFloatButton());
							this.frame.getContentPane().add(this.game1View.getEditXIncrField());
							this.frame.getContentPane().add(this.game1View.getEditXIncrButton());
							this.frame.getContentPane().add(this.game1View.getEditYIncrField());
							this.frame.getContentPane().add(this.game1View.getEditYIncrButton());
							this.frame.getContentPane().add(this.game1View.getEditSleepTimeField());
							this.frame.getContentPane().add(this.game1View.getEditSleepTimeButton());
							break;
							
						default:
							break;
					}
					this.addViewToFrame(this.game1View);
				}
				break;
				
			case DEATH:
				this.currentGame.respawn(this.currentGame.getPlayer());
				
				this.game1View.setStartingOffsets();
				
				this.wrapper.setGameState(GameState.PLAY);
				break;
				
			case LOAD:
				this.loadGame1();
				break;
				
			case PLAY:
				/*Use wrapper to handle arrow keys*/
				if (this.byTick) {
					if (this.advanceTick) {
						this.wrapper.tick(this.currentGame, this.rightPressed, this.leftPressed, this.spacePressed, this.downPressed);
						this.advanceTick = false;
					}
				} else {
					this.wrapper.tick(this.currentGame, this.rightPressed, this.leftPressed, this.spacePressed, this.downPressed);
				}
				
				/*Handle room change event - view needs to be updated as well*/
				if (this.currentGame.getRoomChangeEvent()) {
					this.game1View.updateView(this.currentGame);
					this.currentGame.setRoomChangeEvent(false);
				}
				
				/*Change bodies - view needs to be updated as well*/
				if (this.cPressed && !this.cLock) {
					this.cLock = true;
					this.currentGame.changeBody();
					this.game1View.loadPlayer(this.currentGame);
				}
				
				/*Activate a text area*/
				if (this.lPressed && !this.lLock) {
					this.lLock = true;
					this.currentGame.activateTextArea();
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
			this.currentGame = (GameEngine) this.objectIn.readObject();
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
	
	public void unpauseGame(GameWrapper game, GameView view) {
		game.setGameState(game.getLastState());
		game.setLastState(GameState.PAUSE);
		this.frame.getContentPane().removeAll();
		this.screenHandled = false;
		this.game1View.setPauseState(PauseState.PLAYER_INFO);
		this.addViewToFrame(view);
	}
	
	public void toggleWindowedMode(boolean b) {
		this.frame.dispose();
		this.frame = new JFrame();
		if (b) {
			if (this.fullScreen) {
				this.fullScreen = false;
				this.frame.setSize((int)this.screenSize.getWidth(), (int)this.screenSize.getHeight());
				for (JComponent j : this.settingsView.allComponents()) {
					this.frame.getContentPane().add(j);
				}
			}
		} else {
			if (!this.fullScreen) {
				this.fullScreen = true;
				this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
				this.frame.setUndecorated(true);
				for (JComponent j : this.settingsView.allComponents()) {
					this.frame.getContentPane().add(j);
				}
			}
		}
		this.addViewToFrame(this.settingsView);
	}
	
	public void updateViewStates() {
		for (View v : this.allViews) {
			v.updateStates(this.currentState, this.wrapper.getGameState(), this.wrapper.getLastState());
		}
	}
	
	public void changeMaxJumps(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getPlayer().setMaxJumps(num);
	}
	
	public void changeFloatingThreshold(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getPlayer().setFloatingThreshold(num);
	}
	
	public void changeXIncr(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getPlayer().setXIncr(num);
	}
	
	public void changeYIncr(String input) {
		int num = Integer.parseInt(input);
		this.currentGame.getPlayer().setYIncr(num);
	}
	
	public void changeSleepTime(String input) {
		int num = Integer.parseInt(input);
		this.sleepTime = num;
	}
	
	public void bindSpaceBar(View v) {
		final int wifw = JComponent.WHEN_IN_FOCUSED_WINDOW;
		
		final KeyStroke spacePress = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
		
		v.getInputMap(wifw).put(spacePress, "Space Pressed");
		v.getActionMap().put("Space Pressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.setSelect();
				nextState = AppState.SELECT;
			}	
		});
	}
	
	public void bindGameKeys(View v) {
		InputMap i = v.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap a = v.getActionMap();
		
		i.clear();
		a.clear();
		
		final KeyStroke rightPress = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false);
		final KeyStroke leftPress = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false);
		final KeyStroke spacePress = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false);
		final KeyStroke downPress = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false);
		final KeyStroke mPress = KeyStroke.getKeyStroke(KeyEvent.VK_M, 0, false);
		final KeyStroke cPress = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false);
		final KeyStroke lPress = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, false);
		final KeyStroke qPress = KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false);
		final KeyStroke wPress = KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false);
		final KeyStroke rightRelease = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true);
		final KeyStroke leftRelease = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true);
		final KeyStroke spaceRelease = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true);
		final KeyStroke downRelease = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true);
		final KeyStroke cRelease = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true);
		final KeyStroke lRelease = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0, true);
		
		final String rightPressString = "RightPressed";
		final String leftPressString = "LeftPressed";
		final String spacePressString = "SpacePressed";
		final String downPressString = "DownPressed";
		final String mPressString = "MPressed";
		final String cPressString = "CPressed";
		final String lPressString = "LPressed";
		final String qPressString = "QPressed";
		final String wPressString = "WPressed";
		final String rightReleaseString = "RightReleased";
		final String leftReleaseString = "LeftReleased";
		final String spaceReleaseString = "SpaceReleased";
		final String downReleaseString = "DownReleased";
		final String cReleaseString = "CReleased";
		final String lReleaseString = "LReleased";
		
		/*Add to input map*/
		i.put(rightPress, rightPressString);
		i.put(leftPress, leftPressString);
		i.put(spacePress, spacePressString);
		i.put(downPress, downPressString);
		i.put(mPress, mPressString);
		i.put(cPress, cPressString);
		i.put(lPress, lPressString);
		i.put(rightRelease, rightReleaseString);
		i.put(leftRelease, leftReleaseString);
		i.put(spaceRelease, spaceReleaseString);
		i.put(downRelease, downReleaseString);
		i.put(cRelease, cReleaseString);
		i.put(lRelease, lReleaseString);
		/*Debug*/
		i.put(qPress, qPressString);
		i.put(wPress, wPressString);
		
		/*Add to action map*/
		a.put(rightPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightPressed = true;
				game1View.setRightArrow(true);
				return;
			}	
		});
		a.put(leftPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leftPressed = true;
				game1View.setLeftArrow(true);
				return;
			}	
		});
		a.put(spacePressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spacePressed = true;
				game1View.setSpaceBar(true);
				return;
			}	
		});
		a.put(downPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				downPressed = true;
				return;
			}	
		});
		a.put(mPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentState == AppState.GAME1) {
					if (wrapper.getGameState() != GameState.PAUSE) {
						wrapper.setLastState(wrapper.getGameState());
						wrapper.setGameState(GameState.PAUSE);
					} else if (wrapper.getGameState() == GameState.PAUSE) {
						unpauseGame(wrapper, game1View);
					}
				}
				return;
			}	
		});
		a.put(cPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cPressed = true;
				return;
			}	
		});
		a.put(lPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lPressed = true;
				return;
			}	
		});
		a.put(rightReleaseString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightPressed = false;
				game1View.setRightArrow(false);
				return;
			}	
		});
		a.put(leftReleaseString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				leftPressed = false;
				game1View.setLeftArrow(false);
				return;
			}	
		});
		a.put(spaceReleaseString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spacePressed = false;
				game1View.setSpaceBar(false);
				return;
			}	
		});
		a.put(downReleaseString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				downPressed = false;
				return;
			}	
		});
		a.put(cReleaseString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cPressed = false;
				cLock = false;
				return;
			}	
		});
		a.put(lReleaseString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lPressed = false;
				lLock = false;
				return;
			}	
		});
		/*Debug*/
		a.put(qPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				byTick = !byTick;
				return;
			}	
		});
		a.put(wPressString, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				advanceTick = true;
				return;
			}	
		});
	}
	
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
