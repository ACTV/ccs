package actv.rules.start;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import actv.ccs.fact.Auditor;
import actv.ccs.fact.CoolingDown;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class CalmTest extends DroolsTest {
	private ConvictCichlid cc;
	private Auditor auditor;
	
	public CalmTest(){
		super(	"actv/ccs/rules/start/Calm.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setSpeed(9.00f);
		cc.setBaseSpeed(5.00f);
		cc.setState(FishState.CAUTION);
		auditor = new Auditor();
	}
	
	@Test
	public void testValid(){
		execute(auditor, cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(cc.getBaseSpeed(), cc.getSpeed(), .01);
	}
	
	@Test
	public void testInvalid_NotCoolingDown(){
		execute(auditor, cc, new CoolingDown(new ConvictCichlid(), System.currentTimeMillis()+100));
		Assert.assertEquals(FishState.CAUTION, cc.getState());	
	}
	
	@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		execute(auditor, cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
	
}
