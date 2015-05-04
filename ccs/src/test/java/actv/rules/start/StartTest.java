package actv.rules.start;

import graphicslib3D.Point3D;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class StartTest extends DroolsTest {
	private static final String RULE = "Start";
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public StartTest(){
		super(	"actv/ccs/rules/start/Start.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setupFish(){
		cc = new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1));
		cc.setState(FishState.NONE);
		cc.setBaseCautionLevel(6.00f);
		cc.setBaseSpeed(6.00f);
		
		
		objs = new ArrayList<Object>();
	}
	
	@Test
	public void testValid(){
		objs.add(cc);
		
		executeStateless(objs);
		
		Assert.assertEquals(FishState.CAUTION, cc.getState());
		Assert.assertEquals(9.6f, cc.getCautionLevel(), .01);
	}
	
	@Test
	public void testInvalid_ALL(){
		cc.setState(FishState.CAUTION);
		cc.setCautionLevel(cc.getBaseCautionLevel() * 1.6f);
		cc.setSpeed(cc.getBaseSpeed() * 1.6f);
		
		objs.add(cc);
		
		executeStateless(objs);
		
		Assert.assertEquals(9.60f, cc.getCautionLevel(), .01);
		Assert.assertEquals(9.60f, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
	
	@Test
	public void testInvalid_FishState(){
		cc.setState(FishState.FLEE);
		
		objs.add(cc);
		
		executeStateless(objs);
		
		Assert.assertEquals(0, cc.getCautionLevel(), .01);
		Assert.assertEquals(0, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.FLEE, cc.getState());
	}
	
	@Test
	public void testInvalid_SpeedOrCautionLvl(){
		cc.setCautionLevel(6.00f);
		
		objs.add(cc);
		
		executeStateless(objs);
		
		Assert.assertEquals(9.60f, cc.getCautionLevel(), .01);
		Assert.assertEquals(9.60f, cc.getSpeed(), .01);
		Assert.assertEquals(FishState.CAUTION, cc.getState());
	}
}
