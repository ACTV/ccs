package actv.ccs.model;

import actv.ccs.model.type.FishState;

public class ConvictCichlid {
	private float [] location;
	private FishState state;

	public float[] getLocation() {
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
