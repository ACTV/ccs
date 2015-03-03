package actv.rules;

import java.io.IOException;
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
import org.drools.io.impl.ByteArrayResource;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


public class DroolsTest{

	
	private StatelessKnowledgeSession sks; 
	//private StatefulKnowledgeSession sks;
	private String startProc;
	
	public DroolsTest(String drl, String flowFile, String startProc){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		KnowledgeBase kb;
		
//		kbuilder.add(ResourceFactory.newClassPathResource(drl), ResourceType.DRL);
		kbuilder.add(new ByteArrayResource(drl.getBytes()), ResourceType.DRL);
		try {
			ClassPathResource flow = new ClassPathResource(flowFile);
			kbuilder.add(ResourceFactory.newUrlResource(flow.getURL()), ResourceType.BPMN2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//= KnowledgeBaseFactory.newKnowledgeBase();
		//kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
		
		kb = kbuilder.newKnowledgeBase();
	
		sks = kb.newStatelessKnowledgeSession();
//		sks = kb.newStatefulKnowledgeSession();
		
		//sks.startProcess(startProc);
		this.startProc = startProc;
	}
	
	public void execute(Object...objects){
		List<Command<?>> l = new ArrayList<Command<?>>();
		for(Object obj : objects)
			l.add(CommandFactory.newInsert(obj));
		l.add(CommandFactory.newStartProcess(startProc));
		
		sks.execute(CommandFactory.newBatchExecution(l));
		//sks.dispose();
	}
}
