package actv.ccs.fact;

import actv.ccs.model.ConvictCichlid;

/**
 * This fact records the number of degrees left to turn for the rule Move.drl
 */
public class MoveCounter {
	private int counter;
	private int direction;
	private ConvictCichlid convictCichlid;
	
	public MoveCounter(ConvictCichlid convictCichlid, int counter, int direction){
		this.convictCichlid = convictCichlid;
		this.counter = counter;
		this.direction = direction;
	}
	
	public ConvictCichlid getConvictCichlid() {
		return convictCichlid;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getCounter(){
		return this.counter;
	}
	
	public int getDirection(){
		return this.direction;
	}
	
	public void decrement(){
		counter--;
	}
	
	@Override
	public String toString(){
		String dir = direction > 0 ? "right" : "left";
		return "[ MoveCounter: " + convictCichlid.getName() +"; " + counter + ", " + dir + " ]" ;
	}
}
