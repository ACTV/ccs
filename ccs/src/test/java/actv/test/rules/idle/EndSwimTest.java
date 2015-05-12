package actv.test.rules.idle;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.MoveCounter;
import actv.ccs.fact.SwimCounter;
import actv.ccs.model.ConvictCichlid;
import actv.test.rules.CCSTestListener;
import actv.test.rules.DroolsTest;

public class EndSwimTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public EndSwimTest(){
		super(	"actv/ccs/rules/idle/EndSwim.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "End Swim CC", new Point3D(1, 1, 1));
		cc.setDirection(new Vector3D(0,0,1));
	}
	
	@Test
	public void testNotFired(){
		objs = new ArrayList<Object>();
		objs.add(new SwimCounter(cc, 20));
		
		executeStateless(objs);
		
		Assert.assertFalse(CCSTestListener.isFired("End Swim"));
	}
	
	@Test
	public void testFired(){
		objs = new ArrayList<Object>();
		objs.add(new SwimCounter(cc, 0));
		
		executeStateless(objs);
		
		Assert.assertTrue(CCSTestListener.isFired("End Swim"));
	}
	

}
