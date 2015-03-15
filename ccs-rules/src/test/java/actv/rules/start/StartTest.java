package actv.rules.start;

import org.junit.Test;
import org.junit.Assert;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class StartTest extends DroolsTest {
	private ConvictCichlid cc;
	
	public StartTest(){
		super(	"actv/ccs/rules/start/Start.drl", 
				"actv/ccs/flow/start.bpmn",
				"start");
	}
	
	@Test
	public void testValid(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		cc.setBaseCautionLevel(6.00f);
		execute(cc);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
		Assert.assertEquals(9.6f, cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_CautionLevelNULL(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		execute(cc);
		Assert.assertEquals(0.00f, cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid();
		cc.setState(FishState.CAUTION);
		cc.setBaseCautionLevel(5.00f);
		execute(cc);
		Assert.assertEquals(0.00f, cc.getCautionLevel(), .01);
	}
}
