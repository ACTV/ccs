package actv.ccs;

import org.drools.runtime.StatefulKnowledgeSession;

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
		isRunning = true;
		statefulKnowledgeSession.fireUntilHalt();
	}
	
	public void terminate(){
		if(isRunning){
			statefulKnowledgeSession.halt();
			statefulKnowledgeSession.dispose();
			isRunning = false;
		}
	}
	
	public void pauseSession(){
		if(isRunning && !isPaused){
			statefulKnowledgeSession.halt();
			isPaused = true;
		}
	}
	
	public void resumeSession(){
		if(isRunning && isPaused){
			System.out.println("CCSKnowledgeSession Resume");
			statefulKnowledgeSession.fireUntilHalt();
			isPaused = false;
		}
	}
	
	public boolean isRunning() {
		return isRunning;
	}
}
