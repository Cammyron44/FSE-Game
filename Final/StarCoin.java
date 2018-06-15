/**
 * @(#)StarCoin.java
 *
 *
 * @author 
 * @version 1.00 2018/6/4
 */

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class StarCoin {
	private int x, y, newY, num;
	private double frame;
	private boolean picked;
	
    public StarCoin(int xx, int yy, int n) {
    	x = xx;
    	y = yy;
    	newY = yy;
    	frame = 0;
    	num = n;
    	picked = false;
    }
    
    public void addNewY(){
    	newY -= 2;
    }
    
    public void setPickedTrue(){
    	picked = true;
    }
    
    public void increaseFrame(){
    	if (frame > 9){
			frame = 0;
		}
		else{
			frame += 0.1;
		}
    }
    
    public void speedFrame(){
   		if (frame > 9){
			frame = 0;
		}
		else{
			frame += 0.8;
		}
    }
    
    public int getFrame(){
    	return (int) frame;
    }
    
    public boolean getPicked(){
    	return picked;
    }
    
    public int getNewY(){
    	return newY;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    
    public void addX(int xx){
    	x += xx;
    }
    
    public void addY(int yy){
    	y += yy;
    }
    
    public int getNum(){
    	return num;
    }
}