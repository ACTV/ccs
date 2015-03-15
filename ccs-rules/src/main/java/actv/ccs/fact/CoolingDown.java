package actv.ccs.fact;

import actv.ccs.model.ConvictCichlid;

/**
 * 
 * This fact records the end time of a fish's cooldown when in the CAUTION state.
 *
 */
public class CoolingDown {
	private double endTime;
	private ConvictCichlid convictCichlid;

	public CoolingDown(ConvictCichlid convictCichlid, double duration){
		this.convictCichlid = convictCichlid;
		this.endTime = System.currentTimeMillis() + (duration * 1000);
	}

	public double getEndTime() {
		return endTime;
	}

	public ConvictCichlid getConvictCichlid() {
		return convictCichlid;
	}
}
