//Arrow

class Arrow{
	private int damage, direction, x, y;
	public Arrow(int damage, int direction, int x, int y){
		this.damage = damage;
		this.direction = direction;
		this.x = x;
		this.y = y;
	}
	public void move(){
		if(direction == 0){
			x -= 5;
		}
		if(direction == 1){
			x += 5;
		}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}