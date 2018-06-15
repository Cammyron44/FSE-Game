/**
 * @(#)Fireball.java
 *
 *
 * @author 
 * @version 1.00 2018/6/7
 */

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Fireball {
	double x, y, vy;
	int restCount;
	double vOrg;
	boolean rest = false;

    public Fireball(int xx, int yy, double dist) {
    	x = xx;
    	y = yy;
    	vOrg = -dist/50;
    	vy = vOrg;
    }
    
    public void jump(){
    	if (!rest){
    		vy += 0.15;
    		y += vy;
    	}
    }
    
    public void reset(){
    	y = 950;
    	vy = vOrg;
    	rest = true;
    }
    
    public void refresh(){
    	if (rest){
	    	if (restCount >= 100){
	    		rest = false;
	    		restCount = 0;
	    	}
	    	else{
	    		restCount++;
	    	}
    	}   	
    }
    
    public boolean getRest(){
    	return rest;
    }
    
    public int getX(){
    	return (int) x;
    }
    
    public int getY(){
    	return (int) y;
    }    
}