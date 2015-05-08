package actv.ccs;

import java.util.ArrayList;

import org.drools.event.rule.AfterActivationFiredEvent;

/**
 * 
 * Extend the CCSListener class to override the afterActivationFiredEvent method
 *  for testing purposes.
 *
 * @see CCSListener
 */
public class CCSTestListener extends CCSListener {
	private static ArrayList<String> firedRules = new ArrayList<String>();
	
	public CCSTestListener(){
		super();
	}
	
	public static boolean isFired(String rule){
		return firedRules.contains(rule);
	}
	
	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		super.afterActivationFired(event);
		
		String rule = event.getActivation().getRule().getName();
		if(!firedRules.contains(rule)){
			firedRules.add(rule);
		}
	}
}
