package actv.ccs.sageTest.AI;

import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTCondition;

public class IsNearYWall extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;
	private boolean yFlag = false;

	
	public IsNearYWall(AIController aic, ConvictCichlid c, boolean toNegate)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
		
	}
	
	protected boolean check()
	{
		if (cc.getLocation().getY() > 195|| cc.getLocation().getY() < 0 )
		{
			yFlag = true;
		}
		aiController.setNearYWallFlag(yFlag);
		return yFlag;
	}
}