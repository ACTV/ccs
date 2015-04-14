package actv.ccs.sageTest;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import sage.scene.TriMesh;

public class Pot extends TriMesh {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private double xLoc,yLoc; // location stuff test
	private double startX, startY;
	private int type;
	private float length;
	private float width;
	private float height;
	private String name;

	private static float[] vrts = new float[] {0,1,0,-1,-1,1,1,-1,1,1,-1,-1,-1,-1,-1};
	private static float[] cl = new float[] {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,0,1,1};
	private static int[] triangles = new int[] {0,1,2,0,2,3,0,3,4,0,4,1,1,4,2,4,3,2};
	public Pot()
	{
		super();
		 FloatBuffer vertBuf =
		 com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
		 FloatBuffer colorBuf =
		 com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
		 IntBuffer triangleBuf =
		 com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		 this.setVertexBuffer(vertBuf);
		 this.setColorBuffer(colorBuf);
		 this.setIndexBuffer(triangleBuf); 
	} 

	public int getType()
	{
		return type;
	}
	public void setType(int t)
	{
		this.type = t;
	}
	public float getWidth()
	{
		return width;
	}
	public void setWidth(float w)
	{
		this.width = w;
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
	public String getName()
	{
		return name;
	}
	public void setName(String s)
	{
		this.name = s;
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


	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
		
	}


	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}


	public void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		this.pcs.firePropertyChange(propertyName, oldValue, newValue); 
		
	}


}
