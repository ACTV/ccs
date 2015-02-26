package actv.ccs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Tree extends LandScaped implements ICollider, IDrawable
{	
	private int diameter;
	private boolean flag;
	private AffineTransform myTranslate, myRotate, myScale;

	public Tree(double x, double y, int w, int h, int r, int g, int b) 
	{
		super(x, y, w, h, r, g, b);
		diameter = w;
		myTranslate = new AffineTransform();
		myTranslate.translate(x - diameter/2, y - diameter/2);
		myRotate = new AffineTransform();
		myScale = new AffineTransform();
		// set new point for tree
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
	 // accessors and mutators
		public void setFlag(boolean b)
		{
			flag = b;
		}
		public boolean getFlag()
		{
			return flag;
		}
	public int getDiameter()
	{
		return diameter;
	}
	public int getWidth()
	{
		return diameter;
	}
	public int getHeight()
	{
		return diameter;
	}
	public void setDiameter(int d)
	{
		this.setDiameter(d);
	}
	public void draw(Graphics2D g2d)
	{
	
		g2d.setColor(Color.GREEN);
		AffineTransform at = g2d.getTransform();
		g2d.transform(myTranslate);
		 
		g2d.drawOval(0, 0, diameter/2, diameter/2);
		g2d.setTransform(at);
	}
	public boolean collidesWith(ICollider o)
	{
		boolean result = false; // create boolean

		// get both object centers
		int fObjCenterX  = (int) this.getX() + (getWidth()/2);
		int fObjCenterY = (int) this.getY() + (getHeight()/2); 

		int sObjCenterX = (int) ((GameObject) o).getX() + ((GameObject) o).getWidth()/2; 
		int sObjCenterY = (int) ((GameObject) o).getY() + ((GameObject) o).getHeight()/2;

		// get distance between o1bjects (x,y)
		int dx = fObjCenterX - sObjCenterX;
		int dy = fObjCenterY - sObjCenterY;
		int dist = (dx*dx + dy*dy); // get distance
		
		// find square of radii
		int fObjRadius = getWidth()/2;
		int sObjRadius = ((GameObject) o).getWidth()/2;
		int radSquared = ( fObjRadius*fObjRadius + 2*fObjRadius*sObjRadius+ sObjRadius*sObjRadius); // a^2 + 2ab + b^2
		if (dist <= radSquared)
		{
			result = true;
		}
		return result;
	}
	public void handleCollision(ICollider o)
	{
	 if (o instanceof Cichlid )// set tank speed at collision location to 0
	 {
		((Cichlid) o).setSpeed(0);
		System.out.println("cichlid object is stopping. will add rules later!");
	 }

	}

}