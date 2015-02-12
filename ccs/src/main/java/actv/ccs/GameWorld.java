package actv.ccs;

import java.io.File;
import java.util.Vector;

public class GameWorld implements IObservable, IGameWorld {
	
	private GameCollection gameObjectList;
	private Vector<IObserver> observerList;
	private double time = 0;
	private Cichlid cCichlid;
	
	
	public GameWorld()
	{
		gameObjectList = new GameCollection();
		observerList = new Vector<IObserver>();
		time = 0;
	}
	public void spawn()
	{
		System.out.println("calling spawn test");
		spawnTest();
	}
	
	public void spawnTest()
	{
		cCichlid = new Cichlid(Math.random()*900, Math.random()*900, 50, 50, 0, 0, 0,
				(int) (Math.random() * 10), (int) (Math.random() * 360), this);
		gameObjectList.add(cCichlid);
	}
	public void add(GameObject o) // add object to GameWorld
	{
		gameObjectList.add(o);
	}
	public Iterator getIterator()
	{
	 	return gameObjectList.getIterator(); 
	}
	public void removeObj(GameObject obj) // remove object in gameworld
	{
		gameObjectList.removeObject(obj);
	}
	public void addObserver(IObserver o) // add observable to list
	{
		observerList.add(o);	
	}
	public void notifyObservers() // iterate to notify observers of change
	{
		GameWorldProxy proxy = new GameWorldProxy(this);
		 for ( IObserver o : observerList)
		{
			o.update((IObservable)proxy, null);
		}	 
	}	 
	public double getTime() // accessors and mutators
	{
		return time;
	}
	public void setTime(double t)
	{
		time = t; // new time
	}
	public GameCollection getGameObjectList()
	{
		return gameObjectList;
	}
	public Cichlid getCichlid()
	{
		return cCichlid;
	}
	public void setcCichlid(Cichlid cCichlid)
	{
		this.cCichlid = cCichlid;
	}
}
