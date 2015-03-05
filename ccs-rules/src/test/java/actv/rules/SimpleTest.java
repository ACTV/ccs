package actv.rules;

import org.junit.Test;
import org.junit.Assert;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class SimpleTest extends DroolsTest {
	private ConvictCichlid cc;
	public SimpleTest(){
		super(	"actv/ccs/rules/Swim.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Test
	public void tester(){
		cc = new ConvictCichlid();
		cc.setState(FishState.SWIM);
		execute(cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
}
