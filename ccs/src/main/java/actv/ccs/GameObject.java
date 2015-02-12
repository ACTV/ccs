package actv.ccs;

import java.awt.*;
import java.awt.geom.AffineTransform;

abstract class GameObject implements IDrawable
{	
	// declare variables
	private double xLoc; // x location of object
	private double yLoc; // y location of object
	private Color color;	  
	private int width = 0;
	private int height = 0;
	private boolean flag;
	private AffineTransform myTranslate, myRotate, myScale;
	public GameObject(){
	}
	public GameObject(double x, double y, int w, int h, int r, int g, int b)
	{
		myTranslate = new AffineTransform();
		myRotate = new AffineTransform();
		myScale = new AffineTransform();
		xLoc = x;
		yLoc = y;
		width = w;
		height = h;
		color = new Color(r, g, b);
		flag = false;
	}
	// affine transforms
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
	public void setFlag(boolean b)
	{
		flag = b;
	}
	public boolean getFlag()
	{
		return flag;
	}
	public void setY(double Y)
	{
		yLoc = Y;
	}
	public double getY()
	{
		return yLoc;
	}
	public void setX(double X)
	{
		xLoc = X;
	}
	public double getX()
	{
		return xLoc;
	}
	public AffineTransform getRotate()
	{
		return myRotate;
	}
	public AffineTransform getScale()
	{
		return myScale;
	}
	public AffineTransform getTranslate()
	{
		return myTranslate;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public Color getColor()
	{
		return color;
	}
	public void setColor (Color newColor)
	{
		this.color = newColor;
	}
	
}