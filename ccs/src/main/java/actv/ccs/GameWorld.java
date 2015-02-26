package actv.ccs;

import java.io.File;
import java.util.Vector;

public class GameWorld implements IObservable, IGameWorld {
	
	private GameCollection gameObjectList;
	private Vector<IObserver> observerList;
	private double time = 0;
	private Cichlid cCichlid;
	private Plant plantEx;
	private boolean pauseToggle;
	private boolean timerToggle;

	
	public GameWorld()
	{
		gameObjectList = new GameCollection();
		observerList = new Vector<IObserver>();
		time = 0;
	}
	public void spawn()
	{
		System.out.println("calling spawn test");
		spawnCichlid();
		spawnCichlid();
		spawnPlant();
	}
	
	public void spawnCichlid()
	{
		cCichlid = new Cichlid(Math.random()*900, Math.random()*500, 50, 50, 0, 0, 0,
				(int) (Math.random() * 10), (int) (Math.random() * 360), this);
		gameObjectList.add(cCichlid);
		
	}
	public void spawnPlant() 
	{
		plantEx = new Plant(Math.random()*900, Math.random()*500, 20, 20, 0, 0, 0);
		gameObjectList.add(plantEx);	
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
	public Plant getPlant() {
		return plantEx;
	}
	public void setPlant(Plant p) {
		this.plantEx = p;
	}
	public boolean getPause()
	{
		return pauseToggle;
	}
	public void setPause(boolean t)
	{
		pauseToggle = t;
	}
	public boolean getTimerPause()
	{
		return timerToggle;
	}
	public void setTimerToggle(boolean t)
	{
		timerToggle = t;
	}
}
