package actv.ccs.model.ui;

import actv.ccs.model.TankObject;

public class TankController {

	private TankObject tank;
	private SimulationWorld world;
	
	public TankController(TankObject t, SimulationWorld tV)
	{
		this.tank = t;
		this.world = tV;
	}
	public float getTankLength()
	{
		return tank.getTankLength();
	}
	public void setTankLength(float l)
	{
		tank.setTankLength(l);
	}
	public float getTankWidth()
	{
		return tank.getTankWidth();
	}
	public void setTankWidth(float w)
	{
		tank.setTankWidth(w);
	}
	public float getTankHeight()
	{
		return tank.getTankHeight();
	}
	public void setTankHeight(float h)
	{
		tank.setTankHeight(h);
	}
	public float getTankTemperature()
	{
		return tank.getTankTemperature();
	}
	public void setTankTemperature(float t)
	{
		tank.setTankTemperature(t);
	}
	public int getCichlidCount()
	{
		return tank.getCichlidCount();
	}
	public void setCichlidCount(int c)
	{
		tank.setCichlidCount(c);
	}
	public int getPlantCount()
	{
		return tank.getPlantCount();
	}
	public void setPlantCount(int p)
	{
		tank.setPlantCount(p);
	}

	
}
