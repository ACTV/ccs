package actv.rules;

import org.junit.Test;
import actv.ccs.model.ConvictCichlid;

public class SimpleTest extends DroolsTest {
	
	public SimpleTest(){
		super();
	}
	
	@Test
	public void tester(){
		execute("actv.ccs.rules.swim.drl", "actv.ccs.flow.swim.bpmn", "swim", new ConvictCichlid());
	}
}
