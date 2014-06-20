/* (c) 2014 Marian Longa */
/* GameFrame contains all GUI and is a main class */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;


public class GamePanel extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	int[][] board = null;
	Snake snake = null;
	Timer timerRedraw = null;
	Timer timerMove = null;
	ArrayList<Food> foods = null;
	int number1 = -1, number2 = -1;
	boolean gameOver = false;
	int score = 0;
	int speed = MainFrame.INITIAL_SPEED;
	MainFrame mainFrame = null;
	String username;
	
	public GamePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setSize(MainFrame.SCREEN_W, MainFrame.SCREEN_H);
		this.setLayout(null);
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	public void newGame(String username) {
		this.username = username;
		gameOver = false;
		setupInitialValues();
		setupSnake();
		setupDisplayedSum();
		setupFoods();
		setupTimerMove();
		setupTimerRedraw();
	}
	
	private void setupInitialValues() {
		board = new int[MainFrame.BOARD_W][MainFrame.BOARD_H];
		snake = null;
		timerRedraw = null;
		timerMove = null;
		foods = null;
		number1 = -1; number2 = -1;
		gameOver = false;
		score = 0;
		speed = MainFrame.INITIAL_SPEED;
	}
	
	private void setupSnake() {
		snake = new Snake(MainFrame.BOARD_W/2,MainFrame.BOARD_H/2);
		snake.body.add(new Field(MainFrame.BOARD_W/2,MainFrame.BOARD_H/2+1));
		snake.body.add(new Field(MainFrame.BOARD_W/2,MainFrame.BOARD_H/2+2));
	}
	
	private void setupTimerRedraw() {
		timerRedraw = new Timer(1000/MainFrame.FPS, this);
		timerRedraw.start();
	}
	
	private void setupTimerMove() {
		timerMove = new Timer(1000/speed, this);
		timerMove.start();
	}
	
	private void setupDisplayedSum() {
		number1 = Algorithms.randomFromRange(0, MainFrame.MAX_SUM);
		number2 = Algorithms.randomFromRange(0, MainFrame.MAX_SUM - number1);
	}
	
	private void setupFoods() {
		foods = new ArrayList<Food>();
		int sum;
		Field position;
		boolean occupies;
		
		// generate food with correct sum
		sum = number1 + number2;
		while(true) {
			position = new Field(Algorithms.randomFromRange(1, MainFrame.BOARD_W-2), Algorithms.randomFromRange(1, MainFrame.BOARD_H-2));
			// check if snake doesn't occupy that field
			occupies = false;
			for(Field snakeField : snake.getBody()) {
				if(snakeField.getX() == position.getX() && snakeField.getY() == position.getY()) {
					occupies = true;
					break;
				}
			}
			if(!occupies) break;
		}
		foods.add(new Food(sum, position));
		
		// generate other foods with incorrect sums
		for(int i = 0; i < MainFrame.NUMBER_OF_FOODS-1; i++) {
			sum = Algorithms.randomFromRange(1, MainFrame.MAX_SUM);
			while(true) {
				position = new Field(Algorithms.randomFromRange(1, MainFrame.BOARD_W-2), Algorithms.randomFromRange(1, MainFrame.BOARD_H-2));
				// check if snake doesn't occupy that field
				occupies = false;
				for(Field snakeField : snake.getBody()) {
					if(snakeField.getX() == position.getX() && snakeField.getY() == position.getY()) {
						occupies = true;
						break;
					}
				}
				// check if other foods don't occupy that field
				for(Food food : foods) {
					if(food.getX() == position.getX() && food.getY() == position.getY()) {
						occupies = true;
						break;
					}
				}
				if(!occupies) break;
			}
			foods.add(new Food(sum, position));
		}
	}
	
	// prepares a board before it's displayed
	private void setupBoard() {
		// clear the board
		for(int y = 0; y < MainFrame.BOARD_H; y++) {
			for(int x = 0; x < MainFrame.BOARD_W; x++) {
				board[x][y] = 0;
			}
		}
		
		// set boundaries
		for(int x = 0; x < MainFrame.BOARD_W; x++) board[x][0] = board[x][MainFrame.BOARD_H-1] = 1;
		for(int y = 1; y < MainFrame.BOARD_H-1; y++) board[0][y] = board[MainFrame.BOARD_W-1][y] = 1;
		
		// place snake
		board[snake.getBody().get(0).getX()][snake.getBody().get(0).getY()] = 3; // head of snake
		for(int i = 1; i < snake.getBody().size(); i++) {
			board[snake.getBody().get(i).getX()][snake.getBody().get(i).getY()] = 2; // body of snake
		}
		
		// place food
		for(int i = 0; i < foods.size(); i++) {
			board[foods.get(i).getX()][foods.get(i).getY()] = 4; // food
		}
		
	}
	
	// redraw screen
		public void paint(Graphics gScreen) {
		
		//if(!isMenuDisplayed) {
			
			// create buffer image
			Image bufferImage = createImage(MainFrame.SCREEN_W, MainFrame.SCREEN_H);
			Graphics2D g = (Graphics2D) bufferImage.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // antialiasing ON
			
			// clear board
			g.clearRect(0, 0, MainFrame.SCREEN_W, MainFrame.SCREEN_H);
			
			// display the sum of 2 numbers
			g.setFont(new Font("Georgia", Font.PLAIN, 15));
			g.setColor(Color.BLACK);
			g.drawString(Translations.translate("Collect food with the value:"), MainFrame.SCREEN_W/2-85, 24);
			g.setFont(new Font("Georgia", Font.PLAIN, 50));
			g.setColor(Color.BLACK);
			g.drawString(Integer.toString(number1) + " + " + Integer.toString(number2), MainFrame.SCREEN_W/2-70, 66);
			
			// display score
			g.setFont(new Font("Georgia", Font.PLAIN, 15));
			g.setColor(new Color(210,105,30));
			g.drawString(Translations.translate("Score:"), MainFrame.SCREEN_W-150, 24);
			g.setFont(new Font("Georgia", Font.PLAIN, 40));
			g.setColor(new Color(210,105,30));
			g.drawString(Integer.toString(score), MainFrame.SCREEN_W-150, 66);
			
			// display speed
			g.setFont(new Font("Georgia", Font.PLAIN, 15));
			g.setColor(new Color(200,0,0));
			g.drawString(Translations.translate("Speed:"), 100, 24);
			g.setFont(new Font("Georgia", Font.PLAIN, 40));
			g.setColor(new Color(200,0,0));
			g.drawString(Integer.toString(speed), 100, 66);
			
			// display return to menu text
			Font font = new Font("Georgia", Font.PLAIN, 16);
			FontMetrics fontMetrics = g.getFontMetrics(font);
			g.setFont(font);
			g.setColor(new Color(200, 0, 0));
			String s = Translations.translate("Press [ESC] to return to menu");
			g.drawString(s, MainFrame.SCREEN_W - 10 - fontMetrics.stringWidth(s), MainFrame.SCREEN_H -32);
			
			// display board
			g.setFont(new Font("Georgia", Font.PLAIN, 15));
			for(int y = 0; y < MainFrame.BOARD_H; y++) {
				for(int x = 0; x < MainFrame.BOARD_W; x++) {
					// create fields
					g.setColor(new Color(255, 255, 224));
					g.fillRect(MainFrame.BOARD_OFFSET_X + x*(MainFrame.FIELD_SIZE+1), MainFrame.BOARD_OFFSET_Y + y*(MainFrame.FIELD_SIZE+1), MainFrame.FIELD_SIZE, MainFrame.FIELD_SIZE);
					// border
					if(board[x][y] == 1) {
						g.setColor(new Color(96, 47, 107));
						g.fillOval(MainFrame.BOARD_OFFSET_X + x*(MainFrame.FIELD_SIZE+1), MainFrame.BOARD_OFFSET_Y + y*(MainFrame.FIELD_SIZE+1), MainFrame.FIELD_SIZE, MainFrame.FIELD_SIZE);
					}
					// snake's head
					if(board[x][y] == 3) {
						g.setColor(new Color(155, 20, 20));
						g.fillOval(MainFrame.BOARD_OFFSET_X + x*(MainFrame.FIELD_SIZE+1), MainFrame.BOARD_OFFSET_Y + y*(MainFrame.FIELD_SIZE+1), MainFrame.FIELD_SIZE, MainFrame.FIELD_SIZE);
					}
					// snake's body
					if(board[x][y] == 2) {
						g.setColor(new Color(210, 35, 35));
						g.fillOval(MainFrame.BOARD_OFFSET_X + x*(MainFrame.FIELD_SIZE+1), MainFrame.BOARD_OFFSET_Y + y*(MainFrame.FIELD_SIZE+1), MainFrame.FIELD_SIZE, MainFrame.FIELD_SIZE);
					}
					// create food
					if(board[x][y] == 4) {
						g.setColor(new Color(50, 50, 200));
						g.fillOval(MainFrame.BOARD_OFFSET_X + x*(MainFrame.FIELD_SIZE+1), MainFrame.BOARD_OFFSET_Y + y*(MainFrame.FIELD_SIZE+1), MainFrame.FIELD_SIZE, MainFrame.FIELD_SIZE);
						// find what value the food has
						for(int i = 0; i < foods.size(); i++) {
							if(foods.get(i).getX() == x && foods.get(i).getY() == y) {
								g.setColor(Color.WHITE);
								//if(foods.get(i).)
								g.drawString(Integer.toString(foods.get(i).getValue()), MainFrame.BOARD_OFFSET_X + x*(MainFrame.FIELD_SIZE+1) + MainFrame.FIELD_SIZE/4, MainFrame.BOARD_OFFSET_Y + y*(MainFrame.FIELD_SIZE+1) + MainFrame.FIELD_SIZE*3/4);
							}
						}
					}
				}
			}
			
			if(gameOver) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Georgia", Font.PLAIN, 60));
				g.drawString(Translations.translate("Game Over!"), MainFrame.SCREEN_W/2-150, MainFrame.SCREEN_H/2+50);
			}
			
			gScreen.drawImage(bufferImage, 0, 0, MainFrame.SCREEN_W, MainFrame.SCREEN_H, this);
		//}
	}
	
		
	
	private void gameOver() {
		timerMove.stop();
		timerRedraw.stop();
		gameOver = true;
		repaint();
	}

	// ActionListener method
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// screen redraws
		if(e.getSource() == timerRedraw) {
			setupBoard();
			repaint();
		}
		// snake moves
		if(e.getSource() == timerMove) {
			// position of the last part of snake's body before moving (used to add a part to the end of the snake)
			int oldLastPositionX = snake.getBody().get(snake.getBody().size()-1).getX();
			int oldLastPositionY = snake.getBody().get(snake.getBody().size()-1).getY();
			Field f = snake.move(board);
			
			switch(board[f.getX()][f.getY()]){
			case 0: break; // snake entered free space
			case 1: gameOver(); break; // snake bumped into  border
			case 2: gameOver(); break; // snake bumped into its body
			case 3: break; // snake can't bump into its head
			case 4: snakeEatsFood(oldLastPositionX, oldLastPositionY); break;// snake eats a food
			}
		}
	}
	
	// update speed based on score
	private void updateSpeedByScore() {
		// increase/decrease the speed
		speed = MainFrame.INITIAL_SPEED + (int)(score/MainFrame.SPEED_SCORE_COEFFICIENT);
		// update the speed in moving timer
		timerMove.stop();
		setupTimerMove();
	}
	
	private void snakeEatsFood(int oldLastPositionX, int oldLastPositionY) {
		Food eatenFood = findFoodByPosition(snake.getBody().get(0)); // food that snake has just eaten
		if(eatenFood.getValue() == number1 + number2) { // food has correct sum
			snake.addPart(new Field(oldLastPositionX, oldLastPositionY));
			score += MainFrame.SCORE_INCREASE;
			updateSpeedByScore();
		}
		else { // food has wrong sum
			if(snake.getBody().size() != 1) { // snake is at least 2 parts long
				snake.removeLastPart();
				score -= MainFrame.SCORE_DECREASE;
				updateSpeedByScore();
			}
			else { // snake is 1 part long
				gameOver();
			}
		}
		setupDisplayedSum();
		setupFoods();
	}

	private Food findFoodByPosition(Field f) {
		for(Food food : foods) {
			if(food.getX() == f.getX() && food.getY() == f.getY()) return food;
		}
		return null;
	}
	
	// give hint by removing half of the foods
	private void removeHalfOfFoods() {
		score -= MainFrame.SCORE_DECREASE_BY_HINT;
		int howManyToRemove = (int)(foods.size()/2);
		for(int i = 0; i < howManyToRemove; i++) {
			foods.remove(foods.size()-1);
		}
	}
	
	// KeyListener methods
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP) snake.setDirection(0);
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) snake.setDirection(1);
		if(e.getKeyCode() == KeyEvent.VK_DOWN) snake.setDirection(2);
		if(e.getKeyCode() == KeyEvent.VK_LEFT) snake.setDirection(3);
		
		if(e.getKeyCode() == KeyEvent.VK_H) removeHalfOfFoods();
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) returnToMenu();
		
		if(e.getKeyCode() == KeyEvent.VK_Q) {
			timerMove.stop();
			timerRedraw.stop();
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			timerMove.start();
			timerRedraw.start();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	private void returnToMenu() {
		timerMove.stop();
		timerRedraw.stop();
		mainFrame.gameToMenu(username, score);
	}

}
