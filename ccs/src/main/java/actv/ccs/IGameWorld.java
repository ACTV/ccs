package actv.ccs;

public interface IGameWorld { 
	 public void add(GameObject obj); // GameWorld iterators
	 public Iterator getIterator();
	 public void removeObj(GameObject obj);
	} 