/* (c) 2014 Marian Longa */
/* Field represents the x and y position of an object on board */

public class Field {

	int x, y;
	
	public Field(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {return x;}
	public int getY() {return y;}
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}

}
