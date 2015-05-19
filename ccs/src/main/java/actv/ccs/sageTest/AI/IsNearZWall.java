package actv.ccs.sageTest.AI;

import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTCondition;

public class IsNearZWall extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;
	private boolean zFlag = false;

	
	public IsNearZWall(AIController aic, ConvictCichlid c, boolean toNegate)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
		
	}
	
	protected boolean check()
	{
		if (cc.getLocation().getZ() > 195|| cc.getLocation().getZ() < 0 )
		{
			zFlag = true;
		}
		aiController.setNearYWallFlag(zFlag);
		return zFlag;
	}
}