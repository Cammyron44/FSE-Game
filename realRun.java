/**
 * @(#)realRun.java
 *
 *
 * @author 
 * @version 1.00 2018/5/15
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class realRun extends JFrame implements ActionListener, KeyListener {
	
	Timer myTimer;
	GamePanel game;
	int screenX = 1900;
	int screenY = 1000;
	
    public realRun() {
    	super("Run Test");
    	setSize(screenX, screenY);
    	myTimer = new Timer (10, this);
    	myTimer.start();
    	game = new GamePanel();
    	add(game);
    	addKeyListener(this);
    	setResizable(false);
    	setVisible(true);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void actionPerformed(ActionEvent e) {
    	if(game != null){
    		game.refresh();
    		game.repaint();
    	}
    }

    public void keyTyped(KeyEvent e) {}
    
    public void keyPressed(KeyEvent e) {
    	game.setKey(e.getKeyCode(), true);
    }
    
    public void keyReleased(KeyEvent e) {
    	game.setKey(e.getKeyCode(), false);
    }
    
    public static void main(String[]args){
    	new realRun();
    }
    
    
}

class GamePanel extends JPanel {
	
	private boolean[] keys;
	private Image back, backMask, test;
	private Rectangle playerRect, baseRect, platRect;
	Player man;
	String playerDirection;
	
	public GamePanel(){
		keys = new boolean [KeyEvent.KEY_LAST+1];

		back = new ImageIcon("back.jpg").getImage();
		back = back.getScaledInstance(1900, 1000,Image.SCALE_SMOOTH);
		backMask = new ImageIcon("backMask.png").getImage();
		backMask = backMask.getScaledInstance(1900, 1000,Image.SCALE_SMOOTH);
		test = new ImageIcon("test.png").getImage();
		
		playerDirection = "right";
		
		baseRect = new Rectangle(0, 717, 1900, 50);
		platRect = new Rectangle(700, 500, 500, 50);
		
		man = new Player();
	}
	
	public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
		
	public void refresh(){
		playerRect = new Rectangle(man.getXPos(), man.getY(), 50, 100);
		move();
	}
	
	public void move(){
		//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
		if (keys[KeyEvent.VK_D]){
			if (playerDirection.equals("left")){
				man.setVx(0);
			}
			playerDirection = "right";
			man.moveRight();
		}
		else if (keys[KeyEvent.VK_A]){
			if (playerDirection.equals("right")){
				man.setVx(0);
			}
			playerDirection = "left";
			man.moveLeft();
		}
		else{
			if (man.getVx() != 0){
				man.slowDown(playerDirection);
			}
		}
		///////////////////////////////VERTICAL MOVEMENT///////////////////////////////
		if (keys[KeyEvent.VK_SPACE] && !man.getJump() && man.getGround() == true){
			man.setVy(-20);
			man.setJump(true);
			man.setGround(false);
		}
		if (man.getJump()){
			man.jump();
		}
		else{
			man.fall();
		}
		System.out.println(man.getGround());
		System.out.println(man.getXPos());
	}
	
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(test, -man.getX(), 0, this);
		g.setColor(new Color(0, 0, 0));
		g2.fill(playerRect);
	}
}

class Player {
	private double x, xPos, y, vx, vy, sx, sy;
	private String direction;
	private boolean jump, ground;
	int screenX = 1900;
	int screenY = 1000;
	private int mask;
	private BufferedImage image = null;
	
	public Player(){
		x = 0;
		xPos = 200;
		y = 600;
		vx = 0;
		vy = 0;
		direction = "right";
		jump = false;
		mask = 255;
		loadImage();
		ground = true;
	}
	////////////////////////LOAD IMAGE AND PIXEL COLOUR////////////////////////////
	public void loadImage(){
		try {
    		image = ImageIO.read(new File("test.png"));
		} 
		catch (IOException e) {
		}
    }
    
    public boolean getPixelColor(BufferedImage image, int x, int y){
    	int c = image.getRGB(x, y);
		if ((c & 0x00ff0000) >> 16 == 0 && (c & 0x0000ff00) >> 8 == mask && (c & 0x000000ff) == 0){ //checks for (0, 255, 0);
			//            RED                             GREEN							BLUE
			return true;
		}
		else{
			return false;
		}
    }
	///////////////////////////////VERTICAL MOVEMENT///////////////////////////////
	public void jump(){
			if (vy < 0){
				if (getPixelColor(image, (int) xPos, (int) y + (int) vy) == true || getPixelColor(image, (int) xPos + 50, (int) y + (int) vy) == true){
					vy = 0;
				}
				else{
					vy += 1;
					ground = false;
				}
			}
			else{
				if (getPixelColor(image, (int) xPos, (int) y + 100 + (int) vy) == true || getPixelColor(image, (int) xPos + 50, (int) y + 100 + (int) vy) == true){
					vy = 0;
					jump = false;
					ground = true;
				}
				
				else{
					vy += 1;
				}
			}
		y += vy;
	}
	
	public void fall(){
		if (getPixelColor(image, (int) xPos, (int) y + 100 + (int) vy) == true || getPixelColor(image, (int) xPos + 50, (int) y + 100 + (int) vy) == true){
			vy = 0;
			ground = true;
		}
		else{
			ground = false;
			vy += 1;
		}
		y += vy;
	}
	//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
	public void moveRight(){
		if (vx < 5){
			vx += 0.4;
		}
		if (getPixelColor(image, (int) xPos + 53, (int) y + 99) == false && getPixelColor(image, (int) xPos + 53, (int) y) == false){
			if (xPos <= 890){
				xPos += (int) vx;
			}
			else if (x >= 4090){
				if (xPos < 1830){ //max distance
					xPos += (int) vx;
				}
			}
			else{
				x += (int) vx;
			}
		}
	}
	
	public void moveLeft(){
		if (vx < 5){
			vx += 0.4;
		}
		if (getPixelColor(image, (int) xPos - 2, (int) y + 99) == false && getPixelColor(image, (int) xPos - 2, (int) y) == false){
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
	
	public void slowDown(String d){
		if (d.equals("right")){
			if (vx > 0){
				vx -= 0.3;
			}
			if (xPos <= 890){
				xPos += (int) vx;
			}
			else if (x >= 4090){
				if (xPos < 1830){ //max distance
					xPos += (int) vx;
				}
			}
			else{
				x += (int) vx;
			}	
		}
		else{
			if (vx > 0){
				vx -= 0.3;
			}
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
	////////////////////////////GETTERS AND SETTERS////////////////////////////////
	public int getX(){
		return (int) x;
	}
	
	public int getXPos(){
		return (int) xPos;
	}
	
	public int getY(){
		return (int) y;
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
	///////////////////////////////////////////////////////////////////////////////
}