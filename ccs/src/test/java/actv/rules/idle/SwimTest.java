package actv.rules.idle;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class SwimTest extends DroolsTest {
	private ConvictCichlid cc;
	private ArrayList<CCSMemoryObject> objs;
	
	public SwimTest(){
		super(	"actv/ccs/rules/idle/Swim.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc = new ConvictCichlid(1, 2, 3, "test", new Point3D(1, 1, 1));
		cc.setState(FishState.IDLE);
		cc.setDirection(new Vector3D(0,0,1));
	}
	
	@Test
	public void test(){
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		
		executeStateful(5000, objs);
		
	}

}
