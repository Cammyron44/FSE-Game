//Enemy
import java.awt.*;

public class Enemy{
	int x, y, xPos, yPos, hp = 100, maxHP = 100, width = 50, height = 50;
	public Enemy(int x, int y, int sx, int sy){
		this.x = x;
		this.y = y;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	public int getYPos(){
		return yPos;
	}
	public int getXPos(){
		return xPos;
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
		if(player.getXPos() > x){
			x += 2;
		}
		if(player.getXPos() < x){
			x -= 2;
		}
	}
}