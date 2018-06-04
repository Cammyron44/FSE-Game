/**
 * @(#)realRun.java
 *
 *
 * @author 
 * @version 1.00 2018/5/15
 */

import java.awt.*;
import java.util.ArrayList;
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
	private Image back, backMask, test, map1, lava;
	private double lavaX;
	private Rectangle playerRect, baseRect, platRect;
	Player man;
	String playerDirection;
	
	int starCoinCount = 0;
	Integer coin, starCoin;
	Image coinImage, starCoinImage, starCoinBW, starCoinSmall;
	Image[] coinImages, starCoinImages;
	ArrayList <Coin> allCoins;
	ArrayList <StarCoin> allStarCoins, starCoinsPicked;
	Rectangle coinRect, starCoinRect;
	
	
	private BufferedImage mask = null;
	int red, bronze, yellow;
	
	public GamePanel(){
		
		loadImage();
		loadCoins();
		loadStarCoins();
		
		keys = new boolean [KeyEvent.KEY_LAST + 1];

		back = new ImageIcon("back.jpg").getImage();
		back = back.getScaledInstance(1900, 1000,Image.SCALE_SMOOTH);
		backMask = new ImageIcon("backMask.png").getImage();
		backMask = backMask.getScaledInstance(1900, 1000, Image.SCALE_SMOOTH);
		test = new ImageIcon("test1.png").getImage();
		map1 = new ImageIcon("map1.png").getImage();
		
		lava = new ImageIcon("lava.png").getImage();
		lavaX = -1900;

		playerDirection = "right";
		
		baseRect = new Rectangle(0, 717, 1900, 50);
		platRect = new Rectangle(700, 500, 500, 50);
		
		man = new Player();
		
		starCoinSmall = new ImageIcon("coin/starCoin0.png").getImage();
		starCoinBW = new ImageIcon("coin/starCoinBW.png").getImage();
		
		coinImages = new Image[10];
    	for (int i = 0; i < 10; i++){
    		Integer coinNum = i;
    		String imageName = "coin/coin";
    		coinImage = new ImageIcon(imageName + coinNum.toString() + ".png").getImage();
    		coinImages[i] = coinImage;
    	}
    	
    	starCoinImages = new Image[10];
    	for (int i = 0; i < 10; i++){
    		Integer starCoinNum = i;
    		String imageName = "coin/starCoin";
    		starCoinImage = new ImageIcon(imageName + starCoinNum.toString() + ".png").getImage();
    		starCoinImages[i] = starCoinImage;
    	}
	}
	//////////////////////////////////////////////////////////////////////////////
	public void loadImage(){
		try {
    		mask = ImageIO.read(new File("map1Mask.png"));
		} 
		catch (IOException e) {
		}
		red = getPixelCol(mask, 75, 25); //for instant death areas
		bronze = getPixelCol(mask, 125, 25); //regular coins
		yellow = getPixelCol(mask, 175, 25); //star coins
    }
    
    public int getPixelCol(BufferedImage mask, int xx, int yy){
    	return mask.getRGB(xx , yy);
    }
    /////////////////////////////////COINS////////////////////////////////////////
    public void loadCoins(){
    	allCoins = new ArrayList <Coin>();
    	for (int i = 0; i < 500; i++){
			for (int j = 0; j < 20; j++){
				if (getPixelCol(mask, i * 50, j * 50) == bronze && i != 2){ //cannot be 2 because on the mask that space is the base colour (we dont want a coin being created there)
					allCoins.add(new Coin(i * 50, j * 50)); //wherever there is a bronze space on the mask, create a coin object there
				}
			}
    	}
    }
    
    public void checkCoin(){
    	for (int i = 0; i < allCoins.size(); i++){
    		coinRect = new Rectangle(allCoins.get(i).getX() - man.getX(), allCoins.get(i).getY(), 50, 50); //smoother to check rectangles/squares than a point on the coin
    		if (playerRect.intersects(coinRect)){ //player picks up coin			
    			allCoins.get(i).setPickedTrue();
    		}
    		if (allCoins.get(i).getPicked() == true){ //once coin is picked up, used for animation
    			if (allCoins.get(i).getY() - allCoins.get(i).getNewY() < 50){ // how far the coin will raise
    				allCoins.get(i).addNewY(); //raise the coin
    			}
    			else{
    				allCoins.remove(i); //once the coin gets high enough, get rid of it
    				//ADD TO PLAYER SCORE //add to the player score
    			}	
    		}
    	}
    }
    	
	public void coinFrameIncrease(){
		for (int i = 0; i < allCoins.size(); i++){
			if (allCoins.get(i).getPicked() == true){
				allCoins.get(i).speedFrame(); //increase frame + framerate of specific coin if picked up
			}
			else{
				allCoins.get(i).increaseFrame(); //increase frame of all coins at general rate
			}
		}
	}
	///////////////////////////////STAR COINS////////////////////////////////////////
    public void loadStarCoins(){
    	allStarCoins = new ArrayList <StarCoin>();
    	starCoinsPicked = new ArrayList <StarCoin>();
    	int n = 0; //keeps track of which coin it is (helps the player know which coins he needs to pick up (or is has skipped one)
    	for (int i = 0; i < 500; i++){
			for (int j = 0; j < 20; j++){
				if (getPixelCol(mask, i * 50, j * 50) == yellow && i != 3){ //cannot be 3 because on the mask that space is the base colour (we dont want a star coin being created there)
					allStarCoins.add(new StarCoin(i * 50 - 25, j * 50 - 25, n)); //wherever there is a yellow space on the mask, create a coin object there
					n++;
				}
			}
    	}
    }
    
    public void checkStarCoin(){
    	for (int i = 0; i < allStarCoins.size(); i++){
    		starCoinRect = new Rectangle(allStarCoins.get(i).getX() - man.getX(), allStarCoins.get(i).getY(), 100, 100); //smoother to check rectangles/squares than a point on the star coin
    		if (playerRect.intersects(starCoinRect)){ //player picks up coin			
    			allStarCoins.get(i).setPickedTrue();
    		}
    		if (allStarCoins.get(i).getPicked() == true){ //once coin is picked up, used for animation
    			if (allStarCoins.get(i).getY() - allStarCoins.get(i).getNewY() < 100){ // how far the coin will raise
    				allStarCoins.get(i).addNewY(); //raise the coin
    			}
    			else{
    				starCoinsPicked.add(allStarCoins.get(i)); //add to array (used for showing user which coins they have/have not picked up)
    				allStarCoins.remove(i); //once the coin gets high enough, get rid of it
    				starCoinCount ++;
    				//ADD TO PLAYER SCORE //add to the player score
    			}	
    		}
    	}
    }
    	
	public void starCoinFrameIncrease(){
		for (int i = 0; i < allStarCoins.size(); i++){
			if (allStarCoins.get(i).getPicked() == true){
				allStarCoins.get(i).speedFrame(); //increase frame + framerate of specific coin if picked up
			}
			else{
				allStarCoins.get(i).increaseFrame(); //increase frame of all coins at general rate
			}
		}
	}
	//////////////////////////////////////////////////////////////////////////////
	public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
		
	public void refresh(){
		playerRect = new Rectangle(man.getXPos(), man.getYPos(), 50, 100);
		move();
		moveLava();
		checkCoin();
		checkStarCoin();
		coinFrameIncrease();
		starCoinFrameIncrease();
	}
	
	public void move(){
		//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
		if (keys[KeyEvent.VK_D]){
			if (playerDirection.equals("left")){
				man.setVx(0);
			}
			playerDirection = "right";
			man.move(playerDirection);
		}
		else if (keys[KeyEvent.VK_A]){
			if (playerDirection.equals("right")){
				man.setVx(0);
			}
			playerDirection = "left";
			man.move(playerDirection);
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
		//////////////////////////////////////////////////////////////////////////////
	}
	
	public void moveLava(){
		if (lavaX >= 0){
			lavaX = -1900; //move both pictures to the front again (so there is a constant flow of lava)
		}
		else{
			lavaX += 0.4; //speed of lava
		}
	}
		
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(test, -man.getX(), 0, this);
		for (int i = 0; i < 15; i++){ //blitting lava across the entire map
			g.drawImage(lava, (int) lavaX - man.getX() + 1900 * i, 900, this);
		}
		g.drawImage(map1, -man.getX(), 0, this);
		g.setColor(new Color(0, 0, 0));
		g2.fill(playerRect);
		for (int i = 0; i < 3; i++){
			g.drawImage(starCoinBW, 1900 - ((i + 1) * 110), 10 , this); //black and white images of coins (meaning they havent been picked up yet)
		}
		for (int i = 0; i < starCoinsPicked.size(); i++){
			g.drawImage(starCoinSmall, 1900 - ((starCoinsPicked.get(i).getNum() + 1) * 110), 10 , this); //when the coins have been picked up (displays which one you have picked up)
		}
		for (int i = 0; i < allCoins.size(); i++){
			g.drawImage(coinImages[allCoins.get(i).getFrame()], allCoins.get(i).getX() - man.getX(), allCoins.get(i).getNewY(), this); //displays all regular coins
		}
		for (int i = 0; i < allStarCoins.size(); i++){
			g.drawImage(starCoinImages[allStarCoins.get(i).getFrame()], allStarCoins.get(i).getX() - man.getX(), allStarCoins.get(i).getNewY(), this); //displays all star coins (that have not been picked up yet)
		}
	}
}

class Player {
	private double x, xPos, yPos, vx, vy, sx, sy;
	private String direction;
	private boolean jump, ground;
	int screenX = 1900;
	int screenY = 1000;
	private int platform;
	private BufferedImage image = null;
	
	public Player(){
		x = 0; //position of background image on screen
		xPos = 100; //player X
		yPos = 400; //player Y
		vx = 0;
		vy = 0;
		direction = "right";
		jump = false;
		loadImage();
		ground = true;
	
	}
	///////////////////////////////LOAD IMAGE//////////////////////////////////////
	public void loadImage(){
		try {
    		image = ImageIO.read(new File("map1Mask.png"));
		} 
		catch (IOException e) {
		}
		platform = getPixelCol(image, 25, 25); //platform colour
    }
    ///////////////////////////////PIXEL COLOUR////////////////////////////////////
    public int getPixelCol(BufferedImage image, int xx, int yy){
    	return image.getRGB(xx + (int)x, yy);
    }
    
    public boolean getColTop(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x, yy);
    	for (int d = 0; d < 50; d++){
    		c = image.getRGB(d + xx + (int) x, yy);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColBottom(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int )x, yy + 100);
    	for (int d = 0; d < 50; d++){
    		c = image.getRGB(d + xx + (int) x, yy + 100);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColRight(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x + 50, yy + 100);
    	for (int d = 0; d < 100; d++){
    		c = image.getRGB(xx + (int) x + 50, yy + d);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
    
    public boolean getColLeft(BufferedImage image, int xx, int yy){
    	boolean b = false;
    	int c = image.getRGB(xx + (int) x - 1, yy + 100);
    	for (int d = 0; d < 100; d++){
    		c = image.getRGB(xx + (int) x - 1, yy + d);
    		if (c == platform){
    			b = true;
    		}
    	}
    	return b;
    }
    
    
	///////////////////////////////VERTICAL MOVEMENT///////////////////////////////
	public void jump(){
		if (vy < 0){
			for (int i = 0; i < (int) vy * -1; i++){
				if (getColTop(image, (int) xPos, (int) yPos) == false){
					yPos -= 1;
					ground  = false;
				}
				else{
					vy = 0;
					ground = false;
				}
			}
		}
		else{
			for (int i = 0; i < (int) vy; i++){
				if (getColBottom(image, (int) xPos, (int) yPos) == false){
					yPos += 1;
				}
				else{
					vy = 0;
					jump = false;
					ground = true;
				}
			}
		}
		vy += 1;
	}
	
	public void fall(){
		for (int i = 0; i < (int) vy; i++){
				if (getColBottom(image, (int) xPos, (int) yPos) == false){
					yPos += 1;
					ground = false;
				}
				else{
					vy = 0;
					jump = false;
					ground = true;
				}
			}
			vy += 1;
		}
	//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
	public void move(String d){
		if (vx < 5){
			vx += 0.4;
		}
		if (d.equals("right")){
			for (int i = 0; i < (int) vx; i++){
				if (getColRight(image, (int) xPos, (int) yPos) == false){
					if (xPos <= 890){
						xPos += 1;//(int) vx;
					}
					else if (x >= 23090){
						if (xPos < 1830){ //max distance
							xPos += 1;//(int) vx;
						}
					}
					else{
						x += 1;//(int) vx;
					}
				}
			}
		}
		else{
			for (int i = 0; i < (int) vx; i++){
				if (getColLeft(image, (int) xPos, (int) yPos) == false){
					if (xPos >= 910){
						xPos -= 1;//(int) vx;
					}
					else if (x <= 10){
						if (xPos > 10){ //min distance
							xPos -= 1;//(int) vx;
						}
					}
					else{
						x -= 1;//(int) vx;
					}
				}
			}
		}
	}
	
	public void slowDown(String d){
		if (vx > 0){
			vx -= 0.3;
		}
		if (d.equals("right")){
			if (getColRight(image, (int) xPos + (int) vx, (int) yPos) == false){
				if (xPos <= 890){
					xPos += (int) vx;
				}
				else if (x >= 23090){
					if (xPos < 1830){ //max distance
						xPos += (int) vx;
					}
				}
				else{
					x += (int) vx;
				}
			}
		}
		else{
			if (getColLeft(image, (int) xPos - (int) vx, (int) yPos) == false){
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
	}
	////////////////////////////GETTERS AND SETTERS////////////////////////////////
	public int getX(){
		return (int) x;
	}
	
	public int getXPos(){
		return (int) xPos;
	}
	
	public int getYPos(){
		return (int) yPos;
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