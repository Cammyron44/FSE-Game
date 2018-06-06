/**
 * @(#)Ground.java
 *
 *
 * @author 
 * @version 1.00 2018/6/6
 */

import java.awt.*;

class Block{
	private String type;
	private int x, y;
	private Rectangle rect;
	
	public Block(int xx, int yy, String t){
		x = xx;
		y = yy;
		type = t;
		rect = new Rectangle(x, y, 50, 50);
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	public String getType(){
		return type;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}