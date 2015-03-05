package actv.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.core.io.ClassPathResource;



public class DroolsTest{
	private StatelessKnowledgeSession sks; 
	private String startProc;
	
	public DroolsTest(String drl, String flowFile, String startProc){
		super();
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		KnowledgeBase kb;
		
		kbuilder.add(ResourceFactory.newClassPathResource(drl, getClass()), ResourceType.DRL);
		try {
			ClassPathResource flow = new ClassPathResource(flowFile);
			kbuilder.add(ResourceFactory.newUrlResource(flow.getURL()), ResourceType.BPMN2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		kb = kbuilder.newKnowledgeBase();
	
		sks = kb.newStatelessKnowledgeSession();
		
		this.startProc = startProc;
	}
	
	public void execute(Object...objects){
		List<Command<?>> l = new ArrayList<Command<?>>();
		for(Object obj : objects)
			l.add(CommandFactory.newInsert(obj));
		l.add(CommandFactory.newStartProcess(startProc));
		
		sks.execute(CommandFactory.newBatchExecution(l));
	}
}
