package controller;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
    	while (game.isRunning()) {
    		game.updateCurrentState();
    		game.tick();
    		game.paint();
    		try {
    			Thread.sleep(Game.sleepTime);
    		} catch (InterruptedException e) {
    			System.out.println("From main(String[] args): Interrupted Exception (" + e.getMessage() + ")");
    		}
    	}
    	game.getFrame().setVisible(false);
    	game.getFrame().dispose();
	}
}
