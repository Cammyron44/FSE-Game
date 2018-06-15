/**
 * @(#)Player.java
 *
 *
 * @author 
 * @version 1.00 2018/6/6
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Player {
	private int maxDamage, minDamage;
	private double x, xPos, yPos, vx, vy, sx, sy;
	private String direction, type = "archer";
	private boolean jump, ground;
	int screenX = 1900;
	int screenY = 1000;
	int lives, health;
	private int green, black, red, grey;
	private BufferedImage mask = null;
	
	public Player(BufferedImage image){
		x = 0; //position of background image on screen
		xPos = 100; //player X
		yPos = 400; //player Y
		vx = 0;
		vy = 0;
		direction = "right";
		jump = false;
		mask = image;
		green = getPixelCol(mask, 25, 25); //platform colour
		red = getPixelCol(mask, 75, 25); //lava
		black = getPixelCol(mask, 225, 25); //cannons
		grey = getPixelCol(mask, 475, 25);
		ground = true;
		lives = 1;
		health = 100;
	}
	public void loadImage(BufferedImage image){
		mask = image;
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
		if (getColBottom(mask, (int) xPos, (int) yPos, grey)){
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
		if (getColBottom(mask, (int) xPos, (int) yPos, grey)){
			health = 0;
		}
	}
	//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
	public void move(String d){
		if (vx < 5){
			vx += 0.4;
		}
		if (d.equals("right")){
			direction = "right";
			for (int i = 0; i < (int) vx; i++){
				if (!getColRight(mask, (int) xPos, (int) yPos, green) && !getColRight(mask, (int) xPos, (int) yPos, black)){
					if (xPos <= 890){
						xPos += 1;//(int) vx;
					}
					else if (x >= 23090){
						if (xPos < 1830){ //max distance
							xPos += 1;//(int) vx;
						}
					}
					else{
						x += 1;//(int) vx;
					}
				}
			}
		}
		else{
			direction = "left";
			for (int i = 0; i < (int) vx; i++){
				if (!getColLeft(mask, (int) xPos, (int) yPos, green) && !getColLeft(mask, (int) xPos, (int) yPos, black)){
					if (xPos >= 910){
						xPos -= 1;//(int) vx;
					}
					else if (x <= 10){
						if (xPos > 10){ //min distance
							xPos -= 1;//(int) vx;
						}
					}
					else{
						x -= 1;//(int) vx;
					}
				}
			}
		}
		if (getColBottom(mask, (int) xPos, (int) yPos, grey)){
			health = 0;
		}
	}
	
	public void slowDown(String d){
		if (vx > 0){
			vx -= 0.3;
		}
		if (d.equals("right")){
			direction = "right";
			if (!getColRight(mask, (int) xPos + (int) vx, (int) yPos, green) && !getColRight(mask, (int) xPos, (int) yPos, black)){
				if (xPos <= 890){
					xPos += (int) vx;
				}
				else if (x >= 23090){
					if (xPos < 1830){ //max distance
						xPos += (int) vx;
					}
				}
				else{
					x += (int) vx;
				}
			}
		}
		else{
			direction = "left";
			if (!getColLeft(mask, (int) xPos - (int) vx, (int) yPos, green) && !getColLeft(mask, (int) xPos, (int) yPos, black)){
				if (xPos >= 910){
					xPos -= (int) vx;
				}
				else if (x <= 10){
					if (xPos > 10){ //min distance
						xPos -= (int) vx;
					}
				}
				else{
					x -= (int) vx;
				}
			}
		}
		if (getColBottom(mask, (int) xPos, (int) yPos, grey)){
			health = 0;
		}
	}
	/////////////////////////ATTACK///////////////////////////////
	public Arrow shoot(int damage){
		return new Arrow(damage, direction, (int)xPos, (int)yPos);
	}
	
	public int damage(){
		Random random = new Random();
		int d = 0;
		if(type.equals("warrior")){
			minDamage = 40;
			maxDamage = 80;
		}
		if(type.equals("assassin")){
			minDamage = 10;
			maxDamage = 100;
		}
		if(type.equals("archer")){
			minDamage = 20;
			maxDamage = 40;
		}
		while(d < minDamage){
			d = random.nextInt(maxDamage);
		}
		return d;
	}
	////////////////////////////GETTERS AND SETTERS////////////////////////////////
	public int getX(){
		return (int) x;
	}
	
	public int getXPos(){
		return (int) xPos;
	}
	
	public void setX(double d){
		x = d;
	}
	public void setXPos(double d){
		xPos = d;
	}
	
	public int getYPos(){
		return (int) yPos;
	}
	public void setYPos(double d){
		yPos = d;
	}
		
	public int getVx(){
		return (int) vx;
	}
	
	public void setVx(int vx){
		this.vx = vx;
	}
	
	public int getVy(){
		return (int) vy;
	}
	
	public void setVy(int vy){
		this.vy = vy;
	}
	
	public void addVy(double v){
		vy += v;
	}
	
	public boolean getJump(){
		return jump;
	}
	
	public void setJump(boolean b){
		jump = b;
	}
	
	public void setGround(boolean b){
		ground = b;
	}
	
	public boolean getGround(){
		return ground;
	}
	
	public int getHealth(){
		return health;
	}
	///////////////////////////////////////////////////////////////////////////////
	public void takeDamage(int d){
		health -= d;
		if (health < 0){
			health = 0;
		}
	}
	public void addHealth(int i){
		health += i;
		if (health > 100){
			health = 100;
		}
	}
	
	public void addLife(){
		lives++;
	}
	
	public void subtractLife(){
		lives--;
	}
	
	public int getLives(){
		return lives;
	}
}