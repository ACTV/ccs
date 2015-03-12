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
		execute(cc);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
}
