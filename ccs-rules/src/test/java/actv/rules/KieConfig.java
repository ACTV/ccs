package actv.rules;


import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;

public class KieConfig {
	private KieContainer kcontainer;
	private KieBase kbase;
	private KieServices ks;
	
	public KieConfig(String drl, String flow, String ruleflow){
		ks = KieServices.Factory.get();
		// Create the in-memory File System and add the resources files  to it
		KieFileSystem kfs = ks.newKieFileSystem();
		kfs.write(ResourceFactory.newClassPathResource(drl));
		// Create the builder for the resources of the File System
		KieBuilder kbuilder = ks.newKieBuilder(kfs);
		// Build the KieBases
		kbuilder.buildAll();
		// Check for errors
		if (kbuilder.getResults().hasMessages(Level.ERROR)) {
			throw new IllegalArgumentException(kbuilder.getResults().toString());
		}
		// Get the Release ID (mvn style: groupId, artifactId,version)
		ReleaseId relId = kbuilder.getKieModule().getReleaseId();
		// Create the Container, wrapping the KieModule with the given ReleaseId
		kcontainer = ks.newKieContainer(relId);
	}
	
	

	public KieBase getKbase() {
		if(kbase == null){
			KieBaseConfiguration kconf = ks.newKieBaseConfiguration();
			kbase = kcontainer.newKieBase(kconf);
		}
		return kbase;
	}

	public void setKbase(KieBase kbase) {
		this.kbase = kbase;
	}

	public KieContainer getKcontainer() {
		return kcontainer;
	}

	public void setKcontainer(KieContainer kcontainer) {
		this.kcontainer = kcontainer;
	}
	
	
}
