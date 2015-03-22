package actv.rules.start;

import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.Auditor;
import actv.ccs.fact.CoolingDown;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsStreamTest;

public class CoolingDownTest extends DroolsStreamTest {
	private StatefulKnowledgeSession sks;
	private ConvictCichlid cc;
	private Auditor auditor;
	
	public CoolingDownTest(){
		super(	"actv/ccs/rules/start/CoolingDown.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	private void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setBaseCautionLevel(5.00f);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setState(FishState.CAUTION);
		auditor = new Auditor();
	}
	
	@Test
	public void testValid(){
		
		sks = execute(auditor, cc, new CoolingDown(cc));

		sleep(3000);
		
		sks.halt();
		sks.dispose();
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(cc.getBaseCautionLevel(), cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_NotCC(){
		sks = execute(auditor, cc, new CoolingDown(new ConvictCichlid()));
		
		sleep(3000);
		
		sks.halt();
		sks.dispose();
		
		Assert.assertEquals(FishState.CAUTION, cc.getState());	
	}
	
	@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		
		sks = execute(auditor, cc);
		
		sleep(3000);
		
		sks.halt();
		sks.dispose();
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
	
}
