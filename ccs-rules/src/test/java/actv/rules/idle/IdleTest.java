package actv.rules.idle;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import actv.ccs.fact.Auditor;
import actv.ccs.fact.CoolingDown;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class IdleTest extends DroolsTest {
	private ConvictCichlid cc;
	private Auditor auditor;
	
	public IdleTest(){
		super(	"actv/ccs/rules/idle/Idle.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		cc.setIdleWaitTime(0);
		auditor = new Auditor();
	}
	
	@Test
	public void testValid(){
		execute(auditor, cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(1, auditor.getRulesFired().size());
	}

	
}
