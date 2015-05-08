package actv.ccs.listener;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBaseBuilder;
import actv.ccs.CCSKnowledgeSession;
import actv.ccs.fact.PRNG;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;

/**
 * Singleton CCS Knowledge rule base runner
 * 
 */
public class RuleEngineRunner extends Thread{
	private static RuleEngineRunner instance = null;
	private static CCSKnowledgeSession session = CCSKnowledgeSession.getInstance();
	private static Logger logger = LoggerFactory.getLogger(RuleEngineRunner.class);
	private HashMap<String, CCSMemoryObject> map;
	
	
	private RuleEngineRunner(){}
	
	public static RuleEngineRunner getInstance(){
		if(instance == null){
			instance = new RuleEngineRunner();
			instance.setName("Rule Engine Runner");
			// Create the knowledge session
			session.setStatefulKnowledgeSession(CCSKnowledgeBaseBuilder.buildStatefulSession());
		}
		return instance;
	}

	public void newMap(ArrayList<CCSMemoryObject> objects){
		String id;
		boolean hasCichlid = false;
		map = new HashMap<String, CCSMemoryObject>();
		
		// Add the fish
		java.util.Iterator<CCSMemoryObject> itr = objects.iterator();
		while(itr.hasNext()){
			CCSMemoryObject c = itr.next();
			
			if(c instanceof ConvictCichlid){
				hasCichlid = true;
				id = Integer.toString(((ConvictCichlid)c).getCichlidID());
				map.put(id, (ConvictCichlid)c);	
				session.getStatefulKnowledgeSession().insert(c);
			}
		}

		if(!hasCichlid){
			logger.error("No convict cichlid in the tank!!");
			//TODO implement exception?
			return;
		}
	}
	
	public void closeSession() throws InterruptedException{
		logger.info("Closing the session!");
		session.terminate();
	}
	
	public void pauseSession(){
		session.pauseSession();
	}
	
	public void resumeSession(){
		session.resumeSession();
	}
	
	
	public void run() {
		logger.info("Executing KnowledgeBase!");
		session.run();
	}

}
