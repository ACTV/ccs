package actv.ccs.sageTest.AI;

import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTCondition;

public class IsNearXWall extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;
	private boolean xFlag = false;

	
	public IsNearXWall(AIController aic, ConvictCichlid c, boolean toNegate)
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
		}
		aiController.setNearXWallFlag(xFlag);
		return xFlag;
	}
}