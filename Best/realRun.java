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
import java.awt.geom.*;
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
	
	private int screenX = 1900;
	private int screenY = 1000;
	private boolean[] keys;
	private Image back, backMask, test, map1, lava;
	private double lavaX;
	private int time = 0, cooldown = 125;
	private Rectangle playerRect, healthRect;
	Player man;
	String playerDirection, playerAction;
	
	int starCoinCount = 0;
	Integer coin, starCoin;
	Image coinImage, starCoinImage, starCoinBW, starCoinSmall, cannonPic, bullet;
	Image[] coinImages, starCoinImages;
	ArrayList <Coin> allCoins;
	ArrayList <StarCoin> allStarCoins, starCoinsPicked;
	Rectangle coinRect, starCoinRect, RealRect;
	
	ArrayList<Arrow>arrows = new ArrayList<Arrow>();
	ArrayList<Arrow>eArrows = new ArrayList<Arrow>();
	ArrayList<Enemy>enemies = new ArrayList<Enemy>();
	ArrayList<Block>blocks = new ArrayList<Block>();
	ArrayList<Cannon>cannons = new ArrayList<Cannon>();
	ArrayList<Heart>hearts = new ArrayList<Heart>();
	Rectangle heartRect;
	private Image heart;
	
	ArrayList <Fireball> fireballs;
	Rectangle fireRect;
	Image fireball;
	int fireCount = 0;
	double fireAng = 0;
	
	private Image manImage;
	private Image[] manImages;
	private double manFrame;
	
	private BufferedImage mask = null;
	private BufferedImage fireMask = null;
	
	int green, red, bronze, yellow, black, darkRed;
	
	public GamePanel(){
		
		loadImage();
		loadHearts();
		loadCoins();
		loadStarCoins();
		loadFireballs();
		loadBlocks();
		loadCannons();
		
		keys = new boolean [KeyEvent.KEY_LAST + 1];

		back = new ImageIcon("back.jpg").getImage();
		back = back.getScaledInstance(1900, 1000,Image.SCALE_SMOOTH);
		backMask = new ImageIcon("backMask.png").getImage();
		backMask = backMask.getScaledInstance(1900, 1000, Image.SCALE_SMOOTH);
		test = new ImageIcon("test.png").getImage();
		map1 = new ImageIcon("map1.png").getImage();
		cannonPic = new ImageIcon("cannon.png").getImage();
		cannonPic = cannonPic.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		bullet = new ImageIcon("bulletBill.png").getImage();
		bullet = bullet.getScaledInstance(50, 25, Image.SCALE_SMOOTH);
		
		lava = new ImageIcon("lava.png").getImage();
		lavaX = -1900;

		playerDirection = "right";
		
		man = new Player();
		
		starCoinSmall = new ImageIcon("coin/starCoin0.png").getImage();
		starCoinBW = new ImageIcon("coin/starCoinBW.png").getImage();
		
		heart = new ImageIcon("heart.png").getImage();
		heart = heart.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		
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
    	
    	fireball = new ImageIcon("fireball.png").getImage();
    	fireball = fireball.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    	
    	manImages = new Image[41];
    	for (int i = 0; i < 41; i++){
    		Integer manNum = i;
    		String imageName = "char/ness";
    		manImage = new ImageIcon(imageName + manNum.toString() + ".png").getImage();
    		manImage = manImage.getScaledInstance((int) (manImage.getWidth(null) * 2), (int) (manImage.getHeight(null) * 2), Image.SCALE_SMOOTH);
    		manImages[i] = manImage;
    	}
	}
	//////////////////////////////////////////////////////////////////////////////
	public void loadImage(){
		try {
    		mask = ImageIO.read(new File("map1Mask.png"));
    		fireMask = ImageIO.read(new File("map1Fireball.png"));
		} 
		catch (IOException e) {
		}
		green = getPixelCol(mask, 25, 25);
		red = getPixelCol(mask, 75, 25); //for instant death areas
		bronze = getPixelCol(mask, 125, 25); //regular coins
		yellow = getPixelCol(mask, 175, 25); //star coins
		black = getPixelCol(mask, 225, 25);
		darkRed = getPixelCol(mask, 375, 25); //heart
    }
    
    public int getPixelCol(BufferedImage mask, int xx, int yy){
    	return mask.getRGB(xx , yy);
    }
    /////////////////////////////////COINS////////////////////////////////////////
    public void loadCoins(){
    	allCoins = new ArrayList <Coin>();
    	for (int i = 0; i < 500; i++){
			for (int j = 0; j < 20; j++){
				if (getPixelCol(mask, i * 50, j * 50) == bronze && j != 0){ //cannot be 2 because on the mask that space is the base colour (we dont want a coin being created there)
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
	////////////////////////////////////HEARTS/////////////////////////////////////
	public void loadHearts(){
		hearts = new ArrayList <Heart>();
    	for (int i = 0; i < 500; i++){
			for (int j = 0; j < 20; j++){
				if (getPixelCol(mask, i * 50, j * 50) == darkRed && j != 0){
					Heart h = new Heart(i * 50, j * 50);
					hearts.add(h); 
				}
			}
    	}
	}
	
	public void checkHearts(){
		for (int i = 0; i < hearts.size(); i++){
    		heartRect = new Rectangle(hearts.get(i).getX() - man.getX(), hearts.get(i).getY(), 50, 50); //smoother to check rectangles/squares than a point on the star coin
    		if (playerRect.intersects(heartRect)){ //player picks up coin			
    			hearts.remove(hearts.get(i));
    			man.addHealth(50);
    		}
    	}
	}
	////////////////////////////////////BLOCKS/////////////////////////////////////
	public void loadBlocks(){
    	for (int i = 0; i < 500; i++){
			for (int j = 0; j < 20; j++){
				if (getPixelCol(mask, i * 50, j * 50) == green){
					Block block = new Block(i * 50, j * 50, "");
					blocks.add(block); 
				}
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
				if (getPixelCol(mask, i * 50, j * 50) == yellow && j != 0){ //cannot be 3 because on the mask that space is the base colour (we dont want a star coin being created there)
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
    				//ADD TO PLAYER SCORE
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
		
	public void move(){
		//////////////////////////////HORIZONTAL MOVEMENT//////////////////////////////
		if (keys[KeyEvent.VK_D]){
			if (playerDirection.equals("left")){
				man.setVx(0);
				manFrame = 0;
				playerDirection = "right";
			}
			if (!playerAction.equals("run") && !man.getJump()){
				playerAction = "run";
				manFrame = 0;
			}
			man.move(playerDirection);
			if (manFrame < 7){
				manFrame += 0.1;
			}
			else{
				manFrame = 0;		
			}
		}
		else if (keys[KeyEvent.VK_A]){
			if (playerDirection.equals("right")){
				man.setVx(0);
				manFrame = 0;
				playerDirection = "left";
			}
			if (!playerAction.equals("run") && !man.getJump()){
				playerAction = "run";
				manFrame = 0;
			}
			man.move(playerDirection);
			if (manFrame < 7){
				manFrame += 0.1;
			}
			else{
				manFrame = 0;		
			}
		}
		else{
			if (man.getVx() != 0){
				man.slowDown(playerDirection);
				if (manFrame < 7){
					manFrame += 0.08;
				}
				else{
					manFrame = 0;		
				}
			}
			else{
				playerAction = "stand";
				if (manFrame < 4){
					manFrame += 0.05;
				}
				else{
					manFrame = 0;		
				}
			}
		}
		///////////////////////////////VERTICAL MOVEMENT///////////////////////////////
		if (keys[KeyEvent.VK_SPACE] && !man.getJump() && man.getGround()){
			man.setVy(-20);
			man.setJump(true);
			man.setGround(false);
			if (!playerAction.equals("jump")){
				playerAction = "jump";
				manFrame = 0;
			}
		}
		if (man.getJump()){
			man.jump();
			playerAction = "jump";
			if (manFrame < 1){
				manFrame += 0.5;
			}
			else{
				manFrame = 0;		
			}
		}
		else{
			man.fall();
			if (!man.getGround()){
				playerAction = "fall";
				manFrame = 0;
				if (manFrame < 1){
					manFrame += 0.5;
				}
				else{
					manFrame = 0;		
				}
			}
			
		}
		//////////////////////////////////////////////////////////////////////////////
	}
	//////////////////////////////FIREBALLS///////////////////////////////////////
	public void loadFireballs(){
		fireballs = new ArrayList<Fireball>();
		for (int i = 0; i < 500; i++){
			for (int j = 0; j < 20; j++){
				if (getPixelCol(fireMask, i * 50, j * 50) == red && i != 1){ //cannot be 2 because on the mask that space is the base colour (we dont want a coin being created there)
					///fireballs.add(new Fireball(i * 50 - 25, j * 50)); //wherever there is a bronze space on the mask, create a coin object there
					fireballs.add(new Fireball(i * 50 - 25, 900, 1000 - j * 50));
				}
			}
		}
	}
	
	public void jumpFireballs(){
		for (int i = 0; i < fireballs.size(); i++){
			fireballs.get(i).refresh();
			if (!fireballs.get(i).getRest()){
				fireballs.get(i).jump();
			}
			if (fireballs.get(i).getY() >= 950){
				fireballs.get(i).reset();
			}
		}
	}
	
	public void checkFireballs(){
		for(int i = 0; i < fireballs.size(); i++){
			fireRect = new Rectangle(fireballs.get(i).getX() - man.getX(), fireballs.get(i).getY(), 50, 50);
			if(fireRect.intersects(playerRect)){
				man.takeDamage(1);
			}
		}
	}
	
	public void drawFireball(Graphics2D g2,int x,int y){
		AffineTransform saveXform = g2.getTransform();
		AffineTransform at = new AffineTransform();
		int w = fireball.getWidth(this);
		int h = fireball.getHeight(this);
		at.rotate(fireAng, x + w / 2, y + h / 2);
		g2.transform(at);
		g2.drawImage(fireball, x, y, this);
		g2.setTransform(saveXform);		
	}
	///////////////////////////////////////////CANNONS///////////////////////////////////
	public void loadCannons(){
		for(int i = 0; i < 500; i++){
			for(int j = 0; j < 20; j++){
				if(getPixelCol(fireMask, i * 50, j * 50) == black && i != 1){
					cannons.add(new Cannon(i * 50, j * 50, 1000 - j * 50));
				}
			}
		}
	}
	//////////////////////////////////////////////////////////////////////////////
	public void moveLava(){
		if (lavaX >= 0){
			lavaX = -1900; //move both pictures to the front again (so there is a constant flow of lava)
		}
		else{
			lavaX += 0.4; //speed of lava
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////
	public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
		
	public void renderEnemies(){
		for(int i = 0; i < enemies.size(); i++){
			Enemy enemy = enemies.get(i);
			int d = Math.abs(man.getX() - enemy.getX());
			if(d < 800 && Math.abs(man.getYPos() - enemy.getY()) < 150){
				if(d < 250){
					enemy.resetTime();
					//attack
					if(enemy.attack(man)){
						man.takeDamage(20);
					}
				}
			}
    		if(enemy.getHealth() <= 0){
    			enemies.remove(enemy);
    		}
    		enemy.addTime();
		}
	}
	public void moveEnemy(Enemy enemy){
		enemy.move();
	}
	
	public void shootCannons(){
		if(time >= cooldown){
			for(int i = 0; i < cannons.size(); i++){
				Cannon cannon = cannons.get(i);
				eArrows.add(cannon.shoot(man));
				time = 0;
			}
		}
		for(int i = 0; i < eArrows.size(); i++){
			Arrow arrow = eArrows.get(i);
			int d = Math.abs(man.getX() - arrow.getX());
			if(d > 4000){	
				eArrows.remove(arrow);
				i++;
			}
			arrow.move();
			for(int j = 0; j < blocks.size(); j++){
				Block block = blocks.get(j);
				if(arrow.getRect(man).intersects(block.getRect())){
					eArrows.remove(arrow);
					i++;
				}
			}
			if(arrow.getRealRect(man).intersects(RealRect)){
				eArrows.remove(arrow);
				man.takeDamage(arrow.getDamage());
				i++;
			}
		}
		time++;
	}
	
	public void refresh(){
		playerRect = new Rectangle(man.getXPos(), man.getYPos(), 50, 100);
		healthRect = new Rectangle(650, 30, (int)((double) man.getHealth()/100 * 600), 50);
		RealRect = new Rectangle(man.getX(), man.getYPos(), 50, 100);
		move();
		moveLava();
		checkHearts();
		jumpFireballs();
		checkFireballs();
		fireAng += 0.1;
		checkCoin();
		checkStarCoin();
		coinFrameIncrease();
		starCoinFrameIncrease();
		renderEnemies();
		shootCannons();
		if(man.getHealth() <= 0){
			System.out.println("ded");
		}
	}
		
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g.drawImage(test, -man.getX(), 0, this);
		for (int i = 0; i < fireballs.size(); i++){
			drawFireball(g2, fireballs.get(i).getX() - man.getX(), fireballs.get(i).getY());
		}
		for (int i = 0; i < 15; i++){ //blitting lava across the entire map
			g.drawImage(lava, (int) lavaX - man.getX() + 1900 * i, 900, this);
		}
		g.drawImage(map1, -man.getX(), 0, this);
		g.setColor(new Color(0, 0, 0));
		//g2.fill(playerRect);
		for (int i = 0; i < 3; i++){
			g.drawImage(starCoinBW, 1460 + ((i + 1) * 110), 10 , this); //black and white images of coins (meaning they havent been picked up yet)
		}
		for (int i = 0; i < starCoinsPicked.size(); i++){
			g.drawImage(starCoinSmall, 1460 + ((starCoinsPicked.get(i).getNum() + 1) * 110), 10 , this); //when the coins have been picked up (displays which one you have picked up)
		}
		for (int i = 0; i < allCoins.size(); i++){
			g.drawImage(coinImages[allCoins.get(i).getFrame()], allCoins.get(i).getX() - man.getX(), allCoins.get(i).getNewY(), this); //displays all regular coins
		}
		for (int i = 0; i < allStarCoins.size(); i++){
			g.drawImage(starCoinImages[allStarCoins.get(i).getFrame()], allStarCoins.get(i).getX() - man.getX(), allStarCoins.get(i).getNewY(), this); //displays all star coins (that have not been picked up yet)
		}
		for (int i = 0; i < hearts.size(); i++){
			g.drawImage(heart, hearts.get(i).getX() - man.getX(), hearts.get(i).getY(), this); //displays all star coins (that have not been picked up yet)
		}
		for(int i = 0; i < eArrows.size(); i++){
    		Arrow arrow = eArrows.get(i);
    		g.drawImage(bullet, arrow.getX() - man.getX(), arrow.getY(), this);
    	}
    	for(int i = 0; i < cannons.size(); i++){
    		Cannon cannon = cannons.get(i);
    		g.drawImage(cannonPic, cannon.getX() - man.getX(), cannon.getY(), this);
    	}
    	
    
	
    	if (playerAction.equals("stand") && playerDirection.equals("right")){
    		g.drawImage(manImages[8 + (int) manFrame], man.getXPos(), man.getYPos() + 19, this);
    	}
    	else if (playerAction.equals("stand") && playerDirection.equals("left")){
    		int w = manImages[8 + (int) manFrame].getWidth(this);
			int h = manImages[8 + (int) manFrame].getHeight(this);
    		g.drawImage(manImages[8 + (int) manFrame], man.getXPos() + 50, man.getYPos() + 19, - w, h, this);
    	}
    	else if (playerAction.equals("run") && playerDirection.equals("right")){
    		g.drawImage(manImages[(int) manFrame], man.getXPos() - 10, man.getYPos() + 25, this);
    	}
    	else if (playerAction.equals("run") && playerDirection.equals("left")){
    		int w = manImages[(int) manFrame].getWidth(this);
			int h = manImages[(int) manFrame].getHeight(this);
    		g.drawImage(manImages[(int) manFrame], man.getXPos() + 50, man.getYPos() + 19, - w, h, this);
    	}
    	else if ((playerAction.equals("jump") || playerAction.equals("fall")) && playerDirection.equals("right")){
    		g.drawImage(manImages[34 + (int) manFrame], man.getXPos(), man.getYPos(), this);
    	}
    	else if ((playerAction.equals("jump") || playerAction.equals("fall")) && playerDirection.equals("left")){
    		int w = manImages[34 + (int) manFrame].getWidth(this);
			int h = manImages[34 + (int) manFrame].getHeight(this);
    		g.drawImage(manImages[34 + (int) manFrame], man.getXPos() + 50, man.getYPos() + 19, - w, h, this);
    	}
		//////////////////////////////////////HEALTH BAR/////////////////////////////////////
		g.setColor(new Color(255, 255, 255));
		g2.fillRect(645, 25, 610, 60);
		g.setColor(new Color(255, 0, 0));
		g2.fill(healthRect);
		g.setColor(new Color (0, 0, 0));
		/////////////////////////////////////HEALTH NUM//////////////////////////////
		String s = "" + man.getHealth(); //add "" so it creates the string Integer.parseInt() and .toString() did not work)
    	g.setFont(new Font("Arial", Font.PLAIN, 50));
		g.drawString(s, screenX / 2 - g.getFontMetrics().stringWidth(s) / 2, 73); //drawing an centering text
	}
}