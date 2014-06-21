package com.marianlonga.additionsnake;
/* (c) 2014 Marian Longa */
/* Snake contains snake's body and functions for moving the snake */

import java.util.ArrayList;

public class Snake {

	ArrayList<Field> body = null;
	int direction, speed;
	
	public Snake(int x, int y) {
		direction = 0; // 0-up, 1-right, 2-down, 3-left
		//speed = 2; // 2 units / sec
		body = new ArrayList<Field>();
		body.add(new Field(x, y));
	}
	
	public ArrayList<Field> getBody() {
		return body;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	
	// returns true when the snake can move there, false if it can't
	public Field move(int[][] board) {
		int oldX = body.get(0).getX();
		int oldY = body.get(0).getY();
		
		// move head
		if(direction == 0) body.get(0).setY(body.get(0).getY()-1); // up
		if(direction == 1) body.get(0).setX(body.get(0).getX()+1); // right
		if(direction == 2) body.get(0).setY(body.get(0).getY()+1); // down
		if(direction == 3) body.get(0).setX(body.get(0).getX()-1); // left
		
		// move body
		moveBody(oldX, oldY);

		// return new position of head
		return body.get(0);
	}
	
	private void moveBody(int oldX, int oldY) {
		for(int i = 1; i < body.size(); i++) {
			int oldXtemp = body.get(i).getX();
			int oldYtemp = body.get(i).getY();
			body.get(i).setX(oldX);
			body.get(i).setY(oldY);
			oldX = oldXtemp;
			oldY = oldYtemp;
		}
	}
	
	public void addPart(Field f) {
		body.add(f);
	}
	
	public void removeLastPart() {
		body.remove(body.size()-1);
	}
}
