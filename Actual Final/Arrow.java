//Arrow
import java.awt.*;

class Arrow{
	private int damage, x, y, width = 50, height = 25;
	private String direction;
	public Arrow(int damage, String direction, int x, int y){//constructor
		this.damage = damage;
		this.direction = direction;
		this.x = x;
		this.y = y + 10;//add 10 to move bullet down so standing on cannon doesn't collide
	}
	public void move(){//method moves arrow
		if(direction.equals("left")){
			x -= 10;
		}
		if(direction.equals("right")){
			x += 10;
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Rectangle getRect(Player man){
		return new Rectangle(x, y, width, height);
	}
	public Rectangle getRealRect(Player man){
		return new Rectangle(x - man.getXPos(), y, width, height);
	}
	public int getDamage(){//returns damage of arrow
		return damage;
	}
}