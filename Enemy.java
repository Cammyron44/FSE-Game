//Enemy
import java.awt.*;

public class Enemy{
	int x, y, xPos, yPos, hp = 100, maxHP = 100, width = 50, height = 50;
	String direction = "left";
	public Enemy(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public Rectangle getRect(){
		return new Rectangle(x, y, width, height);
	}
	public String getDirection(){
		return direction;
	}
	public void takeDamage(int damage){
		hp -= damage;
	}
	public int getHP(){
		return hp;
	}
	public int getMaxHP(){
		return maxHP;
	}
	public void chase(Player player){
		if(player.getX() > x){
			direction = "right";
			x += 2;
		}
		if(player.getX() < x){
			direction = "left";
			x -= 2;
		}
	}
}