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
	private boolean drawn = false;
	private double bx, fx;
	private boolean []keys;
	ArrayList<Block>blocks = new ArrayList<Block>();
	
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
			if(bx < 0){
				bx += 0.5;
			}
			if(fx < 0){
				fx += 6;
				for(int i = 0; i < blocks.size(); i++){
					blocks.get(i).changeX(6);
				}
			}
		}
		if(keys[KeyEvent.VK_RIGHT] ){
			if(bx > -1000){
				bx -= 0.5;
			}
			if(fx > -12000){
				fx -= 6;
				for(int i = 0; i < blocks.size(); i++){
					blocks.get(i).changeX(-6);
				}
			}
		}
		if(keys[KeyEvent.VK_SPACE]){
		
		}
    }
    public void drawMap(){
	    for(int i = 0; i < 50; i++){
			blocks.add(new Block(100 * i, 1000, 500, 850));
		}
		for(int i = 0; i < 10; i++){
			blocks.add(new Block(500 + 200 * i, 1000, 500, 800 - 100 * i));
		}
		
		drawn = true;
    }
    public void paintComponent(Graphics g){
    	g.drawImage(background, (int)bx, 0, this);
    	g.drawImage(foreground, (int)fx + 10000, 0, this);
    	g.drawImage(foreground, (int)fx, 0, this);
    	g.setColor(new Color(255, 0, 0));
    	g.fillRect(player.getX(), player.getY(), 50, 50);
    	if(drawn == false){
    		drawMap();
    	}
    	for(int i = 0; i < blocks.size(); i++){
    		Block block = blocks.get(i);
    		int d = block.getX() - 950;
    		if(block.getX() > player.getX() - 1200 && block.getX() < player.getX() + 1200){
    			g.setColor(new Color(0, 0, 255));
    			g.fillRect(950 + d, block.getSY(), 100, 50);
    		}
    	}
    }
}