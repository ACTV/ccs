package actv.rules.idle.two;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.ccs.sageTest.MoveActionFactory;
import actv.rules.DroolsTest;

public class MoveToTest extends DroolsTest {
	private ConvictCichlid cc1, cc2;
	private ArrayList<CCSMemoryObject> objs;
	private Logger logger = LoggerFactory.getLogger(MoveToTest.class);
	
	public MoveToTest(){
		super(	"actv/ccs/rules/idle/MoveTo.drl", 
				"actv/ccs/flow/swim.bpmn",
				"swim");
	}
	
	@Before
	public void setCC(){
		cc1 = new ConvictCichlid(1, 2, 3, "test", new Point3D(2, 3, 0));
		cc1.setState(FishState.SWIM);
		cc1.setDirection(new Vector3D(1, 1, 0));
		cc1.setAggroLevel(4.00f);
		cc1.setBaseSpeed(6.00f);
		cc1.setSpeed(8.00f);
		cc1.setInfluence(10);
		
		cc2 = new ConvictCichlid(1, 2, 3, "test", new Point3D(4, 4, 0));
		cc2.setState(FishState.SWIM);
		cc2.setDirection(new Vector3D(1, -1, 0));
		cc2.setAggroLevel(4.00f);
		cc2.setBaseSpeed(6.00f);
		cc2.setSpeed(8.00f);
		cc2.setInfluence(10);
		
	}
	
	@Test
	public void test(){
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc1);
		objs.add(cc2);
		
		logger.info("CC1 Loc {}", cc1.getLocation().toString());
		logger.info("CC1 Dir {}", cc1.getDirection().toString());
		
		logger.info("CC2 Loc {}", cc2.getLocation().toString());
		logger.info("CC2 Dir {}", cc2.getDirection().toString());
		executeStateless(objs);
		
		logger.info("Influence: {}", cc1.getInfluence());
		logger.info("Final distance: {}", MoveActionFactory.distance(cc1, cc2));
		
	}

}
