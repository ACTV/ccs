package actv.ccs.fact;

import java.util.ArrayList;

import actv.ccs.model.CCSMemoryObject;


/**
 * 
 * This fact records the rules that fired.
 *
 */
public class Auditor implements CCSMemoryObject{
	private ArrayList<String> rulesFired;

	public ArrayList<String> getRulesFired(){
		if(rulesFired == null){
			rulesFired = new ArrayList<String>();
		}
		return rulesFired;
	}
}
