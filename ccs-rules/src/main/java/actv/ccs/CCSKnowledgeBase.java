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
import org.drools.command.Command;
import org.drools.conf.EventProcessingOption;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
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
public class CCSKnowledgeBase {
	private static KnowledgeBuilder kbuilder;
	private static KnowledgeBuilderConfiguration config;
	private static final String [] packages = { "actv/ccs/rules/start",
												"actv/ccs/flow"};
	private static final Logger log = LoggerFactory.getLogger(CCSKnowledgeBase.class); 
	
	/**
	 * 
	 * Static entrypoint for main game
	 */
	public static void executeSession(ArrayList<CCSMemoryObject> objs){
		StatefulKnowledgeSession sks = setupSession();
		insertObjects(sks, objs);
		
		//TODO: Not working anymore?
		sks.startProcess("swim");
				
		long start_time = System.currentTimeMillis();
		
		sks.fireAllRules();
		
		log.info("Execution time: {}", System.currentTimeMillis() - start_time);
		sks.dispose();
		
	}
	
	public static StatefulKnowledgeSession executeInfiniteSession(ArrayList<CCSMemoryObject> objs){
		final StatefulKnowledgeSession sks = setupSession();
		insertObjects(sks, objs);
		
		//TODO: Not working anymore?
		sks.startProcess("swim");
		
		new Thread(){
			public void run(){
				sks.fireUntilHalt();
			}
		}.start();
		
		return sks;
	}
	
	private static StatefulKnowledgeSession setupSession(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getKnowledgeBuilderConfiguration()); 

		//TODO: hardcoded the rules for now.
		addDrl(kbuilder, "actv/ccs/rules/start/Cooldown.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Start.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Calm.drl");
		addDrl(kbuilder, "actv/ccs/rules/idle/Idle.drl");
		addBpmn(kbuilder, "actv/ccs/flow/start.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/idle.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/swim.bpmn");
		
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration());
		
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession(getKnowledgeSessionConfiguration(), null);

		

		//sks.addEventListener(new CCSListener());
		
		return sks;
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
		config.setOption(ClockTypeOption.get("pseudo"));
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
		kbuilder.add(ResourceFactory.newClassPathResource(drl), ResourceType.DRL);
	}

	private static void addPackage(KnowledgeBuilder kbuilder, String pkg){
		// Add the package(s)
		kbuilder.add(ResourceFactory.newClassPathResource(pkg), ResourceType.PKG);
	}
	
	private static void addBpmn(KnowledgeBuilder kbuilder, String flowFile){
		ClassPathResource flow = new ClassPathResource(flowFile);
		try{
			kbuilder.add(ResourceFactory.newUrlResource(flow.getURL()), ResourceType.BPMN2);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
