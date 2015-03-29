package actv.rules.idle;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.Auditor;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class IdleTest extends DroolsTest {
	private ConvictCichlid cc;
	private Auditor auditor;
	private long start;
	private ArrayList<CCSMemoryObject> objs;
	
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
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(auditor);
		start = System.currentTimeMillis();
		
	}
	
	@Test
	public void test_IdleOnly(){
		cc.setIdleWaitTime(0);
		
		objs.add(cc);
		
		executeStateful(4000, objs);
		
	}
	
	@After
	public void printExecutionTime(){
		System.out.println("Execution time: " + (System.currentTimeMillis() - start));
	}

	
}
