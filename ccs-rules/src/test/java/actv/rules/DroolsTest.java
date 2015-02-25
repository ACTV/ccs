package actv.rules;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


public class DroolsTest{
	private KieSession session;
	private KieServices kieServices;
	private KieContainer kContainer;
	private String startProc;
	
	public DroolsTest(String drl, String path, String startProc){
		kieServices = KieServices.Factory.get();
		kContainer = kieServices.getKieClasspathContainer();
		session = kContainer.newKieSession("test_session");
		this.startProc = startProc;
	}
	
	public void execute(Object...objects){
		for(Object obj : objects)
			session.insert(obj);
		session.startProcess(startProc);
		session.fireAllRules();
		session.dispose();
	}
}
