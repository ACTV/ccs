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

import actv.ccs.fact.PRNG;
import actv.ccs.listener.CCSListener;

/**
 * The CCSKnowledgeBaseBuilder contains three methods to build and create a Stateful Knowledge Session for simulation.
 *
 */
public class CCSKnowledgeBaseBuilder{
	private static final Logger log = LoggerFactory.getLogger(CCSKnowledgeBaseBuilder.class); 
	
	/**
	 * This method creates a Stateful Knowledge Session with all the available rules to fire with ONLY.
	 * No objects are inserted in the return session.
	 * 
	 * @return
	 */
	public static StatefulKnowledgeSession buildStatefulSession(){
		// TODO: Paths to drls are hardcoded in!!
		return setupSession("swim", new String[]{	"actv/ccs/rules/start/CoolingDown.drl",
										 			"actv/ccs/rules/start/Start.drl",
												 	"actv/ccs/rules/start/InitializeCichlid.drl",
													"actv/ccs/rules/start/Calm.drl",
													"actv/ccs/rules/idle/Idle.drl",
													"actv/ccs/rules/idle/Move.drl",
													"actv/ccs/rules/idle/MakeMove.drl",
													"actv/ccs/rules/idle/EndMove.drl",
													"actv/ccs/rules/idle/EndSwim.drl",
													"actv/ccs/rules/idle/Swim.drl",
													"actv/ccs/rules/idle/MakeSwim.drl",
													"actv/ccs/rules/bounds/BoundsX.drl",
													"actv/ccs/rules/bounds/BoundsY.drl",
													"actv/ccs/rules/bounds/BoundsZ.drl",
													//"actv/ccs/rules/idle/MoveTo.drl",
													"actv/ccs/flow/swim.bpmn" });
	}
	
	
	/**
	 * This method creates a Stateful Knowledge Session with all the available rules to fire with. CCSMemoryObjects
	 * that are passed in are inserted into the session.
	 * 
	 * @param objs
	 * @return
	 */
	public static StatefulKnowledgeSession buildStatefulSession(ArrayList<Object> objs){
		StatefulKnowledgeSession sks = buildStatefulSession();
		insertObjects(sks, objs);
		
		return sks;
	}
	
	/**
	 * This method takes in one specific rule for execution
	 * 
	 * @param drl
	 * @param bpmn
	 * @param flow
	 * @param objs
	 * @return
	 */
	public static StatefulKnowledgeSession buildStatefulSession(String drl, String bpmn, String flow, ArrayList<Object> objs){
		StatefulKnowledgeSession sks = setupSession(flow, new String[]{drl, bpmn});
		
		insertObjects(sks, objs);
		
		return sks;
	}

	/**
	 * Creates and returns a prepared StatefulKnowledgeSession
	 * 
	 * @param flow
	 * @param resources
	 * @return
	 */
	private static StatefulKnowledgeSession setupSession(String flow, String [] resources){
		KnowledgeBuilder kbuilder = initKBuilder(resources);
		
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration());
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession(getKnowledgeSessionConfiguration(), null);
		
		sks.startProcess(flow);
		
		sks.setGlobal("logger", log);
		sks.setGlobal("prng", new PRNG());
		
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
	
	private static void insertObjects(StatefulKnowledgeSession sks, ArrayList<Object> objs){
		for(Object obj : objs){
			sks.insert(obj);
		}
		
		// Insert the random number generator
		sks.insert(new PRNG());
	}
	
	/**
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
		
		if (kbuilder.hasErrors()){
            log.error(kbuilder.getErrors().toString());
            throw new RuntimeException(kbuilder.getErrors().toString());
        }
		
		return kbuilder;
	}
	
	private static void addDrl(KnowledgeBuilder kbuilder, String drl){
		kbuilder.add(ResourceFactory.newClassPathResource(drl), ResourceType.DRL);
		log.trace("Added drl: {}", drl);
	}

	private static void addPackage(KnowledgeBuilder kbuilder, String pkg){
		// Add the package(s)
		kbuilder.add(ResourceFactory.newClassPathResource(pkg), ResourceType.PKG);
		log.trace("Adding package: {}", pkg);
	}
	
	private static void addBpmn(KnowledgeBuilder kbuilder, String flowFile){
		ClassPathResource flow = new ClassPathResource(flowFile);
		try{
			kbuilder.add(ResourceFactory.newUrlResource(flow.getURL()), ResourceType.BPMN2);
		}catch(IOException e){
			e.printStackTrace();
		}
		log.trace("Added bpmn: {}", flowFile);
	}

}
