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
	
	@Test
	public void test_Package(){
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
		log.info("testInfinite");
		objs = new ArrayList<CCSMemoryObject>();
		objs.add(cc);
		objs.add(auditor);
		
		StatefulKnowledgeSession sks = CCSKnowledgeBase.executeInfiniteSession(objs);
		long start = System.currentTimeMillis();
		/*if(start + 100000 > System.currentTimeMillis()){
			sks.halt();
			log.info("Halting session!");
		}*/
		log.info("auditor size: {}", auditor.getRulesFired().size());
		//Assert.assertTrue(auditor.getRulesFired().size() >= 0);
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
		sks.dispose();
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
