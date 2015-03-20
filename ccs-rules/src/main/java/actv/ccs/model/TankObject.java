package actv.ccs.model;

public class TankObject {
	private float length;
	private float width;
	private float height;
	private int fishIDList [];
	private float temperature;
	private int cichlidCount;
	private int plantCount;
	
	public TankObject(float l, float w, float h, float t, int cC, int pC, int [] ls)
	{
		this.length = l;
		this.width = w;
		this.height = h;
		this.temperature = t;
		this.cichlidCount = cC;
		this.plantCount = pC;	
		this.fishIDList = ls;
	}

	public float getTankLength()
	{
		return length;
	}
	public void setTankLength(float l)
	{
		this.length = l;
	}
	public float getTankWidth()
	{
		return width;
	}
	public void setTankWidth(float w)
	{
		this.width = w;
	}
	public float getTankHeight()
	{
		return height;
	}
	public void setTankHeight(float h)
	{
		this.height = h;
	}
	public float getTankTemperature()
	{
		return temperature;
	}
	public void setTankTemperature(float t)
	{
		this.temperature = t;
	}
	public int getCichlidCount()
	{
		return cichlidCount;
	}
	public void setCichlidCount(int c)
	{
		this.cichlidCount = c;
	}
	public int getPlantCount()
	{
		return plantCount;
	}
	public void setPlantCount(int p)
	{
		this.plantCount = p;
	}
	public int [] getFishArr()
	{
		if (fishIDList == null)
		{
			fishIDList = new int[3];
			fishIDList[0] = fishIDList[1] = fishIDList[2] = 0;
		}
		return fishIDList;
	}
	public void setFishArr(int [] a)
	{
		fishIDList = a;
	}
	
}
