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
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.rule.builder.dialect.mvel.MVELDialectConfiguration;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * Setup for a JUnit test of a rule.
 *
 */
public class DroolsTest{
	private StatelessKnowledgeSession sks; 
	private String startProc;
	
	public DroolsTest(String drl, String flowFile, String startProc){
		// Set up default Drools dialect to MVEL
		MVELDialectConfiguration conf=(MVELDialectConfiguration)new PackageBuilderConfiguration().getDialectConfiguration("mvel");
		conf.setStrict(false);
		conf.getPackageBuilderConfiguration().setDefaultDialect("mvel");
		  
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		try{ 
			ClassPathResource flow = new ClassPathResource(flowFile);
			kbuilder.add(ResourceFactory.newUrlResource(flow.getURL()), ResourceType.BPMN2);
//			kbuilder.add(ResourceFactory.newClassPathResource(drl, getClass()), ResourceType.DRL);
			byte[] drlByte = drl.getBytes();
			kbuilder.add(ResourceFactory.newByteArrayResource(drlByte), ResourceType.DRL);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		KnowledgeBase kb = kbuilder.newKnowledgeBase();
		
		sks = kb.newStatelessKnowledgeSession();
		
		this.startProc = startProc;
	}
	
	public void execute(Object...objects){
		List<Command<?>> l = new ArrayList<Command<?>>();
		// Insert objects (and start process) into a list for batch execution
		for(Object obj : objects)
			l.add(CommandFactory.newInsert(obj));
		l.add(CommandFactory.newStartProcess(startProc));
		
		sks.execute(CommandFactory.newBatchExecution(l));
	}
}
