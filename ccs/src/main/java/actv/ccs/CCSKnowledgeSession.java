package actv.ccs;

import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton session runner
 *
 * @author TOM
 *
 */
public class CCSKnowledgeSession extends Thread {
	private static CCSKnowledgeSession instance = null;
	private StatefulKnowledgeSession statefulKnowledgeSession;
	private static boolean isRunning;
	private static boolean isPaused;
	private static final Logger logger = LoggerFactory.getLogger(CCSKnowledgeSession.class);

	private CCSKnowledgeSession(){};
	
	public static CCSKnowledgeSession getInstance(){
		if(instance == null){
			instance = new CCSKnowledgeSession();
			instance.setName("CCS Knowledge Session");
		}
		return instance;
	}
	
	public void setStatefulKnowledgeSession(StatefulKnowledgeSession statefulKnowledgeSession){
		this.statefulKnowledgeSession = statefulKnowledgeSession;
	}
	
	public StatefulKnowledgeSession getStatefulKnowledgeSession() {
		return statefulKnowledgeSession;
	}
	
	public void run(){
		if(!isRunning){
			logger.debug("Running session!");
			isRunning = true;
			statefulKnowledgeSession.fireUntilHalt();
		}
	}
	
	public void terminate(){
		if(isRunning){
			logger.debug("CCSKnowledgeSession Terminate");
			statefulKnowledgeSession.halt();
			statefulKnowledgeSession.dispose();
			isRunning = false;
		}
	}
	
	public void pauseSession(){
		if(isRunning && !isPaused){
			logger.debug("CCSKnowledgeSession Pause");
			statefulKnowledgeSession.halt();
			isPaused = true;
		}
	}
	
	public void resumeSession(){
		if(isRunning && isPaused){
			logger.debug("CCSKnowledgeSession Resume");
			statefulKnowledgeSession.fireUntilHalt();
			isPaused = false;
		}
	}
	
	public boolean isRunning() {
		return isRunning;
	}
}
