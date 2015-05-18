package actv.ccs.sageTest.AI;

import graphicslib3D.Point3D;
import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTCondition;
import sage.ai.behaviortrees.BTStatus;

public class IdleNearYWall extends BTAction {
	
	ConvictCichlid cc;

	
	public IdleNearYWall(ConvictCichlid c)
	{
		cc = c;
		
	}
	
	protected BTStatus update(float elapsedTime)
	{
		cc.idleNearYWall();
		return BTStatus.BH_SUCCESS;
	}
}
