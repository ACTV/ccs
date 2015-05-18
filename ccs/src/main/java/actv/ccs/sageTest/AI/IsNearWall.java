package actv.ccs.sageTest.AI;

import graphicslib3D.Point3D;
import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTCondition;

public class IsNearWall extends BTCondition {
	
	AIController aiController;
	ConvictCichlid cc;
	boolean tN;
	
	public IsNearWall(AIController aic, ConvictCichlid c, boolean toNegate)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
		
	}
	
	protected boolean check()
	{
	//	System.out.println("VOGLER IS STILL ALIVE BUT IT'S IMPOSSIBLE?!");
		
		
	//	System.out.println("THE LOCATION: " + cc.getLocation());
		
	// is returning location	
		if (cc.getLocation().getX() > 195|| cc.getLocation().getX() < 0 )
		{
	//		MoveActionFactory.turn(this, 180, new Vector3D(0, 1, 0));
			System.out.println("X BOUNDSE");
			aiController.setNearXWallFlag(true);
			return true;
		}
		if (cc.getLocation().getY() > 195 || cc.getLocation().getY() < 0 )
		{
			System.out.println("Y BOUNDS ");
			aiController.setNearYWallFlag(true);
			return true;
	//		MoveActionFactory.turn(this, 180, new Vector3D(1, 0, 0));
		}
		if (cc.getLocation().getZ() > 195 || cc.getLocation().getZ() < 0 )
		{
	//		MoveActionFactory.turn(this, 180, new Vector3D(0, 1, 0));
			System.out.println("Z BOUNDS ");
			aiController.setNearZWallFlag(true);
			return true;
		}
		else return false;
	}
}