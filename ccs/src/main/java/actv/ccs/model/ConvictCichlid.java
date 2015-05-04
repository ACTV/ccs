package actv.ccs.model;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import actv.ccs.model.type.FishState;

public class ConvictCichlid extends TankObject {
	private Vector3D direction;
	private FishState state;
	private float aggroLevel;
	private float baseAggroLevel;
	private float cautionLevel;
	private float baseCautionLevel;
	private int cichlidID;
	private float speed;
	private float baseSpeed;
	private long idleWaitTime;
	private String gender;
	private double influence = -1;
	private float weight;
	
	private static float[] vrts = new float[] { 0, 1, 0, -1, -1, 1, 1, -1, 1,
			1, -1, -1, -1, -1, -1 };
	private static float[] cl = new float[] { 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1,
			1, 1, 1, 0, 1, 1, 0, 1, 1 };
	private static int[] triangles = new int[] { 0, 1, 2, 0, 2, 3, 0, 3, 4, 0,
			4, 1, 1, 4, 2, 4, 3, 2 };

	/**
	 * 
	 * @param length
	 * @param width
	 * @param height
	 * @param name
	 * @param location
	 */
	public ConvictCichlid(float length, float width, float height, String name, Point3D location) {
		super(length, width, height, name, location);
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
		
		if(influence == -1)
			setInfluence(length * 2);
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
		return super.getLength();
	}

	public void setLength(float length) {
		super.setLength(length);
	}

	public float getHeight() {
		return super.getHeight();
	}

	public void setHeight(float height) {
		super.setHeight(height);
	}

	public float getWeight() {
		return this.weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getName() {
		return super.getName();
	}

	public void setName(String name) {
		super.setName(name);
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
		super.setLocation(location);
	}
	
	public void setlocation(double x, double y, double z){
		super.setLocation(new Point3D(x, y, z));
	}
	
	public Point3D getLocation(){
		return super.getLocation();
	}

	public Vector3D getDirection(){
		return direction;
	}

	public double getInfluence() {
		return influence;
	}

	public void setInfluence(double influence) {
		this.influence = influence;
	}

}
