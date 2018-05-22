//Player
import java.util.*;
class Player {
	private int x = 950, y = 800, px = 950, py = 800, direction, minDamage, maxDamage;
	private String type = "warrior";
	private boolean hit = false;
	private int hp = 100;
	public Player(){}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getPX(){
		return px;
	}
	public int getPY(){
		return py;
	}
	public int getDirection(){
		return direction;
	}
	public void setClass(String t){
		type = t;
		if(type.equals("warrior")){
			minDamage = 40;
			maxDamage = 80;
		}
		if(type.equals("assassin")){
			minDamage = 10;
			maxDamage = 100;
		}
	}
	public int getCooldown(){
		if(type.equals("warrior")){
			return 100;
		}
		if(type.equals("assassin")){
			return 50;
		}
		return 1000;
	}
	public void heal(int n){
		if(hp + n > 100){
			hp = 100;
		}
		else{
			hp += n;
		}
	}
	
	public int damage(){
		Random random = new Random();
		int d = 0;
		while(d < minDamage){
			d = random.nextInt(maxDamage);
		}
		return d;
	}
	
	public void changeX(int n){
		x += n;
	}
	public void move(int n){
		px += n;
	}
	public void setDirection(int d){
		direction = d;
	}
}
	