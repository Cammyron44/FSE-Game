//Player
import java.util.*;
class Player {
	private int x = 950, y = 800, px = 950, py = 800, direction, minDamage, maxDamage;
	private String type = "archer";
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
	}
	public String getType(){
		return type;
	}
	public int getCooldown(){
		if(type.equals("warrior")){
			return 50;
		}
		if(type.equals("assassin")){
			return 20;
		}
		if(type.equals("archer")){
			return 20;
		}
		return 100;
	}
	public int getRange(){
		if(type.equals("warrior")){
			return 300;
		}
		if(type.equals("assassin")){
			return 200;
		}
		return 100;
	}
	public void heal(int n){
		if(hp + n > 100){
			hp = 100;
		}
		else{
			hp += n;
		}
	}
	public Arrow shoot(int damage){
		return new Arrow(damage, direction, x, y);
	}
	
	public int damage(){
		Random random = new Random();
		int d = 0;
		if(type.equals("warrior")){
			minDamage = 40;
			maxDamage = 80;
		}
		if(type.equals("assassin")){
			minDamage = 10;
			maxDamage = 100;
		}
		if(type.equals("archer")){
			minDamage = 20;
			maxDamage = 40;
		}
		while(d < minDamage){
			d = random.nextInt(maxDamage);
		}
		System.out.println(d);
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
	