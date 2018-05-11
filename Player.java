//Player
class Player {
	private int x = 950, y = 800, px = 950, py = 800;
	public Player(){
		
	}
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
	public void move(int n){
		px += n;
	}
}
	