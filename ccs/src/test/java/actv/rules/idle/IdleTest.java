package actv.rules.idle;

import graphicslib3D.Point3D;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class IdleTest extends DroolsTest {
	private ConvictCichlid cc;
	private long start;
	private ArrayList<CCSMemoryObject> objs;
	
	public IdleTest(){
		super(	"actv/ccs/rules/idle/Idle.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1));
		cc.setState(FishState.IDLE);
		cc.setIdleWaitTime(System.currentTimeMillis());
		
		objs = new ArrayList<CCSMemoryObject>();
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
