package actv.rules;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class CCSKnowledgeBaseTest{
	private ConvictCichlid cc;
	private Auditor auditor;
	private static Logger log = LoggerFactory.getLogger(CCSKnowledgeBaseTest.class);

	@Before
	public void setupFish(){
		cc = new ConvictCichlid();
		cc.setState(FishState.NONE);
		cc.setBaseAggroLevel(5.00f);
		cc.setBaseCautionLevel(5.00f);
		cc.setBaseSpeed(5.00f);
		auditor = new Auditor();
	}
	
	@Test
	public void test_Package(){
		
		CCSKnowledgeBase.executeSession(cc, auditor);
		//Assert.assertEquals(FishState.IDLE, cc.getState());
		Assert.assertTrue(auditor.getRulesFired().size() >= 1);
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
