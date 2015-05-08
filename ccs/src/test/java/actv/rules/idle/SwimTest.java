package actv.rules.idle;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import actv.ccs.CCSTestListener;
import actv.ccs.fact.SwimCounter;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class SwimTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public SwimTest(){
		super(	"actv/ccs/rules/idle/Swim.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "Swim CC", new Point3D(1, 1, 1));
		cc.setState(FishState.IDLE);
		cc.setSpeed(4);
		cc.setDirection(new Vector3D(0,0,1));
	}
	
	@Test
	public void test(){
		objs = new ArrayList<Object>();
		objs.add(cc);
		objs.add(new SwimCounter(cc, 10));
		
		executeStateless(objs);
		
		Assert.assertTrue(CCSTestListener.isFired("Swim"));
	}
	
	@Test
	public void testNo(){
		cc.setSpeed(0);
		objs = new ArrayList<Object>();
		objs.add(cc);
		objs.add(new SwimCounter(cc, 10));
		
		executeStateless(objs);
		
		Assert.assertTrue(CCSTestListener.isFired("Swim"));
	}
	
	

}
