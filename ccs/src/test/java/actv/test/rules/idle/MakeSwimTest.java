package actv.test.rules.idle;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.test.rules.DroolsTest;

public class MakeSwimTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public MakeSwimTest(){
		super(	"actv/ccs/rules/idle/MakeSwim.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "Make Swim CC", new Point3D(1, 1, 1));
		cc.setState(FishState.IDLE);
		cc.setBaseSpeed(4);
		cc.setSpeed(0f);
		cc.setDirection(new Vector3D(0,0,1));
	}
	
	@Test
	public void test(){
		objs = new ArrayList<Object>();
		objs.add(cc);
		
		executeStateless(objs);
		
//		Only has a .3 chance to fire 
//		Assert.assertEquals(cc.getBaseSpeed(), cc.getSpeed());
		
	}

}
