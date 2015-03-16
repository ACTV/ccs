package actv.ccs;

import java.io.IOException;
import java.util.ArrayList;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.cdi.KBase;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class CCSKnowledgeBase {
	private static KnowledgeBuilder kbuilder;
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
	public static StatefulKnowledgeSession executeSession(KnowledgeBuilder kbuilder, ArrayList<Object> objs){
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession();
		
		if(kb.getRule("actv.ccs.rules.start", "Start.drl") == null)
			log.info("No Rules!");
		
		if(kb.getProcesses().isEmpty()){
			log.info("No Processes!");
		}else{
		for(org.drools.definition.process.Process p : kb.getProcesses()){
			log.info("Processes: {}", p.getName());
		}
		}
		for(Object obj : objs){
			log.info("Inserting object {}", obj.getClass());
			sks.insert(obj);
		}
	
		sks.startProcess("start");
		
		sks.fireAllRules();
		
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
			config.setProperty("drools.dialect.default", "java");
		}
		return config;
	}
	
	/**
	 * Requires a comma-separated list of paths to resources
	 * @param String
	 * @return KnowledgeBuilder
	 */
	public static KnowledgeBuilder initKBuilder(String [] resources){
		kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getConfig());
		int startExt = 0;
		// Add resources
		for(String rsrc : resources){
			if((startExt = rsrc.lastIndexOf('.')) != -1){
				if(rsrc.substring(startExt, rsrc.length()).equals(".bpmn")){
					log.info("Adding BPMN: {}", rsrc);
					addBpmn(kbuilder, rsrc);
				}else if(rsrc.substring(startExt, rsrc.length()).equals(".drl")){
					log.info("Adding DRL: {}", rsrc);
					addDrl(kbuilder, rsrc);
				}	
			}else{ // Resource is a package
				log.info("Adding PKG: {}", rsrc);
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
