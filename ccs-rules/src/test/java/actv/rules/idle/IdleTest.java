package actv.rules.idle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.Auditor;
import actv.ccs.fact.IdleTimer;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class IdleTest extends DroolsTest {
	private ConvictCichlid cc;
	private Auditor auditor;
	private IdleTimer it;
	
	public IdleTest(){
		super(	"actv/ccs/rules/idle/Idle.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		cc.setIdleWaitTime(System.currentTimeMillis());
		auditor = new Auditor();
		it = new IdleTimer(cc, System.currentTimeMillis());
	}
	
	@Test
	public void testValid_timeEQ(){
		cc.setIdleWaitTime(System.currentTimeMillis());
		execute(auditor, cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(1, auditor.getRulesFired().size());
	}
	
	@Test
	public void testInvalid_timeGT(){
		cc.setIdleWaitTime(System.currentTimeMillis() + 4000);
		execute(auditor, cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(0, auditor.getRulesFired().size());
	}

	
}
