package actv.ccs;

import java.io.IOException;
import java.util.ArrayList;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.process.Process;
import org.drools.definition.rule.Rule;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import actv.ccs.model.CCSMemoryObject;

/*
 * 
 * TODO Cleanup the code!!!
 * 
 */
public class CCSKnowledgeBase{
	private static SessionThread sessionThread;
	private static final Logger log = LoggerFactory.getLogger(CCSKnowledgeBase.class); 
	private static final String [] packages = { "actv/ccs/rules/start",
												"actv/ccs/flow"};
	
	/**
	 * 
	 * Static entrypoint for main game
	 */
	public static void executeSession(ArrayList<CCSMemoryObject> objs){
		StatefulKnowledgeSession sks = setupSession();
		insertObjects(sks, objs);
		
		sks.startProcess("swim");
				
		long start_time = System.currentTimeMillis();
		
		sks.fireAllRules();
		
		log.debug("Execution time: {}", System.currentTimeMillis() - start_time);
		sks.dispose();
		
	}
	
	public static StatefulKnowledgeSession executeInfiniteSession(ArrayList<CCSMemoryObject> objs){
		final StatefulKnowledgeSession sks = setupSession();
		insertObjects(sks, objs);
		
		sks.startProcess("swim");
		
		startTheSession(sks);
		
		return sks;
	}
	
	private static void startTheSession(StatefulKnowledgeSession sks){
		// Execute the rules on another thread
		sessionThread = SessionThread.getInstance();
		sessionThread.setStatefulKnowledgeSession(sks);
		sessionThread.start();
	}
	
	public static Thread getSessionThread(){
		return sessionThread;
	}
	
	private static StatefulKnowledgeSession setupSession(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getKnowledgeBuilderConfiguration()); 

		//TODO: hardcoded the rules for now.
		addDrl(kbuilder, "actv/ccs/rules/start/Cooldown.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Start.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Calm.drl");
		addDrl(kbuilder, "actv/ccs/rules/idle/Idle.drl");
		addDrl(kbuilder, "actv/ccs/rules/Swim.drl");
		addBpmn(kbuilder, "actv/ccs/flow/start.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/idle.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/swim.bpmn");
		
		if (kbuilder.hasErrors()){
            log.error(kbuilder.getErrors().toString());
            throw new RuntimeException(kbuilder.getErrors().toString());
        }
		
		for(KnowledgePackage kp : kbuilder.getKnowledgePackages()){
			log.info("Knowledge Package: {}", kp.getName());
			for(Rule r : kp.getRules()){
				log.info("Rule: {}", r.getName());
			}
			for(Process p : kp.getProcesses()){
				log.info("Process: {}", p.getName());
			}
		}
		
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration());
		
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession(getKnowledgeSessionConfiguration(), null);

		addEventListeners(sks);
		
		return sks;
	}
	
	private static void addEventListeners(StatefulKnowledgeSession sks){
		sks.addEventListener((WorkingMemoryEventListener)new CCSListener());
		sks.addEventListener((ProcessEventListener)new CCSListener());
		sks.addEventListener((AgendaEventListener)new CCSListener());
	}
	
	private static KnowledgeBuilderConfiguration getKnowledgeBuilderConfiguration(){
		// Set up default Drools dialect to mvel
		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		config.setProperty("drools.dialect.default", "mvel");
		return config;
	}
	
	private static KnowledgeBaseConfiguration getKnowledgeBaseConfiguration(){
		KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);
		return config;
	}
	
	private static KnowledgeSessionConfiguration getKnowledgeSessionConfiguration(){
		KnowledgeSessionConfiguration config = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
//		config.setOption(ClockTypeOption.get("pseudo"));
		config.setOption(ClockTypeOption.get("realtime"));
		return config;
	}
	
	private static void insertObjects(StatefulKnowledgeSession sks, ArrayList<CCSMemoryObject> objs){
		for(Object obj : objs){
			sks.insert(obj);
		}
	}
	
	/**
	 * Requires a comma-separated list of paths to resources
	 * @param String
	 * @return KnowledgeBuilder
	 */
	public static KnowledgeBuilder initKBuilder(String [] resources){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getKnowledgeBuilderConfiguration());
		int startExt = 0;
		// Add resources
		for(String rsrc : resources){
			if((startExt = rsrc.lastIndexOf('.')) != -1){
				if(rsrc.substring(startExt, rsrc.length()).equals(".bpmn")){
					addBpmn(kbuilder, rsrc);
				}else if(rsrc.substring(startExt, rsrc.length()).equals(".drl")){
					addDrl(kbuilder, rsrc);
				}	
			}else{ // Resource is a package
				addPackage(kbuilder, rsrc);
			}
		}
		
		return kbuilder;
	}
	
	private static void addDrl(KnowledgeBuilder kbuilder, String drl){
		log.debug("Adding resource {}", drl);
		kbuilder.add(ResourceFactory.newClassPathResource(drl), ResourceType.DRL);
	}

	private static void addPackage(KnowledgeBuilder kbuilder, String pkg){
		// Add the package(s)
		kbuilder.add(ResourceFactory.newClassPathResource(pkg), ResourceType.PKG);
	}
	
	private static void addBpmn(KnowledgeBuilder kbuilder, String flowFile){
		log.debug("Adding resource {}", flowFile);
		ClassPathResource flow = new ClassPathResource(flowFile);
		try{
			kbuilder.add(ResourceFactory.newUrlResource(flow.getURL()), ResourceType.BPMN2);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
