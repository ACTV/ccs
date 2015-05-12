package actv.ccs.model.ui;

import actv.ccs.model.ConvictCichlid;

public interface ISimulationWorld { 
	 public void add(ConvictCichlid obj); // GameWorld iterators
	 public Iterator getIterator();
	 public void removeObj(ConvictCichlid obj);
	} 