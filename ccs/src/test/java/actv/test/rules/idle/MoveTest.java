package actv.test.rules.idle;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.fact.MoveCounter;
import actv.ccs.model.ConvictCichlid;
import actv.test.rules.DroolsTest;

public class MoveTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	private MoveCounter mc;
	
	public MoveTest(){
		super(	"actv/ccs/rules/idle/Move.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "Move CC", new Point3D(1, 1, 1));
		cc.setDirection(new Vector3D(0,0,1));
		mc = new MoveCounter(cc, 20, 1);
	}
	
	@Test
	public void test(){
		objs = new ArrayList<Object>();
		objs.add(cc);
		objs.add(mc);
		
		Vector3D dir = cc.getDirection();
		
		executeStateless(objs);
		
		System.out.println("Direction: " + cc.getDirection());
		Assert.assertFalse(dir.equals(cc.getDirection()));
	}

}
