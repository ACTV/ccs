package actv.rules.start;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.CoolingDown;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class CoolingDownTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<CCSMemoryObject> objs;
	
	public CoolingDownTest(){
		super(	"actv/ccs/rules/start/CoolingDown.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid();
		cc.setBaseCautionLevel(5.00f);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setState(FishState.CAUTION);
		
		objs = new ArrayList<CCSMemoryObject>();
	}
	
	@Test
	public void testValid(){

		objs.add(cc);
		objs.add(new CoolingDown(cc));
		
		executeStateful(3000, objs);
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(cc.getBaseCautionLevel(), cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_NotCC(){

		objs.add(cc);
		objs.add(new CoolingDown(new ConvictCichlid()));
		
		executeStateful(2000, objs);
		Assert.assertEquals(FishState.CAUTION, cc.getState());	
	}
	
	@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid();
		cc.setState(FishState.IDLE);

		objs.add(cc);
		
		executeStateful(2000, objs);
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
	
}
