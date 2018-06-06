/**
 * @(#)Coin.java
 *
 *
 * @author 
 * @version 1.00 2018/6/1
 */


public class Coin {
	private int x, y, newY;
	private double frame;
	private boolean picked;
	
    public Coin(int xx, int yy) {
    	x = xx;
    	y = yy;
    	newY = yy;
    	frame = 0;
    	picked = false;
    }
    
    public void addNewY(){
    	newY -= 2;
    }
    
    public void setPickedTrue(){
    	picked = true;
    }
    
    public void increaseFrame(){
    	if (frame > 9){
			frame = 0;
		}
		else{
			frame += 0.1;
		}
    }
    
    public void speedFrame(){
   		if (frame > 9){
			frame = 0;
		}
		else{
			frame += 0.6;
		}
    }
    
    public int getFrame(){
    	return (int) frame;
    }
    
    public boolean getPicked(){
    	return picked;
    }
    
    public int getNewY(){
    	return newY;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
}