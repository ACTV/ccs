package actv.rules.idle;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class MoveTest extends DroolsTest {
	private ConvictCichlid cc;
	private Auditor auditor;
	private ArrayList<CCSMemoryObject> objs;
	
	public MoveTest(){
		super(	"actv/ccs/rules/idle/Move.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		cc.setDirection(100);
		auditor = new Auditor();
	}
	
	@Test
	public void test(){
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		CCSKnowledgeBase.executeInfiniteSession("actv/ccs/rules/idle/Move.drl", 
												"actv/ccs/flow/swim.bpmn", 
												"swim", 
												objs);
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CCSKnowledgeBase.disposeSession();
		
		System.out.println("Rules: " + auditor.getRulesFired().size());
		System.out.println("Direction: " + cc.getDirection());
	}

}
