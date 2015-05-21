package actv.ccs.sageTest.AI;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphicslib3D.Vector3D;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.X_POS;
import actv.ccs.model.type.FishState;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class IdleNearXWall extends BTAction {
	private static final Vector3D XRV = new Vector3D(1, 0, 0);
	private static final Vector3D XLV = new Vector3D(-1, 0, 0);
	private ConvictCichlid cc;
	private static final Logger logger = LoggerFactory.getLogger(IdleNearXWall.class);
	
	public IdleNearXWall(ConvictCichlid c)
	{
		cc = c;
	}
	
	protected BTStatus update(float elapsedTime)
	{
		double angle = 0;
		
		if(cc.getXpos() == X_POS.XR){
			angle = (Math.acos(cc.getDirection().normalize().dot(XRV))) * (190/Math.PI);
		}else if(cc.getXpos() == X_POS.XL){
			angle = (Math.acos(cc.getDirection().normalize().dot(XLV))) * (190/Math.PI);
		}else{
			return BTStatus.BH_INVALID;
		}
		
		
//		logger.debug("X angle: {}, {}", cc.getName(), (int)angle);
		
		if( cc.getXpos() == X_POS.XR){
//			cc.turn((float)(180-angle), new Vector3D(0, 1, 0));
			cc.setDirection(XLV);
		}else{
			cc.setDirection(XRV);
		}
		
		cc.idleNearXWall();
		if(cc.getState() != FishState.IDLE)
			cc.setIdleWaitTime(System.currentTimeMillis() + (new Random(System.nanoTime())).nextInt(2000) + 1000);
		
//		logger.debug("X dir after: {}, {}", cc.getName(), cc.getDirection());
		
		cc.setState(FishState.IDLE);
		return BTStatus.BH_SUCCESS;
	}
}
