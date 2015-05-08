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

import actv.ccs.CCSKnowledgeBaseBuilder;
import actv.ccs.CCSKnowledgeSession;
import actv.ccs.CCSTestListener;
import actv.ccs.fact.PRNG;
import actv.ccs.model.CCSMemoryObject;

/**
 * 
 * 	Create/Execute a stateless session to unit test a rule.
 *
 */
public class DroolsTest{
	private CCSKnowledgeSession session = CCSKnowledgeSession.getInstance();
	private String drl, bpmn, startProc;
	private static final Logger logger = LoggerFactory.getLogger(DroolsTest.class);
	
	public DroolsTest(String drl, String flowFile, String startProc){
		this.drl = drl;
		this.bpmn = flowFile;
		this.startProc = startProc;
	}
	
	/**
	 * Executes session with the calling thread sleeping a number of seconds indicated by the parameter threadSleep
	 * 
	 * @param threadSleep
	 * @param objects
	 */
	public void executeStateful(long threadSleep, ArrayList<CCSMemoryObject> objects){
			
		session.setStatefulKnowledgeSession(CCSKnowledgeBaseBuilder.buildStatefulSession(drl, bpmn, startProc, objects));
		session.start();
		
		try {
			Thread.sleep(threadSleep);
		} catch (InterruptedException e) {
			// 	TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		session.terminate();
	}
	
	/**
	 * Execute stateless session
	 * 
	 * @param objs
	 */
	public void executeStateless(ArrayList<Object> objs){
		// Retrieve KnowledgeBuilder
		KnowledgeBuilder kbuilder = CCSKnowledgeBaseBuilder.initKBuilder(new String[]{drl, bpmn});
		
		if(kbuilder.hasErrors()){
			System.out.println(kbuilder.getErrors());
			return;
		}
		
		// Create KnowledgeBase from KnowledgeBuilder
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		
		// Initialize a stateless knowledge session
		StatelessKnowledgeSession sks = kb.newStatelessKnowledgeSession();
		
		sks.addEventListener((WorkingMemoryEventListener)new CCSTestListener());
		sks.addEventListener((ProcessEventListener)new CCSTestListener());
		sks.addEventListener((AgendaEventListener)new CCSTestListener());
		
		sks.setGlobal("logger", logger);
		sks.setGlobal("prng", new PRNG());
		
		List<Command<?>> l = new ArrayList<Command<?>>();
		
		// Insert objects (and start process) into a list for batch execution
		for(Object obj : objs){
			l.add(CommandFactory.newInsert(obj));
		}
		l.add(CommandFactory.newStartProcess(startProc));

		long start = System.currentTimeMillis();
		sks.execute(CommandFactory.newBatchExecution(l));
		logger.info("Execution time {}", System.currentTimeMillis() - start);		
	}
	
}
