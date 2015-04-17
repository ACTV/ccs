package actv.rules;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.CCSKnowledgeBase;
import actv.ccs.CCSListener;
import actv.ccs.model.CCSMemoryObject;

/**
 * 
 * 	Create/Execute a stateless session to unit test a rule.
 *
 */
public class DroolsTest{
	private String drl, bpmn, startProc;
	private static final Logger logger = LoggerFactory.getLogger(DroolsTest.class);
	
	public DroolsTest(String drl, String flowFile, String startProc){
		this.drl = drl;
		this.bpmn = flowFile;
		this.startProc = startProc;
	}
	
	/**
	 * Execute with a 6 second thread sleep
	 * @param objects
	 */
	public void executeStateful(long threadSleep, ArrayList<CCSMemoryObject> objects){
			
		CCSKnowledgeBase.executeInfiniteSession(drl, bpmn, startProc, objects);
		try {
			Thread.sleep(threadSleep);
		} catch (InterruptedException e) {
			// 	TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CCSKnowledgeBase.disposeSession();
	}
	
	/**
	 * Execute stateless session
	 * @param objects
	 */
	public void executeStateless(ArrayList<CCSMemoryObject> objects){
		// Retrieve KnowledgeBuilder
		KnowledgeBuilder kbuilder = CCSKnowledgeBase.initKBuilder(new String[]{drl, bpmn});
		
		if(kbuilder.hasErrors()){
			System.out.println(kbuilder.getErrors());
			return;
		}
		
		// Create KnowledgeBase from KnowledgeBuilder
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		
		// Initialize a stateless knowledge session
		StatelessKnowledgeSession sks = kb.newStatelessKnowledgeSession();
		
		sks.addEventListener((WorkingMemoryEventListener)new CCSListener());
		sks.addEventListener((ProcessEventListener)new CCSListener());
		sks.addEventListener((AgendaEventListener)new CCSListener());
				
		List<Command<?>> l = new ArrayList<Command<?>>();
		
		// Insert objects (and start process) into a list for batch execution
		for(CCSMemoryObject obj : objects){
			l.add(CommandFactory.newInsert(obj));
		}
		l.add(CommandFactory.newStartProcess(startProc));

		long start = System.currentTimeMillis();
		sks.execute(CommandFactory.newBatchExecution(l));
		logger.info("Execution time {}", System.currentTimeMillis() - start);		
	}
	
}
