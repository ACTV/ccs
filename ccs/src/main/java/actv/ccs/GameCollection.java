package actv.ccs;

import java.util.Vector;

public class GameCollection implements Iterable
{
	private Vector<GameObject> gameCollection; 

	public GameCollection()
	{
		gameCollection = new Vector<GameObject>();
	}
	public void add(GameObject obj)
	{
		gameCollection.add(obj);
	}
	public int size()
	{
		return gameCollection.size();
	}
	public void removeObject(GameObject o)
	{
		gameCollection.remove(o);
	}
	public Iterator getIterator()
	{
		return new GameObjectIterator();
	}
	private class GameObjectIterator implements Iterator 
	{
		private int index;
		
		public GameObjectIterator()
		{
			index = -1;
		}
		public boolean hasNext()
		{
			if (gameCollection.size() <= 0)
			{
				return false;
			}
			if (index == gameCollection.size() - 1)
			{
				return false;
			}
			return true;
		}
		public GameObject getNext()
		{
			index++;
			return gameCollection.elementAt(index);
		}
		public void remove()
		{
			if (index > -1)
			//	gameCollection.remove(index);
				gameCollection.removeElementAt(index);
		}

	}
}
