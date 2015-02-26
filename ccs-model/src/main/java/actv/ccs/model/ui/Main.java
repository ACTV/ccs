package actv.ccs.model.ui;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.*;

public class Main {

	// so the model is the Convict Cichlid
	// view is user view
	// controller is the observer
	
	// changelog 2-26-15
	/*
	 *  going to add in later stuff for fish tank
	 *  and figure out to make a view.
	 *  this is pretty much run ui but i'm going to add a jframe later on.
	 */
	
	public static void main(String[] args) {
		
		ConvictCichlid cichlid = getFromDB();
		
		TestView view = new TestView();
		
		TestController testC = new TestController(cichlid, view);
		
		testC.updateView();
		testC.setFishLocation(new float[] {0,1});
		
		testC.updateView();
	}
	
	private static ConvictCichlid getFromDB()
	{
		ConvictCichlid c = new ConvictCichlid();
		c.setLocation(new float[] {1,1});
		c.setState(FishState.NONE);
		return c;
	}

}
