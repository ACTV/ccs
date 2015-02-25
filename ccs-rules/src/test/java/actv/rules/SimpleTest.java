package actv.rules;

import org.junit.Test;
import actv.ccs.model.ConvictCichlid;

public class SimpleTest extends DroolsTest {
	
	public SimpleTest(){
		super("Swim.drl", "actv.ccs.rules", "swim");
	}
	
	@Test
	public void tester(){
		ConvictCichlid cc = new ConvictCichlid();
		execute(cc);
	}
}
