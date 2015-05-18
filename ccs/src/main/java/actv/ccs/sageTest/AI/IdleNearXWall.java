package actv.ccs.sageTest.AI;

import graphicslib3D.Point3D;
import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTCondition;
import sage.ai.behaviortrees.BTStatus;

public class IdleNearXWall extends BTAction {
	
	ConvictCichlid cc;

	
	public IdleNearXWall(ConvictCichlid c)
	{
		cc = c;
		
	}
	
	protected BTStatus update(float elapsedTime)
	{
		cc.idleNearXWall();
		return BTStatus.BH_SUCCESS;
	}
}
