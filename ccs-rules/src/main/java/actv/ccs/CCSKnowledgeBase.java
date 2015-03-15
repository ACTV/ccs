package actv.ccs;

import java.util.HashMap;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class CCSKnowledgeBase {
	private KnowledgeBuilder kbuilder;

	public static KnowledgeBuilder initKBuilder(String...resources){
		// Set up default Drools dialect to MVEL
		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		config.setProperty("drools.dialect.default", "java");
		
		return KnowledgeBuilderFactory.newKnowledgeBuilder(config);
	}
	
	public void ExecuteCCSKB(HashMap<String, Object> tankObjects){
		kbuilder = initKBuilder();
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		tankObjects.put("startProcess", "start");
		
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession();
		//TODO: Inserting objects into a stateful session with a hash map.
	}
	

}
