package actv.rules;

import junit.framework.Assert;

import org.drools.runtime.ExecutionResults;
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
		ExecutionResults res = execute(cc);
		System.out.println(cc.getName());
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
}
