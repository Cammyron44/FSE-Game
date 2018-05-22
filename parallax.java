//test for parallax scrolling
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.*;

public class parallax extends JFrame implements ActionListener,KeyListener{
	javax.swing.Timer myTimer;
	GamePanel game; 

    public parallax() {
		super("parallax");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1900, 1000);

		myTimer = new javax.swing.Timer(20, this);
		myTimer.start();

		game = new GamePanel();
		add(game);
		addKeyListener(this);
		setResizable(false);
		setVisible(true);
		
    }
    
	public void actionPerformed(ActionEvent evt){
		if(game != null){
			game.refresh();
			game.repaint();
		}
	}

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
    	game.setKey(e.getKeyCode(),true);
    }

    public void keyReleased(KeyEvent e) {
    	game.setKey(e.getKeyCode(),false);
    }

    public static void main(String[] arguments) {
		parallax frame = new parallax();
    }
}

class GamePanel extends JPanel{
	private Image background, foreground;
	private Player player;
	private boolean drawn = false, attack = false;
	private double bx, fx;
	private int cooldown, time = 0;
	private boolean []keys;
	ArrayList<Block>blocks = new ArrayList<Block>();
	ArrayList<Block>sBlocks = new ArrayList<Block>();
	ArrayList<Enemy>enemies = new ArrayList<Enemy>();
	ArrayList<Enemy>sEnemies = new ArrayList<Enemy>();
	
	public GamePanel(){
		
		player = new Player();
		keys = new boolean[KeyEvent.KEY_LAST+1];
		background = new ImageIcon("Images/background.jpg").getImage();
		foreground = new ImageIcon("Images/foreground.png").getImage();
		
		background = background.getScaledInstance(5000, 1000, background.SCALE_SMOOTH);
		foreground = foreground.getScaledInstance(10000, 1000, foreground.SCALE_SMOOTH);
		
		setSize(1900, 1000);
	}
	
	public void setKey(int k, boolean v) {
    	keys[k] = v;
    }
    
    public void refresh(){
    	if(keys[KeyEvent.VK_LEFT] ){
    		player.setDirection(0);
			if(fx == -12000 && player.getX() > 950){
				player.changeX(-6);
			}
			else{
				if(bx < 0){
					bx += 0.5;
				}
				if(fx < 0){
					fx += 6;
					player.move(-6);
				}
				if(fx == 0){
					if(player.getX() > 50){
						player.changeX(-6);	
					}
				}	
			}
    	}
		if(keys[KeyEvent.VK_RIGHT] ){
			player.setDirection(1);
			if(fx == 0 && player.getX() < 950){
				player.changeX(6);	
			}
			else{
				if(bx > -1000){
					bx -= 0.5;
				}
				if(fx > -12000){
					fx -= 6;
					player.move(6);
				}
				if(fx == -12000){
					if(player.getX() < 1850){
						player.changeX(6);	
					}
				}	
			}
		}
		if(keys[KeyEvent.VK_SPACE]){
			if(time < 0){
				attack();
				time = cooldown;
			}
		}
		if(drawn == false){
    		drawMap();
    	}
		for(int i = 0; i < blocks.size(); i++){
    		Block block = blocks.get(i);
    		if(block.getX() > player.getPX() - 1200 && block.getX() < player.getPX() + 1200){
    			sBlocks.add(block);
    			blocks.remove(block);
    		}
		}
		for(int i = 0; i < enemies.size(); i++){
			Enemy enemy = enemies.get(i);
			if(enemy.getX() > player.getPX() - 1200 && enemy.getX() < player.getPX() + 1200){
				sEnemies.add(enemy);
				enemies.remove(enemy);
			}
		}
		for(int i = 0; i < sBlocks.size(); i++){
			Block block = sBlocks.get(i);
    		if(block.getX() < player.getPX() - 1200 || block.getX() > player.getPX() + 1200){
    			blocks.add(block);
    			sBlocks.remove(block);
    		}
		}
		for(int i = 0; i < sEnemies.size(); i++){
			Enemy enemy = sEnemies.get(i);
			int d = Math.abs(player.getPX() - enemy.getX());
			if(d < 850){
				enemy.chase(player);
			}
    		if(enemy.getX() > player.getPX() + 1200 || enemy.getX() < player.getPX() - 1200){
    			enemies.add(enemy);
    			sEnemies.remove(enemy);
    		}
		}
		time -= 1;
		
    }
    public void attack(){
    	for(Enemy enemy : sEnemies){
    		if(player.getDirection() == 0){
    			if(player.getX() - enemy.getX() > 0 && player.getX() - enemy.getX() < 300 && enemy.getY() == player.getY()){
    				enemy.takeDamage(player.damage());
    				System.out.println("hit");
    			}
    		}
    		if(player.getDirection() == 1){
    			if(player.getX() - enemy.getX() < 0 && player.getX() - enemy.getX() > -300 && enemy.getY() == player.getY()){
    				enemy.takeDamage(player.damage());
    				System.out.println("hit");
    			}
    		}
    	}
    	System.out.println("attack");
    }
    
    public void drawMap(){
	    for(int i = 0; i < 50; i++){
			blocks.add(new Block(100 * i, 1000, 850));
		}
		for(int i = 0; i < 10; i++){
			blocks.add(new Block(1000 + 200 * i, 1000, 800 - 100 * i));
		}
		for(int i = 0; i < 3; i++){
			enemies.add(new Enemy(2000 + 2000 * i, 1000, 1000, player.getY()));	
		}
		
		drawn = true;
		
    }
    
    public void paintComponent(Graphics g){
    	
    	g.drawImage(background, (int)bx, 0, this);
    	g.drawImage(foreground, (int)fx + 10000, 0, this);
    	g.drawImage(foreground, (int)fx, 0, this);
    	
    	for(int i = 0; i < sBlocks.size(); i++){
    		Block block = sBlocks.get(i);
    		g.setColor(new Color(0, 0, 255));
    		g.fillRect(block.getX() - player.getPX() + 950, block.getSY(), 100, 50);
    	}
    	
    	for(int i = 0; i < sEnemies.size(); i++){
    		Enemy enemy = sEnemies.get(i);
    		g.setColor(new Color(0, 255, 0));
    		g.fillRect(enemy.getX() - player.getPX() + 950, enemy.getSY(), 100, 50);
    	}
    	
    	g.setColor(new Color(255, 0, 0));
    	g.fillRect(player.getX(), player.getY(), 50, 50);
    	
    }
}