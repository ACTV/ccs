package actv.ccs.sageTest.AI;

import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTCondition;

public class IsNearWall extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;
	private boolean xFlag = false;
	private boolean yFlag = false;
	private boolean zFlag = false;

	
	public IsNearWall(AIController aic, ConvictCichlid c, boolean toNegate)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
		
	}
	
	protected boolean check()
	{
		if (cc.getLocation().getX() > 195|| cc.getLocation().getX() < 0 )
		{
			xFlag = true;
			return true;
		}
		if (cc.getLocation().getY() > 195 || cc.getLocation().getY() < 0 )
		{
			yFlag = true;
		}
		if (cc.getLocation().getZ() > 195 || cc.getLocation().getZ() < 0 )
		{
			zFlag = true;
		}
		
		aiController.setNearXWallFlag(xFlag);
		aiController.setNearYWallFlag(yFlag);
		aiController.setNearZWallFlag(zFlag);
		
		return xFlag || yFlag || zFlag;
	}
}