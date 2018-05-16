/**
 * @(#)getRGB.java
 *
 *
 * @author 
 * @version 1.00 2018/5/11
 */


import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class getRGB {
	
	static BufferedImage image = null;
	
    public getRGB() {
    	
    }
    
    public static void main(String[]args){
    	loadImage();
    	System.out.println(getPixelColor(image, 1000, 500));
    }
    
    public static void loadImage(){
		try {
    		image = ImageIO.read(new File("backMask.png"));
		} 
		catch (IOException e) {
		}
    }
    
    public static Color getPixelColor(BufferedImage image, int x, int y){
    	int clr = image.getRGB(x, y); 
		return new Color ((clr & 0x00ff0000) >> 16, (clr & 0x0000ff00) >> 8, clr & 0x000000ff);
    }
    
}