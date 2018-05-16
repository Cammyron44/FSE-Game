/**
 * @(#)runTest.java
 *
 *
 * @author 
 * @version 1.00 2018/5/2
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class runTest extends JFrame implements ActionListener, KeyListener {
	
	Timer myTimer;
	GamePanel game;
	int screenX = 1900;
	int screenY = 1000;
	
    public runTest() {
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
    	new runTest();
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
		/*///////////////////////////////////////////////////////////////////////////////
		moveMan();
		///////////////////////////////////////////////////////////////////////////////
		if (keys[KeyEvent.VK_SPACE]){
			man.setJumpTrue();
		}
		if (man.getJump()){
			man.jump();			
		}
		else if (!baseRect.contains(man.getX(), man.getY() + 102) && !baseRect.contains(man.getX() + 50, man.getY() + 102)){
			man.setJumpFalse();
			man.fall();
		}*/
		
	}
	
	/*public void moveMan(){
		if (keys[KeyEvent.VK_D]){
			if (playerDirection.equals("left")){
				man.resetVx();
			}
			playerDirection = "right";
			man.move(playerDirection);
		}
		else if (keys[KeyEvent.VK_A]){
			if (playerDirection.equals("right")){
				man.resetVx();
			}
			playerDirection = "left";
			man.move(playerDirection);
		}	
		else{
			man.slowDown(playerDirection);
		}
	}*/
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(backMask, 0, 0, this);
		g.setColor(new Color(0, 255, 0));
		//g2.fill(baseRect);
		//g2.fill(platRect);
		g.setColor(new Color(0, 0, 0));
		g2.fill(playerRect);
		//g.setColor(new Color(255, 0, 0));
		//g.drawRect(man.getX(), man.getY(), 25, 50);
	}
}

class Player {
	private double x, y, vx, vy, sx, sy;
	private String direction;
	private boolean jump;
	int screenX = 1900;
	int screenY = 1000;
	private Image backMask;
	private Color mask;
	private BufferedImage image = null;
	
	public Player(){
		x = 650;
		y = 600;
		vx = 0;
		vy = -30;
		direction = "right";
		jump = false;
		mask = new Color (0, 255, 0);
		loadImage();		
	}
	///////////////////////////////////////////////////////////////////////////////
	public void loadImage(){
		try {
    		image = ImageIO.read(new File("backMask.png"));
		} 
		catch (IOException e) {
		}
    }
    
    public Color getPixelColor(BufferedImage image, int x, int y){
    	int clr = image.getRGB(x, y); 
		return new Color ((clr & 0x00ff0000) >> 16, (clr & 0x0000ff00) >> 8, clr & 0x000000ff);
    }
	///////////////////////////////////////////////////////////////////////////////
	public void move(String d){
		if (getPixelColor(image, (int) x + 52, (int) y + 101) != mask){
		
		if (vx < 4){
			vx += 0.1;
		}
	}
		if (d.equals("left")){
			x -= vx;
		}
		else{
			x += vx;
		}
	}
	
	public void slowDown(String d){
		if (vx > 0){
			vx -= 0.1;
		}
		else {
			vx = 0;
		}
		if (d.equals("right")){
			x += vx;
		}
		else{
			x -= vx;
		}
	}
	
	public void resetVx(){
		vx = 0;
	}
	///////////////////////////////////////////////////////////////////////////////
	public void jump(){
		if(getPixelColor(image, (int) x, (int) y) != mask || getPixelColor(image, (int) x + 50, (int) y) != mask){
			vy = 0;
		}
		else{
			vy += 0.9;
		}
		y += vy;
		
	}
	
	public void setJumpTrue(){
		jump = true;
	}
	
	public void setJumpFalse(){
		vy = -30;
		jump = false;
	}
	
	public void fall(){
		y += 0.5;
	}
	
	public boolean getJump(){
		return jump;
	}
	///////////////////////////////////////////////////////////////////////////////
	public int getX(){
		return (int) x;
	}
	
	public int getY(){
		return (int) y;
	}
	
	public int getVY(){
		return (int) vy; 
	}
}