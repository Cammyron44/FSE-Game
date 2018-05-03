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
	private Image back;
	private Rectangle playerRect;
	Player man;
	String playerDirection;
	
	public GamePanel(){
		keys = new boolean [KeyEvent.KEY_LAST+1];
		man = new Player();
		back = new ImageIcon("back.jpg").getImage();
		back = back.getScaledInstance(1300, 800,Image.SCALE_SMOOTH);
		playerDirection = "right";
	}
	
	public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
		
	public void refresh(){
		playerRect = new Rectangle(man.getX(), man.getY(), 50, 50);
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
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(back, 0, 0, this);
		g2.fill(playerRect);		
	}
}

class Player {
	private double px, py, vx, vy, sx, sy;
	private String direction = "right";
	
	public Player(){
		px = 650;
		py = 524;
		vx = 0;
		vy = 0;
	}
	
	public void move(String d){
		if (vx < 4){
			vx += 0.1;
		}
		if (d.equals("left")){
			px -= vx;
		}
		else{
			px += vx;
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
			px += vx;
		}
		else{
			px -= vx;
		}
	}
	
	public void resetVx(){
		vx = 0;
	}
	
	public int getX(){
		return (int) px;
	}
	
	public int getY(){
		return (int) py;
	}
}