package actv.ccs.sageTest.AI;

import sage.ai.behaviortrees.BTCondition;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.Y_POS;

public class IsNearYWall extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;

	
	public IsNearYWall(AIController aic, ConvictCichlid c, boolean toNegate)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
	}
	
	protected boolean check()
	{
		if (cc.getLocation().getY() > 195){
			cc.setYpos(Y_POS.YT);
		}
		else if( cc.getLocation().getY() < 0 ){
			cc.setYpos(Y_POS.YB);
		}else{
			cc.setYpos(Y_POS.NONE);
			return false;
		}
		
		return true;
	}
}