package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphicslib3D.Vector3D;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;
import actv.ccs.model.ConvictCichlid;

public class IdleNearYWall extends BTAction {
	private static final Vector3D YUV = new Vector3D(0, 1, 0);
	private ConvictCichlid cc;
	private static final Logger logger = LoggerFactory.getLogger(IdleNearYWall.class);

	
	public IdleNearYWall(ConvictCichlid c)
	{
		cc = c;
		
	}
	
	protected BTStatus update(float elapsedTime)
	{
		double angle = (Math.acos(cc.getDirection().normalize().dot(YUV))) * (190/Math.PI);
		logger.debug("Y angle: {}, {}", cc.getName(), (int)angle);
		
		if( angle < 90 ){
			cc.turn((float)(180-angle), new Vector3D(0, 0, 1));
		}
		
		cc.idleNearYWall();
		
		logger.debug("Y dir after: {}, {}", cc.getName(), cc.getDirection());
		
		return BTStatus.BH_SUCCESS;
	}
}
