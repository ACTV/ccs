package actv.ccs.sageTest;

import sage.scene.Group;
import sage.terrain.TerrainBlock;

public class FishTankProxy{
	private FishTank fishTank;
	
	public FishTankProxy(FishTank fishTank){
		this.fishTank = fishTank;
	}

	public int getCichlidCount() {
		return fishTank.getCichlidCount();
	}

	public int getObjectCount() {
		return fishTank.getObjectCount();
	}

	public float getTemperature() {
		return fishTank.getTemperature();
	}

	public int getTimer() {
		return fishTank.getTimer();
	}

	public Group getFishWalls() {
		return fishTank.getFishWalls();
	}

	public TerrainBlock getTerrain() {
		return fishTank.getTerrain();
	}
	
}
