package actv.ccs;

import org.drools.runtime.StatefulKnowledgeSession;

/**
 * Singleton session runner
 *
 * @author TOM
 *
 */
public class SessionThread extends Thread {
	private static SessionThread instance = null;
	private static boolean isRunning;
	private StatefulKnowledgeSession statefulKnowledgeSession;

	protected SessionThread(){};
	
	public static SessionThread getInstance(){
		if(instance == null){
			instance = new SessionThread();
			instance.setName("Session Thread");
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
		statefulKnowledgeSession.fireUntilHalt();
		isRunning = true;
	}
	
	public void terminate(){
		if(isRunning){
			statefulKnowledgeSession.halt();
			statefulKnowledgeSession.dispose();
			isRunning = false;
		}
	}
	
	public void pauseSession(){
		if(isRunning){
			statefulKnowledgeSession.halt();
			isRunning = false;
		}
	}
	
	public void resumeSession(){
		if(!isRunning && statefulKnowledgeSession != null){
			statefulKnowledgeSession.fireUntilHalt();
			isRunning = true;
		}
	}
	
	public boolean isRunning() {
		return isRunning;
	}
}
