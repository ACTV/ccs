package actv.ccs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

// goddamn fish why won't you show up?!


public class Cichlid extends Movable implements IDrawable, IMovable {
	 private GameWorld gw;
	 private AffineTransform myTranslate, myRotate, myScale;
	 

	 public Cichlid(double x, double y, int w, int h, int r, int g, int b, int speed, int direction,GameWorld GW)
	 {
		 super(x, y, w, h, r, g, b, speed, direction);
		 gw = GW;
		 myTranslate = new AffineTransform();
		 myTranslate.translate(x, y);
		 myScale = new AffineTransform();
		 myRotate = new AffineTransform();
	 }
	// new AffineTransforms
		 public void rotate (double degrees)
		 {
			 myRotate.rotate(Math.toRadians(degrees));
		 }
		 public void translate (double dx, double dy)
		 {
			 myTranslate.translate(dx, dy);
		 }
		 public void scale (double sx, double sy)
		 {
			 myScale.scale(sx, sy);
		 }
		 public void setDirection(int dir)
		 {
			 this.direction = dir;
		 }
		 public void draw(Graphics2D g2d) // draw object
		 {	
			 g2d.setColor(Color.blue);
			 AffineTransform at = g2d.getTransform();
			 g2d.transform(myScale);
			 g2d.transform(myTranslate);
			 g2d.transform(this.getRotate());
			 g2d.drawRect(0, 0, 300, 300);
			 g2d.setTransform(at);
		 }
		 
		public int getWidth()
		{
			return 300;
		}
		public int getHeight()
		{
			return 300;
		}
		public void move(double time) // time in miliseconds
		{	
			 double deltaX, deltaY; // add to new Location later
		 double timeDist = time; // get the elapsed time
		 double spd = getSpeed(); // get the current speed
		 double dist = spd * timeDist; // find the distance from speed * time
		 
		 deltaX = (Math.cos(Math.toRadians(0 - getDirection() ))*dist); // fill delta X and y
			 deltaY = (Math.sin(Math.toRadians(0 - getDirection() ))*dist);
			  myTranslate.translate( deltaX, deltaY);
			  this.setX(myTranslate.getTranslateX() );
			  this.setY(myTranslate.getTranslateY() );
		}
}
