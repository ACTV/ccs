package actv.rules.idle;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.time.SessionPseudoClock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.fact.IdleTimer;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class IdleTest extends DroolsTest {
	private ConvictCichlid cc;
	private Auditor auditor;
	private long start;
	
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
		start = System.currentTimeMillis();
	}
	
	@Test
	public void test(){
		cc.setIdleWaitTime(0);
		ArrayList<CCSMemoryObject> objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		StatefulKnowledgeSession sks = CCSKnowledgeBase.executeInfiniteSession(objs);
		
		//Sleep the thread for 10 seconds to allow for interval execution
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertEquals(5, auditor.getRulesFired().size());
	}
	
	@After
	public void printExecutionTime(){
		System.out.println("Execution time: " + (System.currentTimeMillis() - start));
	}

	
}
