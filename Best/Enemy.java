//Enemy
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Enemy{
	private int health = 100, maxHealth = 100, width = 50, height = 50, cooldown = 50, time = 0, xPos, yPos, red, green, black, blue, pink;
	private BufferedImage mask = null, enemy = null;
	private boolean jump, ground;
	double vx, vy, x, y;
	String direction = "left";
	public Enemy(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int)y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public Rectangle getRect(){
		return new Rectangle((int)x, (int)y, width, height);
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
	public int getMaxHealth(){
		return maxHealth;
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
	public boolean attack(Player man){
		if(direction == "left"){
			Rectangle leftRect = new Rectangle((int)x - 200, (int)y, 200, height);
			if(leftRect.intersects(man.getRect())){
				return true;
			}
			return false;
		}
		if(direction == "right"){
			Rectangle rightRect = new Rectangle((int)x + 200, (int)y, 200, height);
			if(rightRect.intersects(man.getRect())){
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
	public void loadImage(){
		try {
    		mask = ImageIO.read(new File("map1Mask.png"));
    		enemy = ImageIO.read(new File("map1Enemy.png"));
		} 
		catch (IOException e) {
		}
		green = getPixelCol(mask, 25, 25); //platform colour
		red = getPixelCol(mask, 75, 25); //lava
		black = getPixelCol(mask, 225, 25); //cannons
		blue = getPixelCol(enemy, 275, 25);
		pink = getPixelCol(enemy, 325, 25);
    }
	///////////////////////////////PIXEL COLOUR////////////////////////////////////
    public int getPixelCol(BufferedImage image, int xx, int yy){
    	return image.getRGB(xx + (int)x, yy);
    }
   
    public boolean getColTop(BufferedImage image, int xx, int yy, int col){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x, yy);
    	for (int d = 0; d < 50; d++){
    		c = image.getRGB(d + xx + (int) x, yy);
    		if (c == col){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColBottom(BufferedImage image, int xx, int yy, int col){
    	boolean b = false;
    	int c = image.getRGB(xx + (int )x, yy + 100);
    	for (int d = 0; d < 50; d++){
    		c = image.getRGB(d + xx + (int) x, yy + 100);
    		if (c == col){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColRight(BufferedImage image, int xx, int yy, int col){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x + 50, yy + 100);
    	for (int d = 0; d < 100; d++){
    		c = image.getRGB(xx + (int) x + 50, yy + d);
    		if (c == col){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColLeft(BufferedImage image, int xx, int yy, int col){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x - 1, yy + 100);
    	for (int d = 0; d < 100; d++){
    		c = image.getRGB(xx + (int) x - 1, yy + d);
    		if (c == col){
    			b = true;
    		}
    	}
    	return b;
    }
	///////////////////////////////VERTICAL MOVEMENT///////////////////////////////
	public void jump(){
		if (vy < 0){
			for (int i = 0; i < (int) vy * -1; i++){
				if (!getColTop(mask, (int) xPos, (int) yPos, green) && !getColTop(mask, (int) xPos, (int) yPos, black)){
					yPos -= 1;
					ground  = false;
				}
				else{
					vy = 0;
					ground = false;
				}
			}
		}
		else{
			for (int i = 0; i < (int) vy; i++){
				if (!getColBottom(mask, (int) xPos, (int) yPos, green) && !getColBottom(mask, (int) xPos, (int) yPos, black)){
					yPos += 1;
				}
				else{
					vy = 0;
					jump = false;
					ground = true;
				}
			}
		}
		vy += 1;
		if (getColBottom(mask, (int) xPos, (int) yPos, red)){
			health = 0;
		}
	}
	
	public void fall(){
		for (int i = 0; i < (int) vy; i++){
			if (!getColBottom(mask, (int) xPos, (int) yPos, green) && !getColBottom(mask, (int) xPos, (int) yPos, black)){
				yPos += 1;
				ground = false;
			}
			else{
				vy = 0;
				jump = false;
				ground = true;
			}
		}
		vy += 1;
		if (getColBottom(mask, (int) xPos, (int) yPos, red)){
			health = 0;
		}
	}
	//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
	public void move(){
		if (vx < 5){
			vx += 0.4;
		}
		if (direction.equals("right")){
			for (int i = 0; i < (int) vx; i++){
				x++;
				if (getColRight(mask, (int) xPos, (int) yPos, pink)){
					direction = "left";
				}
			}
		}
		else{
			for (int i = 0; i < (int) vx; i++){
				x++;
				if (getColLeft(mask, (int) xPos, (int) yPos, pink)){
					direction = "right";
				}
			}
		}
		if (getColBottom(mask, (int) xPos, (int) yPos, red)){
			health = 0;
		}
	}
	
	public void slowDown(String d){
		if (vx > 0){
			vx -= 0.3;
		}
		if (direction.equals("right")){
			for (int i = 0; i < (int) vx; i++){
				x++;
				if (getColRight(mask, (int) xPos, (int) yPos, pink)){
					direction = "left";
				}
			}
		}
		else{
			for (int i = 0; i < (int) vx; i++){
				x++;
				if (getColLeft(mask, (int) xPos, (int) yPos, pink)){
					direction = "right";
				}
			}
		}
		if (getColBottom(mask, (int) xPos, (int) yPos, red)){
			health = 0;
		}
	}
}