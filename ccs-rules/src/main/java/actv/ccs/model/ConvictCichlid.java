package actv.ccs.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeSupport;

import actv.ccs.model.type.FishState;

public class ConvictCichlid extends PropertyChangeSupport implements CCSMemoryObject, IDrawable {
	private float [] location;
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
	
	/*
	public ConvictCichlid(float [] loc, FishState s, float aL, float bLA, float cL, float bCL, float len, float hei, float wei, String n, int id, float spd, float bSpd, long iTime, String gnd){
		location = loc;
		state = s;
		aggroLevel = aL;
		baseAggroLevel = bLA;
		cautionLevel = cL;
		baseCautionLevel = bCL;
		length = len;
		height = hei;
		weight = wei;
		name = n;
		cichlidID = id;
		speed = spd;
		baseSpeed = bSpd;
		idleWaitTime = iTime;
		gender = gnd;
	}
	*/
	public ConvictCichlid()
	{
		super(ConvictCichlid.class);
		state = FishState.NONE;
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
	public void draw(Graphics g) {
		System.out.println("obh here");
		
	}

	
}
