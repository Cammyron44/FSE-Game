/**
 * @(#)Ground.java
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

class Block{
	private String type;
	private int x, y;
	private Rectangle rect;
	
	public Block(int xx, int yy, String t){//constructor
		x = xx;
		y = yy;
		type = t;
		rect = new Rectangle(x, y, 50, 50);
	}
	
	public Rectangle getRect(){//returns rect
		return rect;
	}
	
	public int getX(){//returns x
		return x;
	}
	
	public int getY(){//returns y
		return y;
	}
}