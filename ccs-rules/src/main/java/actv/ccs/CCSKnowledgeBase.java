package actv.ccs;

import java.util.HashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class CCSKnowledgeBase {
	
	public static void ExecuteCCSKB(HashMap<String, Object> tankObjects){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		tankObjects.put("startProcess", "start");
		
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession();
		//TODO: Inserting objects into a stateful session with a hash map.
	}
	
	//TODO:
	private void initKBuilder(KnowledgeBuilder kbuilder, String...resources){
		
	}

}
