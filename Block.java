//Block

import java.awt.*;

class Block{
	private String type;
	private int x, y, xPos, yPos, width = 50, height = 50;
	private Rectangle rect;
	
	public Block(int x, int y){
		this.x = x;
		this.y = y;
		rect = new Rectangle(x, y, width, height);
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
	public Rectangle getRect(){
		return rect;
	}
}