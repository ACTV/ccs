package actv.rules.start;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import actv.ccs.fact.Auditor;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class StartTest extends DroolsTest {
	private static final String RULE = "Start";
	private ConvictCichlid cc;
	private Auditor auditor;
	
	public StartTest(){
		super(	"actv/ccs/rules/start/Start.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setupFish(){
		cc = new ConvictCichlid();
		cc.setState(FishState.NONE);
		cc.setBaseCautionLevel(6.00f);
		cc.setBaseSpeed(6.00f);
		
		auditor = new Auditor();
	}
	
	@Test
	public void testValid(){
		execute(cc, auditor);
		Assert.assertEquals(auditor.getRulesFired().get(0), RULE);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
		Assert.assertEquals(9.6f, cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_ALL(){
		cc.setState(FishState.CAUTION);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setSpeed(cc.getBaseSpeed() * 1.6f);
		
		execute(cc, auditor);
		Assert.assertEquals(0, auditor.getRulesFired().size());
		Assert.assertEquals(9.60f, cc.getCautionLevel(), .01);
		Assert.assertEquals(9.60f, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
	
	@Test
	public void testInvalid_FishState(){
		cc.setState(FishState.FLEE);
		
		execute(cc, auditor);
		Assert.assertEquals(0, auditor.getRulesFired().size());
		Assert.assertEquals(0, cc.getCautionLevel(), .01);
		Assert.assertEquals(0, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.FLEE, cc.getState());
	}
	
	@Test
	public void testInvalid_SpeedOrCautionLvl(){
		cc.setCautionLevel(6.00f);
		
		execute(cc, auditor);
		Assert.assertEquals(auditor.getRulesFired().get(0), RULE);
		Assert.assertEquals(9.60f, cc.getCautionLevel(), .01);
		Assert.assertEquals(9.60f, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
}
