package actv.test.rules.start;

import graphicslib3D.Point3D;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.CoolingDown;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.test.rules.DroolsTest;

public class CoolingDownTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public CoolingDownTest(){
		super(	"actv/ccs/rules/start/CoolingDown.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1));
		cc.setBaseCautionLevel(5.00f);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setState(FishState.CAUTION);
		
		objs = new ArrayList<Object>();
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
		objs.add(new CoolingDown(new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1))));
		
		executeStateful(2000, objs);
		Assert.assertEquals(FishState.CAUTION, cc.getState());	
	}
	
	@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1));
		cc.setState(FishState.IDLE);

		objs.add(cc);
		
		executeStateful(2000, objs);
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
	
}
