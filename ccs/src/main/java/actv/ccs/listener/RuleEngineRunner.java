package actv.ccs.listener;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.sageTest.TestCichlid;

/**
 * Singleton CCS Knowledge rule base runner
 * 
 * @author TOM
 *
 */
public class RuleEngineRunner extends Thread{
	private static RuleEngineRunner instance = null;
	private HashMap<String, CCSMemoryObject> map;
	private boolean isRunning = false;
	private static Logger logger = LoggerFactory.getLogger(RuleEngineRunner.class);
	
	private RuleEngineRunner(){}
	
	public static RuleEngineRunner getInstance(){
		if(instance == null){
			instance = new RuleEngineRunner();
			instance.setName("Rule Engine Runner");
		}
		return instance;
	}
	
	public boolean isRunning(){
		return this.isRunning;
	}

	public void newMap(ArrayList<CCSMemoryObject> objects){
		String id;
		boolean hasCichlid = false;
		map = new HashMap<String, CCSMemoryObject>();
		
		// Add the fish
		java.util.Iterator<CCSMemoryObject> itr = objects.iterator();
		while(itr.hasNext()){
			CCSMemoryObject c = itr.next();
			
			if(c instanceof TestCichlid){
				hasCichlid = true;
				id = Integer.toString(((TestCichlid)c).getCichlidID());
				map.put(id, (TestCichlid)c);	
			}
		}

		// Add the auditor
		id = "Auditor";
		map.put(id,  new Auditor());
		
		if(!hasCichlid){
			logger.error("No convict cichlid in the tank!!");
			//TODO implement exception?
			return;
		}
	}
	
	public void closeSession() throws InterruptedException{
		logger.info("Closing the session!");
		CCSKnowledgeBase.disposeSession();
		isRunning = false;
	}
	
	public int pauseSession(){
		if(isRunning){
			int ret = CCSKnowledgeBase.pauseSession();
			isRunning = false;
			return ret;
		}
		return -1;
	}
	
	public int resumeSession(){
		if(!isRunning){
			int ret = CCSKnowledgeBase.resumeSession();
			isRunning = true;
			return ret;
		}
		return -1;
	}
	
	
	public void run() {
		logger.info("Executing KnowledgeBase!");
		CCSKnowledgeBase.executeInfiniteSession(new ArrayList<CCSMemoryObject>(map.values()));
		isRunning = true;
	}
}
