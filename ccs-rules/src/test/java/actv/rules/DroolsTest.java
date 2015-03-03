package actv.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;


public class DroolsTest{

	
	//private StatelessKnowledgeSession sks; 
	private StatefulKnowledgeSession sks;
	private String startProc;
	
	public DroolsTest(String drl, String flowFile, String startProc){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource(drl), ResourceType.DRL);
		kbuilder.add(ResourceFactory.newFileResource(flowFile), ResourceType.BPMN2);
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase();
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
	
//		sks = kb.newStatelessKnowledgeSession();
		sks = kb.newStatefulKnowledgeSession();
		
		//sks.startProcess(startProc);
		this.startProc = startProc;
	}
	
	public ExecutionResults execute(Object...objects){
		List<Command<?>> l = new ArrayList<Command<?>>();
		for(Object obj : objects)
			l.add(CommandFactory.newInsert(obj));
		l.add(CommandFactory.newStartProcess(startProc));
		
		ExecutionResults res = sks.execute(CommandFactory.newBatchExecution(l));
		sks.dispose();
		return res;
	}
}
