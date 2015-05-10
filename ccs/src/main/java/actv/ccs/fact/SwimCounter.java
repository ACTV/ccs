package actv.ccs.fact;

import actv.ccs.model.ConvictCichlid;

/**
 * This fact records the time for swimming for the rule Swim.drl
 */
public class SwimCounter {
	private int counter;
	private ConvictCichlid convictCichlid;
	
	public SwimCounter(ConvictCichlid convictCichlid, int counter){
		this.convictCichlid = convictCichlid;
		this.counter = counter;
	}
	
	public ConvictCichlid getConvictCichlid() {
		return convictCichlid;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getCounter(){
		return this.counter;
	}
	
	@Override
	public String toString(){
		return "[ SwimCounter: " + convictCichlid.getName() +"; " + counter + " ]" ;
	}
}
