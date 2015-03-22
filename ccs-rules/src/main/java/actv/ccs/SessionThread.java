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
	}
}
