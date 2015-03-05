package actv.rules;

import junit.framework.Assert;

import org.junit.Test;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class SimpleTest extends DroolsTest {
	
	public SimpleTest(){
		super(	"actv/ccs/rules/Swim.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Test
	public void tester(){
		ConvictCichlid cc = new ConvictCichlid();
		cc.setState(FishState.SWIM);
		execute(cc);
		Assert.assertEquals(FishState.IDLE.toString(), cc.getState().toString());
	}
}
