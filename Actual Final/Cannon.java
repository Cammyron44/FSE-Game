//Cannon
import java.awt.*;

class Cannon{
	private int x, y, xPos;
	public Cannon(int x, int y, int xPos){
		this.x = x;
		this.y = y;
		this.xPos = xPos;
	}
	public Arrow shoot(Player man){//creates arrow object at cannon
		return new Arrow(10, "left", x, y + 10);	
	}
	//Getter methods
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getXPos(){
		return xPos;
	}
}
