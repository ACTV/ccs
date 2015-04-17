package actv.rules.idle;

import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.Auditor;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.sageTest.TestCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class TestMoveTest extends DroolsTest {
	private TestCichlid cc;
	private Auditor auditor;
	private ArrayList<CCSMemoryObject> objs;
	
	public TestMoveTest(){
		super(	"actv/ccs/rules/idle/TestMove.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new TestCichlid();
		cc.setState(FishState.IDLE);
		cc.setDirection(new Vector3D(0,0,1));
		auditor = new Auditor();
	}
	
	@Test
	public void test(){
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		executeStateful(5000, objs);
		
		System.out.println("Rules: " + auditor.getRulesFired().size());
		System.out.println("Direction: " + cc.getDirection());
	}

}
