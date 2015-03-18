package actv.rules;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
import org.drools.event.rule.WorkingMemoryEventListener;
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
		
		sks.addEventListener(new WorkingMemoryEventListener() {
			
			public void objectUpdated(ObjectUpdatedEvent event) {
				System.out.println("Updated " + event.getObject().toString());
			}
			
			public void objectRetracted(ObjectRetractedEvent event) {
				// TODO Auto-generated method stub
			}
			
			public void objectInserted(ObjectInsertedEvent event) {
				// TODO Auto-generated method stub
			}
		});

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
