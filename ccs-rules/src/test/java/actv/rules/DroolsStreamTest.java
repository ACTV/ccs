package actv.rules;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.conf.EventProcessingOption;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.runtime.StatefulKnowledgeSession;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.CCSListener;
import actv.ccs.SessionThread;
import actv.ccs.model.CCSMemoryObject;

/**
 * 
 * 	Create/Execute a stateless session to unit test a rule.
 *
 */
public class DroolsStreamTest{
	private SessionThread sessionThread;
	private StatefulKnowledgeSession sks; 
	private String startProc;
	
	public DroolsStreamTest(String drl, String flowFile, String startProc){
		// Retrieve KnowledgeBuilder
		KnowledgeBuilder kbuilder = CCSKnowledgeBase.initKBuilder(new String[]{drl, flowFile});
		
		if(kbuilder.hasErrors()){
			System.out.println(kbuilder.getErrors());
			return;
		}
		
		// Create KnowledgeBase from KnowledgeBuilder
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration());
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		// Initialize a stateful knowledge session
		sks = kb.newStatefulKnowledgeSession();
		
		sks.addEventListener((WorkingMemoryEventListener)new CCSListener());
		sks.addEventListener((ProcessEventListener)new CCSListener());
		sks.addEventListener((AgendaEventListener)new CCSListener());

		this.startProc = startProc;
	}
	
	/**
	 * 
	 * Execution specific to stateless junit session
	 */
	public StatefulKnowledgeSession execute(CCSMemoryObject...objects){
		
		// Insert objects (and start process) 
		for(Object obj : objects){
			sks.insert(obj);
		}
		sks.startProcess(startProc);
		
		new Thread(){
			public void run(){
				// Execute the rules on another thread
				sessionThread = SessionThread.getInstance();
				sessionThread.setStatefulKnowledgeSession(sks);
				sessionThread.run();
			}
		}.start();
		
		return sks;
	}
	
	private KnowledgeBaseConfiguration getKnowledgeBaseConfiguration(){
		KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);
		return config;
	}
}
