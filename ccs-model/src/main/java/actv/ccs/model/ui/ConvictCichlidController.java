package actv.ccs.model.ui;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class ConvictCichlidController {
	
	private ConvictCichlid cichlid;
	private SimulationWorld world;
	
	public ConvictCichlidController(ConvictCichlid c, SimulationWorld v)
	{
		this.cichlid = c;
		this.world = v;
	}
	
	public void setFishState(FishState s)
	{
		cichlid.setState(s);
	}
	public FishState getFishState()
	{
		return cichlid.getState();
	}
	
	public void setFishLocation(float[] l)
	{
		cichlid.setLocation(l);
	}
	public float[] getFishLocation()
	{
		return cichlid.getLocation();
	}
	public float getAggro()
	{
		return cichlid.getAggroLevel();
	}
	private void setAggroLevel(float a)
	{
		cichlid.setAggroLevel(a); 
	}
	public float getLength()
	{
		return cichlid.getLength();
	}
	public void setLength(float f)
	{
		cichlid.setLength(f);
	}
	public float getHeight()
	{
		return cichlid.getHeight();
	}
	public void setHeight(float h)
	{
		cichlid.setHeight(h);
	}
	public float getWeight()
	{
		return cichlid.getWeight();
	}
	public void setWeight(float w)
	{
		cichlid.setWeight(w);
	}
	public String getName()
	{
		return cichlid.getName();
	}
	public void setName(String s)
	{
		cichlid.setName(s);
	}
	public int getID()
	{
		return cichlid.getCichlidID();
	}
	public void setID(int i)
	{
		cichlid.setCichlidID(i);
	}
	
	public void updateView()
	{
		world.printData(cichlid.getState(), cichlid.getLocation(), cichlid.getAggroLevel(), cichlid.getLength(), cichlid.getHeight(), cichlid.getWeight(), cichlid.getName(), cichlid.getCichlidID());
	}
	
}
