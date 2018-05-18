//Player
import java.util.*;
class Player {
	private int x = 950, y = 800, px = 950, py = 800, direction, minDamage, maxDamage, cooldown;
	private String type = "";
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
	public String setClass(String t){
		type = t;
		if(type.equals("warrior")){
			minDamage = 10;
			maxDamage = 50;
		}
	}
	public int heal(int n){
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
	