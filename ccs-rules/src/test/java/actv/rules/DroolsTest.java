package actv.rules;

import java.util.List;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;

/**
 * Unit test for simple App.
 */
public class DroolsTest 
    extends TestCase
{
	private StatelessKieSession session;
	private KieBase kBase;
	private KieServices kieServices;
	private KieContainer kContainer;
	
	public DroolsTest(){
		kieServices = KieServices.Factory.get();
		kContainer = kieServices.getKieClasspathContainer();
		
		kBase = kContainer.getKieBase("kbase");
		session = kBase.newStatelessKieSession();
	}
	
	public void execute(String rule, String flow, String flowName, Object...objects){
		List cmds = new ArrayList();
		for(Object obj : objects)
			cmds.add(obj);
		
		session.execute(cmds);
	}
}
