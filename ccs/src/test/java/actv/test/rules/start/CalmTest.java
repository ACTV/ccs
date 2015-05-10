package actv.test.rules.start;

import graphicslib3D.Point3D;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.CoolingDown;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.test.rules.DroolsTest;

public class CalmTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public CalmTest(){
		super(	"actv/ccs/rules/start/Calm.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1));
		cc.setSpeed(9.00f);
		cc.setBaseSpeed(5.00f);
		cc.setState(FishState.CAUTION);
		
		objs = new ArrayList<Object>();
	}
	
	@Test
	public void testValid(){
		objs.add(cc);
		executeStateless(objs);
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertEquals(cc.getBaseSpeed(), cc.getSpeed(), .01);
	}
	
	@Test
	public void testInvalid_NotCoolingDown(){
		objs.add(new CoolingDown(new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1))));
		executeStateless(objs);
		Assert.assertEquals(FishState.CAUTION, cc.getState());	
	}
	
	@Test
	public void testInvalid_FishState(){
		cc = new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1));
		cc.setState(FishState.IDLE);
		
		objs.add(cc);
		
		executeStateless(objs);
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
	}
	
}
