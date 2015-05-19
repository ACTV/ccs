package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphicslib3D.Vector3D;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.X_POS;
import actv.ccs.model.ConvictCichlid.Y_POS;
import actv.ccs.model.type.FishState;

public class IdleNearYWall extends BTAction {
	private static final Vector3D YUV = new Vector3D(0, 1, 0);
	private static final Vector3D YBV = new Vector3D(0, -1, 0);
	private ConvictCichlid cc;
	private static final Logger logger = LoggerFactory.getLogger(IdleNearYWall.class);

	
	public IdleNearYWall(ConvictCichlid c)
	{
		cc = c;
		
	}
	
	protected BTStatus update(float elapsedTime)
	{
		cc.setState(FishState.IDLE);
		double angle = 0;
		
		if(cc.getYpos() == Y_POS.YT){
			angle = (Math.acos(cc.getDirection().normalize().dot(YUV))) * (190/Math.PI);
		}else if(cc.getYpos() == Y_POS.YB){
			angle = (Math.acos(cc.getDirection().normalize().dot(YBV))) * (190/Math.PI);
		}else{
			logger.error("How did we get here?!");
		}
		
		logger.debug("Y angle: {}, {}", cc.getName(), (int)angle);
		
		if( angle < 90 ){
			cc.turn((float)(180-angle), new Vector3D(0, 0, 1));
		}
		
		cc.idleNearYWall();
		
		logger.debug("Y dir after: {}, {}", cc.getName(), cc.getDirection());
		
		return BTStatus.BH_SUCCESS;
	}
}
