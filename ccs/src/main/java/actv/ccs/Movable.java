package actv.ccs;

public abstract class Movable extends GameObject // is part of GameObjects
{
	 // direction
	 protected int direction; // always even multiples of 5 degrees
	 private int speed;
	 int i;
	 public Movable(double x, double y, int w, int h, int r, int g, int b, int s, int d)  
	 {
		super(x, y, w, h, r, g, b);
		speed = s;
		direction = d;
	 }
	 public int getDirection() // accessors and mutators
	 {
		 return direction;
	 }
	 public void setDirection(int d)
	 {
		 direction = d;
	 }
	 public int getSpeed()
	 {
		 return speed;
	 }
	 public void setSpeed(int spd)
	 {
		 speed = spd;
	 }

	 
}
