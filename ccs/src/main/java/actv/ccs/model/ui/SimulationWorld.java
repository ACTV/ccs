package actv.ccs.model.ui;

import actv.ccs.listener.CCChangeListener;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.TankObject;
import actv.ccs.model.type.FishState;

public class SimulationWorld {
	
	private ConvictCichlid cichlid;
	private TankObject tank;
	
	public SimulationWorld()
	{		
		cichlid = getFromDB();
		cichlid.addPropertyChangeListener(new CCChangeListener());
		
		ConvictCichlidController controller = new ConvictCichlidController(cichlid, this);
		TankController tankController = new TankController(tank, this);
		
		
	}
	
	public void printData(FishState state, float[] location, float aggroLevel, float length, float height, float weight, String name, int id){
		System.out.println("FishState is " + state);
		System.out.println("Fish location is " + location);
		System.out.println("Fish aggro is " + aggroLevel);
		System.out.println("Fish length is " + length);
		System.out.println("Fish height is " + height);
		System.out.println("Fish weight is " + weight);
		System.out.println("Fish name is " + name);
		System.out.println("Fish ID is " + id);
	}
	private static ConvictCichlid getFromDB()
	{
		ConvictCichlid c = new ConvictCichlid();
		c.setLocation(new float[] {1,1});
		c.setState(FishState.NONE);
		c.setLength(10);
		c.setHeight(5);
		c.setWeight(10);
		c.setName("Shark");
		c.setCichlidID(0);
		return c;
	}

}