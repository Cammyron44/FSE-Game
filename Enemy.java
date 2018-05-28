//Enemy
public class Enemy{
	int x, y, sx, sy, hp = 100, maxHP = 100;
	public Enemy(int x, int y, int sx, int sy){
		this.x = x;
		this.y = y;
		this.sx = sx;
		this.sy = sy;
	}
	public int getSY(){
		return sy;
	}
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return y;
	}
	public void takeDamage(int damage){
		hp -= damage;
	}
	public int getHP(){
		return hp;
	}
	public int getMaxHP(){
		return maxHP;
	}
	public void chase(Player player){
		if(player.getPX() > x){
			x += 2;
		}
		if(player.getPX() < x){
			x -= 2;
		}
	}
}