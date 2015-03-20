package actv.ccs.listener;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import actv.rules.CCSKnowledgeBaseTest;

/**
 * Singleton runner
 * 
 * @author TOM
 *
 */
public class EngineRunner implements Runnable{
	private static EngineRunner instance = null;
	private static ArrayList<Object> runnerObjs;
	private static Logger logger = LoggerFactory.getLogger(EngineRunner.class);
	
	protected EngineRunner(){}
	
	public static EngineRunner getInstance(Object...objs){
		if(instance == null){
			instance = new EngineRunner();
		}
		runnerObjs = new ArrayList<Object>();
		for(Object obj : objs){
			runnerObjs.add(obj);
		}
		return instance;
	}

	public void run() {
		logger.info("Executing KnowledgeBase!");
		//CCSKnowledgeBase.executeInfiniteSession(runnerObjs);
	}
}
