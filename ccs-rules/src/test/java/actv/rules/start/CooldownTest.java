package actv.rules.start;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import actv.ccs.fact.Auditor;
import actv.ccs.fact.CoolingDown;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class CooldownTest extends DroolsTest {
	private ConvictCichlid cc;
	private Auditor auditor;
	
	public CooldownTest(){
		super(	"actv/ccs/rules/start/Cooldown.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setBaseCautionLevel(5.00f);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setState(FishState.CAUTION);
		auditor = new Auditor();
	}
	
	//@Test
	public void testValid(){
		execute(auditor, cc, new CoolingDown(cc));
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(cc.getBaseCautionLevel(), cc.getCautionLevel(), .01);
	}
	
	//@Test
	public void testInvalid_NotCC(){
		//execute(auditor, cc, new CoolingDown(new ConvictCichlid());
		Assert.assertEquals(FishState.CAUTION, cc.getState());	
	}
	
	//@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		execute(auditor, cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
	
}
