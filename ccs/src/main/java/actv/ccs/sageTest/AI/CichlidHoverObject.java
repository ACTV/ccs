package actv.ccs.sageTest.AI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphicslib3D.Vector3D;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.X_POS;
import actv.ccs.model.type.FishState;
import sage.ai.behaviortrees.BTAction;
import sage.ai.behaviortrees.BTStatus;

public class CichlidHoverObject extends BTAction {

	private ConvictCichlid cc;
	private static final Logger logger = LoggerFactory.getLogger(CichlidHoverObject.class);
	
	public CichlidHoverObject(ConvictCichlid c)
	{
		cc = c;
	}
	
	protected BTStatus update(float elapsedTime)
	{
		cc.setState(FishState.IDLE);
		
		logger.debug("Fish is hiding near a large plant /test: {}", cc.getName());

		cc.hoverNearLargePlant();
		
		return BTStatus.BH_SUCCESS;
	}
}
