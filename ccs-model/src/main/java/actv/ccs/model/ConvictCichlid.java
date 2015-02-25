package actv.ccs.model;

import actv.ccs.model.type.FishState;

public class ConvictCichlid extends Object{
	private float [] location;
	private FishState state;

	public ConvictCichlid(){
		super();
		state = FishState.NONE;
	}
	
	public float[] getLocation() {
		if(location == null){
			location = new float[2];
			location[0] = location[1] = 0;
		}
		return location;
	}
	public void setLocation(float[] location) {
		this.location = location;
	}
	public FishState getState() {
		return state;
	}
	public void setState(FishState state) {
		this.state = state;
	}
}
