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
		System.out.println("VOGLER IS STILL ALIVE BUT IT'S IMPOSSIBLE?!");
		Point3D ccP = new Point3D(cc.getLocation().getX(), cc.getLocation().getY(), cc.getLocation().getZ());
		if (ccP.getX() > 195|| ccP.getX() < 0 )
		{
	//		MoveActionFactory.turn(this, 180, new Vector3D(0, 1, 0));
			System.out.println("X BOUNDS ARE HAPPENING");
			aiController.setNearWallFlag(true);
			return true;
		}
		if (ccP.getY() > 195 || ccP.getY() < 0 )
		{
			System.out.println("Y BOUNDS ARE HAPPENING");
			aiController.setNearWallFlag(true);
			return true;
	//		MoveActionFactory.turn(this, 180, new Vector3D(1, 0, 0));
		}
		if (ccP.getZ() > 195 || ccP.getZ() < 0 )
		{
	//		MoveActionFactory.turn(this, 180, new Vector3D(0, 1, 0));
			System.out.println("Z BOUNDS ARE HAPPENING");
			aiController.setNearWallFlag(true);
			return true;
		}
		else return false;
	}
}