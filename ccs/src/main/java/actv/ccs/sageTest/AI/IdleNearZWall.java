package actv.ccs.sageTest.AI;

import graphicslib3D.Vector3D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.Z_POS;
import actv.ccs.model.type.FishState;

public class IdleNearZWall extends BTAction {
	private static final Vector3D ZFV = new Vector3D(0, 0, 1);
	private static final Vector3D ZBV = new Vector3D(0, 0, -1);
	private ConvictCichlid cc;
	private static final Logger logger = LoggerFactory.getLogger(IdleNearZWall.class);
	
	public IdleNearZWall(ConvictCichlid c)
	{
		cc = c;
		
	}
	
	protected BTStatus update(float elapsedTime)
	{
		double angle = 0;
		
		if(cc.getZpos() == Z_POS.ZF){
			angle = (Math.acos(cc.getDirection().normalize().dot(ZFV))) * (190/Math.PI);
		}else if(cc.getZpos() == Z_POS.ZB){
			angle = (Math.acos(cc.getDirection().normalize().dot(ZBV))) * (190/Math.PI);
		}else{
			logger.error("{}: How  Z did we get here?!", cc.getName());
			return BTStatus.BH_FAILURE;
		}
		
		cc.setState(FishState.IDLE);
		
		logger.debug("Z angle: {}, {}", cc.getName(), (int)angle);
		
		if( angle < 90 ){
			cc.turn((float)(180-angle), new Vector3D(0, 1, 0));
		}
		
		cc.idleNearZWall();
		
		logger.debug("Z dir after: {}, {}", cc.getName(), cc.getDirection());
		
		return BTStatus.BH_SUCCESS;
	}
}
