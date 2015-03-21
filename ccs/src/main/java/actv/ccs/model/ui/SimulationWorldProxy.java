package actv.ccs.model.ui;

import actv.ccs.model.ConvictCichlid;

public class SimulationWorldProxy implements IObservable, ISimulationWorld 
{ 
	 // code here to accept and hold a GameWorld, provide implementations 
	 // of all the public methods in a GameWorld, forward allowed 
	 // calls to the actual GameWorld, and reject calls to methods 
	 // which the outside should not be able to access in the GameWorld. 
	 // PROXY

	private SimulationWorld realSW; // proxy

	 public SimulationWorldProxy(SimulationWorld sw)
	 {
		 realSW = sw;
	 }
	 public Iterator getIterator ()
	 {
		return realSW.getIterator(); // error
	 }
	 public void add (ConvictCichlid obj)
	 {
		 realSW.add(obj);
	 }
	 public void removeObj (ConvictCichlid obj)
	 {
	 	// null
	 }
	 public void addObserver(IObserver o) {
		realSW.addObserver(o);	
	 }
	 public void notifyObservers() {
		realSW.notifyObservers();
	 }
	 
	 // heheheh

} 