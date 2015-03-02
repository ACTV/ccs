package actv.rules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatelessKnowledgeSession;



public class DroolsTest{
//	private KieSession session;
//	private KieServices kieServices;
//	private KieContainer kContainer;
//	private String startProc;
//	
//	public DroolsTest(String drl, String path, String startProc){
//		kieServices = KieServices.Factory.get();
//		kContainer = kieServices.getKieClasspathContainer();
//		session = kContainer.newKieSession("test_session");
//		this.startProc = startProc;
//	}
//	
//	public void execute(Object...objects){
//		for(Object obj : objects)
//			session.insert(obj);
//		session.startProcess(startProc);
//		session.fireAllRules();
//		session.dispose();
//	}
	
	private StatelessKnowledgeSession sks; 
	private String startProc;
	
	public DroolsTest(String drl, String startProc){
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
//		ClassPathResource cpr = new ClassPathResource(startProc);
//		try{
			kbuilder.add(ResourceFactory.newFileResource(startProc), ResourceType.BPMN2);
//		}catch(IOException e){
//			e.getMessage();
//			e.printStackTrace();
//		}
		//kbuilder.add(ResourceFactory.newFileResource(startProc), ResourceType.BPMN2);
	//	kbuilder.add(ResourceFactory.newFileResource(drl), ResourceType.DRL);
		KnowledgeBase kb = KnowledgeBaseFactory.newKnowledgeBase();
		kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
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
