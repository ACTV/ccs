package actv.rules.start;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import actv.ccs.fact.CoolingDown;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class CooldownTest extends DroolsTest {
	private ConvictCichlid cc;
	
	public CooldownTest(){
		super(	"actv/ccs/rules/start/Cooldown.drl", 
				"actv/ccs/flow/start.bpmn",
				"start");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setBaseCautionLevel(5.00f);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setState(FishState.CAUTION);
	}
	
	@Test
	public void testValid(){
		execute(cc, new CoolingDown(cc, System.currentTimeMillis()+100));
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(cc.getBaseCautionLevel(), cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_NotCC(){
		execute(cc, new CoolingDown(new ConvictCichlid(), System.currentTimeMillis()+100));
		Assert.assertEquals(FishState.CAUTION, cc.getState());	
	}
	
	@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		execute(cc);
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
	
}
