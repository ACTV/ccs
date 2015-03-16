package actv.rules;

import java.util.ArrayList;

import org.drools.builder.KnowledgeBuilder;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;

public class CCSKnowledgeBaseTest{
	private ConvictCichlid cc;

	@Before
	public void setupFish(){
		cc = new ConvictCichlid();
		cc.setState(FishState.SWIM);
		cc.setBaseAggroLevel(5.00f);
		cc.setBaseCautionLevel(5.00f);
		cc.setBaseSpeed(5.00f);
	}
	
	@Test
	public void test_Package(){
		ArrayList<Object> objs = new ArrayList<Object>();
		objs.add(cc);
		
		KnowledgeBuilder kbuilder = CCSKnowledgeBase.initKBuilder(CCSKnowledgeBase.getPackages());
		StatefulKnowledgeSession sks = CCSKnowledgeBase.executeSession(kbuilder, objs);
		sks.halt();
		
	}
}
