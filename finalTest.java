/**
 * @(#)finalTest.java
 *
 *
 * @author 
 * @version 1.00 2018/5/11
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class finalTest extends JFrame implements ActionListener, KeyListener {
	
	Timer myTimer;
	GamePanel game;
	int screenX = 1900;
	int screenY = 1000;
	
    public finalTest() {
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
    	new finalTest();
    }
    
    
}

class GamePanel extends JPanel {
	
	private boolean[] keys;
	private Image back, backMask;
	private Rectangle playerRect, baseRect, platRect;
	Player man;
	String playerDirection;
	
	public GamePanel(){
		keys = new boolean [KeyEvent.KEY_LAST+1];

		back = new ImageIcon("back.jpg").getImage();
		back = back.getScaledInstance(1900, 1000,Image.SCALE_SMOOTH);
		backMask = new ImageIcon("backMask.png").getImage();
		backMask = backMask.getScaledInstance(1900, 1000,Image.SCALE_SMOOTH);
		
		playerDirection = "right";
		
		baseRect = new Rectangle(0, 717, 1900, 50);
		platRect = new Rectangle(700, 500, 500, 50);
		
		man = new Player();
	}
	
	public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
		
	public void refresh(){
		playerRect = new Rectangle(man.getX(), man.getY(), 50, 100);
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
		/*else{
			if (man.getVx() != 0){
				man.slowDown(playerDirection);
			}
		}*/
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
	}
	
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(backMask, 0, 0, this);
		g.setColor(new Color(0, 0, 0));
		g2.fill(playerRect);
	}
}

class Player {
	private double x, y, vx, vy, sx, sy;
	private String direction;
	private boolean jump, ground;
	int screenX = 1900;
	int screenY = 1000;
	private Image backMask;
	private int mask;
	private BufferedImage image = null;
	
	public Player(){
		x = 600;
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
    		image = ImageIO.read(new File("backMask.png"));
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
				if (getPixelColor(image, (int) x, (int) y + (int) vy) == true || getPixelColor(image, (int) x + 50, (int) y + (int) vy) == true){
					vy = 0;
				}
				else{
					vy += 1;
					ground = false;
				}
			}
			else{
				if (getPixelColor(image, (int) x, (int) y + 100 + (int) vy) == true || getPixelColor(image, (int) x + 50, (int) y + 100 + (int) vy) == true){
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
		if (getPixelColor(image, (int) x, (int) y + 100 + (int) vy) == true || getPixelColor(image, (int) x + 50, (int) y + 100 + (int) vy) == true){
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
		//vx = 0;
		if (vx < 5){
			vx += 0.4;
		}
		//for (int i = 0; i < 5; i++){
			if (getPixelColor(image, (int) x + 53, (int) y + 99) == false && getPixelColor(image, (int) x + 53, (int) y) == false){
				x += (int) vx;
				//vx += 1;
			}
			else{
				vx = 0;
			}
		//}
	}
	
	public void moveLeft(){
		//vx = 0;
		if (vx < 5){
			vx += 0.4;
		}
		//for (int i = 0; i < 5; i++){
			if (getPixelColor(image, (int) x - 2, (int) y + 99) == false && getPixelColor(image, (int) x - 2, (int) y) == false){
				x -= (int) vx;
				//vx += 1;
			}
			else{
				vx = 0;
			}
		//}
	}
	
	public void slowDown(String d){
		if (d.equals("right")){
			if (vx > 0){
				vx -= 0.3;
			}
			if (getPixelColor(image, (int) x + 52, (int) y + 99) == true || getPixelColor(image, (int) x + 52, (int) y) == true){
				vx = 0;
			}
			x += vx;
		}
		else{
			if (vx > 0){
				vx -= 0.3;
			}
			if (getPixelColor(image, (int) x - 2, (int) y + 99) == true || getPixelColor(image, (int) x - 2, (int) y) == true){
				vx = 0;
			}
			x -= vx;
		}
	}
	////////////////////////////GETTERS AND SETTERS////////////////////////////////
	public int getX(){
		return (int) x;
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