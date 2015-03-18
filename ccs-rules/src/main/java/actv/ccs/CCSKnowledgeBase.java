package actv.ccs;

import java.io.IOException;
import java.util.ArrayList;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/*
 * 
 * TODO Cleanup the code!!!
 * 
 */
public class CCSKnowledgeBase {
	//private static KnowledgeBuilder kbuilder;
	private static KnowledgeBuilderConfiguration config;
	private static final String [] packages = { "actv/ccs/rules/start",
												"actv/ccs/flow"};
	private static final Logger log = LoggerFactory.getLogger(CCSKnowledgeBase.class); 
	
	public static String [] getPackages(){
		return packages;
	}
	
	
	/**
	 * 
	 * Static entrypoint for main game
	 */
	public static void executeSession(Object...objs){
		StatefulKnowledgeSession sks = setupSession();
		insertObjects(sks, objs);
		
		long start_time = System.currentTimeMillis();
//		sks.execute(CommandFactory.newBatchExecution(cmds));
		
		sks.fireAllRules();
		
		log.info("Execution time: {}", System.currentTimeMillis() - start_time);
		sks.dispose();
		
	}
	
	public static void executeInfiniteSession(Object...objs){
		StatefulKnowledgeSession sks = setupSession();
		insertObjects(sks, objs);
		
		long start_time = System.currentTimeMillis();
		sks.fireUntilHalt();
		sks.halt();
		log.info("Execution time: {}", System.currentTimeMillis() - start_time);
		sks.dispose();
	}
	
	private static void insertObjects(StatefulKnowledgeSession sks, Object[] objs){
		for(Object obj : objs){
			//cmds.add(CommandFactory.newInsert(obj));
			sks.insert(obj);
		}
		//cmds.add(CommandFactory.newStartProcess("start"));
//		sks.startProcess("start");
		sks.startProcess("swim");
	}
	
	private static StatefulKnowledgeSession setupSession(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getConfig()); 

		//TODO: hardcoded the rules for now.
		addDrl(kbuilder, "actv/ccs/rules/start/Cooldown.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Start.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Calm.drl");
		addDrl(kbuilder, "actv/ccs/rules/idle/Idle.drl");
		addBpmn(kbuilder, "actv/ccs/flow/start.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/idle.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/swim.bpmn");
		
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession();

		ArrayList<Command> cmds = new ArrayList<Command>();
		
		
		//TODO Temporary event listener
		sks.addEventListener(new WorkingMemoryEventListener() {
			
			public void objectUpdated(ObjectUpdatedEvent event) {
				// TODO Auto-generated method stub
			}
			
			public void objectRetracted(ObjectRetractedEvent event) {
				// TODO Auto-generated method stub
			}
			
			public void objectInserted(ObjectInsertedEvent event) {
				log.info("Inserted {}", event.getObject().toString());
			}
		});
		
		return sks;
	}
	
	/**
	 * 
	 * Setup the KnowledgeBuilderConfiguration
	 * 
	 */
	public static KnowledgeBuilderConfiguration getConfig(){
		if(config == null){
			// Set up default Drools dialect to Java
			KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
			config.setProperty("drools.dialect.default", "mvel");
		}
		return config;
	}
	
	/**
	 * Requires a comma-separated list of paths to resources
	 * @param String
	 * @return KnowledgeBuilder
	 */
	public static KnowledgeBuilder initKBuilder(String [] resources){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getConfig());
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
		// TODO: Remove hardcoded path(s) for scalability in the future
		kbuilder.add(ResourceFactory.newClassPathResource(pkg), ResourceType.PKG);
	}
	
	private static void addBpmn(KnowledgeBuilder kbuilder, String flowFile){
		// Add rule and flow file to the KnowledgeBuilder
		ClassPathResource flow = new ClassPathResource(flowFile);
		try{
			kbuilder.add(ResourceFactory.newUrlResource(flow.getURL()), ResourceType.BPMN2);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
/*	public void ExecuteCCSKB(ArrayList<TankObject> tankObjects){
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
//		tankObjects.put("startProcess", "start");
		
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession();
		//TODO: Inserting objects into a stateful session with a hash map.
		for(TankObject obj : resources){
			sks.insert(obj);
		}
	}*/
	

}
