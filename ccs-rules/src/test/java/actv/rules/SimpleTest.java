package actv.rules;

import junit.framework.Assert;

import org.junit.Test;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class SimpleTest extends DroolsTest {
	
	public SimpleTest(){
		super("src/main/resources/actv/ccs/rules/Swim.drl", "src/test/resources/actv/ccs/flow/swim.bpmn");
	}
	
	@Test
	public void tester(){
		ConvictCichlid cc = new ConvictCichlid();
		cc.setState(FishState.SWIM);
		execute(cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
}
