package actv.ccs.fact;

import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;


/**
 * 
 * This fact initializes the idle time.
 *
 */
public class IdleTimer implements CCSMemoryObject{
	private ConvictCichlid convictCichlid;
	private long idleTime;
	
	
	public IdleTimer(ConvictCichlid convictCichlid, long idleTime){
		this.convictCichlid = convictCichlid;
		this.idleTime = idleTime;
	}


	public ConvictCichlid getConvictCichlid() {
		return convictCichlid;
	}


	public void setConvictCichlid(ConvictCichlid convictCichlid) {
		this.convictCichlid = convictCichlid;
	}


	public long getIdleTime() {
		return idleTime;
	}


	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}
}
