/* (c) 2014 Marian Longa */
/* GameFrame contains all GUI and is a main class */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;


public class GameFrame extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	// CONSTANTS
	final int	SCREEN_W				= 800,	// screen width (in px)
				SCREEN_H				= 600,	// screen height (in px)
				BOARD_W					= 25,	// the width of board (in number of fields)
				BOARD_H					= 16,	// the height of board (in number of fields)
				BOARD_OFFSET_X			= 35,	// left margin of the field (int px)
				BOARD_OFFSET_Y			= 120,	// top margin of the field (in px)
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
	
	
	int[][] board = new int[BOARD_W][BOARD_H];
	Snake snake = null;
	Timer timerRedraw = null;
	Timer timerMove = null;
	Timer timerSpecialFood = null;
	ArrayList<Food> foods = null;
	int number1 = -1, number2 = -1;
	boolean gameOver = false;
	int score = 0;
	int speed = INITIAL_SPEED;
	
	public static void main(String[] args) {
		(new GameFrame()).setVisible(true);
	}
	
	public GameFrame() {
		super();
		this.setSize(SCREEN_W, SCREEN_H);
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		
		setupSnake();
		setupDisplayedSum();
		setupFoods();
		setupTimerMove();
		setupTimerRedraw();
	}
	
	private void setupSnake() {
		snake = new Snake(BOARD_W/2,BOARD_H/2);
		snake.body.add(new Field(BOARD_W/2,BOARD_H/2+1));
		snake.body.add(new Field(BOARD_W/2,BOARD_H/2+2));
	}
	
	private void setupTimerRedraw() {
		timerRedraw = new Timer(1000/FPS, this);
		timerRedraw.start();
	}
	
	private void setupTimerMove() {
		timerMove = new Timer(1000/speed, this);
		timerMove.start();
	}
	
	private void setupDisplayedSum() {
		number1 = randomFromRange(0, MAX_SUM);
		number2 = randomFromRange(0, MAX_SUM - number1);
	}
	
	// returns random number from the interval [beginPos, endPos]
	private int randomFromRange(int beginPos, int endPos) {
		return (
			(int) (
				Math.round (
					Math.random() * (endPos - beginPos)
				)
			)
			+ beginPos
		);
	}
	
	private void setupFoods() {
		foods = new ArrayList<Food>();
		int sum;
		Field position;
		boolean occupies;
		
		// generate food with correct sum
		sum = number1 + number2;
		while(true) {
			position = new Field(randomFromRange(1, BOARD_W-2), randomFromRange(1, BOARD_H-2));
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
		for(int i = 0; i < NUMBER_OF_FOODS-1; i++) {
			sum = randomFromRange(1, MAX_SUM);
			while(true) {
				position = new Field(randomFromRange(1, BOARD_W-2), randomFromRange(1, BOARD_H-2));
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
		for(int y = 0; y < BOARD_H; y++) {
			for(int x = 0; x < BOARD_W; x++) {
				board[x][y] = 0;
			}
		}
		
		// set boundaries
		for(int x = 0; x < BOARD_W; x++) board[x][0] = board[x][BOARD_H-1] = 1;
		for(int y = 1; y < BOARD_H-1; y++) board[0][y] = board[BOARD_W-1][y] = 1;
		
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
		
		// create buffer image
		Image bufferImage = createImage(SCREEN_W, SCREEN_H);
		Graphics2D g = (Graphics2D) bufferImage.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // antialiasing ON
		
		// clear board
		g.clearRect(0, 0, SCREEN_W, SCREEN_H);
		
		// display the sum of 2 numbers
		g.setFont(new Font("Georgia", Font.PLAIN, 15));
		g.setColor(Color.BLACK);
		g.drawString("Collect food with the value:", SCREEN_W/2-85, 48);
		g.setFont(new Font("Georgia", Font.PLAIN, 50));
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString(number1) + " + " + Integer.toString(number2), SCREEN_W/2-70, 90);
		
		// display score
		g.setFont(new Font("Georgia", Font.PLAIN, 15));
		g.setColor(new Color(210,105,30));
		g.drawString("Score:", SCREEN_W-150, 48);
		g.setFont(new Font("Georgia", Font.PLAIN, 40));
		g.setColor(new Color(210,105,30));
		g.drawString(Integer.toString(score), SCREEN_W-150, 90);
		
		// display speed
		g.setFont(new Font("Georgia", Font.PLAIN, 15));
		g.setColor(new Color(200,0,0));
		g.drawString("Speed:", 100, 48);
		g.setFont(new Font("Georgia", Font.PLAIN, 40));
		g.setColor(new Color(200,0,0));
		g.drawString(Integer.toString(speed), 100, 90);
		
		// display board
		g.setFont(new Font("Georgia", Font.PLAIN, 15));
		for(int y = 0; y < BOARD_H; y++) {
			for(int x = 0; x < BOARD_W; x++) {
				// create fields
				g.setColor(new Color(255, 255, 224));
				g.fillRect(BOARD_OFFSET_X + x*(FIELD_SIZE+1), BOARD_OFFSET_Y + y*(FIELD_SIZE+1), FIELD_SIZE, FIELD_SIZE);
				// border
				if(board[x][y] == 1) {
					g.setColor(new Color(96, 47, 107));
					g.fillOval(BOARD_OFFSET_X + x*(FIELD_SIZE+1), BOARD_OFFSET_Y + y*(FIELD_SIZE+1), FIELD_SIZE, FIELD_SIZE);
				}
				// snake's head
				if(board[x][y] == 3) {
					g.setColor(new Color(155, 20, 20));
					g.fillOval(BOARD_OFFSET_X + x*(FIELD_SIZE+1), BOARD_OFFSET_Y + y*(FIELD_SIZE+1), FIELD_SIZE, FIELD_SIZE);
				}
				// snake's body
				if(board[x][y] == 2) {
					g.setColor(new Color(210, 35, 35));
					g.fillOval(BOARD_OFFSET_X + x*(FIELD_SIZE+1), BOARD_OFFSET_Y + y*(FIELD_SIZE+1), FIELD_SIZE, FIELD_SIZE);
				}
				// create food
				if(board[x][y] == 4) {
					g.setColor(new Color(50, 50, 200));
					g.fillOval(BOARD_OFFSET_X + x*(FIELD_SIZE+1), BOARD_OFFSET_Y + y*(FIELD_SIZE+1), FIELD_SIZE, FIELD_SIZE);
					// find what value the food has
					for(int i = 0; i < foods.size(); i++) {
						if(foods.get(i).getX() == x && foods.get(i).getY() == y) {
							g.setColor(Color.WHITE);
							//if(foods.get(i).)
							g.drawString(Integer.toString(foods.get(i).getValue()), BOARD_OFFSET_X + x*(FIELD_SIZE+1) + FIELD_SIZE/4, BOARD_OFFSET_Y + y*(FIELD_SIZE+1) + FIELD_SIZE*3/4);
						}
					}
				}
			}
		}
		
		if(gameOver) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Georgia", Font.PLAIN, 60));
			g.drawString("Game Over!", SCREEN_W/2-150, SCREEN_H/2+50);
		}
		
		gScreen.drawImage(bufferImage, 0, 0, SCREEN_W, SCREEN_H, this);
		
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
		speed = INITIAL_SPEED + (int)(score/SPEED_SCORE_COEFFICIENT);
		// update the speed in moving timer
		timerMove.stop();
		setupTimerMove();
	}
	
	private void snakeEatsFood(int oldLastPositionX, int oldLastPositionY) {
		Food eatenFood = findFoodByPosition(snake.getBody().get(0)); // food that snake has just eaten
		if(eatenFood.getValue() == number1 + number2) { // food has correct sum
			snake.addPart(new Field(oldLastPositionX, oldLastPositionY));
			score += SCORE_INCREASE;
			updateSpeedByScore();
		}
		else { // food has wrong sum
			if(snake.getBody().size() != 1) { // snake is at least 2 parts long
				snake.removeLastPart();
				score -= SCORE_DECREASE;
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
		score -= SCORE_DECREASE_BY_HINT;
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
