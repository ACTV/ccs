package actv.rules;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.runtime.StatelessKnowledgeSession;

import actv.ccs.CCSKnowledgeBase;

/**
 * 
 * 	Create/Execute a stateless session to unit test a rule.
 *
 */
public class DroolsTest{
	private StatelessKnowledgeSession sks; 
	private String startProc;
	
	public DroolsTest(String drl, String flowFile, String startProc){
		// Retrieve KnowledgeBuilder
		KnowledgeBuilder kbuilder = CCSKnowledgeBase.initKBuilder(new String[]{drl, flowFile});
		
		if(kbuilder.hasErrors()){
			System.out.println(kbuilder.getErrors());
			return;
		}
		
		// Create KnowledgeBase from KnowledgeBuilder
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		
		// Initialize a stateless knowledge session
		sks = kb.newStatelessKnowledgeSession();
		
		this.startProc = startProc;
	}
	
	/**
	 * 
	 * Execution specific to stateless junit session
	 */
	public void execute(Object...objects){
		List<Command<?>> l = new ArrayList<Command<?>>();
		
		// Insert objects (and start process) into a list for batch execution
		for(Object obj : objects){
			l.add(CommandFactory.newInsert(obj));
		}
		l.add(CommandFactory.newStartProcess(startProc));
		
		sks.execute(CommandFactory.newBatchExecution(l));
	}
}
