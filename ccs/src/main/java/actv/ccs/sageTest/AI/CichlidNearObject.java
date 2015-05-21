package actv.ccs.sageTest.AI;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.ConvictCichlid.X_POS;
import actv.ccs.sageTest.MyGame;
import sage.ai.behaviortrees.BTCondition;

public class CichlidNearObject extends BTCondition {
	
	private AIController aiController;
	private ConvictCichlid cc;
	private MyGame mg;

	
	public CichlidNearObject(AIController aic, ConvictCichlid c, boolean toNegate, MyGame g)
	{
		super(toNegate);
		aiController = aic;
		cc = c;
		mg = g;
		
	}
	
	protected boolean check()
	{
		if(mg.getLargePlant() != null){
			if (cc.getWorldBound().intersects(
					mg.getLargePlant().getWorldBound())) {
				return true;
			}
		}
		return false;
		
	}
}