package actv.rules.idle;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.CCSTestListener;
import actv.ccs.fact.MoveCounter;
import actv.ccs.model.ConvictCichlid;
import actv.rules.DroolsTest;

public class EndMoveTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public EndMoveTest(){
		super(	"actv/ccs/rules/idle/EndMove.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "End Move CC", new Point3D(1, 1, 1));
		cc.setDirection(new Vector3D(0,0,1));
	}
	
	@Test
	public void test(){
		objs = new ArrayList<Object>();
		objs.add(new MoveCounter(cc, 0, 1));
		
		executeStateless(objs);
		
		Assert.assertTrue(CCSTestListener.isFired("End Move"));
	}
	

}
