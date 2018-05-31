//Arrow

class Arrow{
	private int damage, direction, x, y, width = 30, height = 6;
	public Arrow(int damage, int direction, int x, int y){
		this.damage = damage;
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	public void move(){
		if(direction == 0){
			x -= 15;
		}
		if(direction == 1){
			x += 15;
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getDamage(){
		return damage;
	}
	public boolean collide(Enemy enemy, Player player){
		int ex = enemy.getX() - player.getPX() + 950;
		int ax = x - player.getPX() + 950;
		System.out.println(ax + "     ");
		System.out.println(ex + "     ");
		if(enemy.getSY() >= y && enemy.getSY() - enemy.getHeight() <= y){
			if(direction == 0){
				if(ex > ax && ex - enemy.getWidth() < ax){
					return true;
				}
			}
			if(direction == 1){
				if(ex < ax && ex + enemy.getWidth() > ax){
					return true;
				}
			}
		}
		return false;
	}
}