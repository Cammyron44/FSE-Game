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
			x -= 8;
		}
		if(direction.equals("right")){
			x += 8;
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Rectangle getRect(){
		return new Rectangle(x, y, width, height);
	}
	public int getDamage(){
		return damage;
	}
}