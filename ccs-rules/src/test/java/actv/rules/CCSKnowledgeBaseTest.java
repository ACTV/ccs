package actv.rules;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.drools.time.SessionPseudoClock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.fact.IdleTimer;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class CCSKnowledgeBaseTest{
	private ConvictCichlid cc;
	private Auditor auditor;
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
	}
	
	//@Test
	public void test_Package(){
		log.info("test_Package:");
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		CCSKnowledgeBase.executeSession(objs);
		//Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
	}
	
	//@Test
	public void test_Package_IDLE(){
		cc.setState(FishState.IDLE);
		log.info("test_Package:");
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		CCSKnowledgeBase.executeSession(objs);
		//Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
	}
	
	@Test
	public void testInfinite(){
		log.info(">>Testing infinite execution with initial START fish state");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		StatefulKnowledgeSession sks = CCSKnowledgeBase.executeInfiniteSession(objs);
		long start = System.currentTimeMillis();

		try{
			log.info("Sleeping thread...");
			Thread.sleep(6000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		sks.halt();
		sks.dispose();
		
		log.info("Execution time {}", System.currentTimeMillis() - start);
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
	}
	
	//@Test
	public void testInfinite_Idle(){
		cc.setState(FishState.IDLE);
		cc.setIdleWaitTime(2000);
		
		log.info(">>Testing Infinte with IDLE as starting fish state");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);

		StatefulKnowledgeSession sks = CCSKnowledgeBase.executeInfiniteSession(objs);
		long start = System.currentTimeMillis();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sks.halt();
		sks.dispose();
		
		log.info("Execution time {}", System.currentTimeMillis() - start);
		Assert.assertEquals(2, auditor.getRulesFired().size());
	}
	
	//@Test
	public void testInfinite_Idle_ThreadSleep(){
		cc.setState(FishState.IDLE);
		cc.setIdleWaitTime(2000);
		
		log.info(">>Testing infinite Idle with a session thread sleep");
		
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);

		StatefulKnowledgeSession sks = CCSKnowledgeBase.executeInfiniteSession(objs);
		long start = System.currentTimeMillis();
		
		log.info("Sleeping session thread for 2 seconds");
		try {
			CCSKnowledgeBase.getSessionThread().sleep(2000);;
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("Waking session thread after 2 seconds");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sks.halt();
		sks.dispose();
		
		log.info("Execution time {}", System.currentTimeMillis() - start);
		Assert.assertEquals(3, auditor.getRulesFired().size());
	}
	
	
	@After
	public void printFish(){
		log.info("ConvictCichlid");
		log.info("\tState: {}", cc.getState());
		log.info("\tBase Aggro: {}", cc.getBaseAggroLevel());
		log.info("\tBase Caution: {}", cc.getBaseCautionLevel());
		log.info("\tCaution: {}", cc.getCautionLevel());
		log.info("\tSpeed: {}", cc.getSpeed());
		
		for(String rule : auditor.getRulesFired()){
			log.info("Rule fired: {}", rule);
		}
	}
}
