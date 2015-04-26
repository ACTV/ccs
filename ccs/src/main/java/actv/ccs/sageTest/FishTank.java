package actv.ccs.sageTest;

import sage.scene.Group;
import sage.terrain.TerrainBlock;

/**
 * Interface to hold the components of the fish tank, including walls and floor.
 * @author tnt
 *
 */
public interface FishTank {
	public static int WIDTH = 200;
	public static int HEIGHT = 200;
	public static int DEPTH = 200;
	
	/**
	 * Creates the walls of the fish tank adds them to a sage.scene.Group object.
	 * @return Group
	 */
	public Group createFishTankWalls();
	
	/**
	 * Creates the floor of the fish tank.
	 * @return TerrainBlock
	 */
	public TerrainBlock createTankTerrain();
	
	public void setCichlidCount(int count);
	public int getCichlidCount();
	
	public void setObjectCount(int count);
	public int getObjectCount();
	
	public void setTemperature(float temperature);
	public float getTemperature();
	
	
	public void setTimer(int time);
	public int getTimer();
}
