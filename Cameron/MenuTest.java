/**
 * @(#)MenuTest.java
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

public class MenuTest extends JFrame implements ActionListener, KeyListener {
	
	Timer myTimer;
	GamePanel game;
	int screenX = 1900;
	int screenY = 1000;
	
    public MenuTest() {
    	super("Menu Test");
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
    	new MenuTest();
    }    
}

class GamePanel extends JPanel {
	
	private boolean[] keys;
	private Image back;
	private int screenX = 1900;
	private int screenY = 1000;
	private int menuCountR = 0;
	private int menuCountC = 0;
	private String screen = "menu";
	private Image titleText, playText, controlsText, creditsText, camJackText, quitText, backSpaceText;
	
	public GamePanel(){
		keys = new boolean [KeyEvent.KEY_LAST + 1];
		back = new ImageIcon("test.png").getImage();
		back = back.getScaledInstance(1900, 1000, Image.SCALE_SMOOTH);
		//////////////////////////////////////////////////////////////////////////////////////
		titleText = new ImageIcon("menuText/titleText.png").getImage();
		playText = new ImageIcon("menuText/playText.png").getImage();
		playText = playText.getScaledInstance(300, 100, Image.SCALE_SMOOTH);
		controlsText = new ImageIcon("menuText/controlsText.png").getImage();
		controlsText = controlsText.getScaledInstance(300, 100, Image.SCALE_SMOOTH);
		creditsText = new ImageIcon("menuText/creditsText.png").getImage();
		creditsText = creditsText.getScaledInstance(300, 100, Image.SCALE_SMOOTH);
		quitText = new ImageIcon("menuText/quitText.png").getImage();
		quitText = quitText.getScaledInstance(300, 100, Image.SCALE_SMOOTH);
		backSpaceText = new ImageIcon("menuText/backSpaceText.png").getImage();
		//backSpaceText = backSpaceText
		///////////////////////////////////////PLAY///////////////////////////////////////////
		/////////////////////////////////////CONTROLS/////////////////////////////////////////
		camJackText = new ImageIcon("menuText/camJackText.png").getImage();
		/////////////////////////////////////CREDITS//////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////
	}
	
	public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
	
	public void refresh(){
		if (screen.equals("menu")){
			if (keys[KeyEvent.VK_UP]){
				if (menuCountC == 1){
					menuCountC = 0;
				}
			}
			if (keys[KeyEvent.VK_LEFT]){
				if (menuCountR == 1){
					menuCountR = 0;
				}
			}
			if (keys[KeyEvent.VK_DOWN]){
				if (menuCountC == 0){
					menuCountC = 1;
				}
			}
			if (keys[KeyEvent.VK_RIGHT]){
				if (menuCountR == 0){
					menuCountR = 1;
				}
			}
			if (keys[KeyEvent.VK_ENTER]){
				if (menuCountR == 0){
					if (menuCountC == 0){
						screen = "play";
					}
					else{
						screen = "credits";
					}
				}
				else{
					if (menuCountC == 0){
						screen = "controls";
					}
					else{
						screen = "quit";
					}
				}
				menuCountR = 0;
				menuCountC = 0;
			}
		}
		else if (screen.equals("play")){
			if (keys[KeyEvent.VK_BACK_SPACE]){
				screen = "menu";
			}
		}
		else if (screen.equals("controls")){
			if (keys[KeyEvent.VK_BACK_SPACE]){
				screen = "menu";
			}
		}
		else if (screen.equals("credits")){
			if (keys[KeyEvent.VK_BACK_SPACE]){
				screen = "menu";
			}
		}
		else{
			System.exit(0);
		}
		System.out.println(screen);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		if (screen.equals("menu")){
			g.drawImage(back, 0, 0, this);
			g.setColor(new Color(0, 0, 0));
			g2.setStroke(new BasicStroke(5));
			g.drawImage(titleText, screenX/2 - titleText.getWidth(null)/2, 100, this);
			g.drawImage(playText, 625, 500, this);
			g.drawImage(controlsText, 975, 500, this);
			g.drawImage(creditsText, 625, 650, this);
			g.drawImage(quitText, 975, 650, this);
			g.drawRect(625 + menuCountR * 350, 500 + menuCountC * 150 , 300, 100);
		}
		else if (screen.equals("play")){
			g.drawImage(back, 0, 0, this);
			g.drawImage(backSpaceText, screenX - backSpaceText.getWidth(null) - 10 , 10, this);
		}
		else if (screen.equals("controls")){
			g.drawImage(back, 0, 0, this);
			g.drawImage(backSpaceText, screenX - backSpaceText.getWidth(null) - 10 , 10, this);
		}
		else if (screen.equals("credits")){
			g.drawImage(back, 0, 0, this);
			g.drawImage(camJackText, screenX/2 - camJackText.getWidth(null)/2, screenY/2 - camJackText.getHeight(null)/2, this);
			g.drawImage(backSpaceText, screenX - backSpaceText.getWidth(null) - 10 , 10, this);
		}
	}
}