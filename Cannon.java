//Cannon
import java.awt.*;

class Cannon{
	private int x, y, cooldown = 100, time = 0;
	public Cannon(int x, int y){
		this.x = x;
		this.y = y;
	}
	public Arrow shoot(){
		if(time >= cooldown){
			time = 0;
			return new Arrow(50, "left", x, y);	
		}
		time++;
	}
	
}
