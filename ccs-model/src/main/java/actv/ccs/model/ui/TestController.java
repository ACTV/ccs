package actv.ccs.model.ui;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class TestController {
	
	private ConvictCichlid cichlid;
	private TestView view;
	
	public TestController(ConvictCichlid c, TestView v)
	{
		this.cichlid = c;
		this.view = v;
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
	
	public void updateView()
	{
		view.printData(cichlid.getState(), cichlid.getLocation());
	}
	
}
