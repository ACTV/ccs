package actv.ccs.sageTest;

import sage.scene.Group;
import sage.terrain.TerrainBlock;

/**
 * Interface to hold the components of the fish tank, including walls and floor.
 * @author tnt
 *
 */
public interface FishTank {
	
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
}
