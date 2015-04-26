package actv.ccs.model;

import graphicslib3D.Point3D;
import sage.scene.TriMesh;

public abstract class TankObject extends TriMesh implements CCSMemoryObject {
	private float length;
	private float width;
	private float height;
	private Point3D location;

	public TankObject(float l, float w, float h, String name, Point3D location) {
		this.length = l;
		this.width = w;
		this.height = h;
		this.location = location;
		super.setName(name);
	}

	public float getLength() {
		return length;
	}

	public void setLength(float l) {
		this.length = l;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float w) {
		this.width = w;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float h) {
		this.height = h;
	}

	public String getName() {
		return super.getName();
	}

	public void setName(String name) {
		super.setName(name);
		
	}

	public Point3D getLocation() {
		return location;
	}

	public void setLocation(Point3D location) {
		this.location = location;
	}

}
