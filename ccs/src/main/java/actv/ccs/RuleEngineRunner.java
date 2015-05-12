package actv.ccs;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sage.scene.SceneNode;
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

	public void newMap(ArrayList<SceneNode> objects){
		boolean hasCichlid = false;
		
		// Add the fish
		java.util.Iterator<SceneNode> itr = objects.iterator();
		while(itr.hasNext()){
			SceneNode c = itr.next();
			
			if(c instanceof ConvictCichlid){
				hasCichlid = true;
				session.getStatefulKnowledgeSession().insert((ConvictCichlid)c);
			}
		}

		if(!hasCichlid){
			logger.error("No convict cichlid in the tank!!");
			//TODO implement exception?
			return;
		}
	}
	
	public void closeSession() throws InterruptedException{
		session.terminate();
	}
	
	public void pauseSession(){
		session.pauseSession();
	}
	
	public void resumeSession(){
		session.resumeSession();
	}
	
	public void run() {
		session.start();
	}

}
