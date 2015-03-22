package actv.ccs.fact;

import actv.ccs.model.ConvictCichlid;

/**
 * 
 * This fact records the end time of a fish's cooldown when in the CAUTION state.
 *
 */
public class CoolingDown {
	private ConvictCichlid convictCichlid;

	public CoolingDown(ConvictCichlid convictCichlid){
		this.convictCichlid = convictCichlid;
	}

	public ConvictCichlid getConvictCichlid() {
		return convictCichlid;
	}
}
