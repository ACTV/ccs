package actv.ccs.model;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import actv.ccs.model.type.FishState;
import actv.ccs.sageTest.PropertyChangeSupportZ;
import sage.scene.TriMesh;

public class ConvictCichlid extends TriMesh implements PropertyChangeSupportZ,
		CCSMemoryObject {

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private Point3D location;
	private Vector3D direction;
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

	private static float[] vrts = new float[] { 0, 1, 0, -1, -1, 1, 1, -1, 1,
			1, -1, -1, -1, -1, -1 };
	private static float[] cl = new float[] { 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1,
			1, 1, 1, 0, 1, 1, 0, 1, 1 };
	private static int[] triangles = new int[] { 0, 1, 2, 0, 2, 3, 0, 3, 4, 0,
			4, 1, 1, 4, 2, 4, 3, 2 };

	public ConvictCichlid() {
		super();
		state = FishState.NONE;
		int i;
		FloatBuffer vertBuf = com.jogamp.common.nio.Buffers
				.newDirectFloatBuffer(vrts);
		FloatBuffer colorBuf = com.jogamp.common.nio.Buffers
				.newDirectFloatBuffer(cl);
		IntBuffer triangleBuf = com.jogamp.common.nio.Buffers
				.newDirectIntBuffer(triangles);
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(triangleBuf);
	}

	public FishState getState() {
		return state;
	}

	public void setState(FishState state) {
		this.state = state;
	}

	public float getAggroLevel() {
		return aggroLevel;
	}

	public void setAggroLevel(float g) {
		this.aggroLevel = g;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float f) {
		this.length = f;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float h) {
		this.height = h;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float w) {
		this.weight = w;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name = s;
	}

	public String getGender() {
		return gender;
	}

	public void setDirection(Vector3D direction) {
		this.direction = direction;
	}

	public void setGender(String s) {
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

	public void setLocation(Point3D location){
		this.location = location;
	}
	
	public void setlocation(double x, double y, double z){
		this.location = new Point3D(x, y, z);
	}
	
	public Point3D getLocation(){
		return this.location;
	}

	public Vector3D getDirection(){ // accessors and mutators
		return direction;
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
