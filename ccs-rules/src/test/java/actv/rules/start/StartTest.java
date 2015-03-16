package actv.rules.start;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class StartTest extends DroolsTest {
	private ConvictCichlid cc;
	
	public StartTest(){
		super(	"actv/ccs/rules/start/Start.drl", 
				"actv/ccs/flow/start.bpmn",
				"start");
	}
	
	@Before
	public void setupFish(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);
		cc.setBaseCautionLevel(6.00f);
		cc.setBaseSpeed(6.00f);
	}
	
	@Test
	public void testValid(){
		execute(cc);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
		Assert.assertEquals(9.6f, cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_ALL(){
		cc.setState(FishState.CAUTION);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setSpeed(cc.getSpeed() * 1.6f);
		execute(cc);
		Assert.assertEquals(9.60f, cc.getCautionLevel(), .01);
		Assert.assertEquals(9.60f, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
	
	@Test
	public void testInvalid_FishState(){
		cc.setState(FishState.CAUTION);
		execute(cc);
		Assert.assertEquals(9.60f, cc.getCautionLevel(), .01);
		Assert.assertEquals(9.60f, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
	
	@Test
	public void testInvalid_SpeedOrCautionLvl(){
		cc.setCautionLevel(6.00f);
		execute(cc);
		Assert.assertEquals(9.60f, cc.getCautionLevel(), .01);
		Assert.assertEquals(9.60f, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
}
