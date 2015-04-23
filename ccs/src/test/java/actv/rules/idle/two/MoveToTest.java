package actv.rules.idle.two;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import sage.model.loader.OBJLoader;
import sage.scene.TriMesh;
import actv.ccs.fact.Auditor;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.rules.DroolsTest;

public class MoveToTest extends DroolsTest {
	private ConvictCichlid cc1, cc2;
	private Auditor auditor;
	private ArrayList<CCSMemoryObject> objs;
	
	public MoveToTest(){
		super(	"actv/ccs/rules/idle/two/MoveTo.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc1 = new ConvictCichlid();
		cc1.setState(FishState.SWIM);
		cc1.setDirection(new Vector3D(1, 1, 0));
		cc1.setLocation(new Point3D(2, 3, 0));
		cc1.setAggroLevel(4.00f);
		cc1.setBaseSpeed(6.00f);
		cc1.setSpeed(8.00f);
		cc1.setInfluence(10);
		
		cc2 = new ConvictCichlid();
		cc2.setState(FishState.SWIM);
		cc2.setDirection(new Vector3D(1, -1, 0));
		cc2.setLocation(new Point3D(4, 4, 0));
		cc2.setAggroLevel(4.00f);
		cc2.setBaseSpeed(6.00f);
		cc2.setSpeed(8.00f);
		cc2.setInfluence(10);
		
		auditor = new Auditor();
	}
	
	@Test
	public void test(){
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc1);
		objs.add(cc2);
		objs.add(auditor);
		
		executeStateless(objs);
		
		System.out.println("Rules: " + auditor.getRulesFired().size());
	}

}
