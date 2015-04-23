package actv.rules;

import graphicslib3D.Vector3D;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBaseBuilder;
import actv.ccs.CCSKnowledgeSession;
import actv.ccs.fact.Auditor;
import actv.ccs.listener.RuleEngineRunner;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class CCSKnowledgeBaseTest{
	private ConvictCichlid cc;
	private Auditor auditor;
	private long start;
	private static Logger log = LoggerFactory.getLogger(CCSKnowledgeBaseTest.class);
	private ArrayList<CCSMemoryObject> objs;
	private CCSKnowledgeSession session = CCSKnowledgeSession.getInstance();

	@Before
	public void setupFish(){
		cc = new ConvictCichlid();
		cc.setState(FishState.NONE);
		cc.setBaseAggroLevel(5.00f);
		cc.setBaseCautionLevel(5.00f);
		cc.setBaseSpeed(5.00f);
		cc.setCautionLevel(2f);
		cc.setDirection(new Vector3D(1,3,2));
		cc.setIdleWaitTime(0);
		
		auditor = new Auditor();
		start = System.currentTimeMillis();
	}
	
	@After
	public void printTime(){
		log.info("Execution time {}", System.currentTimeMillis() - start);
	}
	
	@Test
	public void testInfinite(){
		log.info(">>Testing infinite execution with initial NONE fish state");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		//session.setStatefulKnowledgeSession(CCSKnowledgeBaseBuilder.buildStatefulSession(objs));
		RuleEngineRunner r = RuleEngineRunner.getInstance();
		
		try{
			log.info("Sleeping thread...");
			Thread.sleep(6000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		session.terminate();
	}
	
	@Test
	public void testInfinite_Idle(){
		cc.setState(FishState.IDLE);
		cc.setIdleWaitTime(2000);
		
		log.info(">>Testing Infinte with IDLE as starting fish state");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);

		session.setStatefulKnowledgeSession(CCSKnowledgeBaseBuilder.buildStatefulSession(objs));
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		session.terminate();
	}
	
}
