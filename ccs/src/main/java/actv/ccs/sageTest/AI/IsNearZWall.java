package actv.ccs.sageTest.AI;

import sage.ai.behaviortrees.BTCondition;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.Z_POS;

public class IsNearZWall extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;
	private boolean zFlag = false;

	
	public IsNearZWall(AIController aic, ConvictCichlid c, boolean toNegate)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
		
	}
	
	protected boolean check()
	{
		if (cc.getLocation().getZ() > 195){
			cc.setZpos(Z_POS.ZF);
		}
		else if( cc.getLocation().getZ() < 0 ){
			cc.setZpos(Z_POS.ZB);
		}else{
			cc.setZpos(Z_POS.NONE);
			return false;
		}
		
		return true;
	}
}