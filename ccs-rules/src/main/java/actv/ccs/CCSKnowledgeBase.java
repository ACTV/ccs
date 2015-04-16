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
	 * Execute the stateful knowledge session on a separate thread. Uses call fireUntilHalt()
	 * @param objs
	 * @return
	 */
	public static int executeInfiniteSession(ArrayList<CCSMemoryObject> objs){
		final StatefulKnowledgeSession sks = setupSession();
		insertObjects(sks, objs);
		
		sks.startProcess("swim");
		
		new Thread(){
			public void run(){
				startTheSession(sks);
			}
		}.start();
		
		return sks.getId();
	}
	
	/**
	 * Execute the stateful knowledge session on a separate thread. Uses call fireUntilHalt()
	 * Does add all the rules to the knowledge builder
	 * @param objs
	 * @return
	 */
	public static int executeInfiniteSession(String drl, String bpmn, String flow, ArrayList<CCSMemoryObject> objs){
		KnowledgeBuilder kbuilder = initKBuilder(new String[]{drl, bpmn});
		
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration());
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		final StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession(getKnowledgeSessionConfiguration(), null);
		
		insertObjects(sks, objs);
		
		sks.startProcess(flow);
		addEventListeners(sks);
		
		new Thread(){
			public void run(){
				startTheSession(sks);
			}
		}.start();
		
		return sks.getId();
	}

	private static StatefulKnowledgeSession setupSession(){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(getKnowledgeBuilderConfiguration()); 

		//TODO: hardcoded the rules for now.
		addDrl(kbuilder, "actv/ccs/rules/start/CoolingDown.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Start.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/InitializeCichlid.drl");
		addDrl(kbuilder, "actv/ccs/rules/start/Calm.drl");
		addDrl(kbuilder, "actv/ccs/rules/idle/Idle.drl");
		addDrl(kbuilder, "actv/ccs/rules/idle/Move.drl");

		addBpmn(kbuilder, "actv/ccs/flow/start.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/idle.bpmn");
		addBpmn(kbuilder, "actv/ccs/flow/swim.bpmn");
		
		if (kbuilder.hasErrors()){
            log.error(kbuilder.getErrors().toString());
            throw new RuntimeException(kbuilder.getErrors().toString());
        }
		
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase(getKnowledgeBaseConfiguration());
		
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		StatefulKnowledgeSession sks = kb.newStatefulKnowledgeSession(getKnowledgeSessionConfiguration(), null);

		addEventListeners(sks);
		
		return sks;
	}
	
	private static void startTheSession(StatefulKnowledgeSession sks){
		if(sessionThread == null){
			// Execute the rules on another thread
			sessionThread = SessionThread.getInstance();
			sessionThread.setStatefulKnowledgeSession(sks);
			sessionThread.run();
		}
	}
	
	public static void disposeSession(){
		if(sessionThread != null && sessionThread.isRunning()){
			sessionThread.terminate();
			try {
				sessionThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static int pauseSession(){
		if(sessionThread != null && sessionThread.isRunning()){
			sessionThread.pauseSession();
			return sessionThread.getStatefulKnowledgeSession().getId();
		}
		return -1;
	}

	public static int resumeSession(){
		if(sessionThread != null && !sessionThread.isRunning()){
			sessionThread.resumeSession();
			return sessionThread.getStatefulKnowledgeSession().getId();
		}
		return -1;
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
