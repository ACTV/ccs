package actv.ccs.sageTest.AI;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.X_POS;
import sage.ai.behaviortrees.BTCondition;

public class IsNearXWall extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;

	
	public IsNearXWall(AIController aic, ConvictCichlid c, boolean toNegate)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
		
	}
	
	protected boolean check()
	{
		if (cc.getLocation().getX() > 195){
			cc.setXpos(X_POS.XR);
		}
		else if( cc.getLocation().getX() < 0 ){
			cc.setXpos(X_POS.XL);
		}else{
			cc.setXpos(X_POS.NONE);
			return false;
		}
		
		return true;
	}
}