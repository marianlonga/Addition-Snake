package com.marianlonga.additionsnake;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class MainFrame extends JApplet {
	
	// CONSTANTS
	final static int
		SCREEN_W				= 800,	// screen width (in px)
		SCREEN_H				= 600,	// screen height (in px)
		BOARD_W					= 25,	// the width of board (in number of fields)
		BOARD_H					= 16,	// the height of board (in number of fields)
		BOARD_OFFSET_X			= 35,	// left margin of the field (int px)
		BOARD_OFFSET_Y			= 80,	// top margin of the field (in px)
		FIELD_SIZE				= 28,	// the width and height of a field (in px)
		FPS						= 24,	// Frames per second
		INITIAL_SPEED			= 5,	// initial speed in number of fields per second
		NUMBER_OF_FOODS			= 5,	// number of foods present on board
		MAX_SUM					= 20,	// maximum sum of the 2 numbers displayed on top of the screen
		SCORE_INCREASE			= 10,	// how much score is increased when snake eats a food with correct sum
		SCORE_DECREASE			= 5,	// how much score is decreased when snake eats a food with wrong sum
		SCORE_DECREASE_BY_HINT	= 4,	// how much score is decreased when hint is given
		SPEED_SCORE_COEFFICIENT	= 30,	// after increasing/decreasing score by the coefficient, the speed increases/decreases by 1
		SPECIAL_FOOD_PERIOD		= 5000;	// in what intervals should special food appear (in ms)

	private static final long serialVersionUID = 1L;
	static JPanel	gamePanel = null, 
					menuPanel = null,
					highScorePanel = null;
	
	public void init() {
		//super();
		this.setSize(SCREEN_W, SCREEN_H);
		this.setLayout(null);
		
		Translations.setLanguage("English");
		
		setupHighScoreFile();
		
		gamePanel = new GamePanel(this);
		menuPanel = new MenuPanel(this);
		highScorePanel = new HighScorePanel(this);
		
		this.getContentPane().add(gamePanel);
		this.getContentPane().add(menuPanel);
		this.getContentPane().add(highScorePanel);
		
		showMenuPanel();
	}
	
	// creates high score file if it doesn't exist
	private void setupHighScoreFile() {
		File highScoreFile = new File("highscore.txt");
		if(!highScoreFile.exists()) {
			ArrayList<UsernameScoreTuple> usernameScoreTuples = new ArrayList<UsernameScoreTuple>();
			for(int i = 0; i < 10; i++) usernameScoreTuples.add(new UsernameScoreTuple("nobody", 0));
			FileOperations.writeHighScoreFile(highScoreFile, usernameScoreTuples);
		}
	}
	
	// Functions for showing different panels
	private void showMenuPanel() {
		menuPanel.setVisible(true);
		gamePanel.setVisible(false);
		highScorePanel.setVisible(false);
	}
	private void showGamePanel() {
		gamePanel.setVisible(true);
		menuPanel.setVisible(false);
		highScorePanel.setVisible(false);
	}
	private void showHighScorePanel() {
		highScorePanel.setVisible(true);
		gamePanel.setVisible(false);
		menuPanel.setVisible(false);
	}
	
	// called after user has clicked on the New Game button to start new game
	public void menuToGame() {
		String username = JOptionPane.showInputDialog(Translations.translate("Enter your username"));
		if(username != null) {// user hasn't pressed cancel
			if(username.matches("[a-zA-Z0-9]{2,}")) {
				showGamePanel();
				((GamePanel) gamePanel).newGame(username);
			}
			else {
				JOptionPane.showMessageDialog(this, Translations.translate("Username may contain only alphanumeric characters and must be at least 2 letters long!"));
			}
		}
		menuPanel.requestFocusInWindow();
	}
	
	// called after user has pressed [ESC] from game to return to menu
	public void gameToMenu(String username, int gainedScore) {
		showMenuPanel();
		JOptionPane.showMessageDialog(this, Translations.translate("User") + " '" + username + "' " + Translations.translate("has earned") + " " + gainedScore + " " + Translations.translate("points") + "!");
		
		// if score is a high score, write it to high score file
		File highscoreFile = new File("highscore.txt");
		ArrayList<UsernameScoreTuple> usernameScoreTuples = FileOperations.readHighScoreFile(highscoreFile);
		int whereToPutNewScore = -1;
		for(int i = 0; i < usernameScoreTuples.size(); i++) { // determines the position where to insert the new score
			if(gainedScore >= usernameScoreTuples.get(i).getScore()) {
				whereToPutNewScore = i;
				break;
			}
		}
		if(whereToPutNewScore != -1) { // add new score to the list and remove the lowest score
			usernameScoreTuples.add(whereToPutNewScore, new UsernameScoreTuple(username, gainedScore));
			usernameScoreTuples.remove(usernameScoreTuples.size()-1);
			FileOperations.writeHighScoreFile(highscoreFile, usernameScoreTuples);
		}
	}
	// called after user has clicked on the High Score button to start new game
	public void menuToHighScore() {
		((HighScorePanel) highScorePanel).refreshHighScore();
		showHighScorePanel();
	}
	// called after user has pressed [ESC] from high score screen to return to menu
	public void highScoreToMenu() {
		showMenuPanel();
	}
	
	
	

}
