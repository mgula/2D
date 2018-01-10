package controller;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
		while (game.isRunning()) {
			game.run();
		}
		game.getFrame().setVisible(false);
		game.getFrame().dispose();
	}
}
