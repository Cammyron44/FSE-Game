/**
 * @(#)runTestNew.java
 *
 *
 * @author 
 * @version 1.00 2018/5/10
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class runTestNew {

    public runTestNew() {
    }
    
    public static void main(String args[]) throws IOException{
		
		// Getting pixel color by position x and y 
		System.out.println(getPixelColor(1800, 850));
	}
	
	public static Color getPixelColor(int x, int y){
		try{
			File file= new File("backMask.png");
			BufferedImage image = ImageIO.read(file);
			int clr=  image.getRGB(x, y); 
			int  red   = (clr & 0x00ff0000) >> 16;
			int  green = (clr & 0x0000ff00) >> 8;
			int  blue  =  clr & 0x000000ff;
			System.out.println("Red Color value = "+ red);
			System.out.println("Green Color value = "+ green);
			System.out.println("Blue Color value = "+ blue);
			return new Color (red, green, blue);
		}
		catch (IOException ex){
			System.out.println(ex);
		}
		return null;
	}
    
}