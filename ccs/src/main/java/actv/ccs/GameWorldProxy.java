package actv.ccs;

public class GameWorldProxy implements IObservable, IGameWorld 
{ 
	 // code here to accept and hold a GameWorld, provide implementations 
	 // of all the public methods in a GameWorld, forward allowed 
	 // calls to the actual GameWorld, and reject calls to methods 
	 // which the outside should not be able to access in the GameWorld. 
	 // PROXY

	private GameWorld realGW; // proxy

	 public GameWorldProxy(GameWorld gw)
	 {
		 realGW = gw;
	 }
	 public Iterator getIterator ()
	 {
		return realGW.getIterator(); // error
	 }
	 public void add (GameObject obj)
	 {
		 realGW.add(obj);
	 }
	 public void removeObj (GameObject obj)
	 {
	 	// null
	 }
	 public void addObserver(IObserver o) {
		realGW.addObserver(o);	
	 }
	 public void notifyObservers() {
		realGW.notifyObservers();
	 }
} 