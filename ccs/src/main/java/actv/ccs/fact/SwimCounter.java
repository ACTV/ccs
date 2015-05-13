package actv.ccs.fact;

import actv.ccs.model.ConvictCichlid;

/**
 * This fact records the time for swimming for the rule Swim.drl
 */
public class SwimCounter {
	private int counter;
	private int initialCount;
	private ConvictCichlid convictCichlid;
	
	public SwimCounter(ConvictCichlid convictCichlid, int counter){
		this.convictCichlid = convictCichlid;
		this.counter = counter;
		this.initialCount = counter;
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
	
	public int getInitialCount(){
		return this.initialCount;
	}
	
	public void decrement(){
		counter--;
	}
	
	@Override
	public String toString(){
		return "[ SwimCounter: " + convictCichlid.getName() +"; " + counter + " ]" ;
	}
}
