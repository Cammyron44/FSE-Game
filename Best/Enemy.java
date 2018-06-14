//Enemy
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Enemy{
	private int health = 100, cooldown = 50, time = 0, red, green, black, blue, pink;
	private BufferedImage mask = null;
	private boolean jump, ground;
	double vx, vy, x, y;
	String direction = "left";
	public Enemy(int x, int y, BufferedImage mask){
		this.x = x;
		this.y = y;
		this.mask = mask;
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int)y;
	}
	public Rectangle getRect(){
		return new Rectangle((int)x, (int)y, 50, 50);
	}
	public String getDirection(){
		return direction;
	}
	public void takeDamage(int damage){
		health -= damage;
	}
	public int getHealth(){
		return health;
	}
	public int getCooldown(){
		return cooldown;
	}
	public int getTime(){
		return time;
	}
	public void resetTime(){
		time = 0;
	}
	public void addTime(){
		time++;
	}
	public boolean attack(Rectangle manRect){
		if(direction == "left"){
			Rectangle leftRect = new Rectangle((int)x - 200, (int)y, 200, 50);
			if(leftRect.intersects(manRect)){
				return true;
			}
			return false;
		}
		if(direction == "right"){
			Rectangle rightRect = new Rectangle((int)x + 200, (int)y, 200, 50);
			if(rightRect.intersects(manRect)){
				return true;
			}
			return false;
		}
		return false;
	}

	public void chase(Player player){
		if(player.getX() > x){
			direction = "right";
			x += 4;
		}
		if(player.getX() < x){
			direction = "left";
			x -= 4;
		}
	}
	public void move(){
		if(direction.equals("right")){
			x += 2;
		}
		if(direction.equals("left")){
			x -= 2;
		}
	}
}