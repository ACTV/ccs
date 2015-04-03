package actv.ccs.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import actv.ccs.model.type.FishState;

public class ConvictCichlid extends PropertyChangeSupport implements CCSMemoryObject, IDrawable, IMovable, ICollider {
	private float [] location;
	private double xLoc,yLoc; // location stuff test
	private double startX, startY;
	private int direction; 
	private FishState state;
	private float aggroLevel;
	private float baseAggroLevel;
	private float cautionLevel;
	private float baseCautionLevel;
	private float length;
	private float height;
	private float weight;
	private String name;
	private int cichlidID;
	private float speed;
	private float baseSpeed;
	private long idleWaitTime;
	private String gender;
	private AffineTransform myTranslate, myRotate, myScale;
	private Color color;

	public ConvictCichlid()
	{
		super(ConvictCichlid.class);
		state = FishState.NONE;
		this.setX(this.getStartX());
		this.setY(this.getStartY());
		myTranslate = new AffineTransform();
		myTranslate.translate(this.getX(), this.getY());
		myScale = new AffineTransform();
		myRotate = new AffineTransform();
		

	}
	
	public float[] getLocation() {
		if(location == null){
			location = new float[2];
			location[0] = location[1] = 0;
		}
		return location;
	}
	public void setLocation(float[] location) {
		this.location = location;
	}
	public FishState getState() {
		return state;
	}
	public void setState(FishState state) {
		this.state = state;
	}
	public float getAggroLevel()
	{
		return aggroLevel;
	}
	public void setAggroLevel(float g)
	{
		this.aggroLevel = g;
	}
	public float getLength()
	{
		return length;
	}
	public void setLength(float f)
	{
		this.length = f;
	}
	public float getHeight()
	{
		return height;
	}
	public void setHeight(float h)
	{
		this.height = h;
	}
	public float getWeight()
	{
		return weight;
	}
	public void setWeight(float w)
	{
		this.weight = w;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String s)
	{
		this.name = s;
	}
	public String getGender()
	{
		return gender;
	}
	public void setGender(String s)
	{
		this.gender = s;
	}
	
	public float getCautionLevel() {
		return cautionLevel;
	}

	public void setCautionLevel(float cautionLevel) {
		this.cautionLevel = cautionLevel;
	}

	public int getCichlidID() {
		return cichlidID;
	}

	public void setCichlidID(int cichlidID) {
		this.cichlidID = cichlidID;
	}

	public float getBaseCautionLevel() {
		return baseCautionLevel;
	}

	public void setBaseCautionLevel(float baseCautionLevel) {
		this.baseCautionLevel = baseCautionLevel;
	}

	public float getBaseAggroLevel() {
		return baseAggroLevel;
	}

	public void setBaseAggroLevel(float baseAggroLevel) {
		this.baseAggroLevel = baseAggroLevel;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getBaseSpeed() {
		return baseSpeed;
	}

	public void setBaseSpeed(float baseSpeed) {
		this.baseSpeed = baseSpeed;
	}

	public long getIdleWaitTime() {
		return idleWaitTime;
	}

	public void setIdleWaitTime(long idleWaitTime) {
		this.idleWaitTime = idleWaitTime;
	}
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
		 g2d.setColor(getColor());
		 AffineTransform at = g2d.getTransform();
		 g2d.transform(myScale);
		 g2d.transform(myTranslate);
		 g2d.transform(this.getRotate());
		 g2d.fillOval((int) this.getStartX(), (int) this.getStartY(), (int) this.getLength() + 100, (int) this.getHeight() +  100);
		 g2d.setTransform(at);

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
	public void move(double time)
	{
	 double deltaX, deltaY; // add to new Location later
	 double timeDist = time; // get the elapsed time
	 double spd = getBaseSpeed(); // get the current speed
	 double dist = spd * timeDist; // find the distance from speed * time
	 System.out.println("starting x: " +  this.getX());
	 System.out.println("starting y: " +  this.getY());
	 
	 deltaX = (Math.cos(Math.toRadians(this.getX() - getDirection() ))*dist); // fill delta X and y
		 deltaY = (Math.sin(Math.toRadians(this.getY() - getDirection() ))*dist);
		  myTranslate.translate( deltaX, deltaY);
		  this.setX(myTranslate.getTranslateX() );
		  this.setY(myTranslate.getTranslateY() );
		  
		  System.out.println("new x: " + this.getX());
		  System.out.println("new y: " + this.getY());
		  
		  if ( (this.getX() > 900) || this.getY() < -150 || (this.getX() < 0) || (this.getY() > 430) )
		  {
			  System.out.println("BOUNDS HAVE BEEN MET. PREPARE FOR YOUR DOOM!");
			  this.reverse();
			  // then set the state to idle
		  }
		  
	}
	 public int getDirection() // accessors and mutators
	 {
		 return direction;
	 }
	 public Color getColor()
	 {
		 return color;
	 }
	 public void setColor(Color c)
	 {
		 this.color = c;
	 }
	public void setStartY(double Y)
	{
		startY = Y;
	}
	public double getStartY()
	{
		return startY;
	}
	public void setStartX(double X)
	{
		startX = X;
	}
	public double getStartX()
	{
		return startX;
	}
	public void reverse()
	{
		this.setDirection(-direction);
		this.setBaseSpeed(-baseSpeed);
	}
	public boolean collidesWith(ICollider o)
	{
		boolean result = false; // create boolean
	
		// get both object centers
		int fObjCenterX  = (int) ((int) this.getX() + (getLength()/2));
		int fObjCenterY = (int) ((int) this.getY() + (getHeight()/2)); 
		
		int sObjCenterX = (int) ((int) ((ConvictCichlid) o).getX() + ((ConvictCichlid) o).getLength()/2); 
		int sObjCenterY = (int) ((int) ((ConvictCichlid) o).getY() + ((ConvictCichlid) o).getHeight()/2);
		
		// get distance between o1bjects (x,y)
		int dx = fObjCenterX - sObjCenterX;
		int dy = fObjCenterY - sObjCenterY;
		int dist = (dx*dx + dy*dy); // get distance
		
		// find square of radii
		int fObjRadius = (int) (getLength()/2);
		int sObjRadius = (int) (((ConvictCichlid) o).getLength()/2);
		int radSquared = ( fObjRadius*fObjRadius + 2*fObjRadius*sObjRadius+ sObjRadius*sObjRadius); // a^2 + 2ab + b^2
		if (dist <= radSquared)
		{
			result = true;
		}
		return result;
	}
	public void handleCollision(ICollider otherObject)
	{
	
	if (otherObject instanceof ConvictCichlid) 
	{
		((ConvictCichlid) otherObject).setSpeed(0);
		this.setSpeed(0);
		System.out.println("CICHLID COLLISSION! cichlid!!!");
		}
	}
}
