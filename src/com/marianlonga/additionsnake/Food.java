package com.marianlonga.additionsnake;
/* (c) 2014 Marian Longa */
/* Food contains the food's value and position */

public class Food {
	int value;
	Field position;
	
	public Food(int value, Field position) {
		this.value = value;
		this.position = position;
	}
	
	public int getX() {
		return position.getX();
	}
	
	public int getY() {
		return position.getY();
	}
	
	public int getValue() {
		return value;
	}
}
