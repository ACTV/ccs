package actv.ccs.listener;

import java.util.ArrayList;
import java.util.HashMap;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.fact.Auditor;
import actv.ccs.model.CCSMemoryObject;
import actv.ccs.model.ConvictCichlid;
import actv.ccs.model.type.FishState;
import actv.ccs.model.ui.CichlidCollection;
import actv.ccs.model.ui.Iterator;

/**
 * Singleton CCS Knowledge rule base runner
 * 
 * @author TOM
 *
 */
public class RuleEngineRunner extends Thread{
	private static RuleEngineRunner instance = null;
	private HashMap<String, CCSMemoryObject> map;
	private ArrayList<String> cichlidId, objectId;
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

	public void newMap(CichlidCollection cc){
		String id;
		boolean hasCichlid = false;
		map = new HashMap<String, CCSMemoryObject>();
		cichlidId = new ArrayList<String>();
		objectId = new ArrayList<String>();
		
		// Add the fish
		Iterator itr = cc.getIterator();
		while(itr.hasNext()){
			ConvictCichlid c = itr.getNext();
			if(c instanceof ConvictCichlid){
				hasCichlid = true;
				id = Integer.toString(c.getCichlidID());
				cichlidId.add(id);
				map.put(id, c);	
			}
		}
		
		// Add the auditor
		id = "Auditor";
		objectId.add(id);
		map.put(id,  new Auditor());
		
		/*
		 * Insert objects into a hash map using the cichlid id as a key
		 */
		/*
		for(CCSMemoryObject obj : cc){
			if(obj instanceof ConvictCichlid){
				hasCichlid = true;
				id = Integer.toString(((ConvictCichlid) obj).getCichlidID());
				cichlidId.add(id);
				map.put(id, (ConvictCichlid)obj);
				
			}else if(obj instanceof Auditor){
				id = "Auditor";
				objectId.add(id);
				map.put( id, (Auditor)obj);
			}else{
				id = "Fact";
				objectId.add(id);
				map.put(id, obj);
			}
		}
	 */
		
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
		/*
		isSessionHalted = false;
		
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			ConvictCichlid cc = (ConvictCichlid)map.get(cichlidId.get(0));
			
			if(cc.getState() == FishState.IDLE && cc.getIdleWaitTime() >= 4){
				sks.halt();
				isSessionHalted = true;
				logger.info(">>>>>>> Session halted!!");
				break;
			}
		}
		*/
	}
}
