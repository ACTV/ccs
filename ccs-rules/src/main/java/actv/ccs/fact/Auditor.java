package actv.ccs.fact;

import java.util.ArrayList;


/**
 * 
 * This fact records the rules that fired.
 *
 */
public class Auditor {
	private ArrayList<String> rulesFired;

	public ArrayList<String> getRulesFired(){
		if(rulesFired == null){
			rulesFired = new ArrayList<String>();
		}
		return rulesFired;
	}
}
