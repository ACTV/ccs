package actv.ccs.sageTest;

import sage.scene.Group;
import sage.terrain.TerrainBlock;

/**
 * 
 * Interface to hold the components of the fish tank, including walls and floor.
 * 
 */
public interface FishTank {
	public static int DEPTH = 200;
	public static int HEIGHT = 200;
	public static int WIDTH = 200;

	public int getCichlidCount();

	public Group getFishWalls();

	public int getObjectCount();

	public float getTemperature();

	public TerrainBlock getTerrain();

	public int getTimer();

	public void setCichlidCount(int count);

	public void setFishWalls(Group fishWalls);

	public void setObjectCount(int count);

	public void setTemperature(float temperature);

	public void setTerrain(TerrainBlock terrain);

	public void setTimer(int time);
}
