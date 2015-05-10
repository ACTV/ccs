package actv.test.rules.idle;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import actv.ccs.model.ConvictCichlid;
import actv.test.rules.DroolsTest;

public class MakeMoveTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<Object> objs;
	
	public MakeMoveTest(){
		super(	"actv/ccs/rules/idle/MakeMove.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "Move CC", new Point3D(1, 1, 1));
		cc.setDirection(new Vector3D(0,0,1));
	}
	
	@Test
	public void test(){
		objs = new ArrayList<Object>();
		objs.add(cc);
		
		executeStateless(objs);
		
	}

}
