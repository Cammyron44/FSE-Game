//Ground

import java.awt.*;

class Ground{
	private String type;
	private int x, y, xPos, yPos;
	
	public Ground(int x, int y, String type){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, width, height);
	}
}