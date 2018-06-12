//Arrow
import java.awt.*;

class Arrow{
	private int damage, x, y, width = 30, height = 6;
	private String direction;
	public Arrow(int damage, String direction, int x, int y){
		this.damage = damage;
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	public void move(){
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
		return new Rectangle(x + man.getX(), y, width, height);
	}
	public Rectangle getRealRect(Player man){
		return new Rectangle(x - man.getXPos(), y, width, height);
	}
	public int getDamage(){
		return damage;
	}
}