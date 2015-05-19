package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphicslib3D.Vector3D;
import actv.ccs.model.ConvictCichlid;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class IdleNearXWall extends BTAction {
	private static final Vector3D XRV = new Vector3D(1, 0, 0);
	private ConvictCichlid cc;
	private static final Logger logger = LoggerFactory.getLogger(IdleNearXWall.class);
	
	public IdleNearXWall(ConvictCichlid c)
	{
		cc = c;
		
	}
	
	protected BTStatus update(float elapsedTime)
	{
		double angle = (Math.acos(cc.getDirection().normalize().dot(XRV))) * (190/Math.PI);
		
		logger.debug("X angle: {}", angle);
		
		if( angle < 90 ){
			cc.turn(90, new Vector3D(0, 1, 0));
		}
		cc.idleNearXWall();
		
		logger.debug("x dir after: {}", cc.getDirection());
		return BTStatus.BH_SUCCESS;
	}
}
