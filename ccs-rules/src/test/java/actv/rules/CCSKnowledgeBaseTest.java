package actv.rules;

import java.util.ArrayList;

import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class CCSKnowledgeBaseTest{
	private ConvictCichlid cc;
	private Auditor auditor;
	private long start;
	private static Logger log = LoggerFactory.getLogger(CCSKnowledgeBaseTest.class);
	private ArrayList<CCSMemoryObject> objs;

	@Before
	public void setupFish(){
		cc = new ConvictCichlid();
		cc.setState(FishState.NONE);
		cc.setBaseAggroLevel(5.00f);
		cc.setBaseCautionLevel(5.00f);
		cc.setBaseSpeed(5.00f);
		cc.setCautionLevel(2f);
		cc.setIdleWaitTime(0);
		auditor = new Auditor();
		start = System.currentTimeMillis();

	}
	
	@After
	public void printTime(){
		log.info("Execution time {}", System.currentTimeMillis() - start);
	}
	
//	@Test
	public void test_Package(){
		log.info("test_Package:");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		CCSKnowledgeBase.executeSession(objs);
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
	}
	
//	@Test
	public void test_Package_IDLE(){
		log.info("test_Package:");
		
		cc.setState(FishState.IDLE);
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		CCSKnowledgeBase.executeSession(objs);
		
		Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
	}
	
	@Test
	public void testInfinite(){
		log.info(">>Testing infinite execution with initial NONE fish state");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		CCSKnowledgeBase.executeInfiniteSession(objs);

		try{
			log.info("Sleeping thread...");
			Thread.sleep(6000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		CCSKnowledgeBase.disposeSession();
		
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
		Assert.assertTrue(auditor.getRulesFired().contains("Cooling Down"));
	}
	
	@Test
	public void testInfinite_Idle(){
		cc.setState(FishState.IDLE);
		cc.setIdleWaitTime(2000);
		
		log.info(">>Testing Infinte with IDLE as starting fish state");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);

		CCSKnowledgeBase.executeInfiniteSession(objs);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CCSKnowledgeBase.disposeSession();
		
		Assert.assertEquals(2, auditor.getRulesFired().size());
	}
	
}
