package actv.ccs.model.ui;
import actv.ccs.model.*;
import actv.ccs.model.type.FishState;

public class TestView {
	
	public void printData(FishState state, float[] location){
		System.out.println("FishState is " + state);
		System.out.println("Fish location is " + location);
	}
}
