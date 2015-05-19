package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTCondition;
import sage.ai.behaviortrees.BTStatus;

public class IdleNearZWall extends BTAction {
	private static final Vector3D ZFV = new Vector3D(0, 0, 1);
	private ConvictCichlid cc;
	private static final Logger logger = LoggerFactory.getLogger(IdleNearZWall.class);
	
	public IdleNearZWall(ConvictCichlid c)
	{
		cc = c;
		
	}
	
	protected BTStatus update(float elapsedTime)
	{
		double angle = (Math.acos(cc.getDirection().normalize().dot(ZFV))) * (190/Math.PI);
		logger.debug("Z angle: {}, {}", cc.getName(), (int)angle);
		
		if( angle < 90 ){
			cc.turn(90, new Vector3D(0, 1, 0));
		}
		
		cc.idleNearZWall();

		logger.debug("Z dir after: {}, {}", cc.getName(), cc.getDirection());
		
		return BTStatus.BH_SUCCESS;
	}
}
