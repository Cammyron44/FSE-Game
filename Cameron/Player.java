/**
 * @(#)Player.java
 *
 *
 * @author 
 * @version 1.00 2018/6/6
 */

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Player {
	private double x, xPos, yPos, vx, vy, sx, sy;
	private String direction;
	private boolean jump, ground;
	int screenX = 1900;
	int screenY = 1000;
	private int platform, health;
	private BufferedImage image = null;
	
	public Player(){
		x = 0; //position of background image on screen
		xPos = 100; //player X
		yPos = 400; //player Y
		vx = 0;
		vy = 0;
		direction = "right";
		jump = false;
		loadImage();
		ground = true;
		health = 100;
	
	}
	///////////////////////////////LOAD IMAGE//////////////////////////////////////
	public void loadImage(){
		try {
    		image = ImageIO.read(new File("map1Mask.png"));
		} 
		catch (IOException e) {
		}
		platform = getPixelCol(image, 25, 25); //platform colour
    }
    ///////////////////////////////PIXEL COLOUR////////////////////////////////////
    public int getPixelCol(BufferedImage image, int xx, int yy){
    	return image.getRGB(xx + (int)x, yy);
    }
    
    public boolean getColTop(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x, yy);
    	for (int d = 0; d < 50; d++){
    		c = image.getRGB(d + xx + (int) x, yy);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColBottom(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int )x, yy + 100);
    	for (int d = 0; d < 50; d++){
    		c = image.getRGB(d + xx + (int) x, yy + 100);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColRight(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x + 50, yy + 100);
    	for (int d = 0; d < 100; d++){
    		c = image.getRGB(xx + (int) x + 50, yy + d);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColLeft(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x - 1, yy + 100);
    	for (int d = 0; d < 100; d++){
    		c = image.getRGB(xx + (int) x - 1, yy + d);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
	///////////////////////////////VERTICAL MOVEMENT///////////////////////////////
	public void jump(){
		if (vy < 0){
			for (int i = 0; i < (int) vy * -1; i++){
				if (getColTop(image, (int) xPos, (int) yPos) == false){
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
				if (getColBottom(image, (int) xPos, (int) yPos) == false){
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
	}
	
	public void fall(){
		for (int i = 0; i < (int) vy; i++){
				if (getColBottom(image, (int) xPos, (int) yPos) == false){
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
		}
	//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
	public void move(String d){
		if (vx < 5){
			vx += 0.4;
		}
		if (d.equals("right")){
			direction = "right";
			for (int i = 0; i < (int) vx; i++){
				if (getColRight(image, (int) xPos, (int) yPos) == false){
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
				if (getColLeft(image, (int) xPos, (int) yPos) == false){
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
	}
	
	public void slowDown(String d){
		if (vx > 0){
			vx -= 0.3;
		}
		if (d.equals("right")){
			direction = "right";
			if (getColRight(image, (int) xPos + (int) vx, (int) yPos) == false){
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
			if (getColLeft(image, (int) xPos - (int) vx, (int) yPos) == false){
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
	}
	////////////////////////////GETTERS AND SETTERS////////////////////////////////
	public int getX(){
		return (int) x;
	}
	
	public int getXPos(){
		return (int) xPos;
	}
	
	public int getYPos(){
		return (int) yPos;
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
	}
}